plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.postmark)
    // postmark depends on com.fasterxml.jackson.core/jackson-core@2.14.1 which contains a 1 vulnerability
    // https://ossindex.sonatype.org/vulnerability/CVE-2025-52999?component-type=maven&component-name=com.fasterxml.jackson.core%2Fjackson-core
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.0") // postmark depends on jackson-core  version

    api(projects.micronautEmail)
    implementation(mn.reactor)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)

    constraints {
        runtimeOnly("org.apache.tika:tika-core:3.3.1"){
            because("Older versions have security vulnerabilities")
        }
    }
}
