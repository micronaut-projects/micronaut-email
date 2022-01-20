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
import io.micronaut.email.Body;
import io.micronaut.views.ModelAndView;

/**
 * Email HTML Body backed by a template.
 *
 * @author Sergio del Amo
 * @since 1.0
 * @param <T> HTML model
 */
public class TemplateBody<T> implements Body<ModelAndView<T>> {

    @NonNull
    private final ModelAndView<T> modelAndView;

    /**
     * Constructor.
     *
     * @param view  view name to be rendered
     * @param model Model to be rendered against the view
     */
    public TemplateBody(String view, T model) {
        this(new ModelAndView<>(view, model));
    }

    /**
     * @param modelAndView Emails Template's name and model for html
     */
    public TemplateBody(@NonNull ModelAndView<T> modelAndView) {
        this.modelAndView = modelAndView;
    }

    @Override
    @NonNull
    public ModelAndView<T> get() {
        return modelAndView;
    }
}
