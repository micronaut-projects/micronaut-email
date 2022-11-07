plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    api(libs.managed.postmark)
    api(projects.email)
    implementation(mn.micronaut.validation)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
}
