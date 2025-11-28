import org.gradle.api.internal.tasks.testing.TestFramework

plugins {
    id("io.micronaut.build.internal.email-module")
}
dependencies {
    api(libs.managed.mailtrap)
    api(projects.micronautEmail)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.mockito.core)
    testImplementation(mnTest.mockito.junit.jupiter)
}
micronautBuild {
    binaryCompatibility.enabled = false
    testFramework = io.micronaut.build.TestFramework.JUNIT5
}
