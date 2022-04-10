package com.ja.ioniprog.service;

import com.ja.ioniprog.config.security.annotations.IsDoctor;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.exception.IllegalOperationException;
import com.ja.ioniprog.model.dto.PatientDoctorDto;
import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.utils.enums.StateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PatientDoctorService {
    Logger logger = LoggerFactory.getLogger(PatientDoctorService.class);

    private PatientDoctorDao patientDoctorDao;

    public PatientDoctorService(PatientDoctorDao patientDoctorDao) {
        this.patientDoctorDao = patientDoctorDao;
    }

    @Transactional
    @IsDoctor
    public void add(PatientDoctorDto patientDoctorDto) {
        PatientParams patientParams = PatientParams.builder()
                                                    .idPatient(patientDoctorDto.getPatientDto().getIdPatient())
                                                    .idDoctor(patientDoctorDto.getDoctorDto().getIdUser())
                                                    .state(StateEnum.ACTIVE.getName())
                                                    .build();
        if (patientDoctorDao.getPatientDoctors(patientParams).isEmpty()) {
            Patient patient = Patient.builder().id(Integer.parseInt(patientDoctorDto.getPatientDto().getIdPatient())).build();
            User doctor = User.builder().id(Integer.parseInt(patientDoctorDto.getDoctorDto().getIdUser())).build();
            User createdBy = User.builder().id(Integer.parseInt(patientDoctorDto.getCreatedBy().getIdUser())).build();
            PatientDoctor pd = PatientDoctor.createPatientDoctor(patient, doctor, createdBy);
            patientDoctorDao.save(pd);
        } else {
            throw new IllegalOperationException("The doctor already has this patient!");
        }
    }

    /**
     * this methods set a PatientDoctor to DELETED state, Patient entity is not affected at all
     */
    @Transactional
    @IsDoctor
    public void delete(String idPatientDoctor, PatientParams patientParams) {
        if (null != idPatientDoctor && !idPatientDoctor.isEmpty()) {
            PatientDoctor patientDoctorOld = patientDoctorDao.getById(Integer.parseInt(idPatientDoctor));

            if (null != patientDoctorOld) {
                if (patientDoctorOld.getCreatedBy().getId() != Integer.parseInt(patientParams.getLoggedUser().getIdUser())) {
                    logger.info("You cannot delete patients of another doctor! Only your patients.");
                    throw new IllegalOperationException("You cannot delete patients of another doctor! Only your patients.");
                }
                if (patientDoctorOld.getState().equals(StateEnum.ACTIVE.getName()) && patientDoctorOld.getDeletedOn() == null) {
                    patientDoctorOld.setDeletedOn(LocalDateTime.now());
                    patientDoctorOld.setState(StateEnum.DELETED.getName());
                    patientDoctorDao.update(patientDoctorOld);
                } else {
                    logger.info("Operation failed because entity was already deleted!");
                    throw new IllegalOperationException("Operation failed because entity was already deleted!");
                }
            }
        }
    }
}
