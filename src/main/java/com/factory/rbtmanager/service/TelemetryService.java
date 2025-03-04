package com.factory.rbtmanager.service;

import com.factory.rbtmanager.model.dto.TelemetryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryService {

    private static final String DEFAULT_TELEMETRY_TOPIC = "robot/commands";
    private static final String ROBOT_STATUS_TOPIC = "robot/status";

    private final MqttService mqttService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void publishTelemetry(TelemetryRequest request) {
        try {
            var topic = Optional.ofNullable(request.getTopic()).orElse(DEFAULT_TELEMETRY_TOPIC);
            var payload = objectMapper.writeValueAsString(request.getData());

            mqttService.publish(topic, payload);
            log.info("Published telemetry to {}: {}", topic, payload);
        } catch (MqttException e) {
            log.error("Failed to publish telemetry: {}", e.getMessage());
        }
    }

    // Subscribe to telemetry topic on startup
    @PostConstruct
    public void init() {
        try {
            mqttService.subscribe(ROBOT_STATUS_TOPIC, message -> {
                var payload = new String(message.getPayload());
                log.info("Received telemetry from {}: {}", DEFAULT_TELEMETRY_TOPIC, payload);

                try {
                    var telemetryData = objectMapper.readValue(payload, Map.class);
                    log.info("Telemetry data: {}", telemetryData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            log.info("Subscribed to topic: {}", ROBOT_STATUS_TOPIC);
        } catch (MqttException e) {
            log.error("Failed to subscribe to telemetry topic: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to process telemetry: {}", e.getMessage());
        }
    }
}

