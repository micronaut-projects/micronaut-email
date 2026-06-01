plugins {
    `java-library`
}

dependencies {
    annotationProcessor(mn.micronaut.inject.java)
    api(mnTest.micronaut.test.junit5)
    api(projects.micronautEmailMailpitHttpClient)
}
