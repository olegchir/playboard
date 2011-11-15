package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.DeadboltHandler;
import play.*;
import play.mvc.*;

import java.util.*;

import models.*;


/**
 * User: olegchir
 */


@With(Deadbolt.class)
public class Admin extends Controller {

    @Before
    static void setConnectedUser() {
        if (Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }

    public static void index() {
        render();
    }

}