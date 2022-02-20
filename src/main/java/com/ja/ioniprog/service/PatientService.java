package com.ja.ioniprog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.config.persistence.PersistenceConfig;
import com.ja.ioniprog.dao.PatientDao;
import com.ja.ioniprog.dao.audit.PatientAuditDao;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.dto.PatientDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.utils.enums.AuditEnum;
import com.ja.ioniprog.utils.enums.StateEnum;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    Logger logger = LoggerFactory.getLogger(PersistenceConfig.class);

    private PatientDao       patientDao;
    private PatientAuditDao  patientAuditDao;
    private PatientDoctorDao patientDoctorDao;
    private ObjectMapper     jsonMapper;
    private Mapper           dozerMapper;

    @Autowired
    public PatientService(PatientDao patientDao, PatientAuditDao patientAuditDao, PatientDoctorDao patientDoctorDao, ObjectMapper jsonMapper, Mapper dozerMapper) {
        this.patientDao = patientDao;
        this.patientAuditDao = patientAuditDao;
        this.patientDoctorDao = patientDoctorDao;
        this.jsonMapper = jsonMapper;
        this.dozerMapper = dozerMapper;
    }

    // verifica sa aiba rol de doctor cand introduce pacient!!! si in js si in java cu spring security
    @Transactional
    public void save(PatientDto patientDto, PatientParams patientParams) {
        logger.info("PatientService: save patient");
        User userResponsible = User.builder()
                                    .id(Integer.parseInt(patientParams.getCreatedBy().getIdUser()))
                                    .build();
        Patient patient = dozerMapper.map(patientDto, Patient.class);
        patientDao.save(patient);

        PatientAudit insertAudit = new PatientAudit();
        //n ai putut sa scoti asta intr o metoda statica, mai incearca
        String json = null;
        try {
            json = jsonMapper.writeValueAsString(patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Audit audit = Audit.getAudit(AuditEnum.INSERT, userResponsible, json);
        insertAudit.setPatientEntity(patient);
        insertAudit.setAudit(audit);
        patientAuditDao.save(insertAudit);

        PatientDoctor patientDoctor = new PatientDoctor();
        patientDoctor.setPatient(patient);
        patientDoctor.setDoctor(userResponsible);
        patientDoctor.setCreatedBy(userResponsible);
        patientDoctor.setCreatedOn(LocalDateTime.now());
        patientDoctor.setState(StateEnum.ACTIVE.getName());
        patientDoctorDao.save(patientDoctor);
    }

    public PatientDto getById(String idPatient) {
        return dozerMapper.map(patientDao.getById(Integer.parseInt(idPatient)), PatientDto.class);
    }

    public List<PatientDoctorDto> getPatientsForDoctor() {
        List<PatientDoctorDto> patientDoctorDtos = new ArrayList<>();
        List<PatientDoctor> patientDoctors = patientDao.getPatientForDoctor();

        if (patientDoctors != null) {
            patientDoctorDtos = patientDoctors.stream()
                                              .map(patientDoctor -> dozerMapper.map(patientDoctor, PatientDoctorDto.class))
                                              .collect(Collectors.toList());
        }

        return patientDoctorDtos;
    }
}
