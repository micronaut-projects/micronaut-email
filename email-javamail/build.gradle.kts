plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(libs.micronaut.validation)
    api(projects.emailJavamailComposer)
    implementation(mn.micronaut.validation)
    implementation(mn.reactor)
    testImplementation(project(":test-suite-utils"))
    testImplementation(mn.micronaut.http)
    testImplementation(libs.testcontainers)
    testImplementation(mn.micronaut.http.client)
    testCompileOnly(mn.micronaut.inject.groovy)
    testImplementation(mn.micronaut.serde.jackson)
}