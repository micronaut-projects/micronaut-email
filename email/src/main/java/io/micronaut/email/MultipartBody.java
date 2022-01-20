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

import io.micronaut.core.annotation.NonNull;

/**
 * Multipart bodies represent an HTML and text version of the
 * same body content. For providers that support falling back
 * to text for simple email clients both bodies will be sent. For
 * clients that only support a single body, the HTML content will
 * be sent.
 *
 * @author James Kleeh
 * @since 1.0.0
 */
public class MultipartBody implements Body {

    private final Body html;
    private final Body text;

    /**
     * @param html The HTML content
     * @param text The text content
     */
    public MultipartBody(@NonNull Body html, @NonNull Body text) {
        if (html.getType() != BodyType.HTML) {
            throw new IllegalArgumentException("Setting the HTML part in a multipart email must have the HTML body type");
        }
        if (text.getType() != BodyType.TEXT) {
            throw new IllegalArgumentException("Setting the Text part in a multipart email must have the Text body type");
        }
        this.html = html;
        this.text = text;
    }

    /**
     * @param html The HTML content
     * @param text The text content
     */
    public MultipartBody(@NonNull String html, @NonNull String text) {
        this(new StringBody(html), new StringBody(text));
    }

    /**
     * @param html The HTML content
     * @param text The text content
     */
    public MultipartBody(@NonNull Body html, @NonNull String text) {
        this(html, new StringBody(text));
    }

    /**
     * @param html The HTML content
     * @param text The text content
     */
    public MultipartBody(@NonNull String html, @NonNull Body text) {
        this(new StringBody(html), text);
    }

    @NonNull
    @Override
    public String get() {
        return html.get();
    }

    @NonNull
    @Override
    public BodyType getType() {
        return BodyType.HTML;
    }

    /**
     * @return The HTML content
     */
    public Body getHtml() {
        return html;
    }

    /**
     * @return The text content
     */
    public Body getText() {
        return text;
    }
}
