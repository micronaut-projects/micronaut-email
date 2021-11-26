package io.micronaut.email.docs;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.email.EmailCourier;
import io.micronaut.email.TransactionalEmail;
import jakarta.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MockEmailCourier implements EmailCourier {
    private List<TransactionalEmail> emails = new ArrayList<>();

    @Override
    public void send(@NonNull @NotNull @Valid TransactionalEmail email) {
        emails.add(email);
    }

    public List<TransactionalEmail> getEmails() {
        return emails;
    }
}
