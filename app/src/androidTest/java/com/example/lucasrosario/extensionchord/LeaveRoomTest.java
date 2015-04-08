package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Evan on 18/03/2015.
 */
public class LeaveRoomTest extends ActivityUnitTestCase<JoinRoomActivity> {

    public LeaveRoomTest() {
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
        currUser.setUsername("ATester1");

        RoomUser otherUser = new RoomUser();
        otherUser.setUsername("ATester2");


        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        currUser.setCurrentRoom("[Admin Tester] SUTestRoom");
        otherUser.setCurrentRoom("[Admin Tester] SUTestRoom");

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        currUser.setACL(acl);
        otherUser.setACL(acl);


        currUser.save();
        otherUser.save();

    }

    public void testLeaveRoom() throws ParseException {
        ParseRoom room = new ParseRoom();
        room.setLocation(new ParseGeoPoint(0.0, 0.0));
        ParseUser test = new ParseUser();
        test.setUsername("Admin Tester");
        test.setPassword("123");
        room.setCreator(test);
        room.setRoomName("SUTestRoom");
        List<RoomUser> preUsers = room.getRoomUsers();
        assertEquals(2, preUsers.size());
        RoomManager.removeUserFromRoom("ATester1");
        RoomManager.removeUserFromRoom("ATester2");
        List<RoomUser> users = room.getRoomUsers();
        assertEquals(0, users.size());
    }

}
