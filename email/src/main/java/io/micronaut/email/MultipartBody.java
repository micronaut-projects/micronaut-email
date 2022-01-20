package io.micronaut.email;

import io.micronaut.core.annotation.NonNull;

public class MultipartBody implements Body {

    private final Body html;
    private final Body text;

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

    public MultipartBody(@NonNull String html, @NonNull String text) {
        this(new StringBody(html), new StringBody(text));
    }

    public MultipartBody(@NonNull Body html, @NonNull String text) {
        this(html, new StringBody(text));
    }

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

    public Body getHtml() {
        return html;
    }

    public Body getText() {
        return text;
    }
}
