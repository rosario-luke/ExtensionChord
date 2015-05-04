package com.example.lucasrosario.extensionchord;

import android.test.ActivityInstrumentationTestCase2;

import com.example.lucasrosario.extensionchord.parse_objects.ParseMusicQueue;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * The device these tests are running on must have its screen
 * unlocked and lit up in order for these tests to pass.
 *
 * Created by Jakub on 4/9/2015.
 */
public class PlayPauseTest extends ActivityInstrumentationTestCase2<RoomActivity> {

    public PlayPauseTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;
    ParseRoom testRoom;

    @Override
    public void setUp() throws Exception{
        super.setUp();
        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);
        try{
            ParseUser.logIn("Tester", "Banana");
            ParseUser.getCurrentUser().delete();
        }catch(Exception e){
            e.printStackTrace();
        }

        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseGeoPoint geoPoint = new ParseGeoPoint(40.126126, -88.225247);
        roomManager.createRoom("TestRoom","",geoPoint);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        testRoom = RoomManager.getParseRoom("[Tester] TestRoom");
        RoomManager.addUserToRoom("[Tester] TestRoom");
        getActivity().setRoomName("[Tester] TestRoom");
//        testRoom.setParseMusicQueue();
  //      testRoom.save();

        // Lets add 2 songs to the queue.
        LocalTrack sputnikBeep = new LocalTrack(null, "Sputnik: Beep", "NASA", "null", 172374813);
        LocalTrack finalCountdown = new LocalTrack(null, "The final Countdown", "AT1996", "null", 29098443);
        RoomManager.addTrack(sputnikBeep,"[Tester] TestRoom");
        RoomManager.addTrack(finalCountdown,"[Tester] TestRoom");

    }

    @Override
    public void tearDown() throws Exception{
        super.tearDown();
        RoomManager.deleteRoom("[Tester] TestRoom");
        ParseUser.getCurrentUser().delete();
    }

    /**
     * Testing the ability to play music in the room
     */
    public void testPlayMusic(){
        ParseTrack currSong = testRoom.getParseMusicQueue().getTrackList().get(0);
        String currURL = "http://api.soundcloud.com/tracks/" + currSong.getTrackID() + "/stream?client_id=3fe96f34e369ae1ef5cf7e8fcc6c8eec";
        roomActivity.setCurrentMediaPlayerURL(currURL, false);

        // Checking that the music is not already playing
        assertFalse(roomActivity.getMediaPlayer().isPlaying());
        // Starting the music
        roomActivity.startMediaPlayer();
        // Checking the music is playing
        assertTrue(roomActivity.getMediaPlayer().isPlaying());
        roomActivity.getMediaPlayer().stop();
    }

    /**
     * Testing the ability to pause the music in the room
     */
    public void testPauseMusic(){
        ParseTrack currSong = testRoom.getParseMusicQueue().getTrackList().get(0);
        String currURL = "http://api.soundcloud.com/tracks/" + currSong.getTrackID() + "/stream?client_id=3fe96f34e369ae1ef5cf7e8fcc6c8eec";
        roomActivity.setCurrentMediaPlayerURL(currURL, false);

        assertFalse(roomActivity.getMediaPlayer().isPlaying());
        roomActivity.startMediaPlayer();
        // Checking music is playing prior to the pause attempt
        assertTrue(roomActivity.getMediaPlayer().isPlaying());
        // Pausing the music
        roomActivity.stopMediaPlayer();
        // Checking the music is paused
        assertFalse(roomActivity.getMediaPlayer().isPlaying());
    }

    /**
     * Testing the ability to pop songs from the music queue
     */
    public void testPopQueue(){
        ParseMusicQueue testQueue = testRoom.getParseMusicQueue();
        int initListLen = testQueue.getTrackList().size();

        assertTrue(!testQueue.getTrackList().isEmpty());

        testQueue.pop();
        assertTrue(initListLen == testQueue.getTrackList().size() + 1);
    }

    /**
     * Testing that the next song in the queue will be played automatically
     */
    public void testAutoPlay(){
        ParseTrack currSong = testRoom.getParseMusicQueue().getTrackList().get(0);
        String currURL = "http://api.soundcloud.com/tracks/" + currSong.getTrackID() + "/stream?client_id=3fe96f34e369ae1ef5cf7e8fcc6c8eec";
        roomActivity.setCurrentMediaPlayerURL(currURL, false);
        roomActivity.setMediaPlayerOnCompletionListener();

        assertFalse(roomActivity.getMediaPlayer().isPlaying());
        roomActivity.startMediaPlayer();
        assertTrue(roomActivity.getMediaPlayer().isPlaying());

        int initialDuration = roomActivity.getMediaPlayer().getDuration();
        int desiredEnd = initialDuration - 1000;

        roomActivity.getMediaPlayer().seekTo(desiredEnd);
        try {
            Thread.sleep(4000);
        }catch(InterruptedException e){
            fail();
        }

        assertTrue(roomActivity.getMediaPlayer().isPlaying());
        assertFalse(initialDuration == roomActivity.getMediaPlayer().getDuration());

        roomActivity.getMediaPlayer().stop();
    }
}
