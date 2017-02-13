package com.mycompany.apsdtask.service;

import com.mycompany.apsdtask.domain.EMail;
import com.mycompany.apsdtask.repository.EMailRepository;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SpringDataEMailStoringSevice implements LastCheckedService, EMailStoringService, EMailSearchService {

    public static final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final Date PAST;
    public static final Date FUTURE;

    static {
        try {
            PAST = DF.parse("1984-07-13 00:00:00");
            FUTURE = DF.parse("2084-07-13 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    @Autowired
    private EMailRepository eMailRepository;

    @Override
    public Date lastChecked() {
        EMail last = eMailRepository.findTopByOrderByDateDesc();
        return (last == null) ? null : last.getDate();
    }

    @Override
    public EMail findById(Long id) {
        return eMailRepository.findOne(id);
    }

    @Override
    public Stream<EMail> searchEMail(String from, String subject, String after, String before) throws ParseException {
       
        Date afterDate = (StringUtils.isEmpty(after)) ? PAST : DF.parse(after);
        Date beforeDate = (StringUtils.isEmpty(before)) ? FUTURE : DF.parse(before);

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

    @Override
    public <S extends EMail> Iterable<S> save(Iterable<S> itrbl) {
        return eMailRepository.save(itrbl);
    }

}
