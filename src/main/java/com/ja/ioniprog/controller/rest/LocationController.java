package com.ja.ioniprog.controller.rest;

import com.ja.ioniprog.model.dto.audit.UserLocationDto;
import com.ja.ioniprog.model.params.LocationParams;
import com.ja.ioniprog.service.UserLocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

    private UserLocationService userLocationService;

    public LocationController(UserLocationService userLocationService) {
        this.userLocationService = userLocationService;
    }

    @GetMapping(value = "/user")
    @ResponseBody
    public List<UserLocationDto> getUserLocations(LocationParams params) {
        return userLocationService.getUserLocations(params);
    }
}
