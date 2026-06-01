plugins {
    `java-library`
    id("io.micronaut.build.internal.email-tests")
}

dependencies {
    testAnnotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(mnValidation.micronaut.validation.processor)

    testImplementation(mnValidation.micronaut.validation)

    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(mnTest.junit.platform.launcher)

    testImplementation(projects.testSuiteUtils)
    testImplementation(projects.micronautEmail)
    testImplementation(projects.micronautEmailMailpitHttpClient)
    testImplementation(projects.micronautEmailTemplate)
    testImplementation(mn.micronaut.http.client)
    testAnnotationProcessor(mnSerde.micronaut.serde.processor)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnViews.micronaut.views.velocity)
    testImplementation(projects.micronautEmailJavamail)
    testRuntimeOnly(libs.managed.eclipse.angus)
}
tasks.test {
    useJUnitPlatform()
}
