package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.internal.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.Stage;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.android.support.test.deps.guava.base.Throwables;
import com.android.support.test.deps.guava.collect.Sets;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by lucas on 4/9/15.
 */
public class EspressoSignupTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mActivity;

    public EspressoSignupTest() { super(MainActivity.class);}

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

        // Make sure user is logged out before running the test
        ParseUser.logOut();

        try {

            ParseUser.logIn("suTest", "suTest");
            ParseUser.getCurrentUser().delete();
        } catch(ParseException e) {
            Log.i("Login Test", "User not found.");
        }


    }

    @Override
    public void tearDown() throws Exception {
        //Delete the user from the server.

        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //closeAllActivities(getInstrumentation());
        super.tearDown();
    }

    public void testCreateValidUser() throws Exception {

        onView(withId(R.id.signUp))
                .perform(click());

        // Wait for SignUpActivity to load
        Thread.sleep(500);

        onView(withId(R.id.loginUsername))
                .perform(typeText("suTest"), closeSoftKeyboard());

        onView(withId(R.id.loginPassword))
                .perform(typeText("suTest"), closeSoftKeyboard());

        onView(withId(R.id.loginPasswordCheck))
                .perform(typeText("suTest"), closeSoftKeyboard());

        Thread.sleep(200);

        onView(withId(R.id.signUpCreate))
                .perform(click());

        // Wait for the activity to load
        Thread.sleep(500);

        // JoinRoomActivity should now be loaded so we check that something
        // from is displayed on the screen (Reload button in this case)
        onView(withId(R.id.createRoomReload))
                .check(matches(isDisplayed()));
    }

    public void testCreateUserBadPassword() throws Exception {

        onView(withId(R.id.signUp))
                .perform(click());

        // Wait for SignUpActivity to load
        Thread.sleep(500);

        onView(withId(R.id.loginUsername))
                .perform(typeText("suTest"), closeSoftKeyboard());

        onView(withId(R.id.loginPassword))
                .perform(typeText("suTest"), closeSoftKeyboard());

        onView(withId(R.id.loginPasswordCheck))
                .perform(typeText("differentPassword"), closeSoftKeyboard());

        Thread.sleep(200);

        onView(withId(R.id.signUpCreate))
                .perform(click());

        // Wait for the activity to load
        Thread.sleep(500);

        // Check that we are still in SignUpActivity
        onView(withId(R.id.loginUsername))
                .check(matches(isDisplayed()));
    }

    
}
