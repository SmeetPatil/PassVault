package passvault.passvault.models;

import java.util.HashMap;
import java.util.Map;
import java.io.*;

public class UserManager {
    private static final String USER_FILE = "users.csv";
    private Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public boolean addUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        saveUsers();
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, which is fine for a new application
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}