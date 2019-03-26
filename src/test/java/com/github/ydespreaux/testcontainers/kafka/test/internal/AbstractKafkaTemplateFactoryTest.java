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
import com.github.ydespreaux.testcontainers.kafka.test.KafkaTemplateFactory;
import com.github.ydespreaux.testcontainers.kafka.test.domain.WorkstationAvro;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.ydespreaux.testcontainers.kafka.test.internal.BrokerConfiguration.TOPIC_AVRO_PRODUCER;
import static com.github.ydespreaux.testcontainers.kafka.test.internal.BrokerConfiguration.TOPIC_STRING_PRODUCER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public abstract class AbstractKafkaTemplateFactoryTest {


    protected abstract ConfluentKafkaContainer getKafkacontainer();

    @Test
    public void createKafkaTemplateWithOptionalProperties() throws InterruptedException {
        CountDownLatch successLatch = new CountDownLatch(1);
        CountDownLatch failedLatch = new CountDownLatch(1);
        Map<String, Object> optional = new HashMap<>();
        optional.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        optional.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        KafkaTemplate<String, String> template =
                new KafkaTemplateFactory(getKafkacontainer()).createKafkaTemplate(optional);
        ListenableFuture<SendResult<String, String>> future = template.send(TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
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
                new KafkaTemplateFactory(getKafkacontainer())
                        .createKafkaTemplate("org.apache.kafka.common.serialization.StringSerializer", "org.apache.kafka.common.serialization.StringSerializer");
        ListenableFuture<SendResult<String, String>> future = template.send(TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
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
                new KafkaTemplateFactory(getKafkacontainer())
                        .createKafkaTemplate(optional, "org.apache.kafka.common.serialization.StringSerializer", "org.apache.kafka.common.serialization.StringSerializer");
        ListenableFuture<SendResult<String, String>> future = template.send(TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
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
                new KafkaTemplateFactory(getKafkacontainer())
                        .createKafkaTemplate(new StringSerializer(), new StringSerializer());
        ListenableFuture<SendResult<String, String>> future = template.send(TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
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
                new KafkaTemplateFactory(getKafkacontainer())
                        .createKafkaTemplate(optional, new StringSerializer(), new StringSerializer());
        ListenableFuture<SendResult<String, String>> future = template.send(TOPIC_STRING_PRODUCER, "KEY", "Message : produceMessage()");
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
                new KafkaTemplateFactory(getKafkacontainer())
                        .createKafkaTemplate(
                                new StringSerializer(),
                                new AvroSerializerFactory(getKafkacontainer()).createKafkaAvroSerializer(false));

        WorkstationAvro workstation = WorkstationAvro.newBuilder().setId(1L).setName("WS-123456").setSerialNumber("SERIAL-000001").build();

        ListenableFuture<SendResult<String, Object>> future = template.send(TOPIC_AVRO_PRODUCER, "WKS-1", workstation);
        future.addCallback(
                success -> successLatch.countDown(),
                failed -> failedLatch.countDown()
        );
        successLatch.await(5, TimeUnit.SECONDS);
        assertThat(successLatch.getCount(), is(equalTo(0L)));
        assertThat(failedLatch.getCount(), is(equalTo(1L)));
    }

}