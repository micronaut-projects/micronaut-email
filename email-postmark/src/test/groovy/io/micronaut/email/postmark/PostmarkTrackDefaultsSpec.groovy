package io.micronaut.email.postmark

import io.micronaut.context.annotation.Property
import io.micronaut.email.TrackLinks
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "postmark.api-token", value = "xxx")
@MicronautTest(startApplication = false)
class PostmarkTrackDefaultsSpec extends Specification {

    @Inject
    PostmarkConfiguration postmarkConfiguration

    void "by default opens are not tracked"() {
        expect:
        !postmarkConfiguration.getTrackOpens()
    }

    void "by default links are not track"() {
        expect:
        TrackLinks.DO_NOT_TRACK == postmarkConfiguration.getTrackLinks()
    }
}
