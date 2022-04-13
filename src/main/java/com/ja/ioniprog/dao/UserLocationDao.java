package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.UserLocation;
import com.ja.ioniprog.model.params.LocationParams;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class UserLocationDao {

    private final String GET_USER_LOCATIONS = "SELECT ul FROM UserLocation ul " +
                                              "WHERE " +
                                                    "(:idUser is null or ul.user.id = :idUser) and " +
                                                    "(:state is null or ul.state = :state) " +
                                              "order by ul.id";
    @PersistenceContext
    private EntityManager entityManager;

    public List<UserLocation> getUserLocations(LocationParams params) {
        TypedQuery<UserLocation> query = entityManager.createQuery(GET_USER_LOCATIONS, UserLocation.class)
                                                  .setParameter("idUser", params.getIdDoctor() != null ? Integer.parseInt(params.getIdDoctor()) : null)
                                                  .setParameter("state", params.getState());
        List<UserLocation> userLocations = query.getResultList();

        return userLocations != null ? userLocations : Collections.emptyList();
    }
}
