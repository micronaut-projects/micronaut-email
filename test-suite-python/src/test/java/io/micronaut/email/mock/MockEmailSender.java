package io.micronaut.email.mock;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO(python): the Java test-suite's {@code MockEmailSender} is a Python class in the other test suites, but a
 * Python class implementing {@link TransactionalEmailSender} does not compile with micronaut-core 5.2.3 when the
 * validation processor is on the classpath: the inherited interface methods carry {@code @Valid}/{@code @NotNull}
 * parameters and {@code ValidationVisitor} fails with "Element of type [class
 * io.micronaut.inject.ast.ReflectParameterElement] does not support adding annotations at compilation time".
 *
 * @param <I> Email request type
 */
@Requires(property = "mock.emailsender", value = StringUtils.TRUE)
@Named("mock")
@Singleton
public class MockEmailSender<I> implements TransactionalEmailSender<I, Email> {

    private final List<Email> emails = new ArrayList<>();
    private final List<Consumer<I>> requests = new ArrayList<>();

    public List<Email> getEmails() {
        return emails;
    }

    public List<Consumer<I>> getRequests() {
        return requests;
    }

    @Override
    @NonNull
    public String getName() {
        return "mock";
    }

    @NonNull
    @Override
    public Email send(@NonNull @NotNull @Valid Email email,
                      @NonNull @NotNull Consumer<I> emailRequest) throws EmailException {
        emails.add(email);
        requests.add(emailRequest);
        return email;
    }
}
