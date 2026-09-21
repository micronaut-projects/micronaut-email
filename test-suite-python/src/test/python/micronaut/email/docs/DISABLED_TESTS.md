# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Email that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps).

## Reconciliation

- Last generated active `@Disabled` count: 0.
- Last generated command: `rg -n "@Disabled\\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci` (Mailpit via Testcontainers, micronaut-core 5.2.3).
- Last full-suite result: build successful, 8 tests executed, 0 skipped, 0 failed.

## Migration Rules

- Do not define local copies of Micronaut annotation helpers or custom annotation shims in docs snippets.
- Methods that implement or override a Java interface keep the Java (camelCase) name; other methods are snake_case.
- Prefer `@MicronautTest` with injected beans over `ApplicationContext.run()`.
- Python sources cannot live in a package that shadows a Java package that is imported
  (`micronaut.email.mailpit.client` is the package of `MailpitClient`): the Mailpit `OrderService`/`OrderServiceTest`
  snippets live in `io.micronaut.email.docs.mailpit` in every language.
- Java methods named after a Python keyword (`Email.Builder.from(...)`, `MailpitMessage.from()`) are called through the
  keyword-safe alias (`Email.builder().from_(...)`, `message.from_()`); never `getattr(obj, "from")`.
- Prefer normal imports (`from jakarta.mail.internet import MimeMessage`, `from micronaut.email import EmailSender`) over
  `java.type(...)`; imported classes work as runtime type arguments (`BeanContext.containsBean(EmailSender)`).
- Use Python's `logging` module (`LOG = logging.getLogger(__name__)`), not slf4j.
- Container/test-resources properties come from a Java `@ContextConfigurer` (`ApplicationContextConfigurer`), not
  `BootstrapPropertySourceLocator`.

## Active `@Disabled` Tests

None. The keyword-safe alias (`Email.builder().from_(...)`, `message.from_()`) works on objects returned from Java calls
with micronaut-core 5.2.3.

## `java.type` usages

None.

## Workarounds Kept In Tests

| Test | Reason |
| --- | --- |
| `io.micronaut.email.docs.mailpit.OrderServiceTest` | `TestPropertyProvider.getProperties()` is called by Micronaut Test before the application context, and with it the GraalPy runtime, exists, so a Python test class cannot provide the Mailpit container properties. The Python test runs with `@MicronautTest(environments=["mailpit"])` and the Java `io.micronaut.email.docs.MailpitTestConfigurer` (`@ContextConfigurer` implementing `ApplicationContextConfigurer`, `src/test/java`) supplies the SMTP/HTTP properties of the shared Testcontainers container to contexts of that environment via `builder.properties(...)`. Class-level JUnit annotations other than `@MicronautTest`/`@Property` (`@TestInstance`) are not emitted, so the test skips itself with `Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable())`. The configurer uses the `configure(ApplicationContext)` callback: `configure(ApplicationContextBuilder)` runs in the builder constructor, before `@MicronautTest` selects the environments, so the `mailpit` environment can only be checked on the built context. |
| `io.micronaut.email.mock.MockEmailSender` (Java, `test-suite-python/src/test/java`) | The mock `TransactionalEmailSender` of the other suites cannot be a Python class with micronaut-core 5.2.3: the inherited interface methods carry `@Valid`/`@NotNull` parameters and, with the validation processor on the classpath, the Python compile fails with `TypeElementVisitor [io.micronaut.validation.visitor.ValidationVisitor] failed during visitClass: Element of type [class io.micronaut.inject.ast.ReflectParameterElement] does not support adding annotations at compilation time` (`# TODO(python)` in the Java source; a Python `MockEmailSender(TransactionalEmailSender[object, Email])` compiled with 5.2.2). The Python tests inject the Java mock (`from micronaut.email.mock import MockEmailSender`). |
| `io.micronaut.email.docs.CustomizedJavaMailServiceTest` | The request customizer stored by the mock comes back to Python as the original Python function passed to `EmailSender.send(builder, consumer)`, so the test invokes it as a callable instead of `consumer.accept(message)`. The Java `MessageHeaderCapture extends MimeMessage` helper is replaced by a real `MimeMessage` whose header is read back: a Python class overriding a Java method that declares a checked exception (`addHeader throws MessagingException`) does not compile. |

## Intentionally Unsupported Snippet Targets

None.
