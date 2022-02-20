package com.ja.ioniprog.builder;

import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.model.entity.User;
import com.ja.ioniprog.model.entity.UserRole;
import com.ja.ioniprog.service.UserService;
import com.ja.ioniprog.utils.enums.RoleEnum;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserBuilder {

    private UserService userService;
    private DozerBeanMapper mapper;

    @Autowired
    public UserBuilder(UserService userService, DozerBeanMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    public UserDto build(String username) {
        User user = userService.loadUserByUsername(username);
        UserDto userDto = mapper.map(user , UserDto.class);

        List<UserRole> roles = user.getRoles();

        for (UserRole userRole : roles) {
            if (userRole.getRole().getName().contentEquals(RoleEnum.DOCTOR.getName())) {
                userDto.getRoles().add(RoleEnum.DOCTOR.getName());
            }
            else if (userRole.getRole().getName().contentEquals(RoleEnum.MANAGER.getName())) {
                userDto.getRoles().add(RoleEnum.MANAGER.getName());
            }
            else if (userRole.getRole().getName().contentEquals(RoleEnum.ADMIN.getName())) {
                userDto.getRoles().add(RoleEnum.ADMIN.getName());
            }
        }
        return userDto;
    }
}
