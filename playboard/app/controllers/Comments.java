package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.Restrict;
import controllers.deadbolt.Restrictions;
import play.*;
import play.mvc.*;

/**
 * User: olegchir
 */
@Restrictions({@Restrict("admin"),@Restrict("advertiser")})
@With(Deadbolt.class)
public class Comments extends CRUD {
}
