package com.ja.ioniprog.controller.rest;

import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.dto.UserShortDto;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.paging.PageResult;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.service.PatientService;
import com.ja.ioniprog.service.UserService;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    private PatientService patientService;
    private PatientDoctorDao patientDoctorDao;
    private UserService userService;

    public PatientController(PatientService patientService, PatientDoctorDao patientDoctorDao, UserService userService) {
        this.patientService = patientService;
        this.patientDoctorDao = patientDoctorDao;
        this.userService = userService;
    }

    @GetMapping(value = "/get")
    public ResponseEntity<PatientDto> getPatientById(String idPatient) {
        logger.info("PatientController: get patient by id");
        PatientDto patientDto = patientService.getById(idPatient);

        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addPatient(@RequestBody PatientDto patientDto, HttpServletRequest request) {
        logger.info("PatientController: add patient");
        UserDto userDto = LoggedUser.get(request);
        PatientParams patientParams = PatientParams.builder().createdBy(userDto).build();
        patientService.save(patientDto, patientParams);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PostMapping(value = "/paging", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<PatientDoctorDto> getPagination(@RequestBody PatientParams patientParams) {
        logger.info("PatientController: get patients paging");
        return patientService.getPatientsPaging(patientParams);
    }

}
