package io.micronaut.email.validation

import io.micronaut.email.Contact
import io.micronaut.email.Email
import spock.lang.Specification
import spock.lang.Unroll

class AnyRecipientsValidatorSpec extends Specification {

    @Unroll("for to: #to cc: #cc bcc: #bcc validation: #expected")
    void "RecipientUtils ensures at least an address to cc or bcc is present"(Collection<String> to, Collection<String> cc, Collection<String> bcc, boolean expected) {
        given:
        AnyRecipientValidator validator = new AnyRecipientValidator()
        Recipients recipients = createRecipient(to, cc, bcc)
        Email.Builder builder = Email.builder()
        recipients.getTo().stream().forEach(builder::to)
        recipients.getCc().stream().forEach(builder::cc)
        recipients.getBcc().stream().forEach(builder::bcc)

        expect:
        expected == validator.isValid(builder.build(), null)

        where:
        to                  | cc                  | bcc                 || expected
        null                | null                | null                || false
        []                  | []                  | []                  || false
        ['tcook@apple.com'] | null                | null                || true
        null                | ['tcook@apple.com'] | null                || true
        null                | null                | ['tcook@apple.com'] || true
    }

    private static Recipients createRecipient(Collection<String> to, Collection<String> cc, Collection<String> bcc) {
        new Recipients() {
            @Override
            Collection<Contact> getTo() {
                to ? to.collect { it -> new Contact(it) } : (to == null ? null : [])
            }

            @Override
            Collection<Contact> getCc() {
                cc ? cc.collect { it -> new Contact(it) } : (cc == null ? null : [])
            }

            @Override
            Collection<Contact> getBcc() {
                bcc ? bcc.collect { it -> new Contact(it) } : (bcc == null ? null : [])
            }
        }
    }
}
