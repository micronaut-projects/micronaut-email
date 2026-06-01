package io.micronaut.email.mailpit.http.client.serialization.serde;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages("io.micronaut.email.mailpit.http.client.serialization")
@Suite(failIfNoTests = false)
@SuiteDisplayName("Micronaut Serialization for Mailpit HTTP Client Model Serialization")
public class SerdeMailpitSerializationSuite {
}
