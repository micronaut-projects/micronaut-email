plugins {
    id("io.micronaut.build.internal.email-module")
}

dependencies {
    annotationProcessor(mnValidation.micronaut.validation.processor)
    api(mnValidation.validation)
    compileOnly(mnValidation.micronaut.validation)
    api(mn.micronaut.context)
    api(mn.micronaut.core.reactive)
    implementation(mnReactor.micronaut.reactor)
    testCompileOnly(mnValidation.micronaut.validation.processor)
    testImplementation(mnValidation.micronaut.validation)
}
