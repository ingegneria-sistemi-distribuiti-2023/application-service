package com.isd.application.controller;

import com.isd.application.commons.CurrencyEnum;
import com.isd.application.commons.PlacedBetEnum;
import com.isd.application.commons.TransactionStatus;
import com.isd.application.dto.*;
import com.isd.application.repository.PlacedBetRepository;
import com.isd.application.service.PlacedBetService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/app/placedbet")
public class PlacedBetController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlacedBetService placedBetService;

    @GetMapping(path = "/{id}")
    public @ResponseBody
    PlacedBetDTO getPlacedbetById(@PathVariable("id") String placedbetId) throws ResponseStatusException, Exception {
        PlacedBetDTO toRet = new PlacedBetDTO();

        return toRet;
    }

    @GetMapping(path = "/user/{id}")
    public @ResponseBody
    PlacedBetDTO getAllPlacedbetByUserid(@PathVariable("id") String userId) throws ResponseStatusException, Exception {
        PlacedBetDTO toRet = new PlacedBetDTO();

        return toRet;
    }
}


