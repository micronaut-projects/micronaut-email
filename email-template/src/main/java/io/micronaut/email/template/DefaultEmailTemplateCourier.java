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
import io.micronaut.email.EmailCourier;
import io.micronaut.email.Recipient;
import io.micronaut.email.Sender;
import io.micronaut.email.TransactionalEmail;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsRenderer;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Sends Email templates using {@link ViewsRenderer}.
 * @author Sergio del Amo
 * @since 1.0.0
 * @param <T> The model type
 */
@Singleton
public class DefaultEmailTemplateCourier<T> implements EmailTemplateCourier<T> {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEmailTemplateCourier.class);
    private final ViewsRenderer<T> viewsRenderer;
    private final EmailCourier courier;

    /**
     *
     * @param viewsRenderer Utility to render a view.
     * @param courier The Email Courier
     */
    public DefaultEmailTemplateCourier(ViewsRenderer<T> viewsRenderer,
                                       EmailCourier courier) {
        this.viewsRenderer = viewsRenderer;
        this.courier = courier;
    }

    @Override
    public void send(@NonNull @NotNull @Valid Sender sender,
                         @NonNull @NotNull @Valid Recipient recipient,
                         @NonNull @NotBlank String subject,
                         @Nullable ModelAndView<T> text,
                         @Nullable ModelAndView<T> html) {
        Optional<String> renderedTextOptional = content(text);
        Optional<String> renderedHtmlOptional = content(html);
        if (renderedTextOptional.isPresent() || renderedHtmlOptional.isPresent()) {
            TransactionalEmail.Builder builder = emailBuilder(sender, recipient, subject);
            if (renderedTextOptional.isPresent()) {
                builder = builder.text(renderedTextOptional.get());
            }
            if (renderedHtmlOptional.isPresent()) {
                builder = builder.html(renderedHtmlOptional.get());
            }
            courier.send(builder.build());
        }
    }

    @NonNull
    private Optional<String> content(@Nullable ModelAndView<T> modelAndView) {
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

    @NonNull
    private static TransactionalEmail.Builder emailBuilder(@NonNull @NotNull @Valid Sender sender,
                                                           @NonNull @NotNull @Valid Recipient recipient,
                                                           @NonNull @NotBlank String subject) {
        return TransactionalEmail.builder()
                .from(sender.getFrom())
                .recipient(recipient)
                .subject(subject);
    }
}
