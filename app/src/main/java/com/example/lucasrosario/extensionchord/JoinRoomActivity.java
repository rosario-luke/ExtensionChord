package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;

import java.util.List;


public class JoinRoomActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
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

        Button refreshList = (Button) findViewById(R.id.createRoomReload);
        refreshList.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                viewRoomList();
            }
        });

        buildGoogleApiClient();
        mGoogleApiClient.connect();
        RoomListItemView createRoomBtn = (RoomListItemView) findViewById(R.id.createRoom);
        createRoomBtn.setButtonListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Button makeRoom = (Button) findViewById(R.id.submitCreateRoomButton);
                makeRoom.setVisibility(View.VISIBLE);
                EditText ed = (EditText) findViewById(R.id.roomNameField);
                ed.setVisibility(View.VISIBLE);
                viewRoomList();
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
            viewRoomList();

        } else {
            Log.d("Location", "mLastLocation was null");
        }
    }

    private void viewRoomList() {
        List<ParseRoom> rooms = roomManager.getNearbyRooms(0.5, geoPoint);
        String[] values = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            values[i] = rooms.get(i).getRoomName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        ListView lv = (ListView) findViewById(R.id.roomList);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text or do whatever you need.
                String roomName = ((TextView) view).getText().toString();
                RoomManager.addUserToRoom(roomName);

                Intent myIntent = new Intent(JoinRoomActivity.this, RoomActivity.class);
                myIntent.putExtra("roomName", roomName);

                JoinRoomActivity.this.startActivity(myIntent);
            }
        });
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
