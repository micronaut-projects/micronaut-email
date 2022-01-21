/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.email.javamail.sender;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.convert.format.MapFormat;
import java.util.Map;

/**
 * {@link ConfigurationProperties} implementation of {@link JavaMailConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@ConfigurationProperties(JavaMailConfigurationProperties.PREFIX)
public class JavaMailConfigurationProperties implements JavaMailConfiguration {
    /**
     * javamail prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "javamail";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    private boolean enabled = DEFAULT_ENABLED;

    private Map<String, Object> properties;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Mailjet integration is enabled. Default value: `{@value #DEFAULT_ENABLED}`
     *
     * @param enabled True if security is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return properties as listed in Appendix A of the JavaMail spec (particularly mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from)
     */
    @Override
    @Nullable
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * properties as listed in Appendix A of the JavaMail spec (particularly mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from).
     * @param properties properties as listed in Appendix A of the JavaMail spec (particularly mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from)
     */
    public void setProperties(@MapFormat(transformation = MapFormat.MapTransformation.FLAT) Map<String, Object> properties) {
        this.properties = properties;
    }
}
