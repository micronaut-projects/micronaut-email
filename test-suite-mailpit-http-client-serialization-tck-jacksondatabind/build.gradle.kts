plugins {
    `java-library`
}
dependencies {
    testImplementation(mnTest.junit.platform.suite)
    testRuntimeOnly(mnTest.junit.jupiter.engine)
    testImplementation(projects.testSuiteMailpitHttpClientSerializationTck)
    testImplementation(mn.micronaut.jackson.databind)
}
tasks.withType<Test> {
    useJUnitPlatform()
}
