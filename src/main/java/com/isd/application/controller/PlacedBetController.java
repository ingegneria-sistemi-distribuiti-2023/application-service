package com.isd.application.controller;

import com.isd.application.dto.*;
import com.isd.application.service.PlacedBetService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/placedbet")
@RequiredArgsConstructor
public class PlacedBetController {
    private final PlacedBetService placedBetService;

    @GetMapping(path = "/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<PlacedBetDTO> getPlacedbetById(@NotNull @PathVariable("id") Integer placedbetId, @RequestHeader("Username") String username) throws Exception {
        return new ResponseEntity<>(placedBetService.getByBetId(placedbetId), HttpStatus.OK);
    }

    /**
     * Return all PlacedBets given a user id
     * */
    @GetMapping(path = "/user/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public @ResponseBody ResponseEntity<List<PlacedBetDTO>> getAllPlacedbetByUserid(@NotNull @PathVariable("id") Integer userId, @RequestHeader("Username") String username) throws Exception {
        return new ResponseEntity<>(placedBetService.getAllBetsByUserId(userId), HttpStatus.OK);
    }
}


