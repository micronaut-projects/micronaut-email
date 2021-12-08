package io.micronaut.email.docs;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import io.micronaut.email.TransactionalEmailSender;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Named("mock")
@Singleton
public class MockEmailSender implements TransactionalEmailSender<Void> {
    private List<Email> emails = new ArrayList<>();

    @Override
    @NonNull
    public Optional<Void> send(@NonNull @NotNull @Valid Email email) {
        emails.add(email);
        return Optional.empty();
    }

    public List<Email> getEmails() {
        return emails;
    }

    @Override
    @NonNull
    public String getName() {
        return "mock";
    }


}
