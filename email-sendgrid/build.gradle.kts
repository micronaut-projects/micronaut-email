plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.sendgrid.java)
    // sendgrid depends on com.fasterxml.jackson.core/jackson-core@2.14.1 which contains a 1 vulnerability
    // https://ossindex.sonatype.org/vulnerability/CVE-2025-52999?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.1") // postmark depends on jackson-core  version

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
