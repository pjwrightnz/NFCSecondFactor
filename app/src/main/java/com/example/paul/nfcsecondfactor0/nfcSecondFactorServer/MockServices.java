package com.example.paul.nfcsecondfactor0.nfcSecondFactorServer;

/**
 *  MockServices class which mocks a registration and authentication services. It has two primary methods -
 *  register user (add a new user to the database) and authenticate user (confirm a user's ID,
 *  password and NFC Card Match). Additionally, a seedUserData method is used for demonstration
 *  purposes to seed user data.
 *
 * @author Paul Wright
 * @version 1.0 Prototype 1. Activities, intents and servers.
 * @version 1.1 Prototype 2. Add NFC Functionality.
 * @see #registerUser(String userID, String password, String email, String nfcCardID)
 * @see #authenticateUser(String userID, String password, String nfcCardID)
 * @see #seedUserData( )
 *
 */

/**
 *
 */
public class MockServices {

    UserDataPersistence udp = new UserDataPersistence();
    int DEFAULT = 3;
    int PASSWORD_FAILED = 2;
    int NFC_FAILED = 1;
    int AUTHENTICATED = 0;

    /**
     ** Method to register a new user. Boolean return utilised to mock server faults thrown
     * if username is already in use.
     *
     * @param userID
     * @param password
     * @param email
     * @param nfcCardID
     * @return boolean
     *
     */
    public Boolean registerUser(String userID, String password, String email, String nfcCardID) {
        //create new user
        User newUser = new User(userID, password, email, nfcCardID);
        //check if user already exists, return false else add user to the Map. Map used instead of
        //data persistence via hashmap to keep app lite for proof of concept only.
        if (udp.checkUserExists(userID)) {
            return false;
        } else {
            udp.addNewUser(newUser);
            return true;
        }
    }

    /**
     * Method to authenticate an already registered user. return values simulate faults that a
     * server would throw for various failed/successful login attempts.
     *
     * @param userID
     * @param password
     * @param nfcCardID
     * @return returnValue
     *
     */
    public int authenticateUser(String userID, String password, String nfcCardID) {
        int returnValue = DEFAULT;
        if (udp.checkUserExists(userID)) {
            returnValue = PASSWORD_FAILED;
            if (udp.getUser(userID).password.equals(password)) {
                returnValue = NFC_FAILED;
                if (udp.getUser(userID).nfcCardID.equals(nfcCardID)) {
                    returnValue = AUTHENTICATED;
                }
            }
        }
        return returnValue;
    }

    /**
     * Method for test puposes only. Seeds the Hashmap with a user for proof only.
     */
    public void seedUserData() {
        registerUser("Test", "pass", "email", "0x6f936924");
    }
}
