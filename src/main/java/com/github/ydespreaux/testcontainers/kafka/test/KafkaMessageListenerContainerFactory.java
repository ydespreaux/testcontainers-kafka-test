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
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.Map;
import java.util.regex.Pattern;

public class KafkaMessageListenerContainerFactory {

    private final ConfluentKafkaContainer container;

    /**
     * Default constructor
     *
     * @param container
     */
    public KafkaMessageListenerContainerFactory(ConfluentKafkaContainer container) {
        this.container = container;
    }

    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topic
     * @param partitions
     * @param listener
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            String topic,
            int partitions,
            MessageListener<?, ?> listener) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, topic, partitions, listener, null);
    }

    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topic
     * @param partitions
     * @param listener
     * @param optionalProperties
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            String topic,
            int partitions,
            MessageListener<?, ?> listener,
            Map<String, Object> optionalProperties) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, partitions, listener, optionalProperties, new ContainerProperties(topic));
    }

    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topics
     * @param partitions
     * @param listener
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            String[] topics,
            int partitions,
            MessageListener<?, ?> listener) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, topics, partitions, listener, null);
    }

    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topics
     * @param partitions
     * @param listener
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            String[] topics,
            int partitions,
            MessageListener<?, ?> listener,
            Map<String, Object> optionalProperties) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, partitions, listener, optionalProperties, new ContainerProperties(topics));
    }


    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topicPattern
     * @param partitions
     * @param listener
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            Pattern topicPattern,
            int partitions,
            MessageListener<?, ?> listener) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, topicPattern, partitions, listener, null);
    }

    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param topicPattern
     * @param partitions
     * @param listener
     * @param optionalProperties
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
            Deserializer<K> keyDeserializer,
            Deserializer<V> valueDeserializer,
            String group,
            Pattern topicPattern,
            int partitions,
            MessageListener<?, ?> listener,
            Map<String, Object> optionalProperties) throws Exception {
        return createListenerContainer(keyDeserializer, valueDeserializer, group, partitions, listener, optionalProperties, new ContainerProperties(topicPattern));
    }


    /**
     * Create a kafka listener container
     *
     * @param keyDeserializer
     * @param valueDeserializer
     * @param group
     * @param partitions
     * @param listener
     * @param optionalProperties
     * @param containerProperties
     * @param <K>
     * @param <V>
     * @return
     * @throws Exception
     */
    public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(Deserializer<K> keyDeserializer,
                                                                              Deserializer<V> valueDeserializer,
                                                                              String group,
                                                                              int partitions,
                                                                              MessageListener<?, ?> listener,
                                                                              Map<String, Object> optionalProperties,
                                                                              ContainerProperties containerProperties) throws Exception {
        containerProperties.setMessageListener(listener);
        KafkaMessageListenerContainer<K, V> listenerContainer = new KafkaMessageListenerContainer<>(
                createKafkaConsumerFactory(group, keyDeserializer, valueDeserializer, optionalProperties),
                containerProperties);
        listenerContainer.start();
        ContainerTestUtils.waitForAssignment(listenerContainer, partitions);
        return listenerContainer;
    }

    /**
     * @param group
     * @param keyDeserializer
     * @param valueDeserializer
     * @param optionalProperties
     * @param <K>
     * @param <V>
     * @return
     */
    private <K, V> DefaultKafkaConsumerFactory<K, V> createKafkaConsumerFactory(final String group,
                                                                                final Deserializer<K> keyDeserializer,
                                                                                final Deserializer<V> valueDeserializer,
                                                                                final Map<String, Object> optionalProperties) {
        Map<String, Object> properties = KafkaTestUtils.consumerProps(this.container.getBootstrapServers(), group, "true");
        if (optionalProperties != null) {
            properties.putAll(optionalProperties);
        }
        return new DefaultKafkaConsumerFactory<>(properties, keyDeserializer, valueDeserializer);
    }
}
