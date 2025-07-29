package com.myproject.BillGenerateSys.Constant;

public class otherCons {
    //csv bill header
    public static final String BILL_ID = "billId";
    public static final String BILL_DATE = "billDate";
    public static final String TOTAL_AMT = "totalAmt";
    public static final String BILL_GST = "billGst";
    public static final String FINAL_AMT = "finalAmt";
    public static final String PAYMENT_CONFIRMED = "paymentConfirmed";

    //csv product header
    public static final String PRODUCT_ID= "productId";
    public static final String PRODUCT_NAME= "productName";
    public static final String QUANTITY= "quantity";
    public static final String PRICE= "price";
    public static final String GST= "gst";
    public static final String THRESHOLD= "threshold";
    public static final String FILEPATH_PRO= "product-report.csv";

    //twilio
    public static final String WHATSAPP= "whatsapp:";
    //Email
    public static final String EMAIL_CONTENT_TYPE_CSV = "text/plain";
    public static final String ATTACHMENT_TYPE = "text/csv";
    public static final String ATTACHMENT = "attachment";
    public static final String END_POINT = "mail/send";

    public static final String FILEPATH_BILL = "bill_report_";
    public static final String CSV_EXTENSION = ".csv";

    //email content
    public static final String MAIL_SUB = "Your Bill Report";
    public static final String DEAR = "Dear ";
    public static final String BILL_MSG =  ",\n\n" +
            "Thank you for your purchase!\n" +
            "Please find your bill report attached.\n\n" +
            "Regards,\nBilling Team";
    public static final String BILL_FILENAME="bill-report.csv";


}
