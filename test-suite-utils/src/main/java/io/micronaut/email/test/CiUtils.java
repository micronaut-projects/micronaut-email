package io.micronaut.email.test;

public final class CiUtils {

    public static final String ENV_CI = "CI";

    public static boolean runningOnCI() {
        return System.getenv(ENV_CI) != null;
    }
}
