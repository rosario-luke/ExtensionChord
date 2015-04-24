package com.example.lucasrosario.extensionchord.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
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
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.R;
import com.example.lucasrosario.extensionchord.RoomManager;
import com.example.lucasrosario.extensionchord.custom_views.RoomListItemView;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

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
            public void onClick(View v) {

                EditText edName = (EditText) findViewById(R.id.roomNameField);
                EditText edPass = (EditText) findViewById(R.id.roomPasswordField);

                roomManager.createRoom(edName.getText().toString(), edPass.getText().toString(), geoPoint);
                //@TODO WONKY ALERT
//                joinRoom(edName.getText().toString());
            }
        });

        Button refreshList = (Button) findViewById(R.id.createRoomReload);
        Button logoutButton = (Button) findViewById(R.id.createRoomLogout);

        refreshList.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                viewRoomList();
            }
        });
        logoutButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                logout();
            }
        });


        buildGoogleApiClient();
        mGoogleApiClient.connect();
        RoomListItemView createRoomBtn = (RoomListItemView) findViewById(R.id.createRoom);
        createRoomBtn.setButtonListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Button makeRoom = (Button) findViewById(R.id.submitCreateRoomButton);
                makeRoom.setVisibility(View.VISIBLE);
                EditText edName = (EditText) findViewById(R.id.roomNameField);
                EditText edPassword = (EditText) findViewById(R.id.roomPasswordField);
                edName.setVisibility(View.VISIBLE);
                edPassword.setVisibility(View.VISIBLE);
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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            Log.d("Got Location", "Location is" + mLastLocation.toString());
            geoPoint = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            viewRoomList();
        } else {
            Log.d("Location", "mLastLocation was null");
        }
    }

    public void refreshLocation(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            Log.d("Got Location", "Location is" + mLastLocation.toString());
            geoPoint = new ParseGeoPoint(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        } else {
            Log.d("Location", "mLastLocation was null");
        }
    }

    private void logout(){
        ParseUser.getCurrentUser().logOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void viewRoomList() {
        refreshLocation();
        List<ParseRoom> rooms = roomManager.getNearbyRooms(0.5, geoPoint);
        String[] values = new String[rooms.size()];

        for (int i = 0; i < rooms.size(); i++) {
            values[i] = rooms.get(i).getRoomName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.generic_list_item, values);
        ListView lv = (ListView) findViewById(R.id.roomList);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text or do whatever you need.
                final String roomName = ((TextView) view).getText().toString();
                String pass = RoomManager.getParseRoom(roomName).getPassword();
                if(pass == null || pass.equals("")){
                    joinRoom(roomName);
                }
                else {
                    //Ask user for password.
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinRoomActivity.this);
                    builder.setTitle("Please Enter Room Password.");

                    // Set up the input
                    final EditText input = new EditText(JoinRoomActivity.this);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String givenPass = input.getText().toString();
                            String realPass = RoomManager.getParseRoom(roomName).getPassword();
                            if (givenPass.equals(realPass)) {
                                joinRoom(roomName);
                            } else {
                                Toast.makeText(JoinRoomActivity.this, "Error Incorrect Password.", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            }
        });
    }

    public void joinRoom(String roomName){
        RoomManager.addUserToRoom(roomName);

        Intent myIntent = new Intent(JoinRoomActivity.this, RoomActivity.class);
        myIntent.putExtra("roomName", roomName);

        JoinRoomActivity.this.startActivity(myIntent);
    }

    public void onConnectionSuspended(int cs) {
        Log.d("Location", "Could not find location");
        mLastLocation = null;
        geoPoint = null;
    }

    public void onConnectionFailed(ConnectionResult cr) {
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
