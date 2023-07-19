plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.sendgrid.java)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
}
