package com.myproject.BillGenerateSys.Helper;

public class AmountSummary {
    double total;
    double gst;
    public AmountSummary(){}
    public AmountSummary(double total, double gst) {
        this.total = total;
        this.gst = gst;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }
}
