plugins {
    id("io.micronaut.build.internal.kotlin-kapt")
    id("io.micronaut.build.internal.email-tests")
    id("io.micronaut.build.internal.kotlin-base")
}

dependencies {
    testAnnotationProcessor(mnValidation.micronaut.validation.processor)
    testImplementation(mnValidation.micronaut.validation)

    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)

    kaptTest(mn.micronaut.inject.java)

    testImplementation(projects.micronautEmailMailpitHttpClient)
    testImplementation(projects.testSuiteUtils)
    testImplementation(projects.micronautEmail)
    testImplementation(projects.micronautEmailTemplate)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnViews.micronaut.views.velocity)
    testRuntimeOnly(mnTest.junit.platform.launcher)
    testImplementation(projects.micronautEmailJavamail)
    testRuntimeOnly(libs.managed.eclipse.angus)
}
tasks.test {
    useJUnitPlatform()
}
