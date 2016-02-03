package com.example.paul.nfcsecondfactor0.nfcSecondFactorServer;

import java.util.HashMap;


public class UserDataPersistence {

    //static hashmap for user data, string is userID, user has all user data
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
