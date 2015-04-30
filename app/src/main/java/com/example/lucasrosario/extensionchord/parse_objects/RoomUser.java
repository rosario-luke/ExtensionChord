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
    /**
     * Gets the current user's room name
     *
     * @return The room name
     */
    public String getCurrentRoom() {
        return getString("currentRoom");
    }

    /**
     * Sets the current user's room name
     *
     * @param roomName The room name
     */
    public void setCurrentRoom(String roomName) {
        put("currentRoom", roomName);
    }

    /**
     * Returns whether or not this user is an admin
     *
     * @return True if the user is an admin, otherwise false
     */
    public boolean isAdmin() { return getBoolean("isAdmin"); }

    /**
     * Sets whether or not the user is an admin
     *
     * @param isAdmin True if making them an admin, otherwise false
     */
    public void setAdmin(boolean isAdmin) {
        put("isAdmin", isAdmin);
    }

    /**
     * Gets the username
     * @return The username
     */
    public String getUsername() {
        return getString("username");
    }

    /**
     * Sets the username
     * @param username The username
     */
    public void setUsername(String username) {
        put("username", username);
    }

    /**
     * Gets a query allowing you to query RoomUsers
     *
     * @return A RoomUser query
     */
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
