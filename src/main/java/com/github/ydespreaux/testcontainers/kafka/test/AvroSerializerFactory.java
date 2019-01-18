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
