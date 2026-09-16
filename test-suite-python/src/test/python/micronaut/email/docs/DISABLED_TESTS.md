# Python Docs Disabled Test Inventory

This file tracks Python docs examples of Micronaut Email that are present but disabled, or that deviate from the
Java example because the direct port currently fails compilation or at runtime (Python compiler gaps).

## Reconciliation

- Last generated active `@Disabled` count: 5.
- Last generated command: `rg -n "@Disabled\\(" test-suite-python/src/test/python`.
- Last full-suite command: `./gradlew :test-suite-python:test -Ppython-ci` (Mailpit via Testcontainers).
- Last full-suite result: build successful, 8 tests executed, 5 skipped (`@Disabled`, see below), 0 failed.

## Migration Rules

- Do not define local copies of Micronaut annotation helpers or custom annotation shims in docs snippets.
- Methods that implement or override a Java interface keep the Java (camelCase) name; other methods are snake_case.
- Prefer `@MicronautTest` with injected beans over `ApplicationContext.run()`.
- Python sources cannot live in a package that shadows a Java package that is imported
  (`micronaut.email.mailpit.client` is the package of `MailpitClient`): the Mailpit `OrderService`/`OrderServiceTest`
  snippets live in `io.micronaut.email.docs.mailpit` in every language.
- Java methods named after a Python keyword (`Email.Builder.from(...)`, `MailpitMessage.from()`) are called through the
  keyword-safe alias (`Email.builder().from_(...)`, `message.from_()`); never `getattr(obj, "from")`.
- Prefer normal imports (`from jakarta.mail.internet import MimeMessage`) over `java.type(...)`; the remaining `java.type`
  calls are listed below with the reason.
- Use Python's `logging` module (`LOG = logging.getLogger(__name__)`), not slf4j.
- Container/test-resources properties come from a Java `@ContextConfigurer` (`ApplicationContextConfigurer`), not
  `BootstrapPropertySourceLocator`.

## Active `@Disabled` Tests

All five are the same Python compiler gap (`# TODO(python): keyword alias on foreign object`): the keyword-safe alias
(`from_`) is only rewritten to the Java method name when the receiver is a name tracked by the compiler (an imported
class or a module-level `java.type(...)` assignment). On an object returned from a Java call (`Email.builder()`,
`MailpitClient.getMessage(...)`) it fails at runtime with `AttributeError: foreign object has no attribute 'from_'`.
The samples keep the intended `from_` form (never `getattr(obj, "from")`); no restructuring makes the receiver a
tracked name because `Email.Builder`/`MailpitMessage` instances only exist as return values. Remove the `@Disabled`
markers once the compiler resolves aliases on foreign objects.

| Test | Snippet exercised | Reason |
| --- | --- | --- |
| `io.micronaut.email.docs.WelcomeServiceTest` | `WelcomeService` | `Email.builder().from_(...)` |
| `io.micronaut.email.docs.WelcomeWithTemplateServiceTest` | `WelcomeWithTemplateService` | `Email.builder().from_(...)` |
| `io.micronaut.email.docs.SendAttachmentServiceTest` | `SendAttachmentService` | `Email.builder().from_(...)` |
| `io.micronaut.email.docs.CustomizedJavaMailServiceTest` | `CustomizedJavaMailService` | `Email.builder().from_(...)` |
| `io.micronaut.email.docs.mailpit.OrderServiceTest` | `OrderService`, `OrderServiceTest` | `message.from_()` on the `MailpitMessage` returned by `MailpitClient.getMessage("latest")`. Verified locally to pass end to end (Mailpit via Testcontainers, properties from `MailpitTestConfigurer`) with the alias temporarily replaced. |

## `java.type` usages

| File | Reason |
| --- | --- |
| `EmailSenderTest.py` (`EmailSender`) | Runtime type argument to `BeanContext.containsBean(Class)`; the imported interface (`from micronaut.email import EmailSender`) is a Python wrapper (`_MicronautJavaType`) that fails with `Unsupported operation identifier 'typeHashCode'` when passed to Java. |
| `TemplateBodyDecoratorTest.py` (`TemplateBodyDecorator`) | Same: runtime type argument to `BeanContext.containsBean(Class)`, imported interface wrapper rejected by Java. |

## Workarounds Kept In Tests

| Test | Reason |
| --- | --- |
| `io.micronaut.email.docs.mailpit.OrderServiceTest` | `TestPropertyProvider.getProperties()` is called by Micronaut Test before the application context, and with it the GraalPy runtime, exists, so a Python test class cannot provide the Mailpit container properties. The Python test runs with `@MicronautTest(environments=["mailpit"])` and the Java `io.micronaut.email.docs.MailpitTestConfigurer` (`@ContextConfigurer` implementing `ApplicationContextConfigurer`, `src/test/java`) supplies the SMTP/HTTP properties of the shared Testcontainers container to contexts of that environment via `builder.properties(...)`. Class-level JUnit annotations other than `@MicronautTest`/`@Property` (`@TestInstance`) are not emitted, so the test skips itself with `Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable())`. The configurer uses the `configure(ApplicationContext)` callback: `configure(ApplicationContextBuilder)` runs in the builder constructor, before `@MicronautTest` selects the environments, so the `mailpit` environment can only be checked on the built context. |
| `io.micronaut.email.docs.CustomizedJavaMailServiceTest` | The request customizer passed to `EmailSender.send(builder, consumer)` reaches the Python `MockEmailSender` as the original Python function (GraalPy passes callables through), not as a `java.util.function.Consumer`, so the test invokes it as a callable. The Java `MessageHeaderCapture extends MimeMessage` helper is replaced by a real `MimeMessage` whose header is read back (Python classes cannot extend Java classes). |

## Intentionally Unsupported Snippet Targets

None.
