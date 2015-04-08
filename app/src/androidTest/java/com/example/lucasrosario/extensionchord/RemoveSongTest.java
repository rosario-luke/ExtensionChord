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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 4/8/15.
 */
public class RemoveSongTest extends ActivityUnitTestCase<RoomActivity> implements OnSearchTaskCompleted {

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

    public void testDeleteSongBasic() throws Exception{

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            ParseRoom c = (ParseRoom)objs.get(0);
            c.fetchIfNeeded();
            ParseMusicQueue mq = c.getParseMusicQueue().fetchIfNeeded();
            List<ParseTrack> cList = mq.getTrackList();
            for(ParseTrack t: cList){
                Log.d("Track", t.getTrackName());
            }
            ParseTrack first = cList.get(0);
            RoomManager.deleteTrack(first, "[Tester] TestRoom", true);
            mq = c.getParseMusicQueue().fetch();
            cList = mq.getTrackList();
            for(ParseTrack t: cList){
                Log.d("Track", t.getTrackName());
            }
            assertTrue(!mq.getTrackList().contains(first));
            //ParseObject.deleteAll(objs);

        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }



    }





    public void testDeleteEmptySong() throws Exception{

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            ParseRoom c = (ParseRoom)objs.get(0);
            c.fetchIfNeeded();
            ParseMusicQueue mq = c.getParseMusicQueue().fetchIfNeeded();
            List<ParseTrack> cList = mq.getTrackList();

            ParseTrack empty = new ParseTrack();

            RoomManager.deleteTrack(empty, "[Tester] TestRoom", true);

            mq = c.getParseMusicQueue().fetch();
            List<ParseTrack> nList = mq.getTrackList();
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }


        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }


    }

    public void testDeleteNullSong() throws Exception{

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            ParseRoom c = (ParseRoom)objs.get(0);
            c.fetchIfNeeded();
            ParseMusicQueue mq = c.getParseMusicQueue().fetchIfNeeded();
            List<ParseTrack> cList = mq.getTrackList();



            RoomManager.deleteTrack(null, "[Tester] TestRoom", true);

            mq = c.getParseMusicQueue().fetch();
            List<ParseTrack> nList = mq.getTrackList();
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }


        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }


    }

    public void testDeleteSongNonAdmin() throws Exception{

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "[Tester] TestRoom");
        List<ParseObject> objs;

        Log.d("Clean up", "Going to Clean Up Objects");
        try {
            objs = query.find();
            ParseRoom c = (ParseRoom)objs.get(0);
            c.fetchIfNeeded();
            ParseMusicQueue mq = c.getParseMusicQueue().fetchIfNeeded();
            List<ParseTrack> cList = mq.getTrackList();
            for(ParseTrack t: cList){
                Log.d("Track", t.getTrackName());
            }
            ParseTrack first = cList.get(0);
            RoomManager.deleteTrack(first, "[Tester] TestRoom", false);
            mq = c.getParseMusicQueue().fetch();
            List<ParseTrack> nList = mq.getTrackList();
            for(ParseTrack t: cList){
                assertTrue(nList.contains(t));
            }
            //ParseObject.deleteAll(objs);

        } catch(ParseException e) {
            Log.d("Parse Exception", e.getMessage());
        }


    }



    public void onTaskCompleted(Object obj){
        ArrayList<LocalTrack> tList = (ArrayList<LocalTrack>) obj;
        if(tList != null){
            newTracks = tList;
            searchCompleted = true;
        }
    }

}

