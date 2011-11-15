package controllers;

import models.User;

/**
 * User: olegchir
 */
public class Security extends Secure.Security {

    static boolean authentify(String username, String password) {
        return authCore(username,password);
    }

   static boolean authenticate(String username, String password) {
        return authCore(username,password);
    }

    static boolean authCore(String username, String password) {
        return User.connect(username, password) != null;
    }
}
