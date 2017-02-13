package com.mycompany.apsdtask;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.stream.Stream;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class POP3EMailRetrivingService implements EMailRetrivingService {
    final private Properties properties;
    final private String host;
    final private String storeType;
    final private String user;
    final private String password;
    
    @Autowired
    public POP3EMailRetrivingService(
            @Value("pop.gmail.com")String host,
            @Value("pop3") String storeType,
            @Value("andrii.kozak.test@gmail.com") String user,
            @Value("27leoleo") String password) {
        this.host = host;
        this.storeType = storeType;
        this.user = user;
        this.password = password;
        properties = new Properties();

        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
    }
    
    @Override
    public Stream<Message> retrieve(Date cutoff) throws NoSuchProviderException, MessagingException {
        Folder emailFolder = null;
        Store store = null;
        Stream<Message> result = null;
        try {
            Session emailSession = Session.getDefaultInstance(properties);           
            store = emailSession.getStore("pop3s");            
            store.connect(host, user, password);            
            emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            
            Message[] messages = emailFolder.getMessages();
            result = Arrays.stream(messages).filter(message -> {
                boolean isNew;
                try{
                    isNew = (cutoff == null || message.getSentDate().after(cutoff));
                } catch (MessagingException e){
                    throw new RuntimeException(e);
                }
                return isNew;
            });
        } finally {
            emailFolder.close(false);
            store.close(); 
        }
        return result;
    }
}
