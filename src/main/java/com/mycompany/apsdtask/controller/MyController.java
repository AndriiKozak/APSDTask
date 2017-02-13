package com.mycompany.apsdtask.controller;

import com.mycompany.apsdtask.service.LastCheckedService;
import com.mycompany.apsdtask.service.EMailStoringService;
import com.mycompany.apsdtask.service.EMailSearchService;
import com.mycompany.apsdtask.service.EMailRetrivingService;
import com.mycompany.apsdtask.domain.EMail;
import java.text.ParseException;
import java.util.Date;
import java.util.stream.Collectors;
import javax.mail.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class MyController {
    
    @Autowired
    Converter<Message, EMail> converter;
    @Autowired
    private EMailRetrivingService eMailRetrivingService;
    @Autowired
    private EMailStoringService eMailStoringSevice;
    @Autowired
    private EMailSearchService eMailSearchService;
    @Autowired
    private LastCheckedService lastCheckedService;
    
    @RequestMapping("/showMail")
    public String showMail(@RequestParam(value = "id", required = true) Long id, Model model) {
        model.addAttribute("result", eMailStoringSevice.findById(id));
        return "showMail";
    }

    @RequestMapping("/searchMail")
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
        
        model.addAttribute("results", eMailSearchService.searchEMail(from, subject, after, before).collect(Collectors.toList()));
        return "searchMail";
    }

    @RequestMapping("/checkMail")
    public String checkMail() throws Exception {        
        Date cutoff = lastCheckedService.lastChecked();
        eMailStoringSevice.save(eMailRetrivingService.retrieve(cutoff).map(converter::convert).collect(Collectors.toList()));
        return "redirect:searchMail";
    }
}
