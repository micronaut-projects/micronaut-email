plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(libs.managed.postmark)
    api(projects.micronautEmail)
    implementation(mnValidation.micronaut.validation)
    implementation(mn.reactor)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
}
