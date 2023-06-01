plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(projects.micronautEmailJavamailComposer)
    implementation(mnValidation.micronaut.validation)
    implementation(mn.reactor)
    testImplementation(projects.testSuiteUtils)
    testImplementation(libs.testcontainers)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mn.micronaut.http.client.jdk)
}
