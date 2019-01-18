package com.github.ydespreaux.testcontainers.kafka.test;

import com.github.ydespreaux.testcontainers.kafka.test.domain.WorkstationAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.github.ydespreaux.testcontainers.kafka.test.ITAllSuiteTest.kafkaContainer;
import static com.github.ydespreaux.testcontainers.kafka.test.ITAllSuiteTest.kafkaContainerWithSchemaRegistry;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class ITKafkaMessageListenerContainerFactoryTest {

    @Test
    public void createListenerContainerWithSingleTopic() throws Exception {
        KafkaMessageListenerContainer<String, String> listenerContainer = null;
        try {
            /**
             * List des messages reçus
             */
            BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
            listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainer)
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            ITAllSuiteTest.TOPIC_STRING_CONSUMER1,
                            ITAllSuiteTest.TOPIC_STRING_CONSUMER1_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(kafkaContainer)
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(ITAllSuiteTest.TOPIC_STRING_CONSUMER1, "WKS-1", "My message");

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-1")));
            assertThat(record.value(), is(equalTo("My message")));
        } finally {
            if (listenerContainer != null) {
                listenerContainer.stop();
            }
        }
    }

    @Test
    public void createListenerContainerWithMultiTopic() throws Exception {
        KafkaMessageListenerContainer<String, String> listenerContainer = null;
        try {
            /**
             * List des messages reçus
             */
            BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
            listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainer)
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            new String[]{ITAllSuiteTest.TOPIC_STRING_CONSUMER2, ITAllSuiteTest.TOPIC_STRING_CONSUMER3},
                            ITAllSuiteTest.TOPIC_STRING_CONSUMER2_PARTITIONS + ITAllSuiteTest.TOPIC_STRING_CONSUMER3_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(kafkaContainer)
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(ITAllSuiteTest.TOPIC_STRING_CONSUMER2, "WKS-2", "My message 2");

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-2")));
            assertThat(record.value(), is(equalTo("My message 2")));

            template.send(ITAllSuiteTest.TOPIC_STRING_CONSUMER3, "WKS-3", "My message 3");

            record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-3")));
            assertThat(record.value(), is(equalTo("My message 3")));
        } finally {
            if (listenerContainer != null) {
                listenerContainer.stop();
            }
        }
    }

    @Test
    public void createListenerContainerWithPatternTopic() throws Exception {
        KafkaMessageListenerContainer<String, String> listenerContainer = null;
        try {
            /**
             * List des messages reçus
             */
            BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
            listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainer)
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            Pattern.compile("topic-string-pattern.*"),
                            ITAllSuiteTest.TOPIC_STRING_CONSUMER4_PARTITIONS + ITAllSuiteTest.TOPIC_STRING_CONSUMER5_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(kafkaContainer)
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(ITAllSuiteTest.TOPIC_STRING_CONSUMER4, "WKS-4", "My message 4");

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-4")));
            assertThat(record.value(), is(equalTo("My message 4")));

            template.send(ITAllSuiteTest.TOPIC_STRING_CONSUMER5, "WKS-5", "My message 5");

            record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-5")));
            assertThat(record.value(), is(equalTo("My message 5")));
        } finally {
            if (listenerContainer != null) {
                listenerContainer.stop();
            }
        }
    }

    @Test
    public void consumeMessageAvro() throws Exception {
        KafkaMessageListenerContainer<String, Object> listenerContainer = null;
        try {
            /**
             * List des messages reçus
             */
            BlockingQueue<ConsumerRecord<String, WorkstationAvro>> records = new LinkedBlockingQueue<>();
            listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainerWithSchemaRegistry)
                    .createListenerContainer(
                            new StringDeserializer(),
                            new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(false, true),
                            "an_group",
                            ITAllSuiteTest.TOPIC_AVRO_CONSUMER,
                            ITAllSuiteTest.TOPIC_AVRO_CONSUMER_PARTITIONS,
                            (MessageListener<String, WorkstationAvro>) record -> records.add(record));

            KafkaTemplate<String, Object> template = new KafkaTemplateFactory(kafkaContainerWithSchemaRegistry)
                    .createKafkaTemplate(
                            new StringSerializer(),
                            new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(false));

            WorkstationAvro workstation = WorkstationAvro.newBuilder().setId(2L).setName("WS-123456").setSerialNumber("SERIAL-000002").build();
            template.send(ITAllSuiteTest.TOPIC_AVRO_CONSUMER, "WKS-6", workstation);

            ConsumerRecord<String, WorkstationAvro> record = records.poll(60, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-6")));
            WorkstationAvro value = record.value();
            assertThat(value.getId(), is(equalTo(value.getId())));
            assertThat(value.getName(), is(equalTo(value.getName())));
            assertThat(value.getSerialNumber(), is(equalTo(value.getSerialNumber())));
        } finally {
            if (listenerContainer != null) {
                listenerContainer.stop();
            }
        }

    }

}