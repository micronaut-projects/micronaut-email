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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.email.BodyType;
import io.micronaut.http.MediaType;
import io.micronaut.views.ViewsRenderer;
import io.micronaut.views.ViewsRendererLocator;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * {@link io.micronaut.context.annotation.DefaultImplementation} of {@link TemplateBodyDecorator}.
 * @author Sergio del Amo
 * @since 1.0.0
 */
@Requires(beans = ViewsRendererLocator.class)
@Singleton
public class DefaultTemplateBodyDecorator implements TemplateBodyDecorator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultTemplateBodyDecorator.class);

    private final ViewsRendererLocator viewsRendererLocator;

    /**
     *
     * @param viewsRendererLocator ViewRendererLocator
     */
    public DefaultTemplateBodyDecorator(ViewsRendererLocator viewsRendererLocator) {
        this.viewsRendererLocator = viewsRendererLocator;
    }

    @Override
    public Logger getLogger() {
        return LOG;
    }

    @Override
    @NonNull
    public Optional<ViewsRenderer> resolveViewsRenderer(@NonNull BodyType bodyType,
                                                        @NonNull String viewName,
                                                        @Nullable Object data) {
        return viewsRendererLocator.resolveViewsRenderer(viewName, mediaTypeForBodyType(bodyType), data);
    }

    @NonNull
    private MediaType mediaTypeForBodyType(@NonNull BodyType bodyType) {
        switch (bodyType) {
            case TEXT:
                return MediaType.TEXT_PLAIN_TYPE;
            case HTML:
            default:
                return MediaType.TEXT_HTML_TYPE;
        }
    }
}
