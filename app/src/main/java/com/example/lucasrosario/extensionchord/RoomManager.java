package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
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

    public RoomManager(Context c){
        context = c;
    }

    public void createRoom(String roomName, ParseGeoPoint geoPoint){
        // 1
        ParseRoom room = new ParseRoom();
        room.setLocation(geoPoint);
        room.setCreator(ParseUser.getCurrentUser());
        room.setRoomName(roomName);
        room.setParseMusicQueue();

        // 2
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true); //objects created are writable
        room.setACL(acl);

        // 3
        room.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e  == null) {
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
        List<ParseRoom> rooms = new ArrayList<ParseRoom>();
        ParseQuery<ParseRoom> roomQuery = ParseRoom.getQuery();

        roomQuery.whereWithinKilometers("location", point, radius);
        try {
            rooms = roomQuery.find();
        } catch (ParseException e) {
            Toast.makeText(context, "No Rooms Found", Toast.LENGTH_LONG).show();
        }

        Log.d("Testing stuff", "Nearby rooms size: " + rooms.size());

        return rooms;
    }

    /**
     * Adds a user to a room's user list.
     *
     * @param roomName = name of the room to add the user to
     */
    public static boolean addUserToRoom(String roomName) {
        RoomUser newUser = new RoomUser();
        newUser.setUsername(ParseUser.getCurrentUser().getUsername());
        newUser.setCurrentRoom(roomName);
        newUser.setAdmin(false);
        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        newUser.setACL(acl);

        try {
            newUser.save();
            Log.d("RoomManager", "User successfully added to room");
        } catch (ParseException e) {
            Log.d("RoomManager", "Failed to add user to room");
            return false;
        }

        return true;
    }

    public static ParseRoom getParseRoom(String roomName) {
        ParseQuery<ParseRoom> query = ParseRoom.getQuery();
        query.whereEqualTo("roomName", roomName);
        ParseRoom room;

        try {
            room = query.getFirst();
        } catch (ParseException e) {
            room = new ParseRoom();
        }

        return room;
    }

    public static void removeUserFromRoom(String username) {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("username", username);
        List<RoomUser> users;

        try {
            RoomUser.deleteAll(query.find());

            Log.d("RoomManager", "Successfully removed " + username + " from the room");
        } catch (ParseException e) {
            Log.d("RoomManager", "Failed to remove " + username + " from the room");
        }
    }

    public static void deleteRoom(String roomName) {
        ParseRoom room = getParseRoom(roomName);

        try {
            room.deleteMusicQueue();
            room.delete();
            Log.d("RoomManager", "Successfully deleted room: " + roomName);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("RoomManager", "Failed to delete room: " + roomName);
        }

        deleteUsersFrom(roomName);
    }

    private static void deleteUsersFrom(String roomName) {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("currentRoom", roomName);

        try {
            RoomUser.deleteAll(query.find());
            Log.d("RoomManager", "Deleted all RoomUsers from room: " + roomName);
        } catch (ParseException e) {
            Log.d("RoomManager", "Failed to delete RoomUsers from room: " + roomName);
        }
    }

    public static void addTrack (LocalTrack track, String roomName){
        ParseQuery<ParseRoom> query = ParseRoom.getQuery();
        query.whereEqualTo("roomName", roomName);

        ParseMusicQueue currQueue;
        ParseRoom currRoom;

        ParseTrack pTrack;
        pTrack = new ParseTrack();
        pTrack.setTrackAlbum(track.getTrackAlbum());
        pTrack.setTrackArtist(track.getTrackArtist());
        pTrack.setTrackID(track.getTrackID());
        pTrack.setTrackName(track.getTrackName());
        pTrack.setSubmitter(ParseUser.getCurrentUser().getUsername());

        ParseACL acl = new ParseACL();
        acl.setPublicReadAccess(true);
        acl.setPublicWriteAccess(true);
        pTrack.setACL(acl);

        try {
            pTrack.save();
        }catch(ParseException e){
            Log.d("RoomManager", "Failed to add parseTrack in : " + roomName);
        }

        try{
            List<ParseRoom> rList = query.find();
            if(rList.isEmpty()){
                Log.d("Adding Track", "Could not find room");
                return;
            }
            currRoom = query.find().get(0);
            currQueue = currRoom.getParseMusicQueue();
            currQueue.addTrackToQueue(pTrack);
            currQueue.saveInBackground();
        }catch (ParseException e){
            Log.d("RoomManager", e.toString());
        }
    }

    public static boolean deleteTrack(ParseTrack toDelete, String roomName, boolean testFlag){
        ParseRoom currRoom;
        ParseMusicQueue currQueue;
        RoomUser roomUser;
        ParseQuery<RoomUser> ruQuery = ParseQuery.getQuery("RoomUser");
        ruQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        Log.d("CurrentUser", ParseUser.getCurrentUser().getUsername());
        try{
            // TODO: Change this to be asynchronous
            currRoom = RoomManager.getParseRoom(roomName).fetchIfNeeded();
            currQueue = currRoom.getParseMusicQueue().fetchIfNeeded();
            List<RoomUser> ruList = ruQuery.find();
            roomUser = ruList.get(0);
        } catch(ParseException e){

            return false;
        }
        if(roomUser != null && (roomUser.isAdmin() || testFlag)) {
            Log.d("Delete Track", "Deleted Track");
            currQueue.deleteTrack(toDelete);
            try {
                currQueue.save();
            } catch(ParseException e){
                return false;
            }
            return true;
        } else {
            if(roomUser == null){
                Log.d("Delete track", "roomUser was null");
            } else {
                Log.d("Delete track", "was not an admin");
            }
        }
        return false;
    }
}
