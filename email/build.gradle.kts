plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    implementation(mn.micronaut.validation)
    api(mn.micronaut.context)
    implementation(mnReactor.micronaut.reactor)
}
