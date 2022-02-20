package com.ja.ioniprog.utils.application;

import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.utils.enums.ApplicationEnum;

import javax.servlet.http.HttpServletRequest;

public class LoggedUser {
    public static UserDto get(HttpServletRequest request) {
        return (UserDto) request.getSession().getAttribute(ApplicationEnum.LOGGED_USER.getName());
    }

    public static void set(HttpServletRequest request, UserDto userDto) {
        request.getSession().setAttribute(ApplicationEnum.LOGGED_USER.getName(), userDto);
    }
}
