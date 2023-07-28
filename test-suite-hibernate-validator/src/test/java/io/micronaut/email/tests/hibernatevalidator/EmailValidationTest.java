package io.micronaut.email.tests.hibernatevalidator;

import io.micronaut.email.Email;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validator;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(startApplication = false)
class EmailValidationTest {
    @Test
    void fromToAndSubjectAreRequiredValidation(Validator validator) {
        Email email = Email.builder()
            .from("tcook@apple.com")
            .subject("Apple Music")
            .body("Stream music to your device")
            .build();
        assertFalse(validator.validate(email).isEmpty());
    }
}
