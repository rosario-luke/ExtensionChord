package com.example.lucasrosario.extensionchord;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by lucas on 2/21/15.
 */
public class CreateRoomTest extends ActivityUnitTestCase<JoinRoomActivity> {

    public CreateRoomTest(){ super(JoinRoomActivity.class); }

    RoomManager roomManager;
    JoinRoomActivity joinRoomActivity;

    @Override
    public void setUp() throws Exception{
        super.setUp();

        Intent testIntent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
        startActivity(testIntent, null, null);

        joinRoomActivity = getActivity();
        roomManager = new RoomManager(joinRoomActivity);

        Parse.initialize(joinRoomActivity, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

    }

    @Override
    public void tearDown() throws ParseException{
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "TestRoom");
        List<ParseObject> objs = null;
        try {
            objs = query.find();
            for(ParseObject p : objs){
                p.delete();
            }
        } catch(ParseException e){
            fail(e.getMessage());
        }
    }

    public void testCreateRoom(){


        roomManager.createRoom("TestRoom");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseRoom");
        query.whereEqualTo("roomName", "TestRoom");
        List<ParseObject> objs = null;
        try {
            objs = query.find();
        } catch(ParseException e){
            fail(e.getMessage());
        }
        assertNotNull(objs);
        assertEquals(objs.size(), 1);

    }


}
