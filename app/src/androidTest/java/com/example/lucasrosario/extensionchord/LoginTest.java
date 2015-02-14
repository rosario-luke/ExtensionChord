package com.example.lucasrosario.extensionchord;

import android.test.AndroidTestCase;

import com.parse.Parse;
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
        userManager.setTestFlag();
        Parse.initialize(testActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");
    }

    @Override
    public void tearDown() throws ParseException{
        //Delete the user from the server.
        if(ParseUser.getCurrentUser() != null)
            ParseUser.getCurrentUser().delete();
    }

    public void testLogin() throws ParseException{
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseUser.logOut();
        userManager.login("Tester", "Banana");

        assertNotNull(ParseUser.getCurrentUser());
    }

    public void testSignUp(){
        //Testing if user gets signed up and then logged in.
        userManager.signup("Tester", "Banana");

        assertNotNull(ParseUser.getCurrentUser());
    }
}
