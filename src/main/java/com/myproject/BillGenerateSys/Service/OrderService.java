package com.myproject.BillGenerateSys.Service;

import com.myproject.BillGenerateSys.Config.EmailConfig;
import com.myproject.BillGenerateSys.Constant.ErrorCons;
import com.myproject.BillGenerateSys.Constant.MessageCons;
import com.myproject.BillGenerateSys.Dtos.OrderRequestDto;
import com.myproject.BillGenerateSys.ExceptionHandler.InsufficientStockException;
import com.myproject.BillGenerateSys.Helper.AmountSummary;
import com.myproject.BillGenerateSys.Model.Bill;
import com.myproject.BillGenerateSys.Model.Customer;
import com.myproject.BillGenerateSys.Model.OrderItem;
import com.myproject.BillGenerateSys.Model.Product;
import com.myproject.BillGenerateSys.Repository.BillRepo;
import com.myproject.BillGenerateSys.Repository.CustomerRepo;
import com.myproject.BillGenerateSys.Repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CustomerRepo customerRepo;
    private final ProductRepo productRepo;
    private final BillRepo billRepo;
    private final TwilioService twilioService;
    private final CsvGenerateService csvGenerateService;
    private final EmailService emailService;
    private final EmailConfig emailConfig;
    private final ModelMapper modelMapper;
    @Autowired
    public OrderService(CustomerRepo customerRepo, ProductRepo productRepo, TwilioService twilioService, BillRepo billRepo, CsvGenerateService csvGenerateService, EmailConfig emailConfig, EmailService emailService,ModelMapper modelMapper) {
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
        this.twilioService = twilioService;
        this.billRepo = billRepo;
        this.csvGenerateService = csvGenerateService;
        this.emailConfig = emailConfig;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Bill order(OrderRequestDto request){
        logger.info(MessageCons.START_ORDER_PROCESS);
        int random = generateRandomNumber();

        //save customer
        Customer customer = saveCustomer(random,request);

        List<OrderItem> orderItemslist = new ArrayList<>();
        AmountSummary amountSummary = processOrder(request,orderItemslist);

        //create bill
        Bill bill = createBill(customer,orderItemslist,amountSummary);

        // Notify customer
        notifyToCustomer(customer,bill);
        sendEmailToAdmin();
        logger.info(MessageCons.END_ORDER_PROCESS);
        return bill;
    }

    private int generateRandomNumber(){
        return new Random().nextInt(900000) * 100000;
    }

    private Customer saveCustomer(int random,OrderRequestDto req){
        Customer customer = modelMapper.map(req, Customer.class);
        customer.setCustomerId(random);

        logger.info(MessageCons.SAVED_CUSTOMER);
        return customerRepo.save(customer);
    }

    private AmountSummary processOrder(OrderRequestDto request,List<OrderItem> orderItems){
        double total = 0;
        double gst = 0;

        for(OrderRequestDto.ItemDto dto : request.getItems()){
            Product product = productRepo.findByProductNameIgnoreCase(dto.getProductName())
                    .orElseThrow(() -> {
                        logger.error(ErrorCons.PRODUCT_NOT_FOUND + "{}", dto.getProductName());
                        return new NoSuchElementException(ErrorCons.PRODUCT_NOT_FOUND + dto.getProductName());
                    });

           validationForStock(product,dto);

            // Create order item
            OrderItem orderItem = new OrderItem(product,(product.getPrice() * dto.getQuantity()),dto.getQuantity());
            orderItems.add(orderItem);

            total += orderItem.getPrice();
            gst += (orderItem.getPrice() * product.getGst())/100;

        }
        return new AmountSummary(total,gst);
    }

    private void validationForStock(Product product, OrderRequestDto.ItemDto dto){
        if(product.getQuantity() < dto.getQuantity()){
            logger.error(ErrorCons.INSUFFICIENT_STOCK + "{}", dto.getProductName());
            throw new InsufficientStockException(ErrorCons.INSUFFICIENT_STOCK + dto.getProductName() + ErrorCons.AVAILABLE + product.getQuantity());
        }

        // Update stock
        product.setQuantity(product.getQuantity() - dto.getQuantity());
        logger.info(MessageCons.PRODUCT_SAVED);
        productRepo.save(product);

        // Check threshold
        if(product.getQuantity() <= product.getThreshold()){
            logger.warn(ErrorCons.STOCK_ALERT + "{}", dto.getProductName());
            twilioService.sendSMSMessageToAdmin(ErrorCons.STOCK_ALERT + dto.getProductName());
        }
    }

    private Bill createBill(Customer customer,List<OrderItem> orderItemslist,AmountSummary summary){
        Bill bill = new Bill(customer,false,orderItemslist,summary.getTotal(),summary.getGst(),summary.getTotal() + summary.getGst());
        billRepo.save(bill);

        for(OrderItem item : orderItemslist) {
            item.setBill(bill);
        }
        logger.info(MessageCons.CREATED_BILL);
        return bill;
    }

    private void notifyToCustomer(Customer customer,Bill bill){
        String msg = MessageCons.HII + customer.getCustomerName() +
                MessageCons.NOTIFICATION_MSG + bill.getFinalAmt();
        twilioService.sendWhatsAppMessageToCustomer(customer.getMobile(),msg);
        logger.info(MessageCons.SEND_NOTIFICATION);
    }
    private void sendEmailToAdmin(){
        List<Product> products = productRepo.findAll();
        if(products.isEmpty()) {
            logger.error(ErrorCons.PRODUCT_NOT_FOUND);
            throw new NoSuchElementException(ErrorCons.PRODUCT_NOT_FOUND);
        }

        String filePath = csvGenerateService.generateProductCsv(products);
        emailService.sendEmail(
                emailConfig.getAdminMail(),
                MessageCons.EMAIL_SUB,
                MessageCons.EMAIL_MSG,
                filePath,
                MessageCons.PRODUCT_FILE
        );
    }
}
