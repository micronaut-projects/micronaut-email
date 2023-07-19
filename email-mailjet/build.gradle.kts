plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.mailjet.client)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
}
