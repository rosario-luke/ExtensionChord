package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.utility.SoundCloudSearch;
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
 * Created by lucas on 4/8/15.
 */
public class SearchTest extends ActivityInstrumentationTestCase2<RoomActivity> implements OnSearchTaskCompleted {

    public SearchTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;
    boolean searchCompleted = false;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);

        try {
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        } catch(ParseException e) {
            Log.i("Login Test", "User not found.");
        }

        System.out.println("Got past initialize");
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        roomManager.createRoom("TestRoom", "", new ParseGeoPoint(0.0, 0.0));
        Thread.sleep(1000);
        searchCompleted = false;
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

    /**
     * Testing ability to search Soundcloud for a given phrase
     * @throws Exception
     */
    public void testSearchFunctionBasic() throws Exception{



        // Defining/performing the search to be carried out
        new SoundCloudSearch(this).execute("kayne");
        Thread.sleep(1500);
        // Checks that the search completed successfully
        assertTrue(searchCompleted);


    }

    /**
     * Testing the ability to search Soundcloud for phrases containing spaces
     * @throws Exception
     */
    public void testSearchFunctionWithSpaces() throws Exception{


        // Defining/performing search
        new SoundCloudSearch(this).execute("kayne west");
        Thread.sleep(1500);
        // Checks that the search completed successfully
        assertTrue(searchCompleted);


    }

    /**
     * Testing what happens if a search is performed without a search phrase
     * @throws Exception
     */
    public void testSearchFunctionBlank() throws Exception{


        // Searches with blank search
        new SoundCloudSearch(this).execute("");
        Thread.sleep(1500);
        // Checks that the search completed successfully
        assertTrue(searchCompleted);


    }

    public void onTaskCompleted(Object c){
        if(c != null){
            searchCompleted = true;
        }
    }

}
