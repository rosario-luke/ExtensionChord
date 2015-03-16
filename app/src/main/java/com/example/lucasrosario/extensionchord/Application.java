package com.example.lucasrosario.extensionchord;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Handles initialization logic for the application.
 */
public class Application extends android.app.Application{
    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag
    public static final String APPTAG = "ExtensionChord";

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
        Parse.initialize(this, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");
    }
}
