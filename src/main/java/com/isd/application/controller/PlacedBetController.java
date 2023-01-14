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

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/app/placedbet")
public class PlacedBetController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlacedBetService placedBetService;

    // TODO: bisogna modificare i body di response, fare vedere le squadre

    @GetMapping(path = "/{id}")
    public @ResponseBody
    PlacedBetDTO getPlacedbetById(@NotNull @PathVariable("id") Integer placedbetId) throws ResponseStatusException, Exception {
        return placedBetService.getByBetId(placedbetId);
    }

    @GetMapping(path = "/user/{id}")
    public @ResponseBody
    List<PlacedBetDTO> getAllPlacedbetByUserid(@NotNull @PathVariable("id") Integer userId) throws ResponseStatusException, Exception {
        return placedBetService.getAllByUserId(userId);
    }
}


