package com.cc.server;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * mqtt主要是通过主题topic来进行发布和订阅
 */
@Service
public class MqttServer {

    @Autowired
    private PushCallback pushCallback;

    private static MqttClient client;

    @Value("${spring.mqtt.username}")
    private String username;

    @Value("${spring.mqtt.password}")
    private String password;

    @Value("${spring.mqtt.url}")
    private String host;

    @Value("${spring.mqtt.server.id}")
    private String clientId;

    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;

    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout ;   //连接超时

    @Value("${spring.mqtt.keepalive}")
    private int keepalive ;   //连接超时

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MqttServer.client = client;
    }

    /**
     * 建立mqtt连接
     */
    public void connect(){
        try {
            client = new MqttClient(host, clientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(completionTimeout);
            options.setKeepAliveInterval(keepalive);
            MqttServer.setClient(client);
            try {
                client.setCallback(pushCallback);
                client.connect(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("mqtt server 连接成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭mqtt连接
     * @throws MqttException
     */
    public void close() throws MqttException {
        client.disconnect();
        System.out.println("mqtt server 已关闭");
    }

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,String pushMessage){
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,String pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MqttServer.getClient().getTopic(topic);
        if(null == mTopic){
            System.out.println("topic not exist");
        }
        MqttDeliveryToken token;
        try {
            token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){
        try {
            MqttServer.getClient().subscribe(topic, qos);
            System.out.println("mqtt server 订阅成功");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
