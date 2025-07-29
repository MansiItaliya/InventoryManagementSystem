package com.myproject.BillGenerateSys.Service;

import com.myproject.BillGenerateSys.Config.TwilioConfig;
import com.myproject.BillGenerateSys.Constant.ErrorCons;
import com.myproject.BillGenerateSys.Constant.otherCons;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TwilioService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TwilioConfig config;

    @Autowired          //inject through constructor
    public TwilioService(TwilioConfig config) {
        this.config = config;
    }

    public void sendWhatsAppMessageToCustomer(String to, String message) {
        try {
            Message.creator(
                    new PhoneNumber(otherCons.WHATSAPP + to),
                    new PhoneNumber(config.getTwilioWhatsAppNumber()),
                    message
            ).create();
        } catch (Exception e) {
            logger.error(ErrorCons.SEND_WHATSAPP_MSG_FAIL);
            throw new RuntimeException(e);
        }
    }

    public void sendSMSMessageToAdmin(String message) {       //Remove "whatsapp:" prefix for SMS
        try {
            Message.creator(
                    new PhoneNumber(config.getAdminPhoneNumber().replace(otherCons.WHATSAPP, "")),  // Replace with admin number
                    new PhoneNumber(config.getTwilioNumber()),
                    message
            ).create();
        } catch (Exception e) {
            logger.error(ErrorCons.SEND_SMS_FAIL);
            throw new RuntimeException(e);
        }
    }
}
