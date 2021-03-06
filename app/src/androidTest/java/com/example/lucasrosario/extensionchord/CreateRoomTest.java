package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * The device these tests are running on must have its screen
 * unlocked and lit up in order for these tests to pass.
 *
 * Created by lucas on 2/21/15.
 */
public class CreateRoomTest extends ActivityInstrumentationTestCase2<JoinRoomActivity> {

    public CreateRoomTest() {
        super(JoinRoomActivity.class);
    }

    RoomManager roomManager;
    JoinRoomActivity joinRoomActivity;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        joinRoomActivity = getActivity();
        roomManager = new RoomManager(joinRoomActivity);

        try {
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        } catch(ParseException e) {
            Log.i("Login Test", "User not found.");
        }

        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();
    }

    @Override
    public void tearDown() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs = null;
        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            Log.d("Clean up", "Found "+objs.size()+" objects to delete");
            ParseObject.deleteAll(objs);
        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }

        //Delete the user from the server.
        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void testCreateRoom() {
        ParseGeoPoint geoPoint = new ParseGeoPoint(40.126126, -88.225247);
        roomManager.createRoom("TestRoom","",geoPoint);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs = null;
        try {
            objs = query.find();
        } catch(ParseException e) {
            fail(e.getMessage());
        }
        // checks the room has been created successfully
        assertNotNull(objs);
        assertEquals(1, objs.size());
    }


}
