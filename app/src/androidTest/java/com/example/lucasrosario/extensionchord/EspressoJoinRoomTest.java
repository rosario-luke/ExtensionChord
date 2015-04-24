package com.example.lucasrosario.extensionchord;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by lucaspritz on 4/24/15.
 */
public class EspressoJoinRoomTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mActivity;
    RoomManager roomManager;

    public EspressoJoinRoomTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        roomManager = new RoomManager(mActivity);

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

//        roomManager.createRoom("TestRoom", "",new ParseGeoPoint(0.0, 0.0));
//        roomManager.createRoom("TestRoomHasPassword", "password",new ParseGeoPoint(0.0, 0.0));

        Thread.sleep(500);
    }

    @Override
    public void tearDown() throws Exception {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

//        try {
//            objs = query.find();
//            ParseObject.deleteAll(objs);
//
//        } catch(ParseException e) {
//            Log.d("Parse Exception", e.getMessage());
//        }
//
//        query.whereEqualTo("roomName", "[Tester] TestRoomHasPassword");
//
//        try {
//            objs = query.find();
//            ParseObject.deleteAll(objs);
//
//        } catch(ParseException e) {
//            Log.d("Parse Exception", e.getMessage());
//        }

        //Delete the user from the server.
        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.tearDown();
    }

    public void testLoginNoPassword() throws InterruptedException{
        onView(withId(R.id.loginUsername))
                .perform(typeText("Tester"), closeSoftKeyboard());

        onView(withId(R.id.loginPassword))
                .perform(typeText("Banana"), closeSoftKeyboard());

        Thread.sleep(200);

        onView(withId(R.id.login)).perform(click());

        // Wait for the activity to load
        Thread.sleep(1000);

        // JoinRoomActivity should now be loaded so we check that something
        // from is displayed on the screen (Reload button in this case)
        onView(withId(R.id.createRoomReload))
                .check(matches(isDisplayed()));


        onView(withId(R.id.expandCreateRoomBtn))
                .perform(click());

        // Wait for extra buttons to show up
        Thread.sleep(1000);

        // Make a room with no password
        onView(withId(R.id.roomNameField))
                .perform(typeText("testCreateRoomNoPassword"), closeSoftKeyboard());

        onView(withId(R.id.submitCreateRoomButton))
                .perform(click());

        // Wait for new ParseRoom to be created
        Thread.sleep(500);

        onView(withId(R.id.createRoomReload))
                .perform(click());

        // Wait for RoomList to be populated
        Thread.sleep(1000);

        Espresso.onView(withText("[Tester] testCreateRoomNoPassword"))
                .perform(click());

        // Wait for RoomActivity to launch
        Thread.sleep(1000);

        // Test to see if RoomActivity has loaded
        onView(withId(R.id.container))
                .check(matches(isDisplayed()));

        onView(withId(R.id.searchField))
                .check(matches(isDisplayed()));
    }



}
