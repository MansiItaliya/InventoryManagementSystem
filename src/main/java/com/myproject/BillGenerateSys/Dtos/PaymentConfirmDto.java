package com.myproject.BillGenerateSys.Dtos;

import jakarta.validation.constraints.NotNull;

public class PaymentConfirmDto {
    @NotNull(message = "bill id is required")
    private Integer billId;
    private Boolean paymentConfirmed;

    public int getBillId() {
        return billId;
    }

    public void setBillIdId(int billId) {
        this.billId = billId;
    }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    public void setPaymentConfirmed(boolean paymentConfirmed) {
        this.paymentConfirmed = paymentConfirmed;
    }

}
