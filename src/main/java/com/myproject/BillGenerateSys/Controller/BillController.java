package com.myproject.BillGenerateSys.Controller;

import com.myproject.BillGenerateSys.Constant.MessageCons;
import com.myproject.BillGenerateSys.Dtos.OrderRequestDto;
import com.myproject.BillGenerateSys.Dtos.PaymentConfirmDto;
import com.myproject.BillGenerateSys.Model.Bill;
import com.myproject.BillGenerateSys.Model.Product;
import com.myproject.BillGenerateSys.Service.BillService;
import com.myproject.BillGenerateSys.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bill")
public class BillController {

    private final BillService billService;
    private final OrderService orderService;

    @Autowired
    public BillController(BillService billService, OrderService orderService){
        this.billService = billService;
        this.orderService = orderService;
    }

    @PostMapping("add-product")
    public ResponseEntity<String> addProduct(@RequestBody Product product){
        billService.addProduct(product);
        return new ResponseEntity<>(MessageCons.PRODUCT_SAVED,HttpStatus.CREATED);
    }

    @PostMapping("order")
    public ResponseEntity<String> order(@RequestBody @Valid OrderRequestDto request){
        Bill response = orderService.order(request);
        return ResponseEntity.ok(MessageCons.CREATED_BILL + response.getBillId());
    }

    @PostMapping("payment-confirm")
    public ResponseEntity<String> paymentConfirm(@RequestBody @Valid PaymentConfirmDto paymentConfirmDto){
        Bill bill = billService.paymentConfirm(paymentConfirmDto);
        return ResponseEntity.ok(bill.isPaymentConfirmed()? MessageCons.PAYMENT_CONFIRMED : MessageCons.PAYMENT_NOT_CONFIRMED + bill.getBillId());
    }

    @GetMapping("get_bill/{customerId}")
    public ResponseEntity<String> getBill(@PathVariable int customerId){
        List<Bill> bills = billService.billGenerate(customerId);
        if(bills.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageCons.PAYMENT_NOT_CONFIRMED);
        }
        return ResponseEntity.ok(MessageCons.SEND_EMAIL_OF_BILL);
    }
}
