package service;

import java.util.prefs.Preferences;

/**
 * Manages who is logged in, makes sure only one session exists, saves to preferences automatically, and resets on logout.
 * Fully thread-safe.
 */
public class UserSession {

    private static volatile UserSession instance;
    private static final Object lock = new Object(); // Separate lock object (best practice)

    private String userName;
    private String password;
    private String privileges;

    private UserSession(String userName, String password, String privileges) {
        this.userName = userName;
        this.password = password;
        this.privileges = privileges;

        Preferences userPreferences = Preferences.userRoot();
        userPreferences.put("USERNAME", userName);
        userPreferences.put("PASSWORD", password);
        userPreferences.put("PRIVILEGES", privileges);
    }

    public static UserSession getInstance(String userName, String password, String privileges) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new UserSession(userName, password, privileges);
                }
            }
        }
        return instance;
    }

    public static UserSession getInstance(String userName, String password) {
        return getInstance(userName, password, "NONE");
    }

    public synchronized String getUserName() {
        return userName;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized String getPrivileges() {
        return privileges;
    }

    public synchronized void cleanUserSession() {
        userName = null;
        password = null;
        privileges = null;
        instance = null;
    }

    @Override
    public synchronized String toString() {
        return "UserSession{" +
                "userName='" + userName + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }
}
