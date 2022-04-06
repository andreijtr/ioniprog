package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.params.PatientParams;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PatientDoctorDao {

    public static final String GET_PATIENT_DOCTORS = "SELECT pd " +
                                                     "FROM PatientDoctor pd " +
                                                        "JOIN FETCH pd.patient p " +
                                                        "JOIN FETCH pd.doctor d " +
                                                        "JOIN FETCH pd.createdBy " +
                                                     "where " +
                                                        "(:idPatient is null or p.id = :idPatient) and " +
                                                        "(:idDoctor is null or d.id = :idDoctor) and " +
                                                        "(:state is null or pd.state = :state) " +
                                                     "order by pd.id";

    @PersistenceContext
    private EntityManager entityManager;

    public PatientDoctor getById(int idPatientDoctor) {
        return entityManager.find(PatientDoctor.class, idPatientDoctor);
    }

    public List<PatientDoctor> getPatientDoctors(PatientParams patientParams) {
        TypedQuery<PatientDoctor> query = entityManager.createQuery(GET_PATIENT_DOCTORS, PatientDoctor.class);
        query.setParameter("idPatient", patientParams.getIdPatient() == null ? null : Integer.parseInt(patientParams.getIdPatient()));
        query.setParameter("idDoctor", patientParams.getIdDoctor() == null ? null : Integer.parseInt(patientParams.getIdDoctor()));
        query.setParameter("state", patientParams.getState());
        return query.getResultList();
    }

    public void save(PatientDoctor patientDoctor) {
        entityManager.persist(patientDoctor);
    }

    public void update(PatientDoctor patientDoctor) {
        Session session = entityManager.unwrap(Session.class);
        session.update(patientDoctor);
    }
}
