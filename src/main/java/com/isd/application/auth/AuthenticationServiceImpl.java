package com.isd.application.auth;

import org.springframework.stereotype.Service;

/**
 * This service validate the accessToken sended from 'Access-Token' field.
 *
 * */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Override
    public boolean validateToken(String accessToken) {
        // Validate the access token using the database or any other method

        if (accessToken != null && accessToken.equals("pippo")){
            return true;
        }

        return false;
    }
}
