package com.example.lucasrosario.extensionchord;

import com.parse.Parse;

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
        Parse.initialize(this, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");
    }
}