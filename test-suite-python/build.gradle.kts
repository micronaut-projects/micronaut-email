plugins {
    `java-library`
    id("io.micronaut.build.internal.email-tests")
    id("io.micronaut.build.internal.python")
}

dependencies {
    // The Java helper in src/test/java (Mailpit @ContextConfigurer) is processed by javac
    testAnnotationProcessor(mn.micronaut.inject.java)

    // Annotation processors of the Python sources MUST be testImplementation (not testAnnotationProcessor):
    // the Python compiler takes the (jar-resolved) compile classpath as its annotation processor path.
    testImplementation(mnValidation.micronaut.validation.processor)
    testImplementation(mnSerde.micronaut.serde.processor)
    testImplementation(mn.micronaut.inject.python.test)
    testImplementation(mn.micronaut.context.python)

    testImplementation(mnValidation.micronaut.validation)

    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(mnTest.junit.platform.launcher)

    testImplementation(projects.testSuiteUtils)
    testImplementation(projects.micronautEmail)
    testImplementation(projects.micronautEmailMailpitHttpClient)
    testImplementation(projects.micronautEmailTemplate)
    testImplementation(projects.micronautEmailSendgrid)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnViews.micronaut.views.velocity)
    testImplementation(projects.micronautEmailJavamail)
    testRuntimeOnly(libs.managed.eclipse.angus)
    testRuntimeOnly(mnLogging.logback.classic)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    systemProperty("micronaut.python.pool.enabled", "false")
    // a Truffle host-interop assertion trips on varargs overloads
    enableAssertions = false
}
