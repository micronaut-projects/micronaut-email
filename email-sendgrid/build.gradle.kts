plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(libs.managed.sendgrid.java)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    implementation(mnValidation.micronaut.validation)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
}
