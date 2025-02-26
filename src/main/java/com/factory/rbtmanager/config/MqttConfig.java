package com.factory.rbtmanager.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Bean
    public MqttClient mqttClient() throws MqttException {
        var broker = "tcp://localhost:1883";
        var clientId = "RobotManager_" + System.currentTimeMillis();
        var client = new MqttClient(broker, clientId);
        var options = new MqttConnectOptions();
        options.setCleanSession(true); // Fresh session on connect
        client.connect(options);

        return client;
    }
}