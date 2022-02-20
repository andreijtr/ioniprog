package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PatientDao {
    private final String GET_PATIENT_BY_ID = "select p from Patient p " +
                                             "where p.id = :idPatient";

    private final String GET_PATIENTS_FOR_DOCTOR = "select pd from PatientDoctor pd " +
                                                   "join fetch pd.doctor " +
                                                   "join fetch pd.patient " +
                                                   "order by pd.patient.lastName, pd.patient.firstName";

    @PersistenceContext
    private EntityManager entityManager;

    public int save(Patient patient) {
        entityManager.persist(patient);

        return patient.getId();
    }

    public Patient getById(int idPatient) {
        TypedQuery<Patient> query = entityManager.createQuery(GET_PATIENT_BY_ID, Patient.class);
        query.setParameter("idPatient", idPatient);

        return query.getSingleResult();
    }

    public List<PatientDoctor> getPatientForDoctor() {
        TypedQuery<PatientDoctor> query = entityManager.createQuery(GET_PATIENTS_FOR_DOCTOR, PatientDoctor.class)
                .setFirstResult(0)
                .setMaxResults(5);

        return query.getResultList();
    }
}
