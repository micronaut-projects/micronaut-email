plugins {
    id("io.micronaut.build.internal.email-module")
}
dependencies {
    api(mnViews.micronaut.views.core)
    api(projects.micronautEmail)
}
