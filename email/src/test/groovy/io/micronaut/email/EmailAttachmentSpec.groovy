package io.micronaut.email

import spock.lang.Specification
import spock.lang.Unroll

class EmailAttachmentSpec extends Specification {

    // CONVERSION TESTS: FileAttachment/InlineAttachment → Attachment

    void "test FileAttachment builder maps correctly to internal legacy Attachment"() {
        given: "A file attachment created via Builder"
        byte[] content = "PDF-DATA".bytes
        FileAttachment fileAtt = FileAttachment.builder()
                .filename("doc.pdf")
                .contentType("application/pdf")
                .content(content)
                .build()

        when: "It is added to the Email Builder"
        Email email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(fileAtt)
                .build()

        then: "The internal list contains the legacy Attachment mapped correctly"
        email.attachments.size() == 1
        with(email.attachments[0]) {
            filename == "doc.pdf"
            contentType == "application/pdf"
            content == content
            id == null          // Files have no ID
            disposition == null // Files default to null
        }
    }

    void "test InlineAttachment builder maps correctly to internal legacy Attachment"() {
        given: "An inline attachment created via Builder"
        byte[] content = "IMG-DATA".bytes
        InlineAttachment inlineAtt = InlineAttachment.builder()
                .filename("logo.png")
                .contentType("image/png")
                .content(content)
                .contentId("logo123") // Using String convenience method
                .build()

        when: "It is added to the Email Builder"
        Email email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(inlineAtt)
                .build()

        then: "The internal list contains the legacy Attachment mapped correctly"
        email.attachments.size() == 1
        with(email.attachments[0]) {
            filename == "logo.png"
            contentType == "image/png"
            content == content
            id == "logo123"      // ID is preserved
            disposition == "inline"  // Disposition is inline
        }
    }

    // DATA-DRIVEN TESTS: Ordering and Type Mixing

    @Unroll
    void "test mixing multiple attachments preserves order: #desc"() {
        when: "We add multiple attachments to the builder"
        def emailBuilder = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")

        attachments.each { att ->
            if (att instanceof FileAttachment) emailBuilder.attachment((FileAttachment) att)
            if (att instanceof InlineAttachment) emailBuilder.attachment((InlineAttachment) att)
        }

        def email = emailBuilder.build()

        then: "The size, filenames, and dispositions match exactly"
        email.attachments.size() == expectedFilenames.size()
        email.attachments*.filename == expectedFilenames
        email.attachments*.disposition == expectedDispositions

        where:
        desc                  | attachments                                      || expectedFilenames             | expectedDispositions
        "Two Files"           | [file("a.txt"), file("b.txt")]                   || ["a.txt", "b.txt"]             | [null, null]
        "Two Inline"          | [inline("a.png", "1"), inline("b.png", "2")]     || ["a.png", "b.png"]             | ["inline", "inline"]
        "Mixed (File First)"  | [file("doc.pdf"), inline("sig.png", "cid1")]     || ["doc.pdf", "sig.png"]         | [null, "inline"]
        "Mixed (Inline First)"| [inline("header.jpg", "cid2"), file("terms.txt")]|| ["header.jpg", "terms.txt"]     | ["inline", null]
        "Sandwich"            | [inline("top.png", "1"), file("mid.txt"), inline("bot.png", "2")] || ["top.png", "mid.txt", "bot.png"] | ["inline", null, "inline"]
    }

    // CONTENTID TESTS (New Feature)

    void "test ContentId String convenience method"() {
        when:
        def inline = InlineAttachment.builder()
                .filename("logo.png")
                .contentType("image/png")
                .content("IMG".bytes)
                .contentId("simple-string")  // String method
                .build()

        then:
        inline.contentId.value == "simple-string"
        inline.contentId.toHeaderValue() == "<simple-string>"
    }

    void "test ContentId object method still works"() {
        when:
        def inline = InlineAttachment.builder()
                .filename("logo.png")
                .contentType("image/png")
                .content("IMG".bytes)
                .contentId(new ContentId("object-id"))  // Object method
                .build()

        then:
        inline.contentId.value == "object-id"
    }

    void "test ContentId equality and hashCode work correctly"() {
        given:
        def id1 = new ContentId("test")
        def id2 = new ContentId("test")
        def id3 = new ContentId("different")

        expect:
        id1 == id2
        id1 != id3
        id1.hashCode() == id2.hashCode()
    }

    void "test ContentId formats header value correctly"() {
        when:
        def cid = new ContentId("cid123")

        then:
        cid.value == "cid123"
        cid.toHeaderValue() == "<cid123>"
    }

    @Unroll
    void "test toHeaderValue formats various content id values correctly: #desc"() {
        when:
        def cid = new ContentId(inputValue)

        then:
        cid.toHeaderValue() == expectedHeaderValue

        where:
        desc                          | inputValue                  || expectedHeaderValue
        "simple alphanumeric"         | "img-1"                     || "<img-1>"
        "with dots"                   | "attachment.123.xyz"        || "<attachment.123.xyz>"
        "with underscores"            | "image_file_001"            || "<image_file_001>"
        "email-like format"           | "logo@example.com"          || "<logo@example.com>"
        "uuid format"                 | "550e8400-e29b-41d4-a716"   || "<550e8400-e29b-41d4-a716>"
        "single character"            | "x"                         || "<x>"
        "already has angle brackets"  | "<wrapped>"                 || "<<wrapped>>"
    }

