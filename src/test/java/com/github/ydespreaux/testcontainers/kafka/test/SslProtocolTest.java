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

import com.github.ydespreaux.testcontainers.kafka.cmd.AclsOperation;
import com.github.ydespreaux.testcontainers.kafka.rule.ConfluentKafkaContainer;
import com.github.ydespreaux.testcontainers.kafka.test.internal.AbstractKafkaMessageListenerContainerFactoryTest;
import com.github.ydespreaux.testcontainers.kafka.test.internal.AbstractKafkaTemplateFactoryTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.ydespreaux.testcontainers.kafka.test.internal.BrokerConfiguration.*;


/**
 * @author Yoann Despréaux
 */
@Tag("integration")
@Testcontainers
public class SslProtocolTest {


    @Container
    public static final ConfluentKafkaContainer kafkaContainer = new ConfluentKafkaContainer()
            .withKafkaServerCertificates(kafkaServerCertificates)
            .withKafkaClientCertificates(kafkaClientCertificates)
            .withSchemaRegistry(true)
            .withRegisterSpringbootProperties(false)
            // Topics
            .withTopic(TOPIC_STRING_PRODUCER, TOPIC_STRING_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_PRODUCER, TOPIC_AVRO_PRODUCER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER, TOPIC_STRING_CONSUMER_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER1, TOPIC_STRING_CONSUMER1_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER2, TOPIC_STRING_CONSUMER2_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER3, TOPIC_STRING_CONSUMER3_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER4, TOPIC_STRING_CONSUMER4_PARTITIONS, false)
            .withTopic(TOPIC_STRING_CONSUMER5, TOPIC_STRING_CONSUMER5_PARTITIONS, false)
            .withTopic(TOPIC_AVRO_CONSUMER, TOPIC_AVRO_CONSUMER_PARTITIONS, false)

            // Acls
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_PRODUCER, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_AVRO_PRODUCER, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER1, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER2, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER3, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER4, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_STRING_CONSUMER5, GROUP_ID)
            .withAcls(new AclsOperation[]{AclsOperation.READ, AclsOperation.DESCRIBE, AclsOperation.WRITE}, TOPIC_AVRO_CONSUMER, GROUP_ID);

    @Nested
    class SecureKafkaMessageListenerContainerFactoryTest extends AbstractKafkaMessageListenerContainerFactoryTest {

        @Override
        protected ConfluentKafkaContainer getKafkacontainer() {
            return kafkaContainer;
        }

    }

    @Nested
    class SecureKafkaTemplateFactoryTest extends AbstractKafkaTemplateFactoryTest {


        @Override
        protected ConfluentKafkaContainer getKafkacontainer() {
            return kafkaContainer;
        }

    }
}
