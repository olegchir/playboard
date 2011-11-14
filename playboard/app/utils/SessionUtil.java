package utils;

import play.cache.Cache;
import play.mvc.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * User: olegchir
 */
public class SessionUtil {

    public static final String ANON_SESSION_POST_LIST_KEY = "anonymousSessionPostList";
    public static final String EXPIRE_TIME = "10mn";

    public static String getListKey(String sessionId) {
        return ANON_SESSION_POST_LIST_KEY + "_" + sessionId;
    }

    public static void saveCommentAccessInSession(Long commentId) {
        String sessionId = Scope.Session.current().getId();
        String listKey = getListKey(sessionId);
        List<Long> cachedKeys = (List<Long>) Cache.get(listKey);
        if (null == cachedKeys) {
            cachedKeys = new ArrayList<Long>();
        }
        cachedKeys.add(commentId);
        Cache.set(listKey, cachedKeys, EXPIRE_TIME);
    }

    public static boolean checkCommentAccessInSession(Long commentId) {
        String sessionId = Scope.Session.current().getId();
        String listKey = getListKey(sessionId);
        List<Long> cachedKeys = (List<Long>) Cache.get(listKey);

        if (null == cachedKeys) {
            return false;
        }

        for (Long key : cachedKeys) {
            if (null!=key && null!=cachedKeys && key.equals(commentId)) {
                return true;
            }
        }

        return false;
    }
}
