package com.ja.ioniprog.dao.audit;

import com.ja.ioniprog.model.entity.audit.AppointmentAudit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AppointmentAuditDao {

    @PersistenceContext
    private EntityManager entityManager;

    public int save(AppointmentAudit appointmentAudit) {
        entityManager.persist(appointmentAudit);

        return appointmentAudit.getId();
    }
}
