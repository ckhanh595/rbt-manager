package com.factory.rbtmanager.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class MqttService {

    private final MqttClient mqttClient;

    @Autowired
    public MqttService(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish(String topic, String message) throws MqttException {
        var mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    public void subscribe(String topic, Consumer<MqttMessage> messageHandler) throws MqttException {
        mqttClient.subscribe(topic, (topic1, message) -> messageHandler.accept(message));
    }
}