package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.Patient;
import com.ja.ioniprog.model.entity.PatientDoctor;
import com.ja.ioniprog.model.params.PatientParams;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PatientDao {
    private final String GET_PATIENT_BY_ID = "select p from Patient p " +
                                             "where p.id = :idPatient";

    private final String GET_PATIENTS_PAGING =  "select pd " +
                                                "from PatientDoctor pd " +
                                                    "join pd.patient p " +
                                                "where " +
                                                    "pd.doctor.id = :idDoctor and " +
                                                    "(:state is null or pd.state = : state) and " +
                                                    "(:globalSearch is null or (p.firstName = :globalSearch " +
                                                                            "or p.lastName = :globalSearch " +
                                                                            "or p.phone = :globalSearch)) " +
                                               "order by p.lastName, p.firstName";

    private final String GET_COUNT_PATIENTS =   "select count(pd.id) " +
                                                "from PatientDoctor pd " +
                                                    "join pd.patient p " +
                                                "where " +
                                                    "pd.doctor.id = :idDoctor and " +
                                                    "(:state is null or pd.state = : state) and " +
                                                    "(:globalSearch is null or (p.firstName = :globalSearch " +
                                                                            "or p.lastName = :globalSearch " +
                                                                            "or p.phone = :globalSearch)) " +
                                                "order by p.lastName, p.firstName";

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

    public List<PatientDoctor> getPatientsPaging(PatientParams patientParams) {
        TypedQuery<PatientDoctor> query = entityManager.createQuery(GET_PATIENTS_PAGING, PatientDoctor.class)
                                                        .setParameter("idDoctor", Integer.parseInt(patientParams.getDoctor().getIdUser()))
                                                        .setParameter("globalSearch", patientParams.getGlobalSearch())
                                                        .setParameter("state", patientParams.getState())
                                                        .setFirstResult(patientParams.getOffset())
                                                        .setMaxResults(patientParams.getPageSize());
        return query.getResultList();
    }

    public long getCountPaging(PatientParams patientParams) {
        Query query = entityManager.createQuery(GET_COUNT_PATIENTS)
                                    .setParameter("idDoctor", Integer.parseInt(patientParams.getDoctor().getIdUser()))
                                    .setParameter("globalSearch", patientParams.getGlobalSearch())
                                    .setParameter("state", patientParams.getState());
        return (Long) query.getSingleResult();
    }
}
