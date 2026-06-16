package com.sustainabilitytracker.sustainabilitytracker.controllers;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RejectRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.WasteRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.WasteResponse;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.WasteSummaryResponse;
import com.sustainabilitytracker.sustainabilitytracker.repositories.WasteRepository;
import com.sustainabilitytracker.sustainabilitytracker.services.WasteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/waste")
public class WasteController {
}
