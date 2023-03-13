plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    implementation(mnValidation.micronaut.validation)
    api(mn.micronaut.context)
    implementation(mnReactor.micronaut.reactor)

    testCompileOnly(mnValidation.micronaut.validation.processor)
}
