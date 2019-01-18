package com.github.ydespreaux.testcontainers.kafka.test;

import com.github.ydespreaux.testcontainers.kafka.test.domain.WorkstationAvro;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
public class ITKafkaTemplateFactoryTest {


    @Test
    public void createKafkaTemplateWithOptionalProperties() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        Map<String, Object> optional = new HashMap<>();
        optional.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        optional.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainer).createKafkaTemplate(optional);
        ListenableFuture<SendResult<String, String>> future = template.send(ITAllSuiteTest.TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

    @Test
    public void createKafkaTemplateWithKeyAndValueSerializerStr() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainer)
                        .createKafkaTemplate("org.apache.kafka.common.serialization.StringSerializer", "org.apache.kafka.common.serialization.StringSerializer");
        ListenableFuture<SendResult<String, String>> future = template.send(ITAllSuiteTest.TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

    @Test
    public void createKafkaTemplateWithOptionalPropertiesAndKeyAndValueSerializerStr() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        Map<String, Object> optional = new HashMap<>();
        optional.put(ProducerConfig.ACKS_CONFIG, "all");
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainer)
                        .createKafkaTemplate(optional, "org.apache.kafka.common.serialization.StringSerializer", "org.apache.kafka.common.serialization.StringSerializer");
        ListenableFuture<SendResult<String, String>> future = template.send(ITAllSuiteTest.TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

    @Test
    public void createKafkaTemplateWithKeyAndValueSerializer() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainer)
                        .createKafkaTemplate(new StringSerializer(), new StringSerializer());
        ListenableFuture<SendResult<String, String>> future = template.send(ITAllSuiteTest.TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

    @Test
    public void createKafkaTemplateWithOptionalPropertiesAndKeyAndValueSerializer() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        Map<String, Object> optional = new HashMap<>();
        optional.put(ProducerConfig.ACKS_CONFIG, "all");
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainer)
                        .createKafkaTemplate(optional, new StringSerializer(), new StringSerializer());
        ListenableFuture<SendResult<String, String>> future = template.send(ITAllSuiteTest.TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

    @Test
    public void createKafkaTemplateWithAvroValue() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        KafkaTemplate<String, Object> template =
                new KafkaTemplateFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry)
                        .createKafkaTemplate(
                                new StringSerializer(),
                                new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(false));

        WorkstationAvro workstation = WorkstationAvro.newBuilder().setId(1L).setName("WS-123456").setSerialNumber("SERIAL-000001").build();

        ListenableFuture<SendResult<String, Object>> future = template.send(ITAllSuiteTest.TOPIC_AVRO_PRODUCER, "WKS-1", workstation);
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

}