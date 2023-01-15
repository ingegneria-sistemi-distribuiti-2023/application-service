package com.isd.application.service;

import com.isd.application.commons.CustomHttpResponse;
import com.isd.application.commons.CustomServiceException;
import com.isd.application.dto.TransactionRequestDTO;
import com.isd.application.dto.TransactionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {
    private final RestTemplate restTemplate;

    @Value("${auth.service.url}")
    String authServiceUrl;

    public TransactionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransactionResponseDTO depositToAuth(TransactionRequestDTO req, String token) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<TransactionRequestDTO> request = new HttpEntity<>(req, headers);

        ResponseEntity<TransactionResponseDTO> transaction = restTemplate.exchange(
                authServiceUrl + "/auth/transaction/deposit", HttpMethod.POST, request,
                new ParameterizedTypeReference<TransactionResponseDTO>() {});
        // verify status code of request
        if (transaction.getStatusCode() != HttpStatus.OK) {
            // TODO: to remove
            throw new CustomServiceException(new CustomHttpResponse(HttpStatus.NOT_FOUND, "User Not founded"));
        }

        return transaction.getBody();
    }

}
