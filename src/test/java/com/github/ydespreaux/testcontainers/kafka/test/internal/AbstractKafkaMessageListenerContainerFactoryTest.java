/*
 * Copyright (C) 2018 Yoann Despréaux
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING . If not, write to the
 * Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Please send bugreports with examples or suggestions to yoann.despreaux@believeit.fr
 */

package com.github.ydespreaux.testcontainers.kafka.test.internal;

import com.github.ydespreaux.testcontainers.kafka.rule.ConfluentKafkaContainer;
import com.github.ydespreaux.testcontainers.kafka.test.AvroSerializerFactory;
import com.github.ydespreaux.testcontainers.kafka.test.KafkaMessageListenerContainerFactory;
import com.github.ydespreaux.testcontainers.kafka.test.KafkaTemplateFactory;
import com.github.ydespreaux.testcontainers.kafka.test.domain.WorkstationAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.github.ydespreaux.testcontainers.kafka.test.internal.BrokerConfiguration.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public abstract class AbstractKafkaMessageListenerContainerFactoryTest {

    protected abstract ConfluentKafkaContainer getKafkacontainer();

    @Test
    public void createListenerContainerWithSingleTopic() throws Exception {
        KafkaMessageListenerContainer<String, String> listenerContainer = null;
        try {
            /**
             * List des messages reçus
             */
            BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
            listenerContainer = new KafkaMessageListenerContainerFactory(getKafkacontainer())
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            TOPIC_STRING_CONSUMER1,
                            TOPIC_STRING_CONSUMER1_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(getKafkacontainer())
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(TOPIC_STRING_CONSUMER1, "WKS-1", "My message");

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
            listenerContainer = new KafkaMessageListenerContainerFactory(getKafkacontainer())
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            new String[]{TOPIC_STRING_CONSUMER2, TOPIC_STRING_CONSUMER3},
                            TOPIC_STRING_CONSUMER2_PARTITIONS + TOPIC_STRING_CONSUMER3_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(getKafkacontainer())
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(TOPIC_STRING_CONSUMER2, "WKS-2", "My message 2");

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-2")));
            assertThat(record.value(), is(equalTo("My message 2")));

            template.send(TOPIC_STRING_CONSUMER3, "WKS-3", "My message 3");

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
            listenerContainer = new KafkaMessageListenerContainerFactory(getKafkacontainer())
                    .createListenerContainer(
                            new StringDeserializer(),
                            new StringDeserializer(),
                            "an_group",
                            Pattern.compile("topic-string-pattern.*"),
                            TOPIC_STRING_CONSUMER4_PARTITIONS + TOPIC_STRING_CONSUMER5_PARTITIONS,
                            (MessageListener<String, String>) record -> records.add(record));

            KafkaTemplate<String, String> template = new KafkaTemplateFactory(getKafkacontainer())
                    .createKafkaTemplate(new StringSerializer(), new StringSerializer());
            template.send(TOPIC_STRING_CONSUMER4, "WKS-4", "My message 4");

            ConsumerRecord<String, String> record = records.poll(10, TimeUnit.SECONDS);
            assertThat(record, is(notNullValue()));
            assertThat(record.key(), is(equalTo("WKS-4")));
            assertThat(record.value(), is(equalTo("My message 4")));

            template.send(TOPIC_STRING_CONSUMER5, "WKS-5", "My message 5");

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
            listenerContainer = new KafkaMessageListenerContainerFactory(getKafkacontainer())
                    .createListenerContainer(
                            new StringDeserializer(),
                            new AvroSerializerFactory(getKafkacontainer()).createKafkaAvroDeserializer(false, true),
                            "an_group",
                            TOPIC_AVRO_CONSUMER,
                            TOPIC_AVRO_CONSUMER_PARTITIONS,
                            (MessageListener<String, WorkstationAvro>) record -> records.add(record));

            KafkaTemplate<String, Object> template = new KafkaTemplateFactory(getKafkacontainer())
                    .createKafkaTemplate(
                            new StringSerializer(),
                            new AvroSerializerFactory(getKafkacontainer()).createKafkaAvroSerializer(false));

            WorkstationAvro workstation = WorkstationAvro.newBuilder().setId(2L).setName("WS-123456").setSerialNumber("SERIAL-000002").build();
            template.send(TOPIC_AVRO_CONSUMER, "WKS-6", workstation);

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