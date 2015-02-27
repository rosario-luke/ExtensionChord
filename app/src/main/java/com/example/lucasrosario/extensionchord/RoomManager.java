package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 2/21/15.
 */
public class RoomManager {
    private Context context;
    private List<ParseRoom> rooms;

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
        acl.setPublicWriteAccess(true); //objects created are writable
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

    /**
     * Gets a list of nearby rooms in a specified radius (km), must pass in your current location.
     *
     * @param radius = radius in kilometers.
     * @param point = center location of the search.
     */
    public List<ParseRoom> getNearbyRooms(double radius, ParseGeoPoint point) {
        rooms = new ArrayList<ParseRoom>();
        ParseQuery<ParseRoom> roomQuery = ParseRoom.getQuery();

        roomQuery.whereWithinKilometers("location", point, radius);
        try {
            rooms = roomQuery.find();
        }
        catch (ParseException e)
        {
            Toast.makeText(context, "No Rooms Found", Toast.LENGTH_LONG);
        }
        //roomQuery.orderByDescending("createdAt");
        //roomQuery.setLimit(10);
        // Kick off the query in the background
        /*roomQuery.findInBackground(new FindCallback<ParseRoom>() {
            @Override
            public void done(List<ParseRoom> objects, ParseException e) {
                if(objects != null) {
                    for(ParseRoom room : objects)
                    {
                        Log.d("Testing stuff", "Name: " + room.getRoomName());
                        rooms.add(room);
                    }
                }
                Log.d("Testing stuff", "Objects: " + objects.size());
                if (e != null) {
                    if (Application.APPDEBUG) {
                        Log.d(Application.APPTAG, "An error occurred while querying for rooms.", e);
                    }
                    return;
                }
            }
        });*/
        Log.d("Testing stuff", "Room Size: " + rooms.size());

        return rooms;
    }

}
