/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.email.resend;

import io.micronaut.email.EmailException;
import io.micronaut.email.resend.model.ResendError;
import org.jspecify.annotations.Nullable;

/**
 * Exception thrown when Resend responds with an error.
 * @see <a href="https://resend.com/docs/api-reference/errors">Resend errors</a>
 *
 * @since 3.3.0
 */
public class ResendEmailException extends EmailException {

    private final int statusCode;

    @Nullable
    private final transient ResendError error;

    /**
     *
     * @param message Exception message
     * @param statusCode HTTP status code of the Resend response
     * @param error Error returned by Resend
     * @param cause Exception thrown by the HTTP Client
     */
    public ResendEmailException(String message,
                                int statusCode,
                                @Nullable ResendError error,
                                @Nullable Throwable cause) {
        super(message);
        this.statusCode = statusCode;
        this.error = error;
        if (cause != null) {
            initCause(cause);
        }
    }

    /**
     *
     * @return HTTP status code of the Resend response.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @return Error returned by Resend, if the response contained one.
     */
    @Nullable
    public ResendError getError() {
        return error;
    }
}
