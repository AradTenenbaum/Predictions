package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Auth {
    public static boolean admin(HttpServletRequest req, HttpServletResponse resp) {
        String usernameFromSession = Session.getUsername(req);
        if(usernameFromSession == null) {
            Servlet.unauthorized(resp);
            return false;
        }
        if(!usernameFromSession.equals(Constants.ADMIN)) {
            Servlet.forbidden(resp);
            return false;
        }
        return true;
    }

    public static String user(HttpServletRequest req, HttpServletResponse resp) {
        String usernameFromSession = Session.getUsername(req);
        if(usernameFromSession == null) {
            Servlet.unauthorized(resp);
            return null;
        } else {
            return usernameFromSession;
        }
    }
}
