package com.factory.rbtmanager.service;

import com.factory.rbtmanager.model.dto.TelemetryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TelemetryService {

    private static final Logger logger = LoggerFactory.getLogger(TelemetryService.class);
    private static final String DEFAULT_TELEMETRY_TOPIC = "fleet/robot/telemetry";

    private final MqttService mqttService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void publishTelemetry(TelemetryRequest request) {
        try {
            var topic = Optional.ofNullable(request.getTopic()).orElse(DEFAULT_TELEMETRY_TOPIC);
            var payload = objectMapper.writeValueAsString(request.getData());

            mqttService.publish(topic, payload);
            logger.info("Published telemetry to {}: {}", topic, payload);
        } catch (MqttException e) {
            logger.error("Failed to publish telemetry: {}", e.getMessage());
        }
    }

    // Subscribe to telemetry topic on startup
    @PostConstruct
    public void init() {
        try {
            mqttService.subscribe(DEFAULT_TELEMETRY_TOPIC, message -> {
                var payload = new String(message.getPayload());
                logger.info("Received telemetry from {}: {}", DEFAULT_TELEMETRY_TOPIC, payload);

                try {
                    var telemetryData = objectMapper.readValue(payload, Map.class);
                    logger.info("Telemetry data: {}", telemetryData);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

            logger.info("Subscribed to topic: {}", DEFAULT_TELEMETRY_TOPIC);
        } catch (MqttException e) {
            logger.error("Failed to subscribe to telemetry topic: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to process telemetry: {}", e.getMessage());
        }
    }
}
