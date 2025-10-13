plugins {
    java
}
dependencies {
    testAnnotationProcessor(platform(mn.micronaut.core.bom))
    testAnnotationProcessor(mn.micronaut.inject.java)
    testImplementation(mnTest.micronaut.test.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(mnLogging.logback.classic)
    testImplementation(projects.micronautEmailTemplate)
    testImplementation(mnHibernateValidator.micronaut.hibernate.validator)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
tasks.withType<Test> {
    useJUnitPlatform()
}

