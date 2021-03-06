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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.Toggleable;
import io.micronaut.email.TrackLinks;

/**
 * Defines Configuration for Postmark integration.
 * <a href="https://postmarkapp.com">Postmark</a> integration related classes.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public interface PostmarkConfiguration extends Toggleable {

    /**
     *
     * @return Postmark API token.
     */
    @NonNull
    String getApiToken();

    /**
     *
     * @return Whether to track if the email is opened
     */
    boolean getTrackOpens();

    /**
     *
     * @return Whether to track the email's links
     */
    @NonNull
    TrackLinks getTrackLinks();
}
