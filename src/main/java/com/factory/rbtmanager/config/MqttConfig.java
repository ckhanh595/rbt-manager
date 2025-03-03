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
        var broker = "tcp://10.0.2.2:1883";  // Connect from VM to host
        var clientId = "RobotManager_" + System.currentTimeMillis();
        var client = new MqttClient(broker, clientId);
        var options = new MqttConnectOptions();
        options.setCleanSession(true); // Fresh session on connect
        options.setAutomaticReconnect(true); // Auto-reconnect if connection is lost
        options.setConnectionTimeout(10);    // Timeout in seconds
        options.setKeepAliveInterval(60);    // Heartbeat check
        client.connect(options);

        return client;
    }
}