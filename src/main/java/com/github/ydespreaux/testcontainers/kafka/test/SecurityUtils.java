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

import com.github.ydespreaux.testcontainers.kafka.security.Certificates;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yoann Despréaux
 * @since 1.3.0
 */
final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * @return
     */
    public static Map<String, String> buildSSLProperties(Certificates certifcates) {
        if (certifcates == null) {
            return Collections.emptyMap();
        }
        Map<String, String> properties = new HashMap<>();
        properties.put("security.protocol", "SSL");
        properties.put("ssl.keystore.location", certifcates.getKeystorePath().toString());
        properties.put("ssl.keystore.password", certifcates.getKeystorePassword());
        properties.put("ssl.key.password", certifcates.getKeystorePassword());
        if (certifcates.getTruststorePath() != null) {
            properties.put("ssl.truststore.location", certifcates.getTruststorePath().toString());
            properties.put("ssl.truststore.password", certifcates.getTruststorePassword());
        }
        properties.put("ssl.endpoint.identification.algorithm", "");
        return properties;
    }
}
