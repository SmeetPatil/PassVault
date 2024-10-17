package passvault.passvault.models;

import passvault.passvault.utils.DatabaseManager_App;

public class UserManager {
    private DatabaseManager_App dbManagerA;

    public UserManager() {
        dbManagerA = new DatabaseManager_App();
    }

    public boolean addUser(String username, String password, String email) {
        return dbManagerA.addUser(username, password, email);
    }

    public boolean authenticateUser(String username, String password) {
        return dbManagerA.authenticateUser(username, password);
    }

    public boolean isUserRegistered(String username, String email) {
        return dbManagerA.isUserRegistered(username, email);
    }

    public boolean resetPassword(String username, String newPassword) {
        return dbManagerA.resetPassword(username, newPassword);
    }
}