package com.ja.ioniprog.controller.rest;

import com.ja.ioniprog.model.dto.*;
import com.ja.ioniprog.model.entity.Appointment;
import com.ja.ioniprog.service.AppointmentService;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/appointment")
public class AppointmentController {
    private Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(value = "/test")
    public List<EventDto> test(HttpServletRequest request) {
        String startCalendar = request.getParameter("start");
        String endCalendar = request.getParameter("end");

        System.out.println("Start = " + startCalendar);
        System.out.println("End = " + endCalendar);

        OffsetDateTime odt = OffsetDateTime.parse(startCalendar);
        odt.toLocalDate();

        System.out.println("Ca si data: start = " + OffsetDateTime.parse(startCalendar).toLocalDate());
        System.out.println("Ca si data: end = " + OffsetDateTime.parse(endCalendar).toLocalDate());

        return null;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> save(@Valid @RequestBody Appointment appointment, BindingResult bindingResult, HttpServletRequest request) {
        logger.info("AppointmentController: save appointment");
        System.out.println(appointment);
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            throw new ValidationException(errors.toString());
        }

        UserDto loggedUser = LoggedUser.get(request);
        appointmentService.save(appointment, loggedUser);

        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        System.out.println("Am intrat in exception handler in appoiment controller");
        e.printStackTrace();
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
