package com.example.lucasrosario.extensionchord;


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
 */
@ParseClassName("ParseRoom")
public class ParseRoom extends ParseObject {

    public String getRoomName() {
        return getString("roomName");
    }

    public void setRoomName(String value) {
        put("roomName", "[" + getCreator().getUsername() + "] " + value);
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }

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

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint value) {
        put("location", value);
    }

    public static ParseQuery<ParseRoom> getQuery() {
        return ParseQuery.getQuery(ParseRoom.class);
    }

    public ParseMusicQueue getParseMusicQueue() { return (ParseMusicQueue)getParseObject("musicQueue");}

    public void setParseMusicQueue() { put("musicQueue", new ParseMusicQueue());}

    public void deleteMusicQueue() throws Exception{
        ParseMusicQueue queue = getParseMusicQueue();
        List<ParseTrack> tracks = queue.getTrackList();
        for(ParseTrack t : tracks){
            t.delete();
        }
        queue.delete();
    }
}
