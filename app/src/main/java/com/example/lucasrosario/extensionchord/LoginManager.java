package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.widget.Toast;
import android.content.Intent;

import com.parse.LogInCallback;
import com.parse.SignUpCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * LoginManager does all login/signup logic.
 */

public class LoginManager {
    private Context currContext;
    private boolean testFlag;

    /**
     * Constructor expects the current activity
     *
     * @param context = context the LoginManager is being used in.
     */
    public LoginManager(Context context){
        currContext = context;
        testFlag = false;
    }

    /**
     * Sets the test flag to allow for testing.
     */
    public void setTestFlag(){
        testFlag = true;
    }

    /**
     * Attempts to register a user.
     *
     * @param username = username to be signed up.
     * @param password = password associated with the user.
     */
    public void signup(String username, String password){
        if(!testFlag && !username.equalsIgnoreCase("Tester")){
            //Set user information.
            ParseUser currUser = new ParseUser();
            currUser.setUsername(username);
            currUser.setPassword(password);

            //Sign the user up.
            currUser.signUpInBackground(new SignUpCallback(){
                @Override
                public void done(ParseException e){
                    if (e != null) {
                        // Show the error message
                        Toast.makeText(currContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        // Start an intent for the dispatch activity
                        Intent intent = new Intent(currContext, JoinRoomActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        currContext.startActivity(intent);
                    }
                }
            });
        }
        else{
            Toast.makeText(currContext, "Reserved Name Entered!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to log the user into the parse cloud.
     *
     * @param username = username of user logging in.
     * @param password = password associated with the user.
     */
    public void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e){
                //Show error message
                if(e != null){
                    Toast.makeText(currContext, e.getMessage(), Toast.LENGTH_LONG).show();
                }else {
                    // Start an intent for the activity after successful login.
                    Intent intent = new Intent(currContext, JoinRoomActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    currContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * Logout the currently signed in user.
     */
    public void logout(){
        if(ParseUser.getCurrentUser() != null)
            ParseUser.logOut();
        else
            Toast.makeText(currContext, "No user currently logged in to log out.", Toast.LENGTH_LONG).show();
    }
}