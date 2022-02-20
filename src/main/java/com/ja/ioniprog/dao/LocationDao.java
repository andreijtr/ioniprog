package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.Location;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class LocationDao {

    private final String GET_ACTIVE_USER_LOCATIONS = "SELECT l FROM Location l " +
                                                     "JOIN UserLocation ul " +
                                                     "WHERE ul.user.id = :idUser and ul.state = 'ACTIVE'";
    @PersistenceContext
    private EntityManager entityManager;

    public List<Location> getActiveLocation(String idUser) {
        TypedQuery<Location> query = entityManager.createQuery(GET_ACTIVE_USER_LOCATIONS, Location.class)
                                                  .setParameter("idUser", idUser);
        List<Location> locations = query.getResultList();

        return locations != null ? locations : Collections.emptyList();
    }
}
