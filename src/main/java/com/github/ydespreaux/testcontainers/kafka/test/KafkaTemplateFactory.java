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
import com.github.ydespreaux.testcontainers.kafka.security.Certificates;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KafkaTemplateFactory {

    private final ConfluentKafkaContainer container;
    private final Certificates kafkaClientCertificates;

    /**
     * Default constructor
     * @param container
     */
    public KafkaTemplateFactory(ConfluentKafkaContainer container) {
        this(container, container.getKafkaClientCertificates());
    }

    /**
     * Default constructor
     *
     * @param container
     */
    public KafkaTemplateFactory(ConfluentKafkaContainer container, Certificates kafkaClientCertificates) {
        this.container = container;
        this.kafkaClientCertificates = kafkaClientCertificates;
    }

    /**
     * Create a kafkaTemplate with additional properties.
     *
     * @param additionalProperties
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Map<String, Object> additionalProperties) {
        return createKafkaTemplate(additionalProperties, (String) null, null);
    }

    /**
     * Create a kafkatemplate with specific key serializer and value serializer.
     *
     * @param keySerializerClass
     * @param valueSerializerClass
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(String keySerializerClass, String valueSerializerClass) {
        return createKafkaTemplate(null, keySerializerClass, valueSerializerClass);
    }

    /**
     * Create a kafkatemplate with optional properties and a specific key serializer and value serializer.
     *
     * @param additionalProperties
     * @param keySerializerClass
     * @param valueSerializerClass
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(@Nullable Map<String, Object> additionalProperties,
                                                          @Nullable String keySerializerClass,
                                                          @Nullable String valueSerializerClass) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs(additionalProperties, keySerializerClass, valueSerializerClass)));
    }

    /**
     * Create a kafkatemplate with specific key serializer  and value serializer
     * .
     *
     * @param keySerializer
     * @param valueSerializer
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        return createKafkaTemplate(null, keySerializer, valueSerializer);
    }

    /**
     * Create a kafkatemplate with optional properties and specific key serializer  and value serializer
     *
     * @param additionalProperties
     * @param keySerializer
     * @param valueSerializer
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> KafkaTemplate<K, V> createKafkaTemplate(@Nullable Map<String, Object> additionalProperties, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs(additionalProperties, null, null), keySerializer, valueSerializer));
    }


    /**
     * Build properties
     *
     * @return
     */
    private Map<String, Object> producerConfigs(@Nullable Map<String, Object> additionalProperties, @Nullable String keySerializerClass, @Nullable String valueSerializerClass) {
        Map<String, Object> props = new HashMap<>();
        if (additionalProperties != null) {
            props.putAll(additionalProperties);
        }
        if (this.container.isSchemaRegistryEnabled()) {
            props.put("schema.registry.url", this.container.getSchemaRegistryServers());
        }
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.container.getBootstrapServers());
        if (keySerializerClass != null) {
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        }
        if (valueSerializerClass != null) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);
        }
        if (this.container.isSecured()) {
            Objects.requireNonNull(this.kafkaClientCertificates, "Client certificates not set.");
            props.putAll(SecurityUtils.buildSSLProperties(this.kafkaClientCertificates));
        }
        return props;
    }

}
