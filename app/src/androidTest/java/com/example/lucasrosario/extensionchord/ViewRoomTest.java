package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityUnitTestCase;

import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Jakub on 2/27/2015.
 */
public class ViewRoomTest extends ActivityUnitTestCase<MainActivity>{
    RoomManager manager;

    public ViewRoomTest(){
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(testIntent, null, null);

        manager = new RoomManager(getActivity());

        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();
    }

    @Override
    public void tearDown() throws Exception{
        //Delete the user from the server.
        if(ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void testGetRooms() throws Exception{
        ParseGeoPoint testPoint = new ParseGeoPoint(0, 90);
        manager.createRoom("Test Room", testPoint);

        List<ParseRoom> results;

        results = manager.getNearbyRooms(0.5, testPoint);

        assertTrue(results.size() > 0);
        for(ParseRoom room : results)
        {
            room.delete();
        }
    }
}
