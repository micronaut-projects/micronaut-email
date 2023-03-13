plugins {
    id("io.micronaut.build.internal.email-module")
}
dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    implementation(mnValidation.micronaut.validation)
    api(mnViews.micronaut.views.core)
    api(projects.micronautEmail)
}
