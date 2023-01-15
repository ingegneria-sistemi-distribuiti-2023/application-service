package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.PlacedBetService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/placedbet")
public class PlacedBetController {
    private final PlacedBetService placedBetService;

    public PlacedBetController(PlacedBetService placedBetService) {
        this.placedBetService = placedBetService;
    }

    @GetMapping(path = "/{id}")
    public @ResponseBody
    ResponseEntity<PlacedBetDTO> getPlacedbetById(@NotNull @PathVariable("id") Integer placedbetId) throws Exception {
        // TODO: bisogna modificare i body di response, fare vedere le squadre
        return new ResponseEntity<>(placedBetService.getByBetId(placedbetId), HttpStatus.OK);
    }

    /**
     * Return all PlacedBets given a user id
     * */
    @GetMapping(path = "/user/{id}")
    public @ResponseBody ResponseEntity<List<PlacedBetDTO>> getAllPlacedbetByUserid(@NotNull @PathVariable("id") Integer userId) throws Exception {
        return new ResponseEntity<>(placedBetService.getAllBetsByUserId(userId), HttpStatus.OK);
    }
}


