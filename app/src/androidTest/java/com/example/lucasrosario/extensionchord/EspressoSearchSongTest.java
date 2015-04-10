package com.example.lucasrosario.extensionchord;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by lucas on 4/10/15.
 */
public class EspressoSearchSongTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity mActivity;

    public EspressoSearchSongTest() { super( MainActivity.class); }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

        ParseUser.logOut();

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
    public void tearDown() throws Exception {
        //Delete the user from the server.

        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
                ParseQuery<ParseRoom> query = ParseQuery.getQuery("ParseRoom");
                query.whereEqualTo("roomName", "[Tester] testCreateRoom");
                List<ParseRoom> rList = query.find();
                ParseRoom.deleteAll(rList);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        super.tearDown();
    }

    public void testCreateRoom() throws Exception {
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

        onView(withId(R.id.roomNameField))
                .perform(typeText("testCreateRoom"), closeSoftKeyboard());

        onView(withId(R.id.submitCreateRoomButton))
                .perform(click());

        // Wait for new ParseRoom to be created
        Thread.sleep(500);

        onView(withId(R.id.createRoomReload))
                .perform(click());

        // Wait for RoomList to be populated
        Thread.sleep(1000);

        Espresso.onView(withText("[Tester] testCreateRoom"))
                .perform(click());

        // Wait for RoomActivity to launch
        Thread.sleep(1000);

        // Test to see if RoomActivity has loaded
        onView(withId(R.id.container))
                .check(matches(isDisplayed()));

        onView(withId(R.id.searchField))
                .check(matches(isDisplayed()));

        onView(withId(R.id.searchField))
                .perform(typeText("Kanye West"), closeSoftKeyboard());

        onView(withId(R.id.searchBtn))
                .perform(click());

        // Wait for Search Result To Populate

        // TODO: Implement looking for songs


    }


}
