package com.example.lucasrosario.extensionchord.parse_objects;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lucas on 2/21/15.
 * Custom ParseObject that represents a MusicRoom
 */
@ParseClassName("ParseRoom")
public class ParseRoom extends ParseObject {

    /**
     * Returns roomname
     * @return name of the room
     */
    public String getRoomName() {
        return getString("roomName");
    }

    /**
     * Sets the roomname
     * @param value value to set roomname to
     */
    public void setRoomName(String value) {
        put("roomName", "[" + getCreator().getUsername() + "] " + value);
    }

    /**
     * Returns the ParseUser Object for the User that created the room
     * @return ParseUser
     */
    public ParseUser getCreator() {
        return getParseUser("creator");
    }

    /**
     * Sets the Creator field
     * @param creator ParseUser that created the room
     */
    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    /**
     * Removes all duplicate RoomUsers from the List and returns the result.
     *
     * @param roomUsers = List to delete duplicates from
     */
    private List<RoomUser> getListWithoutDuplicates(List<RoomUser> roomUsers) {
        HashSet<String> set = new HashSet<String>();
        List<RoomUser> actualUsers = new ArrayList<RoomUser>();

        // Construct a new list without duplicates
        for (RoomUser user: roomUsers) {
            if (!set.contains(user.getUsername())) {
                actualUsers.add(user);
                set.add(user.getUsername());
            }
        }

        return actualUsers;
    }

    /**
     * Gets a List of RoomUsers in the room.
     */
    public List<RoomUser> getRoomUsers() {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("currentRoom", getRoomName());

        List<RoomUser> roomUsers = new ArrayList<RoomUser>();

        try {
            roomUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return getListWithoutDuplicates(roomUsers);
    }

    /**
     * Gets the number of RoomUsers in the room.
     */
    public int getRoomUsersSize() {
        return getRoomUsers().size();
    }

    /**
     * Gets the location of where the room was created
     * @return
     */
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    /**
     * Sets the location of the room
     * @param value ParseGeoPoint of the location
     */
    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    /**
     * Sets the password for the room
     * @param pass Password for the room
     */
    public void setPassword(String pass){
        put("password", pass);
    }

    /**
     *
     * @return The password for the room
     */
    public String getPassword(){
        return getString("password");
    }

    /**
     * Creates a query object for ParseRooms
     * @return QueryObject
     */
    public static ParseQuery<ParseRoom> getQuery() {
        return ParseQuery.getQuery(ParseRoom.class);
    }

    /**
     * Gets the MusicQueue associated with this room
     * @return ParseMusicQueue for this room
     */
    public ParseMusicQueue getParseMusicQueue() { return (ParseMusicQueue)getParseObject("musicQueue");}

    /**
     * Sets the MusicQueue associated with this room
     */
    public void setParseMusicQueue() { put("musicQueue", new ParseMusicQueue());}

    /**
     * Deletes the music queue associated with this room
     * @throws Exception
     */
    public void deleteMusicQueue() throws Exception{
        ParseMusicQueue queue = getParseMusicQueue();
        List<ParseTrack> tracks = queue.getTrackList();
        for(ParseTrack t : tracks){
            t.delete();
        }
        queue.delete();
    }
}
