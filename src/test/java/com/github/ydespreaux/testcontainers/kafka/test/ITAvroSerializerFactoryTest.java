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

package com.github.ydespreaux.testcontainers.kafka.test;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
public class ITAvroSerializerFactoryTest {

    @Test
    public void createKafkaAvroSerializerWithKey() {
        KafkaAvroSerializer serializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(true);
        assertThat(serializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroSerializerWithKeyAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroSerializer(true);
    }

    @Test
    public void createKafkaAvroSerializerWithValue() {
        KafkaAvroSerializer serializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(false);
        assertThat(serializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroSerializerWithValueAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroSerializer(false);
    }

    @Test
    public void createKafkaAvroDeserializerWithKey() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(true);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithKeyAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(true);
    }

    @Test
    public void createKafkaAvroDeserializerWithValue() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(false);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithValueAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(false);
    }

    @Test
    public void createKafkaAvroDeserializerWithKeyAndNotSpecificAvroReader() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(true, false);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithKeyAndNotSpecificAvroReaderAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(true, false);
    }

    @Test
    public void createKafkaAvroDeserializerWithKeyAndSpecificAvroReader() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(true, true);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithKeyAndSpecificAvroReaderAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(true, true);
    }

    @Test
    public void createKafkaAvroDeserializerWithValueAndNotSpecificAvroReader() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(false, false);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithValueAndNotSpecificAvroReaderAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(false, false);
    }

    @Test
    public void createKafkaAvroDeserializerWithValueAndSpecificAvroReader() {
        KafkaAvroDeserializer deserializer = new AvroSerializerFactory(ITAllSuiteTest.kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(false, true);
        assertThat(deserializer, is(notNullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createKafkaAvroDeserializerWithValueAndSpecificAvroReaderAndSchemaRegistryDisabled() {
        new AvroSerializerFactory(ITAllSuiteTest.kafkaContainer).createKafkaAvroDeserializer(false, true);
    }
}