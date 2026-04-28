plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.mailjet.client)
    api(projects.micronautEmail)
    implementation(mnReactor.micronaut.reactor)
    testImplementation(projects.testSuiteUtils)
    testImplementation(mn.micronaut.http)
    testImplementation(mnSerde.micronaut.serde.jackson)

    constraints {
        runtimeOnly("com.google.code.gson:gson:2.14.0"){
            because("Older versions have security vulnerabilities")
        }
    }
}
