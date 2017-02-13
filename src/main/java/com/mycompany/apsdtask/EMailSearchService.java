/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask;

import java.text.ParseException;
import java.util.stream.Stream;

/**
 *
 * @author Andrii_Kozak1
 */
public interface EMailSearchService {

    Stream<EMail> searchEMail(String from, String subject, String after, String before) throws ParseException;
    
}
