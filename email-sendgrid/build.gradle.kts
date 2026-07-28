plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.sendgrid.java)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testRuntimeOnly(mnTest.junit.platform.launcher)
}
