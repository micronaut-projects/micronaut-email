package io.micronaut.email.docs;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.Email;
import io.micronaut.email.EmailException;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
