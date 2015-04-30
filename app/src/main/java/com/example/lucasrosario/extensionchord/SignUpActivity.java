package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class SignUpActivity extends Activity {
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
}
