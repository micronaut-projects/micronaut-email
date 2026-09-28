plugins {
    id("io.micronaut.build.internal.email-module")
}
dependencies {
    annotationProcessor(mnSerde.micronaut.serde.processor)
    api(projects.micronautEmail)
    api(mnSerde.micronaut.serde.api)
    api(mn.jackson.annotations)
    implementation(mn.micronaut.http.client.core)
    implementation(mnReactor.micronaut.reactor)

    testAnnotationProcessor(mn.micronaut.inject.java)
    testAnnotationProcessor(mnSerde.micronaut.serde.processor)
    testImplementation(mn.micronaut.http.client)
    testImplementation(mn.micronaut.http.server.netty)
    testImplementation(mnSerde.micronaut.serde.jackson)
}
micronautBuild {
    binaryCompatibility.enabled = false
    testFramework = io.micronaut.build.TestFramework.JUNIT6
}
