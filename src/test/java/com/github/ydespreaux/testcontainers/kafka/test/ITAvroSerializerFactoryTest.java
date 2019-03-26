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

import static com.github.ydespreaux.testcontainers.kafka.test.ITAllSuiteTest.kafkaContainer;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
public class ITAvroSerializerFactoryTest {

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