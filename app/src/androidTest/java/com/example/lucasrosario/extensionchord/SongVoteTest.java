package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.example.lucasrosario.extensionchord.parse_objects.ParseMusicQueue;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.utility.SoundCloudSearch;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brett on 3/3/2015.
 */
public class SongVoteTest extends ActivityInstrumentationTestCase2<RoomActivity> implements OnSearchTaskCompleted{

    public SongVoteTest() {
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
        RoomManager.addUserToRoom("TestRoom");
        searchCompleted = false;
        new SoundCloudSearch(this).execute("kayne");
        Thread.sleep(1500);
        for(int i = 0; i<5; i++){
            LocalTrack t = newTracks.get(i);
            roomManager.addTrack(t, "[Tester] TestRoom");
        }
    }

    @Override
    public void tearDown() throws ParseException {
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

    public void testNoSkipSong() throws Exception {
        ParseRoom room = roomManager.getParseRoom("[Tester] TestRoom");
        ParseMusicQueue queue = room.getParseMusicQueue();
        List<ParseTrack> trackList = queue.getTrackList();
        trackList.get(0).addDownvoteUser("User1");
        boolean skip = queue.checkTrackDownvotes(trackList.get(0), 10);
        assertEquals(false, skip);
    }

    public void testEdgeOneSkipSong() throws Exception {
        ParseRoom room = roomManager.getParseRoom("[Tester] TestRoom");
        ParseMusicQueue queue = room.getParseMusicQueue();
        List<ParseTrack> trackList = queue.getTrackList();
        trackList.get(0).addDownvoteUser("User1");
        boolean skip = queue.checkTrackDownvotes(trackList.get(0), 1);
        assertEquals(true, skip);
    }


    public void testSkipSong() throws Exception {
        ParseRoom room = roomManager.getParseRoom("[Tester] TestRoom");
        ParseMusicQueue queue = room.getParseMusicQueue();
        List<ParseTrack> trackList = queue.getTrackList();
        trackList.get(0).addDownvoteUser("User1");
        trackList.get(0).addDownvoteUser("User2");
        trackList.get(0).addDownvoteUser("User3");
        trackList.get(0).addDownvoteUser("User4");
        trackList.get(0).addDownvoteUser("User5");
        trackList.get(0).addDownvoteUser("User6");
        trackList.get(0).addDownvoteUser("User7");
        boolean skip = queue.checkTrackDownvotes(trackList.get(0), 10);
        assertEquals(true, skip);
    }

    public void testEdgeSkipSong() throws Exception {
        ParseRoom room = roomManager.getParseRoom("[Tester] TestRoom");
        ParseMusicQueue queue = room.getParseMusicQueue();
        List<ParseTrack> trackList = queue.getTrackList();
        trackList.get(0).addDownvoteUser("User1");
        trackList.get(0).addDownvoteUser("User2");
        trackList.get(0).addDownvoteUser("User3");
        trackList.get(0).addDownvoteUser("User4");
        trackList.get(0).addDownvoteUser("User5");
        trackList.get(0).addDownvoteUser("User6");
        boolean skip = queue.checkTrackDownvotes(trackList.get(0), 10);
        assertEquals(true, skip);
    }


    public void onTaskCompleted(Object obj){
        ArrayList<LocalTrack> tList = (ArrayList<LocalTrack>) obj;
        if(tList != null){
            newTracks = tList;
            searchCompleted = true;
        }
    }
}
