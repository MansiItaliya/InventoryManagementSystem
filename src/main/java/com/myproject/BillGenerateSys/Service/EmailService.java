package com.myproject.BillGenerateSys.Service;

import com.myproject.BillGenerateSys.Config.EmailConfig;
import com.myproject.BillGenerateSys.Constant.ErrorCons;
import com.myproject.BillGenerateSys.Constant.otherCons;
import com.myproject.BillGenerateSys.ExceptionHandler.IOrelatedException;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class EmailService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final EmailConfig emailConfig;
    @Autowired
    public EmailService(EmailConfig emailConfig){this.emailConfig = emailConfig;}

    public String sendEmail(String toMail,String subject,String msg,String filepath,String fileName){
        Email from = new Email(emailConfig.getSenderMail());
        Email to = new Email(toMail);
        Content content = new Content(otherCons.EMAIL_CONTENT_TYPE_CSV, msg);
        Mail mail = new Mail(from, subject, to, content);

        // Attachment
        try {
        Attachments attachments = new Attachments();
        byte[] fileData = Files.readAllBytes(new File(filepath).toPath());
        String encoded = Base64.getEncoder().encodeToString(fileData);
        attachments.setContent(encoded);
        attachments.setType(otherCons.ATTACHMENT_TYPE);
        attachments.setFilename(fileName);
        attachments.setDisposition(otherCons.ATTACHMENT);

        mail.addAttachments(attachments);

        SendGrid sg = new SendGrid(emailConfig.getApiKey());
        Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint(otherCons.END_POINT);
            request.setBody(mail.build());

            Response response = sg.api(request);
            return "Status code : " + response.getStatusCode() +
                    ", body : " + response.getBody() +
                    ", Headers : " + response.getHeaders();
        } catch (IOException ex) {
            logger.error(ErrorCons.FAIL_SEND_MAIL);
            throw new IOrelatedException(ErrorCons.FAIL_SEND_MAIL);
        }
    }
}
