plugins {
    groovy
    id("io.micronaut.build.internal.email-tests")
}

dependencies {
    testCompileOnly(mnValidation.micronaut.validation.processor)
    testCompileOnly(mn.micronaut.inject.groovy)

    testImplementation(platform(mn.micronaut.core.bom))
    testImplementation(mnValidation.micronaut.validation)

    testImplementation(mnTest.micronaut.test.spock ) {
        exclude(module = "groovy-all")
    }
    testImplementation(projects.micronautEmailMailpitHttpClient)
    testImplementation(projects.testSuiteUtils)
    testImplementation(projects.micronautEmail)
    testImplementation(projects.micronautEmailTemplate)
    testImplementation(mnTest.micronaut.test.spock)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnViews.micronaut.views.velocity)
    testRuntimeOnly(mnTest.junit.platform.launcher)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(projects.micronautEmailJavamail)
    testRuntimeOnly(libs.managed.eclipse.angus)
}

tasks.test {
    useJUnitPlatform()
}
