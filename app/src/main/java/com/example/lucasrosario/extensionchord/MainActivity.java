package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;

/**
 * First activity that comes up when the app launches. Gives a place to sign in that leads to the
 * Join Room Activity, and a button that leads to the signup activity.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(this, JoinRoomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        } else {
            final LoginManager loginManager = new LoginManager(this);
            final Button loginButton = (Button) findViewById(R.id.login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String loginUsername = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
                    String loginPassword = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
                    loginManager.login(loginUsername, loginPassword);
                }
            });

            final Button signUpButton = (Button) findViewById(R.id.signUp);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(MainActivity.this, SignUpActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }
            });
        }
    }
}
