plugins {
    id("io.micronaut.build.internal.module")
}
group = "io.micronaut.email"

dependencies {
    annotationProcessor("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-validation")
}
