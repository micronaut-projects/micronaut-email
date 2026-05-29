plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnSerde.micronaut.serde.processor)
    api(mnSerde.micronaut.serde.api)
    api(mn.micronaut.http.client.core)
    api(mn.jackson.annotations)
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mnSerde.micronaut.serde.jackson)
    testImplementation(mnTestResources.testcontainers.core)
    testImplementation(projects.testSuiteUtils)
}

micronautBuild {
    binaryCompatibility.enabled = false
    testFramework = io.micronaut.build.TestFramework.JUNIT6
}
