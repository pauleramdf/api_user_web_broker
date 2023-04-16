package user.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import user.dto.stocks.StockPricesDTO;


@Service
@RequiredArgsConstructor
public class KafkaService {
    @Value(value = "${config.kafka-config.topic.name}")
    private String topic;
    private final KafkaTemplate<String, StockPricesDTO> kafkaTemplate;

    public void sendStockPricesDTO(String key, StockPricesDTO value){
        kafkaTemplate.send(new ProducerRecord<>(topic, key, value));
    }
}
