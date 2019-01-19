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

import com.github.ydespreaux.testcontainers.kafka.rule.ConfluentKafkaContainer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AvroSerializerFactory {

    private final ConfluentKafkaContainer container;

    /**
     * Default constructor
     *
     * @param container
     */
    public AvroSerializerFactory(ConfluentKafkaContainer container) {
        this.container = container;
    }

    /**
     * Create a avro serializer.
     *
     * @param isKey set if the serializer is applied to the key or message value
     * @return
     */
    public KafkaAvroSerializer createKafkaAvroSerializer(Boolean isKey) {
        Objects.requireNonNull(isKey, "Parameter isKey must be not null !!");
        if (!this.container.isSchemaRegistryEnabled()) {
            throw new IllegalArgumentException("Schema registry must be enabled");
        }
        Map<String, String> config = new HashMap<>();
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.container.getSchemaRegistryServers());
        KafkaAvroSerializer avroSerializer = new KafkaAvroSerializer();
        avroSerializer.configure(config, isKey);
        return avroSerializer;

    }

    /**
     * Create a avro deserializer.
     *
     * @param isKey set if the serializer is applied to the key or message value
     * @return
     */
    public KafkaAvroDeserializer createKafkaAvroDeserializer(Boolean isKey) {
        return createKafkaAvroDeserializer(isKey, true);
    }

    /**
     * Create a avro deserializer.
     *
     * @param isKey              set if the serializer is applied to the key or message value
     * @param specificAvroReader If true, tries to look up the SpecificRecord class
     * @return
     */
    public KafkaAvroDeserializer createKafkaAvroDeserializer(Boolean isKey, Boolean specificAvroReader) {
        Objects.requireNonNull(isKey, "Parameter isKey must be not null !!");
        Objects.requireNonNull(specificAvroReader, "Parameter specificAvroReader must be not null !!");
        if (!this.container.isSchemaRegistryEnabled()) {
            throw new IllegalArgumentException("Schema registry must be enabled");
        }
        Map<String, String> config = new HashMap<>();
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, this.container.getSchemaRegistryServers());
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, specificAvroReader.toString());
        KafkaAvroDeserializer avroDeserializer = new KafkaAvroDeserializer();
        avroDeserializer.configure(config, isKey);
        return avroDeserializer;
    }

}
