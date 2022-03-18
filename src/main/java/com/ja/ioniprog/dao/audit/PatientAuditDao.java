package com.ja.ioniprog.dao.audit;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.audit.PatientAudit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PatientAuditDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(PatientAudit patientAudit) {
        entityManager.persist(patientAudit);
    }
}
