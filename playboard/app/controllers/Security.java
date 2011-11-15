package controllers;

import models.User;

import java.lang.reflect.Method;

/**
 * User: olegchir
 */
public class Security extends Secure.Security {

    static boolean authentify(String username, String password) {
        return authCore(username, password);
    }

    static boolean authenticate(String username, String password) {
        return authCore(username, password);
    }

    static boolean authCore(String username, String password) {
        return User.connect(username, password) != null;
    }

    static boolean check(String profile) {
        if ("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdmin;
        }
        if ("advertiser".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdvertiser;
        }
        return false;
    }

    /*
    private static Object invokeSecurity(String m, Object... args) throws Throwable {
        Class[] argTypes = new Class[]{String.class, Object[].class};
        Method privateStringMethod = Security.class.getDeclaredMethod("invoke", argTypes);
        privateStringMethod.setAccessible(true);
        Object returnValue = privateStringMethod.invoke(null, m, args);
        return returnValue;
    }

    private static void check(Check check) throws Throwable {
        boolean hasProfile = false;
        for (String profile : check.value()) {
            hasProfile = (Boolean) invokeSecurity("check", profile);
            if (hasProfile) // profile found
                break;
        }

        if (!hasProfile) {
            invokeSecurity("onCheckFailed", ""); // profile var not user anyway!
        }
    } */
}
