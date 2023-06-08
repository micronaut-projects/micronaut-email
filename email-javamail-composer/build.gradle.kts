plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(projects.micronautEmail)
    api(libs.managed.jakarta.mail)
    implementation(mnValidation.micronaut.validation)

    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
    testCompileOnly(mn.micronaut.inject.groovy)
    testRuntimeOnly(libs.managed.eclipse.angus)
}
