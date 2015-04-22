package com.example.lucasrosario.extensionchord.parse_objects;

import com.example.lucasrosario.extensionchord.RoomManager;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Brett on 3/3/2015.
 */
@ParseClassName("RoomUser")
public class RoomUser extends ParseObject {
    public String getCurrentRoom() {
        return getString("currentRoom");
    }

    public void setCurrentRoom(String roomName) {
        put("currentRoom", roomName);
    }

    public boolean isAdmin() { return getBoolean("isAdmin"); }

    public void setAdmin(boolean isAdmin) {
        put("isAdmin", isAdmin);
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public static ParseQuery<RoomUser> getQuery() {
        return ParseQuery.getQuery(RoomUser.class);
    }

    /**
     * Allows a user to join a room.
     *
     * @param roomName = name of the room to join
     */
    public void joinRoom(String roomName) {
        RoomManager.addUserToRoom(roomName);
    }
}
