/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author Andrii_Kozak1
 */
@Service
public class EMailStoringSevice {
    
    @Autowired
    private EMailRepository eMailRepository;
    
    public Date lastChecked(){
        EMail last = eMailRepository.findTopByOrderByDateDesc();
        return (last == null) ? null : last.getDate();
    }
    
    public EMail findById(Long id){
        return eMailRepository.findOne(id);
    }
    
    public Stream<EMail> searcEMail(String from, String subject, String after, String before) throws ParseException{
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date afterDate = (StringUtils.isEmpty(after)) ? df.parse("1984-07-13 00:00:00") : df.parse(after);
        Date beforeDate = (StringUtils.isEmpty(before)) ? df.parse("2084-07-13 00:00:00") : df.parse(before);

        Stream<EMail> results;
        if (StringUtils.isEmpty(from)) {
            if (StringUtils.isEmpty(subject)) {
                results = eMailRepository.findByDate(afterDate, beforeDate);
            } else {
                results = eMailRepository.findByDateAndSubject(afterDate, beforeDate, subject);
            }
        } else {
            if (StringUtils.isEmpty(subject)) {
                results = eMailRepository.findByDateAndAuthor(afterDate, beforeDate, from);
            } else {
                results = eMailRepository.findByDateAuthorAndSubject(afterDate, beforeDate, from, subject);
            }
        }
        return results;
    }

    public <S extends EMail> Iterable<S> save(Iterable<S> itrbl) {
        return eMailRepository.save(itrbl);
    }
    
}
