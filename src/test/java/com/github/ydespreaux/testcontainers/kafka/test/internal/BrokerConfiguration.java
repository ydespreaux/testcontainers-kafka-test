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

package com.github.ydespreaux.testcontainers.kafka.test.internal;

import com.github.ydespreaux.testcontainers.kafka.security.Certificates;

public class BrokerConfiguration {

    public static final Certificates kafkaServerCertificates = new Certificates("secrets/kafka.server.keystore.jks", "0123456789", "secrets/kafka.truststore.jks", "0123456789");
    public static final Certificates kafkaClientCertificates = new Certificates("secrets/kafka.client.keystore.jks", "0123456789", "secrets/kafka.truststore.jks", "0123456789");

    public static final String TOPIC_STRING_CONSUMER1 = "topic-string-consumer1";
    public static final String TOPIC_STRING_CONSUMER2 = "topic-string-consumer2";
    public static final String TOPIC_STRING_CONSUMER3 = "topic-string-consumer3";
    public static final String TOPIC_STRING_CONSUMER4 = "topic-string-pattern1";
    public static final String TOPIC_STRING_CONSUMER5 = "topic-string-pattern2";
    public static final String TOPIC_AVRO_CONSUMER = "topic-avro-consumer";
    public static final String TOPIC_STRING_PRODUCER = "topic-string-producer";
    public static final String TOPIC_STRING_CONSUMER = "topic-string-consumer";
    public static final String TOPIC_AVRO_PRODUCER = "topic-avro-producer";

    public static final Integer TOPIC_STRING_CONSUMER1_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER2_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER3_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER4_PARTITIONS = 1;
    public static final Integer TOPIC_STRING_CONSUMER5_PARTITIONS = 1;
    public static final Integer TOPIC_AVRO_CONSUMER_PARTITIONS = 3;
    public static final Integer TOPIC_STRING_PRODUCER_PARTITIONS = 3;
    public static final Integer TOPIC_STRING_CONSUMER_PARTITIONS = 1;
    public static final Integer TOPIC_AVRO_PRODUCER_PARTITIONS = 3;

    public static final String GROUP_ID = "an_group";
}
