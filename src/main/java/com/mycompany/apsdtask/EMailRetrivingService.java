/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask;

import java.util.Date;
import java.util.stream.Stream;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author Andrii_Kozak1
 */
public interface EMailRetrivingService {

    Stream<Message> retrieve(Date cutoff) throws NoSuchProviderException, MessagingException;
    
}
