package io.micronaut.email.mailpit.http.client.serialization.jacksondatabind;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages("io.micronaut.email.mailpit.http.client.serialization")
@Suite(failIfNoTests = false)
@SuiteDisplayName("Micronaut Jackson Databind for Mailpit HTTP Client Model Serialization")
public class JacksonMailpitSerializationSuite {
}
