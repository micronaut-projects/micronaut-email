package io.micronaut.email.javamail

import io.micronaut.context.condition.Condition
import io.micronaut.context.condition.ConditionContext

class GmailCondition implements Condition {
    @Override
    boolean matches(ConditionContext context) {
        return System.getenv("GMAIL_USERNAME") != null && System.getenv("GMAIL_PASSWORD") != null;
    }
}
