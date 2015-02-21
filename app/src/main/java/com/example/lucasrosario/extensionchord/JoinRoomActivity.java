package com.example.lucasrosario.extensionchord;

import android.app.ActionBar;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;


import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;


public class JoinRoomActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ParseGeoPoint geoPoint;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private RoomManager roomManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        roomManager = new RoomManager(this);

        // The make room button to be clicked after you fill out a name
        Button makeRoom = (Button) findViewById(R.id.submitCreateRoomButton);
        makeRoom.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                EditText ed = (EditText) findViewById(R.id.roomNameField);
                roomManager.createRoom(ed.getText().toString(),geoPoint);
            }
        });

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        RoomListItemView createRoomBtn = (RoomListItemView) findViewById(R.id.createRoom);
        createRoomBtn.setButtonListener(new Button.OnClickListener(){
            public void onClick(View v){
                Button makeRoom = (Button) findViewById(R.id.submitCreateRoomButton);
                makeRoom.setVisibility(View.VISIBLE);
                EditText ed = (EditText) findViewById(R.id.roomNameField);
                ed.setVisibility(View.VISIBLE);
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        Log.d("Location", "Going to get location");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
              .addConnectionCallbacks(this)
               .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void onConnected(Bundle bundle) {
        Log.d("Location", "Connected");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Got Location", "Location is" + mLastLocation.toString());
            geoPoint = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            Log.d("Location", "mLastLocation was null");
        }
    }




    public void onConnectionSuspended(int cs){
        Log.d("Location", "Could not find location");
        mLastLocation = null;
        geoPoint = null;
    }

    public void onConnectionFailed(ConnectionResult cr){
        Log.d("Location", "Could not find location");

        mLastLocation = null;
        geoPoint = null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_room, menu);
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
