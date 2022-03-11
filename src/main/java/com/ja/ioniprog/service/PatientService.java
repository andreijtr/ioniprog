package com.ja.ioniprog.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ja.ioniprog.config.persistence.PersistenceConfig;
import com.ja.ioniprog.config.security.annotations.IsDoctor;
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
import com.ja.ioniprog.model.paging.PageResult;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.utils.application.JsonParser;
import com.ja.ioniprog.utils.enums.AuditEnum;
import com.ja.ioniprog.utils.enums.StateEnum;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientService {
    Logger logger = LoggerFactory.getLogger(PatientService.class);

    private PatientDao       patientDao;
    private PatientAuditDao  patientAuditDao;
    private PatientDoctorDao patientDoctorDao;
    private JsonParser       jsonParser;
    private Mapper           dozerMapper;

    public PatientService(PatientDao patientDao, PatientAuditDao patientAuditDao, PatientDoctorDao patientDoctorDao, JsonParser jsonParser, Mapper dozerMapper) {
        this.patientDao       = patientDao;
        this.patientAuditDao  = patientAuditDao;
        this.patientDoctorDao = patientDoctorDao;
        this.jsonParser       = jsonParser;
        this.dozerMapper      = dozerMapper;
    }

    @Transactional
    @IsDoctor
    public void save(PatientDto patientDto, PatientParams patientParams) {
        logger.info("PatientService: save patient");
        User userResponsible = User.builder()
                                    .id(Integer.parseInt(patientParams.getCreatedBy().getIdUser()))
                                    .build();
        Patient patient = dozerMapper.map(patientDto, Patient.class);
        patientDao.save(patient);

        Audit audit = Audit.getAudit(AuditEnum.INSERT, userResponsible, jsonParser.getJson(patient));
        PatientAudit insertAudit = new PatientAudit(patient, audit);
        patientAuditDao.save(insertAudit);

        PatientDoctor patientDoctor = PatientDoctor.createPatientDoctor(patient, userResponsible, userResponsible);
        patientDoctorDao.save(patientDoctor);
    }

    public PatientDto getById(String idPatient) {
        return dozerMapper.map(patientDao.getById(Integer.parseInt(idPatient)), PatientDto.class);
    }

    @IsDoctor
    public PageResult<PatientDoctorDto> getPatientsPaging(PatientParams patientParams) {
        int offset = patientParams.getOffset();
        int pageSize = patientParams.getPageSize();

        List<PatientDoctorDto> patientDoctorDtos = new ArrayList<>();
        List<PatientDoctor> patientDoctors = patientDao.getPatientsPaging(patientParams);
        if (patientDoctors != null) {
            patientDoctorDtos = patientDoctors.stream()
                                              .map(patientDoctor -> dozerMapper.map(patientDoctor, PatientDoctorDto.class))
                                              .collect(Collectors.toList());
        }
        long totalRows = patientDao.getCountPaging(patientParams);
        long totalPages = (totalRows / pageSize) + 1;
        long currentPage = (offset / pageSize) + 1;

        return new PageResult<PatientDoctorDto>(patientDoctorDtos, totalPages, currentPage);
    }
}
