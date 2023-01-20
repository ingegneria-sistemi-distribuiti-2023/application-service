package com.isd.application.auth;

import com.isd.application.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiPathFilter extends OncePerRequestFilter {
    private AuthenticationService auth;

    private static final String HEADER_BEARER = "Bearer ";
    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_USERNAME = "Username";

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPathFilter.class);

    public ApiPathFilter(AuthenticationService auth) {
        this.auth = auth;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        if (path.startsWith("/app/public") || path.contains("swagger") || path.equals("/v3/api-docs")  || path.equals("/favicon.ico") || path.equals("/actuator/health") ){
            // Non bisogna gestire la get dell'Header
            filterChain.doFilter(request, response);
            return;
        } else if (!path.startsWith("/app/")){
            // Gestire i token
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String jwt = request.getHeader(HEADER_AUTH);
        String username = request.getHeader(HEADER_USERNAME);

        if (jwt == null || username == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        jwt = jwt.substring(HEADER_BEARER.length());

        Boolean isValid = auth.validate(username, jwt);

        LOGGER.info("validation for : " + username + ": " + isValid.toString());

        request.setAttribute(HEADER_AUTH, HEADER_BEARER + jwt);

        filterChain.doFilter(request, response);

    }
}
