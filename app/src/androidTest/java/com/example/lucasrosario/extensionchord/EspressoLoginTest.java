package com.example.lucasrosario.extensionchord;
import android.support.test.InstrumentationRegistry;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.activities.MainActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Brett on 4/8/2015.
 */
public class EspressoLoginTest extends ActivityInstrumentationTestCase2<MainActivity> {
    MainActivity mActivity;

    public EspressoLoginTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

        try {
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        } catch(ParseException e) {
            Log.i("Login Test", "User not found.");
        }

        ParseUser testUser = new ParseUser();
        testUser.setUsername("Tester");
        testUser.setPassword("Banana");
        testUser.signUp();
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

    public void testLoginValidUser() throws Exception {
        onView(withId(R.id.loginUsername))
                .perform(typeText("Tester"), closeSoftKeyboard());

        onView(withId(R.id.loginPassword))
                .perform(typeText("Banana"), closeSoftKeyboard());

        onView(withId(R.id.login)).perform(click());

        // Wait for the activity to load
        Thread.sleep(500);

        // JoinRoomActivity should now be loaded so we check that something
        // from is displayed on the screen (Reload button in this case)
        onView(withId(R.id.createRoomReload))
                .check(matches(isDisplayed()));
    }


}
