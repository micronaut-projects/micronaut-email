plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    api(projects.emailJavamailComposer)
    api(mn.micronaut.aws.sdk.v2)
    api(libs.ses)
    implementation(mn.micronaut.reactor)
    implementation(mn.micronaut.validation)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
}
