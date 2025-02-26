package com.factory.rbtmanager.controller;

import com.factory.rbtmanager.model.dto.TelemetryRequest;
import com.factory.rbtmanager.service.TelemetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishTelemetry(@RequestBody TelemetryRequest telemetryRequest) {
        telemetryService.publishTelemetry(telemetryRequest);

        return ResponseEntity.ok("Telemetry published: " + telemetryRequest);
    }
}
