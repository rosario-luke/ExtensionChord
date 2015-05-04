package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;

import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * The device these tests are running on must have its screen
 * unlocked and lit up in order for these tests to pass.
 *
 * Created by Evan on 18/03/2015.
 */
public class SeeUsersTest extends ActivityInstrumentationTestCase2<JoinRoomActivity> {

    public SeeUsersTest() {
        super(JoinRoomActivity.class);
    }

    JoinRoomActivity joinRoomActivity;

    List<RoomUser> users;
    ParseRoom room;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        joinRoomActivity = getActivity();

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

    @Override
    public void tearDown() throws Exception {
        ParseQuery<RoomUser> testQuery = RoomUser.getQuery();

        testQuery.whereEqualTo("username", "SUTester1");
        for (RoomUser user : testQuery.find())
            user.delete();

        testQuery.whereEqualTo("username", "SUTester2");
        for (RoomUser user : testQuery.find())
            user.delete();
    }

    /**
     * Test to show that the correct number of users are in the room when checking its occupants
     * @throws ParseException
     */
    public void testSeeUsers() throws ParseException {
        room = new ParseRoom();
        room.setLocation(new ParseGeoPoint(0.0, 0.0));
        ParseUser test = new ParseUser();
        test.setUsername("Tester");
        test.setPassword("123");
        room.setCreator(test);
        room.setRoomName("SUTestRoom");
        users = room.getRoomUsers();
        assertEquals(2, users.size());
    }

    /**
     * Test to show that creating admins functions correctly
     * @throws ParseException
     */
    public void testAdmin() throws ParseException {
        RoomUser testAdmin = new RoomUser();
        // checks user is not already admin
        assertFalse(testAdmin.isAdmin());
        // makes user an admin
        testAdmin.setAdmin(true);
        // checks user is now admin
        assertTrue(testAdmin.isAdmin());
    }


}
