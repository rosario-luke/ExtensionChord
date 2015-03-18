package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Evan on 18/03/2015.
 */
public class SeeUsersTest extends ActivityUnitTestCase<RoomActivity> {

    public SeeUsersTest() {
        super(RoomActivity.class);
    }

    RoomManager roomManager;
    RoomActivity joinRoomActivity;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), JoinRoomActivity.class);
        startActivity(testIntent, null, null);

        joinRoomActivity = getActivity();
        roomManager = new RoomManager(joinRoomActivity);

        Parse.initialize(joinRoomActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

        RoomUser currUser = new RoomUser();
        currUser.setUsername("SUTester1");

        RoomUser otherUser = new RoomUser();
        otherUser.setUsername("SUTester2");

        ParseGeoPoint geoPoint = new ParseGeoPoint(0.0, 0.0);
        roomManager.createRoom("SUTestRoom",geoPoint);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        currUser.setCurrentRoom("SUTestRoom");
        otherUser.setCurrentRoom("SUTestRoom");

    }

    @Override
    public void tearDown() throws ParseException {
        ParseQuery<ParseRoom> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] SUTestRoom");
        List<ParseRoom> objs = null;
        List<RoomUser> users = null;
        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            Log.d("Clean up", "Found room to delete");
            users = objs.get(0).getRoomUsers();
            users.get(0).delete();
            users.get(1).delete();
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

    public void testSeeUsers() {

        ParseQuery<ParseRoom> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] SUTestRoom");
        List<ParseRoom> objs = null;
        try {
             objs = query.find();
        } catch(ParseException e) {
            fail(e.getMessage());
        }
        assertNotNull(objs);
        assertEquals(2, objs.get(0).getRoomUsers().size());


    }


}
