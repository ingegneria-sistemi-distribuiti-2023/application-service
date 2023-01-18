package com.isd.application.auth;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
@NoArgsConstructor
@Configuration
public class SecretKeyInterceptor implements ClientHttpRequestInterceptor  {
    private String SECRET_AUTH;
    private String SECRET_GAME;
    private String SECRET_SESSION;

    private static final String AUTH = "/auth/";
    private static final String GAME = "/game/";
    private static final String SESSION = "/session";
    private final static String SECRET_HEADER = "Secret-Key";

    public SecretKeyInterceptor(String sAuth, String sGame, String sSession) {
        this.SECRET_AUTH = sAuth;
        this.SECRET_GAME = sGame;
        this.SECRET_SESSION = sSession;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();

        String path = request.getURI().getPath();

        if (path.startsWith(AUTH)) {
            headers.add(SECRET_HEADER, SECRET_AUTH);
        } else if (path.startsWith(GAME)) {
            headers.add(SECRET_HEADER, SECRET_GAME);
        } else if (path.startsWith(SESSION)) {
            headers.add(SECRET_HEADER, SECRET_SESSION);
        }

        return execution.execute(request, body);
    }
}
