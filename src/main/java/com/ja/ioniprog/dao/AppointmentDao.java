package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.Appointment;
import com.ja.ioniprog.model.params.AppointmentParams;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentDao {

    private final String GET_APPOINTMENTS = "select a from Appointment a " +
                                            "where (:id is null or a.id = :id) " +
                                            "and (:idDoctor is null or a.doctor.id = :idDoctor) " +
                                            "and (:idLocation is null or a.location.id = :idLocation) " +
                                            "and (:idPatient is null or a.patient.id = :idPatient) " +
                                            "and (:day is null or a.day = :day) " +
                                            "and (:startCalendar is null or a.day >= :startCalendar) " +
                                            "and (:endCalendar is null or a.day <= :endCalendar) ";

    @PersistenceContext
    private EntityManager entityManager;

    public int save(Appointment appointment) {
        entityManager.persist(appointment);

        return appointment.getId();
    }

    public Optional<List<Appointment>> getAppointments(AppointmentParams params) {
        TypedQuery<Appointment> query = entityManager.createQuery(GET_APPOINTMENTS, Appointment.class)
                                                    .setParameter("id", params.getId())
                                                    .setParameter("idDoctor", params.getIdDoctor())
                                                    .setParameter("idLocation", params.getIdLocation())
                                                    .setParameter("idPatient", params.getIdPatient())
                                                    .setParameter("day", params.getDay())
                                                    .setParameter("startCalendar", params.getStartCalendar())
                                                    .setParameter("endCalendar", params.getEndCalendar());

        List<Appointment> appointments = query.getResultList();
        return Optional.ofNullable(appointments);
    }
}
