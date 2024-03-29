package com.ja.ioniprog.controller.rest;

import com.ja.ioniprog.exception.IllegalOperationException;
import com.ja.ioniprog.exception.NoChangeDetectedException;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.dto.UserShortDto;
import com.ja.ioniprog.model.dto.audit.PatientAuditDto;
import com.ja.ioniprog.model.paging.PageResult;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.service.PatientDoctorService;
import com.ja.ioniprog.service.PatientService;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {
    private Logger logger = LoggerFactory.getLogger(PatientController.class);
    private PatientService patientService;
    private PatientDoctorService patientDoctorService;

    public PatientController(PatientService patientService, PatientDoctorService patientDoctorService) {
        this.patientService = patientService;
        this.patientDoctorService = patientDoctorService;
    }

//    @GetMapping(value = "/get")
//    public ResponseEntity<PatientDto> getPatientById(String idPatient) {
//        logger.info("PatientController: get patient by id");
//        PatientDto patientDto = patientService.getById(idPatient);
//
//        return new ResponseEntity<>(patientDto, HttpStatus.OK);
//    }
    @PostMapping(value = "/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PatientDoctorDto> getPatientDoctors(@RequestBody PatientParams patientParams) {
        logger.info("PatientController: get patientDoctors by params");
        return patientService.getPatientDoctors(patientParams);
    }

    @GetMapping(value = "/audit", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PatientAuditDto> getAuditByIdPatient(String idPatient) {
        logger.info("PatientController: get audit for idPatient " + idPatient);
        return patientService.getAuditByIdPatient(idPatient);
    }

    @PostMapping(value = "/paging", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResult<PatientDoctorDto> getPagination(@RequestBody PatientParams patientParams) {
        logger.info("PatientController: get patients paging");
        return patientService.getPatientsPaging(patientParams);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addPatient(@RequestBody PatientDto patientDto, HttpServletRequest request) {
        logger.info("PatientController: add patient");
        UserDto userDto = LoggedUser.get(request);
        PatientParams patientParams = PatientParams.builder().createdBy(userDto).build();
        patientService.save(patientDto, patientParams);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePatient(@Valid @RequestBody PatientDto patientDto, HttpServletRequest request) {
        logger.info("PatientController: update patient");
        UserDto userDto = LoggedUser.get(request);
        PatientParams patientParams = PatientParams.builder().loggedUser(userDto).build();
        try {
            patientService.update(patientDto, patientParams);
        } catch (NoChangeDetectedException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (OptimisticLockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/deletePatientDoctor")
    public ResponseEntity<String> deletePatientDoctor(@RequestParam String idPatientDoctor, HttpServletRequest request) {
        logger.info("PatientController: delete patientDoctor");
        UserDto userDto = LoggedUser.get(request);
        PatientParams patientParams = PatientParams.builder().loggedUser(userDto).build();
        try {
            patientDoctorService.delete(idPatientDoctor, patientParams);
        } catch (IllegalOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> transferPatient(@RequestBody PatientDoctorDto patientDoctorDto, HttpServletRequest request) {
        try {
            UserDto userConnected = LoggedUser.get(request);
            patientDoctorDto.setCreatedBy(UserShortDto.builder().idUser(userConnected.getIdUser()).build());
            patientDoctorService.add(patientDoctorDto);
        } catch (IllegalOperationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Operation failed, because error occurred. Contact administrator.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("SUCCESS!", HttpStatus.OK);
    }

    @GetMapping(value = "/lov/users")
    public List<UserShortDto> getLovDoctors() {
        return patientService.getLovDoctors();
    }
}
