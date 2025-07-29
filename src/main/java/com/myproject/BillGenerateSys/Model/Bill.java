package com.myproject.BillGenerateSys.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bill",cascade = CascadeType.ALL)
    List<OrderItem> items;

    private LocalDate billDate = LocalDate.now();
    private Double totalAmt;
    private Double billGst;
    private Double finalAmt;
    private boolean paymentConfirmed = false;

    public Bill(){}

    public Bill(Customer customer, boolean paymentConfirmed, List<OrderItem> items, Double totalAmt, Double billGst, Double finalAmt) {
        this.customer = customer;
        this.paymentConfirmed = paymentConfirmed;
        this.items = items;
        this.totalAmt = totalAmt;
        this.billGst = billGst;
        this.finalAmt = finalAmt;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getBillGst() {
        return billGst;
    }

    public void setBillGst(Double billGst) {
        this.billGst = billGst;
    }

    public Double getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Double finalAmt) {
        this.finalAmt = finalAmt;
    }

    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    public void setPaymentConfirmed(boolean paymentConfirmed) {
        this.paymentConfirmed = paymentConfirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return Objects.equals(billId, bill.billId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(billId);
    }
}
