package com.mycompany.apsdtask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MyController {

    @Autowired
    private EMailRepository eMailRepository;

    @RequestMapping("/showMail")
    public String showMail(@RequestParam(value = "id", required = true) Long id, Model model) {
        model.addAttribute("result", eMailRepository.findOne(id));
        return "showMail";
    }

    @RequestMapping("searchMail")
    public String searchMail(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "subject", required = false) String subject,
            @RequestParam(value = "after", required = false) String after,
            @RequestParam(value = "before", required = false) String before,
            Model model) throws ParseException {
        model.addAttribute("pfrom", from);
        model.addAttribute("psubject", subject);
        model.addAttribute("pafter", after);
        model.addAttribute("pbefore", before);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Date afterDate = (StringUtils.isEmpty(after)) ? df.parse("1984-07-13 00:00:00") : df.parse(after);
        Date beforeDate = (StringUtils.isEmpty(before)) ? df.parse("2084-07-13 00:00:00") : df.parse(before);

        List<EMail> results;
        if (StringUtils.isEmpty(from)) {
            if (StringUtils.isEmpty(subject)) {
                results = eMailRepository.findByDate(afterDate, beforeDate).collect(Collectors.toList());
            } else {
                results = eMailRepository.findByDateAndSubject(afterDate, beforeDate, subject).collect(Collectors.toList());
            }
        } else {
            if (StringUtils.isEmpty(subject)) {
                results = eMailRepository.findByDateAndAuthor(afterDate, beforeDate, from).collect(Collectors.toList());
            } else {
                results = eMailRepository.findByDateAuthorAndSubject(afterDate, beforeDate, from, subject).collect(Collectors.toList());
            }
        }
        model.addAttribute("results", results);
        return "searchMail";
    }

    @RequestMapping("/checkMail")
    public String checkMail() throws Exception {
        String host = "pop.gmail.com";
        String storeType = "pop3";
        String user = "andrii.kozak.test@gmail.com";
        String password = "27leoleo";
        //create properties field
        Properties properties = new Properties();

        properties.put("mail.pop3.host", host);
        properties.put("mail.pop3.port", "995");
        properties.put("mail.pop3.starttls.enable", "true");
        Session emailSession = Session.getDefaultInstance(properties);

        Store store = emailSession.getStore("pop3s");

        store.connect(host, user, password);

        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        Message[] messages = emailFolder.getMessages();

        if (messages.length > 0) {
            EMail last = eMailRepository.findTopByOrderByDateDesc();
            final Date cutoff = (last == null) ? null : last.getDate();
            Arrays.stream(messages).map(EMail::new).filter(mail -> cutoff == null || mail.getDate().after(cutoff)).forEach(eMailRepository::save);
        }
       
        emailFolder.close(false);
        store.close();
        return "redirect:searchMail";
    }
}
