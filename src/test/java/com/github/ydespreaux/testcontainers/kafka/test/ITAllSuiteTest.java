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
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author xpax624
 */
@RunWith(Suite.class)
@SuiteClasses({
        ITAvroSerializerFactoryTest.class,
        ITKafkaTemplateFactoryTest.class,
        ITKafkaMessageListenerContainerFactoryTest.class
})
public class ITAllSuiteTest {

    public static final String TOPIC_STRING_PRODUCER = "topic-string-producer";
    public static final String TOPIC_AVRO_PRODUCER = "topic-avro-producer";
    public static final String TOPIC_STRING_CONSUMER1 = "topic-string-consumer1";
    public static final String TOPIC_STRING_CONSUMER2 = "topic-string-consumer2";
    public static final String TOPIC_STRING_CONSUMER3 = "topic-string-consumer3";
    public static final String TOPIC_STRING_CONSUMER4 = "topic-string-pattern1";
    public static final String TOPIC_STRING_CONSUMER5 = "topic-string-pattern2";
    public static final String TOPIC_AVRO_CONSUMER = "topic-avro-consumer";
    public static final Integer TOPIC_STRING_PRODUCER_PARTITIONS = 3;
    public static final Integer TOPIC_STRING_CONSUMER1_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER2_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER3_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER4_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER5_PARTITIONS = 1;
    public static final Integer TOPIC_AVRO_PRODUCER_PARTITIONS = 3;
    public static final Integer TOPIC_AVRO_CONSUMER_PARTITIONS = 3;

    @ClassRule
    public static final ConfluentKafkaContainer kafkaContainerWithSchemaRegistry = new ConfluentKafkaContainer()
            .withRegisterSpringbootProperties(false)
            .withSchemaRegistry(true)
            .withTopic(TOPIC_STRING_PRODUCER, TOPIC_STRING_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER1, TOPIC_STRING_CONSUMER1_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_PRODUCER, TOPIC_AVRO_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_CONSUMER, TOPIC_AVRO_CONSUMER_PARTITIONS, false);

    @ClassRule
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer()
            .withRegisterSpringbootProperties(false)
            .withTopic(TOPIC_STRING_PRODUCER, TOPIC_STRING_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER1, TOPIC_STRING_CONSUMER1_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER2, TOPIC_STRING_CONSUMER2_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER3, TOPIC_STRING_CONSUMER3_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER4, TOPIC_STRING_CONSUMER4_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER5, TOPIC_STRING_CONSUMER5_PARTITIONS, false);

}
