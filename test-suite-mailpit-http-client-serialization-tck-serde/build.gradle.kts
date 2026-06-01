plugins {
    `java-library`
}
dependencies {
    testImplementation(mnTest.junit.platform.suite)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(projects.testSuiteMailpitHttpClientSerializationTck)
    testAnnotationProcessor(mnSerde.micronaut.serde.processor)
    testImplementation(mnSerde.micronaut.serde.jackson)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
