package com.mycompany.apsdtask.service;

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
    private static final String INBOX = "INBOX";
    private static final String POP3S = "pop3s";
    private static final String MAILPOP3STARTTLSENABLE = "mail.pop3.starttls.enable";
    private static final String MAILPOP3PORT = "mail.pop3.port";
    private static final String MAILPOP3HOST = "mail.pop3.host";
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

        properties.put(MAILPOP3HOST, host);
        properties.put(MAILPOP3PORT, "995");
        properties.put(MAILPOP3STARTTLSENABLE, "true");
    }
    
    
    @Override
    public Stream<Message> retrieve(Date cutoff) throws NoSuchProviderException, MessagingException {
        Folder emailFolder = null;
        Store store = null;
        Stream<Message> result = null;
        try {
            Session emailSession = Session.getDefaultInstance(properties);           
            store = emailSession.getStore(POP3S);            
            store.connect(host, user, password);            
            emailFolder = store.getFolder(INBOX);
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
