package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.PatientDoctor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PatientDoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    public PatientDoctor getById(int idPatientDoctor) {
        return entityManager.find(PatientDoctor.class, idPatientDoctor);
    }

    public void save(PatientDoctor patientDoctor) {
        entityManager.persist(patientDoctor);
    }

    public void update(PatientDoctor patientDoctor) {
        Session session = entityManager.unwrap(Session.class);
        session.update(patientDoctor);
    }


}
