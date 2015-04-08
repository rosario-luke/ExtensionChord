package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.view.View;

import com.example.lucasrosario.extensionchord.OnSearchTaskCompleted;
import com.example.lucasrosario.extensionchord.RoomActivity;
import com.example.lucasrosario.extensionchord.RoomManager;
import com.example.lucasrosario.extensionchord.SoundCloudSearch;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by lucas on 4/8/15.
 */
public class SearchTest extends ActivityUnitTestCase<RoomActivity> implements OnSearchTaskCompleted {

    public SearchTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;
    boolean searchCompleted = false;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), RoomActivity.class);
        startActivity(testIntent, null, null);

        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);

        Parse.initialize(roomActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

        System.out.println("Got past initialize");
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        roomManager.createRoom("TestRoom", new ParseGeoPoint(0.0, 0.0));
        Thread.sleep(1000);

        //roomManager.addUserToRoom("[Tester] TestRoom");
        Thread.sleep(1000);

    }

    public void tearDown() throws Exception {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            Log.d("Clean up", "Found " + objs.size() + " objects to delete");
            roomManager.deleteRoom("[Tester] TestRoom");
            //ParseObject.deleteAll(objs);

        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }

        if (ParseUser.getCurrentUser() != null) {
            try {
                ParseUser.getCurrentUser().delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void testSearchFunction() throws Exception{

        /*roomActivity.setTestFlag(true);

        View emptyView = new View(roomActivity);
        roomActivity.onSearchBtnClick(emptyView);

        // Wait for SearchFragment to populate
        Thread.sleep(1000);*/

        new SoundCloudSearch(this).execute("kayne");
        Thread.sleep(1500);
        assertTrue(searchCompleted);

    }

    public void onTaskCompleted(Object c){
        if(c != null){
            searchCompleted = true;
        }
    }

}
