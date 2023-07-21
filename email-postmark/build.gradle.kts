plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.postmark)
    api(projects.micronautEmail)
    implementation(mn.reactor)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
}
