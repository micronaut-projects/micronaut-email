plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)

    api(projects.micronautEmailJavamailComposer)
    api(mnAws.micronaut.aws.sdk.v2)
    api(libs.ses)

    implementation(mnReactor.micronaut.reactor)
    implementation(mnValidation.micronaut.validation)

    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
}
