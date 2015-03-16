package com.example.lucasrosario.extensionchord;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
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

    public List<RoomUser> getRoomUsers() {
        ParseQuery<RoomUser> query = RoomUser.getQuery();
        query.whereEqualTo("currentRoom", getRoomName());

        List<RoomUser> roomUsers = new ArrayList<RoomUser>();

        try {
            roomUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return roomUsers;
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
}
