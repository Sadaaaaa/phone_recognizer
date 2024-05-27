package com.example.phone_recognizer.controller;

import com.example.phone_recognizer.service.CheckPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CheckPhoneController {

    private final CheckPhoneService checkPhoneService;

    @Autowired
    public CheckPhoneController(CheckPhoneService checkPhoneService) {
        this.checkPhoneService = checkPhoneService;
    }

    @GetMapping("/check")
    public ModelAndView checkPhone(@RequestParam String phone) {
        ModelAndView model = new ModelAndView();
        ResponseEntity<?> response = checkPhoneService.checkPhone(phone);

        if (phone == null || !phone.matches("\\d+") || phone.length() < 11 || phone.startsWith("0")
                || response.getBody() == null) {
            model.addObject("errorMessage", "Wrong phone number!");
            model.setViewName("index");
            return model;
        }

        model.addObject("country", response.getBody());
        model.setViewName("index");
        return model;
    }
}