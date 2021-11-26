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
package io.micronaut.email.template;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.email.Email;
import io.micronaut.email.EmailHeader;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Renders an Email with a supplied {@link io.micronaut.views.ViewsRenderer}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
public final class EmailRenderUtils {
    private static final Logger LOG = LoggerFactory.getLogger(EmailRenderUtils.class);

    /**
     * Renders a {@link Email} with text and html views.
     * @param viewsRenderer Utility to render teh view
     * @param emailHeader Email Sender, recipients and subjects
     * @param text Emails Template's name and model for text
     * @param html Emails Template's name and model for html
     * @param <T> The model type
     * @return A rendered {@link Email}
     */
    @NonNull
    public static <T> Email render(@NonNull ViewsRenderer<T> viewsRenderer,
                                   @NonNull @NotNull @Valid EmailHeader emailHeader,
                                   @Nullable ModelAndView<T> text,
                                   @Nullable ModelAndView<T> html) {
        Email.Builder builder = Email.builder(emailHeader);
        Optional<String> renderedText = content(viewsRenderer, text);
        if (renderedText.isPresent()) {
            builder = builder.text(renderedText.get());
        }
        Optional<String> renderedHtml = content(viewsRenderer, html);
        if (renderedHtml.isPresent()) {
            builder = builder.html(renderedHtml.get());
        }
        return builder.build();
    }

    @NonNull
    private static <T> Optional<String> content(@NonNull ViewsRenderer<T> viewsRenderer, @Nullable ModelAndView<T> modelAndView) {
        if (modelAndView == null) {
            return Optional.empty();
        }
        if (modelAndView.getView().isPresent()) {
            String viewName = modelAndView.getView().get();
            Writable writable = viewsRenderer.render(viewName, modelAndView.getModel().orElse(null), null);
            StringWriter stringWriter = new StringWriter();
            try {
                writable.writeTo(stringWriter);
                return Optional.of(stringWriter.toString());
            } catch (IOException e) {
                LOG.error("IO exception writing template to String", e);
            }
        }
        return Optional.empty();
    }
}
