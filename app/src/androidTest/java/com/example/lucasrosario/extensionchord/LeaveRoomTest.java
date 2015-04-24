package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;

import com.example.lucasrosario.extensionchord.activities.JoinRoomActivity;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Evan on 18/03/2015.
 */
public class LeaveRoomTest extends ActivityInstrumentationTestCase2<JoinRoomActivity> {

    public LeaveRoomTest() {
        super(JoinRoomActivity.class);
    }

    JoinRoomActivity joinRoomActivity;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        joinRoomActivity = getActivity();

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
