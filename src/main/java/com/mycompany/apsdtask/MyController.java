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
    private EMailRetrivingService eMailRetrivingService;
    @Autowired
    private EMailStoringSevice eMailStoringSevice;
    
    @RequestMapping("/showMail")
    public String showMail(@RequestParam(value = "id", required = true) Long id, Model model) {
        model.addAttribute("result", eMailStoringSevice.findById(id));
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
        
        model.addAttribute("results", eMailStoringSevice.searcEMail(from, subject, after, before).collect(Collectors.toList()));
        return "searchMail";
    }

    @RequestMapping("/checkMail")
    public String checkMail() throws Exception {        
        Date cutoff = eMailStoringSevice.lastChecked();
        eMailStoringSevice.save(eMailRetrivingService.retrieve(cutoff).map(EMail::new).collect(Collectors.toList()));
        return "redirect:searchMail";
    }
}
