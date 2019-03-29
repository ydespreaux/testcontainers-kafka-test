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

/**
 *
 */
package com.github.ydespreaux.testcontainers.kafka.test;

import com.github.ydespreaux.testcontainers.kafka.rule.ConfluentKafkaContainer;
import com.github.ydespreaux.testcontainers.kafka.test.internal.AbstractKafkaMessageListenerContainerFactoryTest;
import com.github.ydespreaux.testcontainers.kafka.test.internal.AbstractKafkaTemplateFactoryTest;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.ydespreaux.testcontainers.kafka.test.internal.BrokerConfiguration.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


/**
 * @author Yoann Despréaux
 */
@Tag("integration")
@Testcontainers
public class PlaintextProtocolTest {

    @Container
    public static ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer()
            .withRegisterSpringbootProperties(false)
            .withSchemaRegistry(true)
            .withTopic(TOPIC_STRING_PRODUCER, TOPIC_STRING_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_PRODUCER, TOPIC_AVRO_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER, TOPIC_STRING_CONSUMER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER1, TOPIC_STRING_CONSUMER1_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER2, TOPIC_STRING_CONSUMER2_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER3, TOPIC_STRING_CONSUMER3_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER4, TOPIC_STRING_CONSUMER4_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER5, TOPIC_STRING_CONSUMER5_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_CONSUMER, TOPIC_AVRO_CONSUMER_PARTITIONS, false);

    @Nested
    class AvroSerializerFactoryTest {

        @Test
        public void createKafkaAvroSerializerWithKey() {
            KafkaAvroSerializer serializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroSerializer(true);
            assertThat(serializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroSerializerWithValue() {
            KafkaAvroSerializer serializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroSerializer(false);
            assertThat(serializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithKey() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(true);
            assertThat(deserializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithValue() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(false);
            assertThat(deserializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithKeyAndNotSpecificAvroReader() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(true, false);
            assertThat(deserializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithKeyAndSpecificAvroReader() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(true, true);
            assertThat(deserializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithValueAndNotSpecificAvroReader() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(false, false);
            assertThat(deserializer, is(notNullValue()));
        }

        @Test
        public void createKafkaAvroDeserializerWithValueAndSpecificAvroReader() {
            KafkaAvroDeserializer deserializer = new AvroSerializerFactory(kafkaContainer).createKafkaAvroDeserializer(false, true);
            assertThat(deserializer, is(notNullValue()));
        }
    }

    @Nested
    class KafkaMessageListenerContainerFactoryTest extends AbstractKafkaMessageListenerContainerFactoryTest {

        @Override
        protected ConfluentKafkaContainer getKafkacontainer() {
            return kafkaContainer;
        }
    }

    @Nested
    class KafkaTemplateFactoryTest extends AbstractKafkaTemplateFactoryTest {


        @Override
        protected ConfluentKafkaContainer getKafkacontainer() {
            return kafkaContainer;
        }

    }
}
