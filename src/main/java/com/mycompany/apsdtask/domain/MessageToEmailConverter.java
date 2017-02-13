/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask.domain;

import com.mycompany.apsdtask.domain.EMail;
import java.io.IOException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageToEmailConverter implements Converter<Message,EMail> {
    @Override
    public EMail convert(Message message){
       EMail eMail = new EMail();
       try {
            eMail.setAuthor(((InternetAddress) message.getFrom()[0]).getAddress());
            eMail.setDate(message.getSentDate());
            eMail.setSubject(message.getSubject());
            Object contnet = message.getContent();
            eMail.setBody((contnet instanceof MimeMultipart) ? ((MimeMultipart) contnet).getBodyPart(0).getContent().toString() : contnet.toString());
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }
       return eMail;
    } 
}
