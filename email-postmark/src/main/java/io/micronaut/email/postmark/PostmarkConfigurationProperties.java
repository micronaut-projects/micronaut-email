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
package io.micronaut.email.postmark;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.TrackLinks;

import jakarta.validation.constraints.NotBlank;

/**
 * {@link ConfigurationProperties} implementation of {@link PostmarkConfiguration}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(property = PostmarkConfigurationProperties.PREFIX + ".api-token")
@ConfigurationProperties(PostmarkConfigurationProperties.PREFIX)
public class PostmarkConfigurationProperties implements PostmarkConfiguration {
    /**
     * postmark prefix.
     */
    @SuppressWarnings("WeakerAccess")
    public static final String PREFIX = "postmark";

    /**
     * The default enable value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_ENABLED = true;

    /**
     * The default track opens value.
     */
    @SuppressWarnings("WeakerAccess")
    public static final boolean DEFAULT_TRACK_OPENS = false;

    /**
     * The default track links value.
     */
    @SuppressWarnings("WeakerAccess")
    private static final TrackLinks DEFAULT_TRACK_LINKS = TrackLinks.DO_NOT_TRACK;

    private boolean enabled = DEFAULT_ENABLED;

    @NonNull
    @NotBlank
    private String apiToken;

    private boolean trackOpens = DEFAULT_TRACK_OPENS;

    @NonNull
    private TrackLinks trackLinks = DEFAULT_TRACK_LINKS;

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * If Postmark integration is enabled. Default value: `{@value #DEFAULT_ENABLED}`
     *
     * @param enabled True if security is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     *
     * @return Postmark API token.
     */
    @Override
    @NonNull
    public String getApiToken() {
        return apiToken;
    }

    /**
     * Postmark API token.
     * @param apiToken Postmark API token.
     */
    public void setApiToken(@NonNull String apiToken) {
        this.apiToken = apiToken;
    }

    /**
     * Whether to track if the email is opened. Default value: `{@value #DEFAULT_TRACK_OPENS}`
     * @param trackOpens Whether to track if the email is opened
     */
    public void setTrackOpens(boolean trackOpens) {
        this.trackOpens = trackOpens;
    }

    /**
     * Whether to track the email's links. Default value DO_NOT_TRACK.
     * @param trackLinks Whether to track the email's links
     */
    public void setTrackLinks(@NonNull TrackLinks trackLinks) {
        this.trackLinks = trackLinks;
    }

    @Override
    public boolean getTrackOpens() {
        return trackOpens;
    }

    @Override
    @NonNull
    public TrackLinks getTrackLinks() {
        return trackLinks;
    }
}
