package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.parse_objects.ParseMusicQueue;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 2/21/15.
 * Class used to manage ParseRoom objects such as creating, deleting, and editing Rooms
 */
public class RoomManager {
    private Context context;

    public RoomManager(Context c){
        context = c;
    }

    /**
     * Creates a new room and saves it to Parse
     * @param roomName  name of the room to add the user to
     * @param roomPassword password for room
     * @param geoPoint location of room
     */
    public void createRoom(String roomName, String roomPassword, ParseGeoPoint geoPoint){
        // 1
        ParseRoom room = new ParseRoom();
        room.setLocation(geoPoint);
        room.setCreator(ParseUser.getCurrentUser());
        room.setRoomName(roomName);
        room.setPassword(roomPassword);
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

    /**
     * Gets a ParseRoom object by roomname
     * @param roomName name of room to fetch
     * @return ParseRoom object with the roomname specified
     */
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

    /**
     * Removes a user from a room
     * @param username user to remove from the user's current room
     */
    public static void removeUserFromRoom(String username) {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("username", username);
        List<RoomUser> users;

        try {
            RoomUser.deleteAll(query.find());

        } catch (ParseException e) {
            Log.d("RoomManager", "Failed to remove " + username + " from the room");
        }
    }

    /**
     * Deletes room with specified roomname
     * @param roomName name of room to delete
     */
    public static void deleteRoom(String roomName) {
        ParseRoom room = getParseRoom(roomName);

        try {
            room.deleteMusicQueue();
            room.delete();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("RoomManager", "Failed to delete room: " + roomName);
        }

        deleteUsersFrom(roomName);
    }

    /**
     * Deletes all users from a specified room
     * @param roomName name of room to clear all users from
     */
    private static void deleteUsersFrom(String roomName) {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("currentRoom", roomName);

        try {
            RoomUser.deleteAll(query.find());
        } catch (ParseException e) {
            Log.d("RoomManager", "Failed to delete RoomUsers from room: " + roomName);
        }
    }

    /**
     * Adds a track to the music queue of specified room
     * @param track Track to add
     * @param roomName Name of room to add track to
     */
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

    /**
     * Checks if a track has a quorum of votes necessary to delete it
     * @param track track to check
     * @param roomName name of room that track resides in
     * @return whether the track was deleted
     */
    public static boolean checkTrack(ParseTrack track, String roomName)
    {
        ParseRoom currRoom = getParseRoom(roomName);
        ParseMusicQueue queue = currRoom.getParseMusicQueue();
        if(queue.checkTrackDownvotes(track, currRoom.getRoomUsersSize())) {
            deleteTrack(track, roomName, true);
            return true;
        }
        return false;
    }

    /**
     * Deletes a track from a specified room
     * @param toDelete track to delete
     * @param roomName name of room to delete track from
     * @param testFlag testflag for overriding admin privileges
     * @return if the track was deleted
     */
    public static boolean deleteTrack(ParseTrack toDelete, String roomName, boolean testFlag){
        ParseRoom currRoom;
        ParseMusicQueue currQueue;
        RoomUser roomUser;
        ParseQuery<RoomUser> ruQuery = RoomUser.getQuery();
        ruQuery.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
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
