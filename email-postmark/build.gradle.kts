plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    api(libs.managed.postmark)
    api(projects.micronautEmail)
    implementation(mn.reactor)
    testImplementation(mn.micronaut.http)
    testImplementation(projects.testSuiteUtils)

    constraints {
        api("org.apache.httpcomponents.client5:httpclient5:5.6.4") {
            because("Postmark 1.13.0 brings vulnerable HttpClient 5.5 and HttpCore 5.3.4 versions")
        }
        runtimeOnly("org.apache.tika:tika-core:3.3.1"){
            because("Older versions have security vulnerabilities")
        }
    }
}
