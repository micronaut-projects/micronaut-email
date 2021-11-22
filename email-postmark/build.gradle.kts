plugins {
    id("io.micronaut.build.internal.module")
}

group = "io.micronaut.email"

dependencies {
    annotationProcessor("io.micronaut:micronaut-validation")
    implementation("com.wildbit.java:postmark:1.7.5")
    implementation("io.micronaut:micronaut-validation")
    implementation("com.sendgrid:sendgrid-java:4.8.0")
    implementation(project(":email"))
}