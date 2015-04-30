package com.example.lucasrosario.extensionchord;

import com.example.lucasrosario.extensionchord.parse_objects.ParseMusicQueue;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Handles initialization logic for the application.
 */
public class Application extends android.app.Application{

    /**
     * onCreate function runs when the application starts.
     * This is where Parse is enabled.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(ParseRoom.class);
        ParseObject.registerSubclass(RoomUser.class);
        ParseObject.registerSubclass(ParseMusicQueue.class);
        ParseObject.registerSubclass(ParseTrack.class);

        Parse.initialize(this, Constants.PARSE_APP_ID, Constants.PARSE_CLIENT_KEY);
    }
}
