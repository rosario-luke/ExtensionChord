package com.example.lucasrosario.extensionchord;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;


public class RoomActivity extends Activity {
    private String[] mDrawerStrings;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private SearchFragment searchFragment;
    private ViewQueueFragment viewQueueFragment;
    private Fragment curFragment;
    private String roomName;

    public String getRoomName(){
        return roomName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);

        createNavigationDrawer();

        searchFragment = new SearchFragment();
        viewQueueFragment = new ViewQueueFragment();

        // Is this unnecessary? vvv
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container,searchFragment,"SearchFragment");
        fragmentTransaction.commit();
        curFragment = searchFragment;

        //setUpSearchFragment();

    }

    private void createNavigationDrawer() {
        if (ParseUser.getCurrentUser() != null) {
            String userName = ParseUser.getCurrentUser().getUsername();
            TextView userNameTextView = (TextView) findViewById(R.id.drawer_username);
            userNameTextView.setText("User: " + userName);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roomName = bundle.getString("roomName");
            TextView roomNameTextView = (TextView) findViewById(R.id.drawer_roomname);
            roomNameTextView.setText("Room: " + roomName);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.room_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.room_left_drawer);

        mDrawerStrings = getResources().getStringArray(R.array.navdrawer_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.generic_list_item, mDrawerStrings));

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        setUpSearchFragment();
                        break;
                    default:
//                        Toast.makeText(RoomActivity.this, "Clicked item at index: " + position, Toast.LENGTH_LONG).show();
                        setUpViewQueueFragment();
                        break;
                }
                //Toast.makeText(RoomActivity.this, "Clicked item at index: " + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        RoomManager.removeUserFromRoom(ParseUser.getCurrentUser().getUsername());
        ParseRoom room = RoomManager.getParseRoom(roomName);

        String creatorUsername = room.getCreator().getUsername();
        String currentUsername = ParseUser.getCurrentUser().getUsername();
        if (creatorUsername.equals(currentUsername)) {
            RoomManager.deleteRoom(roomName);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room, menu);
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

    public void setUpSearchFragment(){
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container,searchFragment,"SearchFragment");
//        fragmentTransaction.hide(curFragment);
        fragmentTransaction.commit();
        curFragment = searchFragment;
    }

    public void setUpViewQueueFragment(){
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container,viewQueueFragment,"ViewQueueFragment");
//        fragmentTransaction.hide(curFragment);
        fragmentTransaction.commit();
        curFragment = viewQueueFragment;
    }

    public void onSearchBtnClick(View v){
        searchFragment.onSearchBtnClick(v);
    }

    public void addTracks(ArrayList<LocalTrack> tracks){
        searchFragment.addTracks(tracks);
    }
}
