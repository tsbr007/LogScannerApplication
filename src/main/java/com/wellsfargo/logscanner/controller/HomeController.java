package com.wellsfargo.logscanner.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.wellsfargo.logscanner.service.LogScannerService;
import com.wellsfargo.logscanner.utils.EmailNotification;

import java.util.List;

@Controller
public class HomeController {

    private final LogScannerService logScannerService;

    public HomeController(LogScannerService logScannerService) {
        this.logScannerService = logScannerService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<EmailNotification> notifications = logScannerService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "index";
    }
}
