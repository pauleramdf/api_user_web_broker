package user.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import user.dto.stocks.StockPricesDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {

    @Value(value = "${config.kafka-config.address}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, StockPricesDTO> KafkaProducerFactory() {
        log.info("bootstrapAddress",bootstrapAddress);
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "54.232.4.177:19092,54.232.4.177:29092,54.232.4.177:39092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "3000");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        props.put(ProducerConfig.LINGER_MS_CONFIG, "300");
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(256 * 1024));

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, StockPricesDTO> kafkaTemplate() {
        return new KafkaTemplate<>(KafkaProducerFactory());
    }
}
