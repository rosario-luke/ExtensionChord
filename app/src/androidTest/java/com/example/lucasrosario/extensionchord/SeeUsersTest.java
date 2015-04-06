package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Evan on 18/03/2015.
 */
public class SeeUsersTest extends ActivityUnitTestCase<JoinRoomActivity> {

    public SeeUsersTest() {
        super(JoinRoomActivity.class);
    }

    RoomManager roomManager;
    JoinRoomActivity joinRoomActivity;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), JoinRoomActivity.class);
        startActivity(testIntent, null, null);

        joinRoomActivity = getActivity();

        Parse.initialize(joinRoomActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

        RoomUser currUser = new RoomUser();
        currUser.setUsername("SUTester1");

        RoomUser otherUser = new RoomUser();
        otherUser.setUsername("SUTester2");


        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        currUser.setCurrentRoom("[Tester] SUTestRoom");
        otherUser.setCurrentRoom("[Tester] SUTestRoom");

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        currUser.setACL(acl);
        otherUser.setACL(acl);


        currUser.save();
        otherUser.save();

    }

    public void testSeeUsers() throws ParseException {

        ParseRoom room = new ParseRoom();
        room.setLocation(new ParseGeoPoint(0.0, 0.0));
        ParseUser test = new ParseUser();
        test.setUsername("Tester");
        test.setPassword("123");
        room.setCreator(test);
        room.setRoomName("SUTestRoom");
        List<RoomUser> users = room.getRoomUsers();
        assertEquals(2, users.size());

        for(RoomUser user : users)
        {
            user.delete();
        }
    }

    public void testAdmin() throws ParseException {
        RoomUser testAdmin = new RoomUser();
        assertTrue(testAdmin.isAdmin() == false);
        testAdmin.setAdmin(true);
        assertTrue(testAdmin.isAdmin() == true);
    }


}
