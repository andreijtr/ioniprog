package com.ja.ioniprog.dao.audit;

import com.ja.ioniprog.model.entity.audit.PatientAudit;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PatientAuditDao {

    private static final String GET_AUDIT_BY_ID_PATIENT = "SELECT pa FROM PatientAudit pa where pa.patientEntity.id = :idPatient order by pa.id";

    @PersistenceContext
    private EntityManager entityManager;

    public List<PatientAudit> getAuditByIdPatient(int idPatient) {
        TypedQuery<PatientAudit> query = entityManager.createQuery(GET_AUDIT_BY_ID_PATIENT, PatientAudit.class);
        query.setParameter("idPatient", idPatient);
        return query.getResultList();
    }

    public void save(PatientAudit patientAudit) {
        entityManager.persist(patientAudit);
    }
}
