package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import play.*;
import play.mvc.*;

/**
 * User: olegchir
 */
@Restrictions(@Restrict("admin"))
@With(Deadbolt.class)
public class Users extends CRUD {

}
