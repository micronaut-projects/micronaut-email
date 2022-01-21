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
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Body;
import io.micronaut.email.BodyType;
import io.micronaut.views.ModelAndView;

import java.util.Optional;

/**
 * Email HTML Body backed by a template.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @param <T> HTML model
 */
public class TemplateBody<T> implements Body {

    @NonNull
    private final ModelAndView<T> modelAndView;

    @NonNull
    private String body = StringUtils.EMPTY_STRING;

    @NonNull
    private final BodyType bodyType;

    /**
     * Body HTML with view name and model.
     * @param view  view name to be rendered
     * @param model Model to be rendered against the view
     */
    public TemplateBody(@Nullable String view, @Nullable T model) {
        this(new ModelAndView<>(view, model));
    }

    /**
     * @param modelAndView Emails Template's name and model for html
     */
    public TemplateBody(@NonNull ModelAndView<T> modelAndView) {
        this(BodyType.HTML, modelAndView);
    }

    /**
     * @param view  View name to be rendered
     * @param model Model to be rendered against the view
     * @param bodyType The content type of the template
     */
    public TemplateBody(@NonNull BodyType bodyType,
                        @Nullable String view,
                        @Nullable T model) {
        this(bodyType, new ModelAndView<>(view, model));
    }

    /**
     * @param modelAndView Emails Template's name and model for html
     * @param bodyType The content type of the template
     */
    public TemplateBody(@NonNull BodyType bodyType, @NonNull ModelAndView<T> modelAndView) {
        this.modelAndView = modelAndView;
        this.bodyType = bodyType;
    }

    /**
     * @return The model and view
     */
    @NonNull
    public ModelAndView<T> getModelAndView() {
        return modelAndView;
    }

    /**
     * @param body The result of the view being rendered
     */
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    @NonNull
    public Optional<String> get(@NonNull BodyType bodyType) {
        return this.bodyType == bodyType ? Optional.of(body) : Optional.empty();
    }
}
