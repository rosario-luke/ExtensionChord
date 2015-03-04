package com.example.lucasrosario.extensionchord;

/**
 * Created by Brett on 3/3/2015.
 */
public class RoomUser {

    /**
     * Allows a user to join a room.
     *
     * @param roomName = name of the room to join
     */
    public void joinRoom(String roomName) {
        RoomManager.addUserToRoom(roomName);
    }
}
