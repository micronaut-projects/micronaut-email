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
import io.micronaut.email.EmailWithoutContent;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

/**
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <H> HTML model
 * @param <T> Text model
 */
@Singleton
public class DefaultEmailRenderer<H, T> implements EmailRenderer<H, T> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEmailRenderer.class);
    private final ViewsRenderer<H> htmlViewsRenderer;
    private final ViewsRenderer<T> textViewsRenderer;

    /**
     *
     * @param htmlViewsRenderer Utility to render the HTML view
     * @param textViewsRenderer Utility to render the Text View
     */
    public DefaultEmailRenderer(ViewsRenderer<H> htmlViewsRenderer,
                                ViewsRenderer<T> textViewsRenderer) {
        this.htmlViewsRenderer = htmlViewsRenderer;
        this.textViewsRenderer = textViewsRenderer;
    }

    @Override
    @NonNull
    public Email render(@NonNull EmailWithoutContent email,
                        @Nullable ModelAndView<H> html,
                        @Nullable ModelAndView<T> text) {
        return new Email(email,
                content(htmlViewsRenderer, html).orElse(null),
                content(textViewsRenderer, text).orElse(null));
    }

    /**
     *
     * @param viewsRenderer Utility to render a view
     * @param modelAndView Model and View
     * @param <M> The model Type
     * @return Rendered view as String
     */
    @NonNull
    private static <M> Optional<String> content(@NonNull ViewsRenderer<M> viewsRenderer,
                                                @Nullable ModelAndView<M> modelAndView) {
        if (modelAndView == null) {
            return Optional.empty();
        }
        if (modelAndView.getView().isPresent()) {
            String viewName = modelAndView.getView().get().toString();
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
