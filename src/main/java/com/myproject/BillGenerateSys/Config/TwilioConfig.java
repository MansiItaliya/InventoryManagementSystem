package com.myproject.BillGenerateSys.Config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account.sid}")
    private String sid;

    @Value("${twilio.auth.token}")
    private String token;

    @Value("${twilio.phone.number}")
    private String twilioNumber;

    @Value("${admin.phone.number}")
    private String adminPhoneNumber;

    @Value("${twilio.whatsapp.from}")
    private String twilioWhatsAppNumber;

    @PostConstruct
    public void init(){
        Twilio.init(sid,token);
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTwilioNumber() {
        return twilioNumber;
    }

    public void setTwilioNumber(String twilioNumber) {
        this.twilioNumber = twilioNumber;
    }

    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public String getTwilioWhatsAppNumber() {
        return twilioWhatsAppNumber;
    }

    public void setTwilioWhatsAppNumber(String twilioWhatsAppNumber) {
        this.twilioWhatsAppNumber = twilioWhatsAppNumber;
    }
}
