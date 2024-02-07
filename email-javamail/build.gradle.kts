plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(projects.micronautEmailJavamailComposer)
    implementation(mn.reactor)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mnTestResources.testcontainers.core)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mn.micronaut.http.client.jdk)
    testRuntimeOnly(libs.managed.eclipse.angus)
}
