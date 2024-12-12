/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baidu.fsg.uid.util;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * DockerUtils
 */
@Slf4j
@UtilityClass
public final class DockerUtils {

    /**
     * Environment param keys
     */
    private static final String ENV_KEY_HOST = "JPAAS_HOST";
    private static final String ENV_KEY_PORT = "JPAAS_HTTP_PORT";
    private static final String ENV_KEY_PORT_ORIGINAL = "JPAAS_HOST_PORT_8080";
    /**
     * Docker host & port
     */
    private static String dockerHost = "";
    private static String dockerPort = "";
    /**
     * Whether is docker
     */
    private static boolean isDocker;

    static {
        retrieveFromEnv();
    }

    /**
     * Retrieve docker host
     *
     * @return empty string if not a docker
     */
    public static String getDockerHost() {
        return dockerHost;
    }

    /**
     * Retrieve docker port
     *
     * @return empty string if not a docker
     */
    public static String getDockerPort() {
        return dockerPort;
    }

    /**
     * Whether a docker
     *
     * @return 是否docker
     */
    public static boolean isDocker() {
        return isDocker;
    }

    /**
     * Retrieve host & port from environment
     */
    private static void retrieveFromEnv() {
        // retrieve host & port from environment
        dockerHost = System.getenv(ENV_KEY_HOST);
        dockerPort = System.getenv(ENV_KEY_PORT);

        // not found from 'JPAAS_HTTP_PORT', then try to find from 'JPAAS_HOST_PORT_8080'
        if (CharSequenceUtil.isBlank(dockerPort)) {
            dockerPort = System.getenv(ENV_KEY_PORT_ORIGINAL);
        }

        boolean hasEnvHost = CharSequenceUtil.isNotBlank(dockerHost);
        boolean hasEnvPort = CharSequenceUtil.isNotBlank(dockerPort);

        // docker can find both host & port from environment
        if (hasEnvHost && hasEnvPort) {
            isDocker = true;
            // found nothing means not a docker, maybe an actual machine
        } else if (!hasEnvHost && !hasEnvPort) {
            isDocker = false;
        } else {
            log.error("Missing host or port from env for Docker. host:{}, port:{}", dockerHost, dockerPort);
            throw new RuntimeException(
                    "Missing host or port from env for Docker. host:" + dockerHost + ", port:" + dockerPort);
        }
    }
}
