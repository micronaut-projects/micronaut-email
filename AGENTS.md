# Micronaut Email Agent Guidance

This repository contains Micronaut Email modules and provider integrations for
transactional email. Keep changes focused on email APIs, provider modules,
documentation, tests, or build logic that directly supports those modules.

## Repository Map

- `email`: core email API and shared behavior.
- `email-javamail`, `email-javamail-composer`, `email-sendgrid`,
  `email-amazon-ses`, `email-postmark`, `email-mailjet`, `email-mailtrap`, and
  `email-template`: provider and integration modules.
- `email-bom`: dependency-management BOM.
- `test-suite*` and `test-suite-utils`: functional and compatibility tests.
- `src/main/docs`: user guide sources.
- `buildSrc`, `config`, `gradle`, and root Gradle files: build and release
  infrastructure.
  
The Gradle project names for folders starting with `email-` are prefixed with `micronaut-`. For example, for `email-javamail`, the Gradle project name is `micronaut-email-javamail`. In other words, to run the tests in that folder, use `./gradlew :micronaut-email-javamail:test`.

To run the tests in the `test-suite` folder, use `./gradlew :test-suite:test`. Please note that it is not prefixed because the folder name does not start with `email-`.

## Working Rules

- Use the Gradle wrapper for all build work.
- Scope implementation changes to the affected module and its tests.
- Preserve binary compatibility for public APIs; run `./gradlew japiCmp` when a
  public API surface changes.
- Do not put implementation code in the root project. The root project
  coordinates the build and documentation.
- Follow existing `.github/instructions/*.instructions.md` and `.clinerules/*`
  guidance for coding and documentation details.

## Verification

- For code changes, run the narrowest affected module compile/test tasks first.
  Example: `./gradlew :micronaut-email-javamail:test`.
- For documentation changes, run `./gradlew docs` when the guide output is
  affected.
- For formatting or license-header failures on new files, use
  `./gradlew -q spotlessApply`, then rerun the failed check.
- Record any skipped verification with the exact reason.
Mk 