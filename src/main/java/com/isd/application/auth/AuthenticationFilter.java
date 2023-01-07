package com.isd.application.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Remote Authenticator
 * Create class that validate token to all request to sessionService
 * */
@Component
public class AuthenticationFilter implements Filter {
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String accessToken = httpRequest.getHeader("Access-Token");

        // TODO: è corretto fare una cosa di questa ? altrimenti authenticationService è null
        authenticationService = new AuthenticationServiceImpl();

        if (authenticationService.validateToken(accessToken)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendError(HttpStatus.UNAUTHORIZED.value());
        }

    }
}