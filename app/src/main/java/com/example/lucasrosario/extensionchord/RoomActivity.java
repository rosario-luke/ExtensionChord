package com.example.lucasrosario.extensionchord;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.parse.Parse;
import com.parse.ParseUser;

import java.util.ArrayList;


public class RoomActivity extends Activity {
    private String[] mDrawerStrings;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private SearchFragment searchFragment;
    private ViewQueueFragment viewQueueFragment;
    private ViewRoomUsersFragment viewRoomUsersFragment;
    private Fragment curFragment;
    private String roomName;
    private MediaPlayer currentMediaPlayer = new MediaPlayer();
    private boolean testFlag = false;

    public String getRoomName(){
        return roomName;
    }

    public void setCurrentMediaPlayerURL(String url){
        try {
            currentMediaPlayer.setDataSource(url);
            currentMediaPlayer.prepare();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setTestFlag(boolean b){
        testFlag = b;
    }

    public void startMediaPlayer(){
        try {
            currentMediaPlayer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void stopMediaPlayer(){
        currentMediaPlayer.pause();
    }

    public void resetMediaPlayer(){
        currentMediaPlayer.reset();
        currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void setMediaPlayerOnCompletionListener(){
        currentMediaPlayer.setOnCompletionListener (new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                ParseRoom currRoom = RoomManager.getParseRoom(roomName);
                currRoom.getParseMusicQueue().pop();
                resetMediaPlayer();
                viewQueueFragment.refresh();
                setMediaPlayerOnCompletionListener();

                startMediaPlayer();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(this, "f539HwpFiyK3DhDsOb7xYRNwCtr7vCeMihU776Vk", "tH1ktzEjhCBZSvMzVR9Thjqj6sDtrrb1gwUYIlh1");
        currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setMediaPlayerOnCompletionListener();

        setContentView(R.layout.activity_room);

        createNavigationDrawer();

        searchFragment = new SearchFragment();
        viewQueueFragment = new ViewQueueFragment();
        viewRoomUsersFragment = new ViewRoomUsersFragment();
        Bundle bundle = getIntent().getExtras();
        Bundle args = new Bundle();
        try{
            args.putString("room_name",bundle.getString("roomName"));
        }catch(NullPointerException e){
            args.putString("room_name", "[Tester] TestRoom");
        }

        viewRoomUsersFragment.setArguments(args);
        viewRoomUsersFragment.newInstance();


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

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                EditText myEditText = (EditText) findViewById(R.id.searchField);
                if (myEditText != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        setUpSearchFragment();
                        break;
                    case 1:
                        setUpRoomUsersFragment();
                        break;
                    case 2:
                        setUpViewQueueFragment();
                        break;
                    case 3:
                        onBackPressed();
                        break;
                }
                mDrawerLayout.closeDrawers();
            }
        });
    }

    @Override
    protected void onDestroy(){
        RoomManager.removeUserFromRoom(ParseUser.getCurrentUser().getUsername());
        ParseRoom room = RoomManager.getParseRoom(roomName);
/*
        String creatorUsername = room.getCreator().getUsername();
        String currentUsername = ParseUser.getCurrentUser().getUsername();
        if (creatorUsername.equals(currentUsername)) {
            RoomManager.deleteRoom(roomName);
            stopMediaPlayer();
        }*/

        super.onDestroy();

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

    public void setUpRoomUsersFragment(){
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container,viewRoomUsersFragment,"ViewRoomUsersFragment");
//        fragmentTransaction.hide(curFragment);
        fragmentTransaction.commit();
        curFragment = viewRoomUsersFragment;
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
        searchFragment.onSearchBtnClick(v, testFlag);

    }

    public void onRefreshClick(View v) throws Exception{
        viewQueueFragment.onRefreshClick(v);
    }

    /*public void addTracks(ArrayList<LocalTrack> tracks){
        if(curFragment.equals(searchFragment)) {
            searchFragment.addTracks(tracks);
        }
    }*/
}
