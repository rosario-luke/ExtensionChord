package com.example.lucasrosario.extensionchord;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        put("roomName", value);
    }

    public ParseUser getCreator() {
        return getParseUser("creator");
    }

    public void setCreator(ParseUser creator) {
        put("creator", creator);
    }

    public void addRoomUser(String username) {
        add("roomUsers", username);
    }

    public List<String> getRoomUsers() {
        return getList("roomUsers");
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

}
