package controllers;

import controllers.deadbolt.Deadbolt;
import controllers.deadbolt.DeadboltHandler;
import controllers.deadbolt.ExternalizedRestrictionsAccessor;
import controllers.deadbolt.RestrictedResourcesHandler;
import models.MyRole;
import models.User;
import models.deadbolt.ExternalizedRestrictions;
import models.deadbolt.RoleHolder;
import play.mvc.Controller;

import java.util.List;

/**
 * User: olegchir
 */
public class MyDeadboltHandler extends Controller implements DeadboltHandler {
    public void beforeRoleCheck()
    {
        // Note that if you provide your own implementation of Secure's Security class you would refer to that instead
        if (!Security.isConnected())
        {
            try
            {
                if (!session.contains("username"))
                {
                    flash.put("url", "GET".equals(request.method) ? request.url : "/");
                    Secure.login();
                }
            }
            catch (Throwable t)
            {
                // handle this in an app-specific way
            }
        } else {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }

    public RoleHolder getRoleHolder()
    {
        String userName = Secure.Security.connected();
        User currUser = User.getByUserName(userName);
        return currUser;
    }

    public void onAccessFailure(String controllerClassName)
    {
       forbidden();
    }

    public ExternalizedRestrictionsAccessor getExternalizedRestrictionsAccessor()
    {
        return new ExternalizedRestrictionsAccessor()
        {
            public ExternalizedRestrictions getExternalizedRestrictions(String name)
            {
                return null;
            }
        };
    }

    public RestrictedResourcesHandler getRestrictedResourcesHandler()
    {
        return null;
    }
}
