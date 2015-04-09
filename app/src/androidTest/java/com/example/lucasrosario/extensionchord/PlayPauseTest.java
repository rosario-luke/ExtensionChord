package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

/**
 * Created by Jakub on 4/9/2015.
 */
public class PlayPauseTest extends ActivityUnitTestCase<RoomActivity> {

    public PlayPauseTest() {
        super(RoomActivity.class);
    }

    RoomActivity roomActivity;
    RoomManager roomManager;
    ParseRoom testRoom;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), JoinRoomActivity.class);
        startActivity(testIntent, null, null);

        roomActivity = getActivity();
        roomManager = new RoomManager(roomActivity);

        Parse.initialize(roomActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

        //Testing if the user can login given correct details.
        ParseUser currUser = new ParseUser();
        currUser.setPassword("Banana");
        currUser.setUsername("Tester");
        currUser.signUp();

        ParseGeoPoint geoPoint = new ParseGeoPoint(40.126126, -88.225247);
        roomManager.createRoom("TestRoom",geoPoint);
        testRoom = RoomManager.getParseRoom("[Tester] TestRoom");

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
            fail();
        }

        //Lets add 2 songs to the queue.
      //  LocalTrack testTrack = new LocalTrack(null, );


    }

    public void testPlayMusic(){

    }

    public void testPauseMusic(){

    }

    public void testPopQueue(){
        ParseMusicQueue testQueue = testRoom.getParseMusicQueue();
        int initListLen = testQueue.getTrackList().size();

        assertTrue(!testQueue.getTrackList().isEmpty());

        testQueue.pop();
        assertTrue(initListLen > testQueue.getTrackList().size());
    }

    public void testAutoPlay(){

    }
}
