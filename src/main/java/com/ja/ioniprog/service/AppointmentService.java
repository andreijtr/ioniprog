package com.ja.ioniprog.service;

import com.ja.ioniprog.config.security.annotations.IsDoctor;
import com.ja.ioniprog.dao.AppointmentDao;
import com.ja.ioniprog.dao.audit.AppointmentAuditDao;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.Appointment;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.AppointmentAudit;
import com.ja.ioniprog.model.entity.audit.Audit;
import com.ja.ioniprog.utils.enums.AuditEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {
    Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private AppointmentDao appointmentDao;
    private AppointmentAuditDao appointmentAuditDao;

    public AppointmentService(AppointmentDao appointmentDao, AppointmentAuditDao appointmentAuditDao) {
        this.appointmentDao = appointmentDao;
        this.appointmentAuditDao = appointmentAuditDao;
    }

    @Transactional
    @IsDoctor
    public void save(Appointment appointment, UserDto loggedUser) {
        logger.info("PatientService: save patient");

        appointmentDao.save(appointment);

        Audit audit = Audit.getAudit(AuditEnum.INSERT, User.builder().id(Integer.parseInt(loggedUser.getIdUser())).build(), null);
        AppointmentAudit appointmentAudit = new AppointmentAudit(appointment, audit);

        appointmentAuditDao.save(appointmentAudit);
    }
}
