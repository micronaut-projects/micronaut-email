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
package io.micronaut.email;

/**
 * Whether to track if the links are clicked in an email.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public enum TrackLinks {
    /**
     * Whether to track links only in HTML emails.
     */
    HTML,
    /**
     * Whether to track links only in Plain text emails.
     */
    TEXT,
    /**
     * Whether to track links in HTML and Plain text emails.
     */
    HTML_AND_TEXT,
    /**
     * Whether email links should not be tracked neither in HTML nor in plain text emails.
     */
    DO_NOT_TRACK;
}
