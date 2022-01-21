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
import io.micronaut.email.MultipartBody;
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
        Optional<Body> optionalBody = emailBuilder.getBody();
        if (optionalBody.isPresent()) {
            Body body = optionalBody.get();
            if (body instanceof TemplateBody) {
                if (body.get(BodyType.HTML).isPresent()) {
                    renderBody((TemplateBody<?>) body, BodyType.HTML);
                } else if (body.get(BodyType.TEXT).isPresent()) {
                    renderBody((TemplateBody<?>) body, BodyType.TEXT);
                }
            } else if (body instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) body;
                if (multipartBody.getHtml() instanceof TemplateBody) {
                    renderBody((TemplateBody<?>) multipartBody.getHtml(), BodyType.HTML);
                }
                if (multipartBody.getText() instanceof TemplateBody) {
                    renderBody((TemplateBody<?>) multipartBody.getText(), BodyType.TEXT);
                }
            }
        }
    }

    /**
     * @param body Template Body
     * @param bodyType Body Type
     */
    default void renderBody(@NonNull TemplateBody<?> body, @NonNull BodyType bodyType) {
        ModelAndView<?> modelAndView = body.getModelAndView();
        String viewName = modelAndView.getView().orElse(null);
        if (viewName == null) {
            return;
        }
        Optional<ViewsRenderer> optionalViewsRenderer = resolveViewsRenderer(bodyType, viewName, modelAndView.getModel().orElse(null));
        if (!optionalViewsRenderer.isPresent()) {
            return;
        }
        Writable writable = optionalViewsRenderer.get().render(viewName, modelAndView.getModel().orElse(null), null);
        StringWriter stringWriter = new StringWriter();
        try {
            writable.writeTo(stringWriter);
            body.setBody(stringWriter.toString());
        } catch (IOException e) {
            if (getLogger().isErrorEnabled()) {
                getLogger().error("IO exception writing template to String", e);
            }
        }
    }
}
