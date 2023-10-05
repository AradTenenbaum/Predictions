package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Auth {
    public static void admin(HttpServletRequest req, HttpServletResponse resp) {
        String usernameFromSession = Session.getUsername(req);
        if(usernameFromSession == null) {
            Servlet.unauthorized(resp);
        }
        if(!usernameFromSession.equals(Constants.ADMIN)) {
            Servlet.forbidden(resp);
        }
    }
}
