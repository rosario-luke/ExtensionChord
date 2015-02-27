package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;
import com.parse.ParseUser;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BELOW IS HANDLED IN APPLICATION.JAVA, REMOVE IF APPLICATION IS WORKING.
        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);
        // Enables Parse with application key for ExtensionChord
        //Parse.initialize(this, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");

        // TEST CODE FOR PARSE BELOW - DELETE LATER
        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "foofoo");
        //testObject.saveInBackground();
        // DELETE ABOVE TEST CODE LATER
        if(ParseUser.getCurrentUser() != null){
            Intent intent = new Intent(this, JoinRoomActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }
        else {
            final LoginManager loginManager = new LoginManager(this);
            final Button loginButton = (Button) findViewById(R.id.login);
            loginButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    loginManager.login(((EditText) findViewById(R.id.loginUsername)).getText().toString(), ((EditText) findViewById(R.id.loginPassword)).getText().toString());
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
