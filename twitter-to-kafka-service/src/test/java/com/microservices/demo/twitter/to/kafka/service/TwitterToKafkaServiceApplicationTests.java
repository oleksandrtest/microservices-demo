package com.microservices.demo.twitter.to.kafka.service;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@Import(MockSerdeConfig.class)
public class TwitterToKafkaServiceApplicationTests {

    @Autowired
    private KafkaProducer<Long, TwitterAvroModel> kafkaProducer;

    @Test
    public void testKafka() throws InterruptedException {
        TwitterAvroModel twitterAvroModel = TwitterAvroModel.newBuilder()
                .setId(12345L)
                .setUserId(1234L)
                .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .setText("TEST")
                .build();
        kafkaProducer.send("twitter-topic", twitterAvroModel.getUserId(), twitterAvroModel);
        Thread.sleep(20000L);
    }
}
