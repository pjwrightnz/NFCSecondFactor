package com.example.paul.nfcsecondfactor0.nfcSecondFactorServer;

import java.util.HashMap;

/**
 *  UserDataPersisatnce class. Mocks the data persistence of the server. Due to the NFC Card data
 *  sensitivities, the data is only persisted for teh life of the application. The database is
 *  seeded with a test account and test NFC Card. The methods allow adding a User, getting a user
 *  and checking if a user exists.
 *
 * @author Paul Wright
 * @version 1.0 Prototype 1. Activities, intents and servers.
 * @see #checkUserData(String userID)
 * @see #addNewUser(User newUser)
 * @see #getUser(String userID)
 */
public class UserDataPersistence {

    /**
     * Static Hashmap for user data, string is userID, user has all user data
     */
    public static HashMap<String, User> userData = new HashMap<String, User>();

    //checkUser Exists
    public boolean checkUserData(String userID) {
        if (userData.containsKey(userID)) {
            return true;
        } else {
            return false;
        }
    }
    // method to add user on registration
    public void addNewUser(User newUser) {
        userData.put(newUser.userID, newUser);
    }

    //method to get existing user
    public User getUser(String userID) {
        User existingUser = userData.get(userID);
        return  existingUser;
    }


}
