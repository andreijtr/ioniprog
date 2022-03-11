package com.ja.ioniprog.controller.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.utils.application.JsonParser;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ViewController {

    private JsonParser jsonParser;

    @Autowired
    public ViewController(JsonParser jsonParser) {
        this.jsonParser = jsonParser;
    }

    @GetMapping("/dashboard.html")
    public ModelAndView getDashboardLtePage(HttpServletRequest request) throws JsonProcessingException {
        ModelAndView model = new ModelAndView("dashboard");
        model.addObject("userConnected", jsonParser.getJson(LoggedUser.get(request)));

        return model;
    }

    @GetMapping("/login-page.html")
    public String getLoginPage() {
        return "login-page";
    }

    @GetMapping("/patient")
    public String getPatientPage() {
        return "/fragments/patient :: patient";
    }
}
