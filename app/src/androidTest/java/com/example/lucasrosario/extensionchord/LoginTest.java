package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseUser;
import com.parse.ParseException;

/**
 *  Login logic tests.
 *  Tests LoginManager.java
 */
public class LoginTest extends ActivityUnitTestCase<MainActivity> {
    LoginManager userManager;
    MainActivity testActivity;

    public LoginTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(testIntent, null, null);

        testActivity = getActivity();
        userManager = new LoginManager(testActivity);
        userManager.setTestFlag();

        Parse.initialize(testActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");
        try {
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        }
        catch(ParseException e){
            Log.i("Login Test", "User not found.");
        }
    }

    @Override
    public void tearDown() throws ParseException{
        //Delete the user from the server.
        if(ParseUser.getCurrentUser() != null)
            ParseUser.getCurrentUser().delete();
    }

    public void testLogin() throws Exception{
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseUser.logOut();
        Thread.sleep(1000);
        userManager.login("Tester", "Banana");

        assertNotNull(ParseUser.getCurrentUser());
    }

    public void testSignUp() throws Exception{
        //Testing if user gets signed up and then logged in.
        userManager.signup("Tester", "Banana");
        Thread.sleep(1000);
        assertNotNull(ParseUser.getCurrentUser());
    }

    public void testLogOut() throws ParseException{
        //First we need to create a user to log in then out with.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();
        assertNotNull(ParseUser.getCurrentUser());

        //Now we need to test the logout functionality.
        userManager.logout();
        assertNull(ParseUser.getCurrentUser());
    }
}
