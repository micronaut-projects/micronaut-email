plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    api(projects.email)
    api(libs.managed.jakarta.mail)
    implementation(mn.micronaut.validation)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
    testCompileOnly(mn.micronaut.inject.groovy)
}
