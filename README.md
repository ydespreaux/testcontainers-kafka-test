spring-testcontainers-kafka-test
=========================
Create KafkaTemplate, KafkaMessageContainerListener for tests


Librairie utilitaire pour les tests d'intégration Kafka

Versions
-----------

|   lib-testcontainers-kafka-utils |   Spring Boot |    testcontainers-kafka        |
|:--------------------------------:|:-------------:|:------------------------------:|
|   1.1.0                          |     2.0.x     |    1.0.x                       |
|   1.0.0                          |     1.5.x     |    1.0.x                       |

#### Add the Maven dependency

```xml
<dependency>
    <groupId>com.github.ydespreaux.testcontainers</groupId>
    <artifactId>testcontainers-kafka-test</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
</dependency>
```

#### AvroSerializerFactory

Cette fabrique permet de créer des serializer / deserializer AVRO.

```java
/**
 * Create a avro serializer.
 *
 * @param isKey set if the serializer is applied to the key or message value
 * @return
 */
public KafkaAvroSerializer createKafkaAvroSerializer(Boolean isKey);
/**
 * Create a avro deserializer.
 * @param isKey set if the serializer is applied to the key or message value
 * @return
 */
public KafkaAvroDeserializer createKafkaAvroDeserializer(Boolean isKey);
/**
 * Create a avro deserializer.
 * @param isKey set if the serializer is applied to the key or message value
 * @param specificAvroReader If true, tries to look up the SpecificRecord class
 * @return
 */
public KafkaAvroDeserializer createKafkaAvroDeserializer(Boolean isKey, Boolean specificAvroReader);
```

Exemple d'utilisation

```java
@ClassRule
public static final ConfluentKafkaContainer kafkaContainerWithSchemaRegistry = new ConfluentKafkaContainer()
        .withSchemaRegistry(true)

KafkaAvroSerializer keySerializer = new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(true);
KafkaAvroSerializer valueSerializer = new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(false);
```

#### KafkaTemplateFactory

Cette fabrique permet de créer des KafkaTemplate.

```java
/**
 * Create a kafkaTemplate with additional properties.
 */
public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Map<String, Object> additionalProperties);

/**
 * Create a kafkatemplate with specific key serializer and value serializer.
 */
public <K, V> KafkaTemplate<K, V> createKafkaTemplate(String keySerializerClass, String valueSerializerClass);

/**
 * Create a kafkatemplate with optional properties and a specific key serializer and value serializer.
 */
public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Map<String, Object> additionalProperties, String keySerializerClass, String valueSerializerClass);

/**
 * Create a kafkatemplate with specific key serializer  and value serializer.
 */
public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Serializer<K> keySerializer, Serializer<V> valueSerializer);

/**
 * Create a kafkatemplate with optional properties and specific key serializer  and value serializer
 */
public <K, V> KafkaTemplate<K, V> createKafkaTemplate(Map<String, Object> additionalProperties, Serializer<K> keySerializer, Serializer<V> valueSerializer);
```

Exemple d'utilisation

```java
@ClassRule
public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer();

KafkaTemplate<String, String> template =
        new KafkaTemplateFactory(kafkaContainer)
                .createKafkaTemplate("org.apache.kafka.common.serialization.StringSerializer", "org.apache.kafka.common.serialization.StringSerializer");
```

Format Avro:

```java
@ClassRule
public static final ConfluentKafkaContainer kafkaContainerWithSchemaRegistry = new ConfluentKafkaContainer()
        .withSchemaRegistry(true);
KafkaTemplate<String, Object> template =
        new KafkaTemplateFactory(kafkaContainerWithSchemaRegistry)
            .createKafkaTemplate(
                new StringSerializer(),
                new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroSerializer(false));
```

#### KafkaMessageListenerContainerFactory

Cette fabrique permet de créer des conteneurs d'écoute.

```java
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        String topic,
        int partitions,
        MessageListener<?, ?> listener) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        String topic,
        int partitions,
        MessageListener<?, ?> listener,
        Map<String, Object> optionalProperties) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        String[] topics,
        int partitions,
        MessageListener<?, ?> listener) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        String[] topics,
        int partitions,
        MessageListener<?, ?> listener,
        Map<String, Object> optionalProperties) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        Pattern topicPattern,
        int partitions,
        MessageListener<?, ?> listener) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(
        Deserializer<K> keyDeserializer,
        Deserializer<V> valueDeserializer,
        String group,
        Pattern topicPattern,
        int partitions,
        MessageListener<?, ?> listener,
        Map<String, Object> optionalProperties) throws Exception;
public <K, V> KafkaMessageListenerContainer<K, V> createListenerContainer(Deserializer<K> keyDeserializer,
                                                                           Deserializer<V> valueDeserializer,
                                                                           String group,
                                                                           int partitions,
                                                                           MessageListener<?, ?> listener,
                                                                           Map<String, Object> optionalProperties,
                                                                           ContainerProperties containerProperties) throws Exception;
```

Exemple d'utilisation

```java
@ClassRule
public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer();

BlockingQueue<ConsumerRecord<String, String>> records = new LinkedBlockingQueue<>();
KafkaMessageListenerContainer<String, String> listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainer)
        .createListenerContainer(
                new StringDeserializer(),
                new StringDeserializer(),
                "an_group",
                "my_topic",
                1,
                (MessageListener<String, String>) record -> records.add(record));

```

Message au format AVRO

```java
@ClassRule
public static final ConfluentKafkaContainer kafkaContainerWithSchemaRegistry = new ConfluentKafkaContainer()
        .withSchemaRegistry(true)

BlockingQueue<ConsumerRecord<String, WorkstationAvro>> records = new LinkedBlockingQueue<>();
KafkaMessageListenerContainer<String, Object> listenerContainer = new KafkaMessageListenerContainerFactory(kafkaContainerWithSchemaRegistry)
        .createListenerContainer(
                new StringDeserializer(),
                new AvroSerializerFactory(kafkaContainerWithSchemaRegistry).createKafkaAvroDeserializer(false, true),
                "an_group",
                "my_topic_avro",
                1,
                (MessageListener<String, WorkstationAvro>) record -> records.add(record));
```

## Changelog


### [1.0.0]

#### Added

- Ajout de la fabrique AvroSerializerFactory
- Ajout de la fabrique KafkaTemplateFactory
- Ajout de la fabrique KafkaMessageListenerContainerFactory

