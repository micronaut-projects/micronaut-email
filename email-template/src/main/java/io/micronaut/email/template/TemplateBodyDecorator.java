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

import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.io.Writable;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.email.Email;
import io.micronaut.email.EmailDecorator;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.ViewsRenderer;
import org.slf4j.Logger;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Decorates emails whose text or html is of type {@link TemplateBody} by rendering those templates.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@DefaultImplementation(DefaultTemplateBodyDecorator.class)
public interface TemplateBodyDecorator extends EmailDecorator {
    Logger getLogger();

    /**
     *
     * @param bodyType Emails Body type
     * @param viewName template view name
     * @param data Template Model
     * @return The view rendered to be used
     */
    @NonNull
    Optional<ViewsRenderer> resolveViewsRenderer(@NonNull BodyType bodyType,
                                                 @NonNull String viewName,
                                                 @Nullable Object data);

    @Override
    default void decorate(@NonNull @NotNull Email.Builder emailBuilder) {
        renderBody(BodyType.TEXT, emailBuilder.getText().orElse(null)).ifPresent(emailBuilder::text);
        renderBody(BodyType.HTML, emailBuilder.getHtml().orElse(null)).ifPresent(emailBuilder::html);
    }

    /**
     * @param bodyType The Body Type
     * @param body Template Body
     * @return rendered template
     */
    @NonNull
    default Optional<String> renderBody(@NonNull BodyType bodyType, @Nullable Body<?> body) {
        if (body == null) {
            return Optional.empty();
        }
        if (!(body instanceof TemplateBody)) {
            return Optional.empty();
        }
        TemplateBody<?> templateTextBody = (TemplateBody<?>) body;
        ModelAndView<?> modelAndView = templateTextBody.get();
        if (!modelAndView.getView().isPresent()) {
            return Optional.empty();
        }
        String viewName = modelAndView.getView().get();
        Optional<ViewsRenderer> optionalViewsRenderer = resolveViewsRenderer(bodyType, viewName, modelAndView.getModel().orElse(null));
        if (!optionalViewsRenderer.isPresent()) {
            return Optional.empty();
        }
        Writable writable = optionalViewsRenderer.get().render(viewName, modelAndView.getModel().orElse(null), null);
        StringWriter stringWriter = new StringWriter();
        try {
            writable.writeTo(stringWriter);
            return Optional.of(stringWriter.toString());
        } catch (IOException e) {
            if (getLogger().isErrorEnabled()) {
                getLogger().error("IO exception writing template to String", e);
            }
        }
        return Optional.empty();
    }
}
