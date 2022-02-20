package com.ja.ioniprog.service;

import com.ja.ioniprog.config.persistence.PersistenceConfig;
import com.ja.ioniprog.dao.UserDao;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.params.UserParams;
import com.ja.ioniprog.utils.enums.ApplicationEnum;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(PersistenceConfig.class);

    private UserDao userDao;
    private Mapper dozerMapper;

    @Autowired
    public UserService(UserDao userDao, Mapper dozerMapper) {
        this.userDao = userDao;
        this.dozerMapper = dozerMapper;
    }

    @Transactional(readOnly = true)
    public User loadUserByUsername(String username) {
        List<User> user = userDao.loadUserByUsername(username);

        return (user != null && !user.isEmpty()) ? user.get(0) : null;
    }

    @Transactional(readOnly = true)
    public User getUserById(String idUser) {
        UserParams params = UserParams.builder().id(idUser).build();
        List<User> user = userDao.getUsers(params);

        return user.get(0);
    }

    @Transactional
    public void decreaseLoginAttempts(String username) {
        logger.info("UserService: decrease login attempts");
        UserParams userParam = UserParams.builder().username(username).build();
        User user = userDao.getUsers(userParam).get(0);
        int loginAttemptsRemaining = user.getLoginAttemptsRemaining();

        if (loginAttemptsRemaining > 0) {
            loginAttemptsRemaining--;
            user.setLoginAttemptsRemaining(loginAttemptsRemaining);
            userDao.updateUser(user);
        }
    }

    @Transactional
    public void resetLoginAttempts(String username) {
        logger.info("UserService: reset login attempts");
        int loginAttempts = Integer.parseInt(ApplicationEnum.LOGIN_ATTEMPTS.getName());

        UserParams userParam = UserParams.builder().username(username).build();
        User user = userDao.getUsers(userParam).get(0);

        user.setLoginAttemptsRemaining(loginAttempts);
        userDao.updateUser(user);
    }
}
