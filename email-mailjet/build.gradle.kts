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
        runtimeOnly("org.json:json:20260719") {
            because("Older versions have security vulnerabilities (GHSA-ghc9-756g-vrcv)")
        }
        runtimeOnly("com.google.code.gson:gson:2.15.0"){
            because("Older versions have security vulnerabilities")
        }
    }
}
