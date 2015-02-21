package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.widget.Toast;

/**
 * Created by lucas on 2/21/15.
 */
public class RoomManager {

    private Context context;

    public RoomManager(Context c){
        context = c;
    }

    public void createRoom(String roomName, ParseGeoPoint geoPoint){
        // 1
        ParseRoom room = new ParseRoom();
        room.setLocation(geoPoint);
        room.setRoomName(roomName);
        room.setCreator(ParseUser.getCurrentUser());

        // 2
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        room.setACL(acl);

        // 3
        room.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e  == null) {
                    Toast.makeText(context, "Created Room Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
