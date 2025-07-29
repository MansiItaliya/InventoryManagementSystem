package com.myproject.BillGenerateSys.Constant;

public class MessageCons {
    public static final String PRODUCT_SAVED = "successfully saved in DB";
    public static final String CREATED_BILL="Bill generated with Bill ID: ";
    public static final String START_ORDER_PROCESS = "Starting new order process for customer";
    public static final String END_ORDER_PROCESS =  "Order process completed successfully for customer";
    public static final String PAYMENT_CONFIRMED = "Your payment is confirmed";
    public static final String PAYMENT_NOT_CONFIRMED = "Your payment is not confirmed : ";
    public static final String SEND_EMAIL_OF_BILL = "Your bill is send on Email";
    public static final String SAVED_CUSTOMER = "Saved customer in DB";
    public static final String HII = "Hii ";
    public static final String NOTIFICATION_MSG = " your order is placed.\n Total : \nFinalAmt : ";

    public static final String EMAIL_SUB ="Daily Product Stock Report";
    public static final String EMAIL_MSG = "Dear Admin,\n\n" +
            "Please find attached the latest product stock report as of today.\n\n" +
            "Regards,\n" +
            "BillingApp System\n" ;
    public static final String PRODUCT_FILE ="product-report.csv";

    public static final String START_BILL_CSV = "starting CSV generation for product";
    public static final String START_PRODUCT_CSV = "starting CSV generation for Bills";
    public static final String CSV_SUCCEED = "CSV File successfully generated..";
    public static final String SEND_NOTIFICATION = "WhatsApp bill notification sent to customer";
}
