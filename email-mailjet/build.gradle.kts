plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    api(libs.managed.mailjet.client)
    api(projects.email)
    implementation(mn.micronaut.reactor)
    implementation(mn.micronaut.validation)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
}
