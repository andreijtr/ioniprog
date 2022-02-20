package com.ja.ioniprog.controller.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewController {

    @GetMapping("/dashboard.html")
    public ModelAndView getDashboardLtePage(HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        ModelAndView model = new ModelAndView("dashboard");
        model.addObject("userConnected", objectMapper.writeValueAsString(LoggedUser.get(request)));

        return model;
    }

    @GetMapping("/login-page.html")
    public String getLoginPage() {
        return "login-page";
    }

    @GetMapping("/welcome")
    public String getWelcomePage() {
        return "welcome";
    }

    @GetMapping("/patient")
    public String getPatientPage() {
        return "/fragments/patient :: patient";
    }
}
