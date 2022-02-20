package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.params.PatientParams;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PatientDoctorDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(PatientDoctor patientDoctor) {
        entityManager.persist(patientDoctor);
    }

    public List<PatientDoctor> getPagination(PatientParams patientParams) {
        TypedQuery<PatientDoctor> query = entityManager.createQuery("select pd from PatientDoctor pd", PatientDoctor.class);
        List<PatientDoctor> result = query.getResultList();
        return result;
    }
}