    void "test toHeaderValue returns non-null value"() {
        given:
        def cid = new ContentId("test-id")

        when:
        def headerValue = cid.toHeaderValue()

        then:
        headerValue != null
        headerValue instanceof String
    }

    void "test ContentId rejects null value"() {
        when:
        new ContentId(null)

        then:
        thrown(NullPointerException)
    }

    // ATTACHMENT ISINLINE TESTS

    void "test Attachment isInline returns true for inline disposition"() {
        given:
        def attachment = new Attachment(
                "logo.png",
                "image/png",
                "DATA".bytes,
                "cid-123",
                "inline"
        )

        expect:
        attachment.isInline()
    }

    void "test Attachment isInline returns false for null disposition"() {
        given:
        def attachment = new Attachment(
                "document.pdf",
                "application/pdf",
                "DATA".bytes,
                null,
                null
        )

        expect:
        !attachment.isInline()
    }

    void "test Attachment isInline returns false for attachment disposition"() {
        given:
        def attachment = new Attachment(
                "report.pdf",
                "application/pdf",
                "DATA".bytes,
                null,
                "attachment"
        )

        expect:
        !attachment.isInline()
    }

    @Unroll
    void "test Attachment isInline with various dispositions: #desc"() {
        given:
        def attachment = new Attachment(
                "file.txt",
                "text/plain",
                "DATA".bytes,
                null,
                disposition
        )

        expect:
        attachment.isInline() == expectedIsInline

        where:
        desc                      | disposition   || expectedIsInline
        "exactly inline"          | "inline"      || true
        "null disposition"        | null          || false
        "attachment disposition"  | "attachment"  || false
        "empty string"            | ""            || false
        "INLINE uppercase"        | "INLINE"      || false
        "Inline mixed case"       | "Inline"      || false
        "inline with spaces"      | " inline "    || false
        "form-data"               | "form-data"   || false
    }

    void "test InlineAttachment created attachment isInline is true"() {
        given:
        def inline = InlineAttachment.builder()
                .filename("image.png")
                .contentType("image/png")
                .content("IMG".bytes)
                .contentId("test-cid")
                .build()

        when:
        def email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(inline)
                .build()

        then:
        email.attachments.size() == 1
        email.attachments[0].isInline()
    }

    void "test FileAttachment created attachment isInline is false"() {
        given:
        def file = FileAttachment.builder()
                .filename("document.pdf")
                .contentType("application/pdf")
                .content("PDF".bytes)
                .build()

        when:
        def email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(file)
                .build()

        then:
        email.attachments.size() == 1
        !email.attachments[0].isInline()
    }

    // BACKWARD COMPATIBILITY TESTS

    void "test legacy Attachment API still works"() {
        given:
        Attachment legacy = new Attachment("old.pdf", "application/pdf", "DATA".bytes, null, "attachment")

        when:
        Email email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(legacy)
                .build()

        then:
        email.attachments.size() == 1
        email.attachments[0].filename == "old.pdf"
    }

    void "test mixing new FileAttachment and legacy Attachment"() {
        given:
        FileAttachment newFile = FileAttachment.builder()
                .filename("new.pdf")
                .contentType("application/pdf")
                .content("NEW".bytes)
                .build()

        Attachment legacy = new Attachment("old.doc", "application/msword", "OLD".bytes, null, null)

        when:
        Email email = Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment(newFile)
                .attachment(legacy)
                .build()

        then:
        email.attachments.size() == 2
        email.attachments*.filename == ["new.pdf", "old.doc"]
    }

    // BUILDER VALIDATION TESTS

    void "test FileAttachment builder enforces required fields"() {
        when:
        FileAttachment.builder()
                .filename("test.txt")
                .contentType("text/plain")
                .build()  // Missing content

        then:
        thrown(NullPointerException)
    }

    void "test InlineAttachment builder enforces ContentId requirement"() {
        when:
        InlineAttachment.builder()
                .filename("test.png")
                .contentType("image/png")
                .content("data".bytes)
                .build()  // Missing contentId

        then:
        thrown(NullPointerException)
    }

    void "test InlineAttachment builder rejects null String contentId"() {
        when:
        InlineAttachment.builder()
                .filename("test.png")
                .contentType("image/png")
                .content("data".bytes)
                .contentId((String) null)
                .build()

        then:
        thrown(NullPointerException)
    }

    // ERROR HANDLING TESTS

    void "test Email builder rejects null FileAttachment"() {
        when:
        Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment((FileAttachment) null)
                .build()

        then:
        thrown(IllegalArgumentException)
    }

    void "test Email builder rejects null InlineAttachment"() {
        when:
        Email.builder()
                .from("sender@example.com")
                .to("test@example.com")
                .subject("Test")
                .body("Body")
                .attachment((InlineAttachment) null)
                .build()

        then:
        thrown(IllegalArgumentException)
    }

    // HELPER METHODS

    private static FileAttachment file(String name) {
        return FileAttachment.builder()
                .filename(name)
                .contentType("text/plain")
                .content("test".bytes)
                .build()
    }

    private static InlineAttachment inline(String name, String cid) {
        return InlineAttachment.builder()
                .filename(name)
                .contentType("image/png")
                .content("test".bytes)
                .contentId(cid)
                .build()
    }
}
