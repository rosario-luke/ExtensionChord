package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Jakub on 2/27/2015.
 */
public class ViewRoomTest extends ActivityInstrumentationTestCase2<MainActivity> {
    RoomManager manager;

    public ViewRoomTest(){
        super(MainActivity.class);
    }

    List<ParseRoom> results;
    ParseGeoPoint testPoint;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        manager = new RoomManager(getActivity());

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

        testPoint = new ParseGeoPoint(0, 90);
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

        if (results != null)
            for(ParseRoom room : results) {
                room.delete();
            }
    }

    public void testGetRooms() throws Exception {
        manager.createRoom("Test Room", "",testPoint);

        results = manager.getNearbyRooms(0.5, testPoint);

        assertTrue(results.size() > 0);
    }
}
