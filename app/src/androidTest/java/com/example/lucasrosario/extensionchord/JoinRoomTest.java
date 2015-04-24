package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.activities.RoomActivity;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Brett on 3/3/2015.
 */
public class JoinRoomTest extends ActivityInstrumentationTestCase2<RoomActivity> {
    public JoinRoomTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);

        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        roomManager.createRoom("TestRoom", "",new ParseGeoPoint(0.0, 0.0));
        Thread.sleep(1000);
    }

    @Override
    public void tearDown() throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            Log.d("Clean up", "Found " + objs.size() + " objects to delete");
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

    public void testJoinRoom() throws Exception {
        RoomUser user = new RoomUser();

        ParseQuery<ParseRoom> query = ParseRoom.getQuery();
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        ParseRoom testRoom = new ParseRoom();

        try {
            testRoom = query.getFirst();
        } catch (ParseException e) {
            fail();
        }

        user.joinRoom(testRoom.getRoomName());

        List<RoomUser> roomUsers = testRoom.getRoomUsers();

        assertEquals("Tester", roomUsers.get(0).getUsername());
    }
}
