package com.ja.ioniprog.unit;

import com.ja.ioniprog.config.security.annotations.IsDoctor;
import com.ja.ioniprog.dao.PatientDoctorDao;
import com.ja.ioniprog.exception.IllegalOperationException;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.params.PatientParams;
import com.ja.ioniprog.utils.enums.StateEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.time.LocalDateTime;

@Service
public class PatientDoctorService {
    Logger logger = LoggerFactory.getLogger(PatientDoctorService.class);

    private PatientDoctorDao patientDoctorDao;

    public PatientDoctorService(PatientDoctorDao patientDoctorDao) {
        this.patientDoctorDao = patientDoctorDao;
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
