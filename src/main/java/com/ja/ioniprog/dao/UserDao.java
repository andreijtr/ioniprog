package com.ja.ioniprog.dao;

import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.params.UserParams;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
public class UserDao {

    private final String LOAD_USER_BY_USERNAME = "SELECT u FROM User u "  +
                                                 "JOIN FETCH u.roles ur " +
                                                 "JOIN FETCH ur.role "    +
                                                 "WHERE u.username = :username AND ur.notExpired = 1";

    private final String GET_USERS             = "SELECT u FROM User u "                              +
                                                 "WHERE (:id is null or u.id = :id) "                 +
                                                 "AND (:username is null or u.username = :username) " +
                                                 "AND (:expired is null or u.expired = :expired)";

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> loadUserByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(LOAD_USER_BY_USERNAME, User.class)
                                              .setParameter("username", username);
        return query.getResultList();
    }

    public List<User> getUsers(UserParams userParam) {
        List<User> users = null;
        if (userParam != null) {
            TypedQuery<User> query = entityManager.createQuery(GET_USERS, User.class)
                    .setParameter("id", userParam.getId())
                    .setParameter("username", userParam.getUsername())
                    .setParameter("expired", userParam.getExpired());
            users = query.getResultList();
        }

        return users == null ? Collections.emptyList() : users;
    }

    public void updateUser(User user) {
        Session session = entityManager.unwrap(Session.class);
        session.update(user);
    }
}
