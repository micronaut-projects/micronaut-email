plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mn.micronaut.validation)
    implementation(mn.micronaut.validation)
    api(libs.micronaut.context)
    api(mn.micronaut.reactor)
}
