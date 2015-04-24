package com.example.lucasrosario.extensionchord.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.LoginManager;
import com.example.lucasrosario.extensionchord.R;


public class SignUpActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final LoginManager loginManager = new LoginManager(this);
        final Button signUpButton = (Button) findViewById(R.id.signUpCreate);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loginPassword = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
                String loginPasswordCheck = ((EditText) findViewById(R.id.loginPasswordCheck)).getText().toString();

                if(loginPassword.equals(loginPasswordCheck)) {
                    String loginUsername = ((EditText) findViewById(R.id.loginUsername)).getText().toString();

                    loginManager.signup(loginUsername, loginPassword);
                } else {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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