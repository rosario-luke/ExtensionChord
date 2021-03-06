package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * The device these tests are running on must have its screen
 * unlocked and lit up in order for these tests to pass.
 *
 *  Login logic tests.
 *  Tests LoginManager.java
 */
public class LoginTest extends ActivityInstrumentationTestCase2<MainActivity> {
    LoginManager userManager;
    MainActivity testActivity;

    public LoginTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        testActivity = getActivity();
        userManager = new LoginManager(testActivity);
        userManager.setTestFlag();

        try {
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        } catch(ParseException e) {
            Log.i("Login Test", "User not found.");
        }
    }

    @Override
    public void tearDown() throws ParseException {
        //Delete the user from the server.
        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Test to show the user can log into a given room with the correct details
     * @throws Exception
     */
    public void testLogin() throws Exception {
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseUser.logOut();
        userManager.login("Tester", "Banana");
        Thread.sleep(1000);

        assertNotNull(ParseUser.getCurrentUser());
    }

    /**
     * Testing the sign up functionality
     * @throws Exception
     */
    public void testSignUp() throws Exception {
        //Testing if user gets signed up and then logged in.

        // Needs be run on the UI thread or else it throws an exception
        testActivity.runOnUiThread(new Runnable() {
            public void run() {
                userManager.signup("Tester", "Banana");
            }
        });
        Thread.sleep(1000);
        assertNotNull(ParseUser.getCurrentUser());
    }

    public void testLogOut() throws ParseException {
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
