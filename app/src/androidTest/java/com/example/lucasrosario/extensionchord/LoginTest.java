package com.example.lucasrosario.extensionchord;

import android.test.AndroidTestCase;

import com.parse.ParseUser;
import com.parse.ParseException;

/**
 *  Login logic tests.
 *  Tests LoginManager.java
 */
public class LoginTest extends AndroidTestCase {
    LoginManager  userManager;
    LoginActivity testActivity;

    @Override
    public void setUp(){
        testActivity = new LoginActivity();
        userManager = new LoginManager(testActivity);
    }

    public void testLogin() throws ParseException{
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseUser.logOut();
        userManager.login("Tester", "Banana");

        assertTrue(ParseUser.getCurrentUser() != null);
    }

    public void testSignUp(){
        //Testing if user gets signed up and then logged in.
        userManager.signup("Tester", "Banana");

        assertTrue(ParseUser.getCurrentUser() != null);
    }
}
