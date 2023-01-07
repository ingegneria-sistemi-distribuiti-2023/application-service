package com.isd.application.auth;

public interface AuthenticationService {
    boolean validateToken(String accessToken);
}
