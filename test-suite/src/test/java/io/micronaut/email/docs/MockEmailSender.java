package io.micronaut.email.docs;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.EmailSender;
import io.micronaut.email.Email;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Named("mock")
@Singleton
public class MockEmailSender implements EmailSender {
    private List<Email> emails = new ArrayList<>();

    @Override
    public void send(@NonNull @NotNull @Valid Email email) {
        emails.add(email);
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
