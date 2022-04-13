package com.ja.ioniprog.service;

import com.ja.ioniprog.config.security.annotations.IsDoctor;
import com.ja.ioniprog.dao.UserLocationDao;
import com.ja.ioniprog.model.dto.audit.UserLocationDto;
import com.ja.ioniprog.model.entity.UserLocation;
import com.ja.ioniprog.model.params.LocationParams;
import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLocationService {

    private UserLocationDao userLocationDao;
    private Mapper dozerMapper;

    public UserLocationService(UserLocationDao userLocationDao, Mapper dozerMapper) {
        this.userLocationDao = userLocationDao;
        this.dozerMapper = dozerMapper;
    }

    @IsDoctor
    public List<UserLocationDto> getUserLocations(LocationParams params) {
        List<UserLocation> userLocations = userLocationDao.getUserLocations(params);

        return userLocations.stream()
                            .map(userLocation -> dozerMapper.map(userLocation, UserLocationDto.class))
                            .collect(Collectors.toList());
    }
}
