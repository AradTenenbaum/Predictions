package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private List<String> users;
    private boolean isAdminConnected;
    public UserService() {
        users = new ArrayList<>();
        isAdminConnected = false;
    }

    public void adminConnected() {
        isAdminConnected = true;
    }

    public void adminLogout() {isAdminConnected = false;}

    public boolean isAdminConnected() {
        return isAdminConnected;
    }

    public boolean isUserExists(String username) {
        Optional<String> user = users.stream().filter(u -> u.equals(username)).findFirst();
        return user.isPresent();
    }

    synchronized public void addUser(String username) {
        users.add(username);
    }
}
