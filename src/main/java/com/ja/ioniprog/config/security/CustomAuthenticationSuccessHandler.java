package com.ja.ioniprog.config.security;

import com.ja.ioniprog.builder.UserBuilder;
import com.ja.ioniprog.config.persistence.PersistenceConfig;
import com.ja.ioniprog.model.dto.UserDto;
import com.ja.ioniprog.utils.application.LoggedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    private UserBuilder userBuilder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        logger.info("User successful logged in!");
        UserDto userConnected = userBuilder.build(authentication.getName());
        LoggedUser.set(request, userConnected);

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/dashboard.html");
    }
}
