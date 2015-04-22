package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.utility.SoundCloudSearch;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 4/8/15.
 */
public class RemoveSongTest extends ActivityInstrumentationTestCase2<RoomActivity> implements OnSearchTaskCompleted {

    public RemoveSongTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;
    boolean searchCompleted = false;
    ArrayList<LocalTrack> newTracks;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);

        System.out.println("Got past initialize");
        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        roomManager.createRoom("TestRoom","", new ParseGeoPoint(0.0, 0.0));
        Thread.sleep(1000);
        RoomManager.addUserToRoom("TestRoom");
        searchCompleted = false;
        new SoundCloudSearch(this).execute("kayne");
        Thread.sleep(1500);
        for(int i = 0; i<5; i++){
            LocalTrack t = newTracks.get(i);
            roomManager.addTrack(t, "[Tester] TestRoom");
        }
        //roomManager.addUserToRoom("[Tester] TestRoom");


    }

    public void tearDown() throws Exception {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        try {
            objs = query.find();
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

    public void testDeleteSongBasic() throws Exception{


        try {

            List<ParseTrack> cList = fetchRoomTracks("[Tester] TestRoom");
            ParseTrack first = cList.get(0);
            cList = deleteAndGetTracks(first, "[Tester] TestRoom", true);
            assertTrue(!cList.contains(first));


        } catch(ParseException e) {
            fail("Could not connect to parse: " + e.getMessage());
        }



    }





    public void testDeleteEmptySong() throws Exception{

        try {

            List<ParseTrack> cList = fetchRoomTracks("[Tester] TestRoom");
            ParseTrack empty = new ParseTrack();
            List<ParseTrack> nList = deleteAndGetTracks(empty, "[Tester] TestRoom", true);
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }


        } catch(ParseException e) {
            fail("Could not connect to parse: " + e.getMessage());

        }


    }

    public void testDeleteNullSong() throws Exception{

        try {

            List<ParseTrack> cList = fetchRoomTracks("[Tester] TestRoom");
            List<ParseTrack> nList = deleteAndGetTracks(null, "[Tester] TestRoom", true);
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }


        } catch(ParseException e) {
            fail("Could not connect to parse: " + e.getMessage());

        }


    }

    public void testDeleteSongNonAdmin() throws Exception{

        try {

            List<ParseTrack> cList = fetchRoomTracks("[Tester] TestRoom");
            List<ParseTrack> nList = deleteAndGetTracks(cList.get(0), "[Tester] TestRoom", false);
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }


        } catch(ParseException e) {
            fail("Could not connect to parse: " + e.getMessage());
        }


    }

    public List<ParseTrack> fetchRoomTracks(String roomName) throws Exception{
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", roomName);
        List<ParseObject> objs;

        objs = query.find();
        ParseRoom c = (ParseRoom)objs.get(0).fetchIfNeeded();

        ParseMusicQueue mq = c.getParseMusicQueue().fetchIfNeeded();
        return mq.getTrackList();


    }

    public List<ParseTrack> deleteAndGetTracks(ParseTrack toDelete, String roomName, boolean isAdmin) throws Exception{
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", roomName);
        List<ParseObject> objs;
        ParseMusicQueue mq;

        objs = query.find();
        ParseRoom c = (ParseRoom)objs.get(0).fetchIfNeeded();


        RoomManager.deleteTrack(toDelete, "[Tester] TestRoom", isAdmin);
        mq = c.getParseMusicQueue().fetch();
        return  mq.getTrackList();
    }



    public void onTaskCompleted(Object obj){
        ArrayList<LocalTrack> tList = (ArrayList<LocalTrack>) obj;
        if(tList != null){
            newTracks = tList;
            searchCompleted = true;
        }
    }

}

