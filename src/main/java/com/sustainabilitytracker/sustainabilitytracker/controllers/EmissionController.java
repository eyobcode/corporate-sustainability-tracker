package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.EmissionRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.EmissionResponse;
import com.sustainabilitytracker.sustainabilitytracker.services.EmissionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/emissions")
@AllArgsConstructor
public class EmissionController {
    private final EmissionService emissionService;

    @PostMapping
    public ResponseEntity<EmissionResponse> submitEmission(
            @Valid
            @RequestBody EmissionRequest emissionRequest,
            UriComponentsBuilder uriBuilder){
        EmissionResponse emissionResponse = emissionService.submitEmission(emissionRequest);
        var uri = uriBuilder.path("/emissions/{emissionsId}").buildAndExpand(emissionResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(emissionResponse);
    }
}
