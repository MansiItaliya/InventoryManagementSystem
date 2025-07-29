package com.myproject.BillGenerateSys.Service;

import com.myproject.BillGenerateSys.Constant.ErrorCons;
import com.myproject.BillGenerateSys.Constant.MessageCons;
import com.myproject.BillGenerateSys.Constant.otherCons;
import com.myproject.BillGenerateSys.Dtos.PaymentConfirmDto;
import com.myproject.BillGenerateSys.Model.Bill;
import com.myproject.BillGenerateSys.Model.Customer;
import com.myproject.BillGenerateSys.Model.Product;
import com.myproject.BillGenerateSys.Repository.BillRepo;
import com.myproject.BillGenerateSys.Repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BillService {
    private static final Logger logger = LoggerFactory.getLogger(BillService.class);

    private final ProductRepo productRepo;
    private final BillRepo billRepo;
    private final CsvGenerateService csvGenerateService;
    private final EmailService emailService;
    @Autowired
    public BillService(ProductRepo productRepo, BillRepo billRepo, CsvGenerateService csvGenerateService, EmailService emailService) {
        this.productRepo = productRepo;
        this.billRepo = billRepo;
        this.csvGenerateService = csvGenerateService;
        this.emailService = emailService;
    }

    public void addProduct(Product product){
        productRepo.save(product);
        logger.info(MessageCons.PRODUCT_SAVED);
    }

//    @PostConstruct  //no need to create post api .only if you want to run automatically
//    public void saveProduct(){
//        Reader reader = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("product.json")));
//        Gson gson = new Gson();
//        Type ListProduct = new TypeToken<List<Product>>(){}.getType();
//        List<Product> list = gson.fromJson(reader,ListProduct);
//        productRepo.saveAll(list);
//    }

    public Bill paymentConfirm(PaymentConfirmDto dto){
        Bill bill = billRepo.findById(dto.getBillId()).orElseThrow(()-> {
            logger.error(ErrorCons.BILLID_NOT_FOUND);
            return new NoSuchElementException(ErrorCons.BILLID_NOT_FOUND);
        });
        bill.setPaymentConfirmed(dto.isPaymentConfirmed());
       return billRepo.save(bill);
    }

    public List<Bill> billGenerate(int customerId){
        String filePath = otherCons.FILEPATH_BILL + customerId + otherCons.CSV_EXTENSION;

        List<Bill> bills = billRepo.findByCustomerCustomerIdAndPaymentConfirmed(customerId, true);

        if (bills.isEmpty()) {
            logger.error(ErrorCons.NO_CONFIRM_BILL);
            throw new NoSuchElementException(ErrorCons.NO_CONFIRM_BILL + customerId);
        }
        Customer customer = bills.get(0).getCustomer();

        csvGenerateService.generateBillCsv(bills, filePath);
        emailService.sendEmail(
                customer.getEmail(),
                otherCons.MAIL_SUB,
                 otherCons.DEAR + customer.getCustomerName() + otherCons.BILL_MSG,
                filePath,
                otherCons.BILL_FILENAME
        );
        logger.info(MessageCons.SEND_EMAIL_OF_BILL);
        return bills;
    }
}
