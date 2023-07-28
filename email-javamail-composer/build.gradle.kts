plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(projects.micronautEmail)
    api(libs.managed.jakarta.mail)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
    testCompileOnly(mn.micronaut.inject.groovy)
    testRuntimeOnly(libs.managed.eclipse.angus)
}
