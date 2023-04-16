package user.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaTopicConfig {
    @Value(value = "${config.kafka-config.topic.name}")
    private String topicName;

    @Value(value = "${config.kafka-config.address}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        log.info("bootstrapAddress kafka admin", bootstrapAddress);
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "54.232.4.177:19092,54.232.4.177:29092,54.232.4.177:39092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(topicName)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}
