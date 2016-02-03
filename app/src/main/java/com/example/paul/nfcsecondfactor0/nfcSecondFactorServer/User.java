package com.example.paul.nfcsecondfactor0.nfcSecondFactorServer;


 /**
 *  User class. Instances of this are created to persist and parse users and compare login user data
 *  to registered data or compare registering users against already utilised UserIDs. The constructor
 *  is called on registration of a new user. getUserID/Password/NFCCard methods are utilised when
 *  authenticating a user. getUserID()  is also utilised to ensure a new user does not register as
 *  an already existing userID.
 *
 * @author Paul Wright
 * @version 1.0 Prototype 1. Activities, intents and servers.
 * @version 1.1 Prototype 2. Add NFC Functionality.
 * @see #User(String userID, String password, String email, String nfcCardID)
 */
 public class User{
    /**
     * Class variables that make up the attributes of a User.
     */
    String userID, password, email, nfcCardID;

    /**
     * public constructing, only constructor available. Creation of a user must include their, userID
     * password, email and NFCCardID.
     * @param userID
     * @param password
     * @param email
     * @param nfcCardID
     */
    public User(String userID, String password, String email, String nfcCardID) {
        this.userID = userID;
        this.password = password;
        this.email = email;
        this.nfcCardID = nfcCardID;
    }
}
