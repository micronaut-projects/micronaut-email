plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(libs.managed.mailjet.client)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    implementation(mnValidation.micronaut.validation)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
    testImplementation(mnSerde.micronaut.serde.jackson)
}
