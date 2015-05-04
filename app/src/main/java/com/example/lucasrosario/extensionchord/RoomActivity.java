package com.example.lucasrosario.extensionchord;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.parse_objects.ParseMusicQueue;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.room_fragments.SearchFragment;
import com.example.lucasrosario.extensionchord.room_fragments.TrackInfoDialogFragment;
import com.example.lucasrosario.extensionchord.room_fragments.ViewQueueFragment;
import com.example.lucasrosario.extensionchord.room_fragments.ViewRoomUsersFragment;
import com.parse.Parse;
import com.parse.ParseUser;

import java.lang.reflect.Field;
import java.util.List;

/**
 * RoomActivity is when you're in a room. Ties together the Search, View Queue, and
 * View Room Users fragments.
 */
public class RoomActivity extends FragmentActivity implements MediaPlayer.OnPreparedListener {
    private DrawerLayout mDrawerLayout;
    private SearchFragment searchFragment;
    private ViewQueueFragment viewQueueFragment;
    private ViewRoomUsersFragment viewRoomUsersFragment;
    private String roomName;
    private MediaPlayer currentMediaPlayer;
    private boolean testFlag = false;
    private ProgressDialog progressDialog;
    private boolean prepared = false;

    /**
     * Stores the room name associated with this activity.
     *
     * @param name = Name of the room
     */
    public void setRoomName(String name) {
        roomName = name;
    }

    /**
     * Gets the room name associated with this activity.
     *
     * @return The room name
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * Shows a dialog fragment for showing track info.
     *
     * @param trackName  = Name of the track
     * @param albumName  = Name of the album
     * @param artistName = Name of the artist
     * @param submitter  = Name of the submitter
     */
    public void showTrackInfoDialog(String trackName, String albumName, String artistName, String submitter) {
        FragmentManager fm = getSupportFragmentManager();
        TrackInfoDialogFragment trackInfoDialogFragment = new TrackInfoDialogFragment();
        Bundle dialogBundle = new Bundle();

        dialogBundle.putString("albumName", albumName);
        dialogBundle.putString("artistName", artistName);
        dialogBundle.putString("trackName", trackName);
        dialogBundle.putString("submitter", submitter);

        trackInfoDialogFragment.setArguments(dialogBundle);

        trackInfoDialogFragment.show(fm, "fragment_track_info");
    }

    /**
     * returns the current media player
     * @return current media player
     */
    public MediaPlayer getMediaPlayer() {
        return currentMediaPlayer;
    }

    /**
     * Sets the URL for the current media player
     * @param url the url to set
     * @param async if the media player should prepare asyncronously
     * @return if the media player url was set
     */
    public boolean setCurrentMediaPlayerURL(String url, boolean async) {
        try {
            currentMediaPlayer.setDataSource(url);
            if (async) {
                currentMediaPlayer.prepareAsync();
                return false;
            } else {
                currentMediaPlayer.prepare();
            }
            prepared = true;
            return false;
        } catch (Exception e) {
            dismissProgressDialog();
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Called when the MediaPlayer is prepared asynchronously and starts the MediaPlayer.
     * @param mp: MediaPlayer that was prepared.
     */
    public void onPrepared(MediaPlayer mp) {
        currentMediaPlayer.start();
        prepared = true;

    }

    public void setTestFlag(boolean b) {
        testFlag = b;
    }

    /**
     * Attempt to start the MediaPlayer.
     */
    public void startMediaPlayer() {
        try {
            if (prepared) {
                currentMediaPlayer.start();
            } else {
                resetMediaPlayer();
                currentMediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the MediaPlayer.
     */
    public void stopMediaPlayer() {
        currentMediaPlayer.pause();
    }

    /**
     * Resets the MediaPlayer by remaking the media player and setting the proper parameters.
     */
    public void resetMediaPlayer() {
        ParseRoom currRoom = RoomManager.getParseRoom(roomName);
        ParseMusicQueue queue = currRoom.getParseMusicQueue();
        currentMediaPlayer.reset();
        currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (!queue.getTrackList().isEmpty()) {
            boolean cError = setCurrentMediaPlayerURL("http://api.soundcloud.com/tracks/" + queue.getTrackList().get(0).getTrackID() + "/stream?client_id=" + Constants.SOUNDCLOUD_API_KEY, false);
            if (cError) {
                Toast.makeText(this, "Error Playing Song: " + queue.getTrackList().get(0).getTrackName(), Toast.LENGTH_SHORT).show();
                queue.pop();
            }
        }
    }

    /**
     * Attempts to reset the MediaPlayer asynchronously.
     */
    public void resetMediaPlayerAsync() {
        ParseRoom currRoom = RoomManager.getParseRoom(roomName);
        ParseMusicQueue queue = currRoom.getParseMusicQueue();
        currentMediaPlayer.reset();
        currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        if (!queue.getTrackList().isEmpty()) {
            boolean cError = setCurrentMediaPlayerURL("http://api.soundcloud.com/tracks/" + queue.getTrackList().get(0).getTrackID() + "/stream?client_id=" + Constants.SOUNDCLOUD_API_KEY, true);
            if (cError) {
                Toast.makeText(this, "Error Playing Song: " + queue.getTrackList().get(0).getTrackName(), Toast.LENGTH_SHORT).show();
                queue.pop();
            }
        }
    }

    /**
     * Sets the callback that is called when the MediaPlayer finishes the current playing track.
     */
    public void setMediaPlayerOnCompletionListener() {
        currentMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                ParseRoom currRoom;
                ParseMusicQueue queue = null;
                boolean cError = true;
                while (cError) {
                    try {
                        currRoom = RoomManager.getParseRoom(roomName).fetch();
                        queue = currRoom.getParseMusicQueue().fetch();
                        cError = false;
                    } catch (Exception e) {
                        // Do nothing
                    }
                }
                queue.pop();
                List<ParseTrack> tList = queue.getTrackList();
                prepared = false;
                if (tList != null && !tList.isEmpty()) {
                    resetMediaPlayer();
                    viewQueueFragment.refresh();
                } else
                    viewQueueFragment.refresh();
            }
        });
    }

    /**
     * Called when the activity is created. Holds initialization logic.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMediaPlayer = new MediaPlayer();
        currentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setMediaPlayerOnCompletionListener();
        currentMediaPlayer.setOnPreparedListener(this);

        setContentView(R.layout.activity_room);

        createNavigationDrawer();

        searchFragment = new SearchFragment();
        viewQueueFragment = new ViewQueueFragment();
        viewRoomUsersFragment = new ViewRoomUsersFragment();
        Bundle bundle = getIntent().getExtras();
        Bundle args = new Bundle();
        try {
            args.putString("room_name", bundle.getString("roomName"));
        } catch (NullPointerException e) {
            args.putString("room_name", "[Tester] TestRoom");
        }

        viewRoomUsersFragment.setArguments(args);
        viewRoomUsersFragment.newInstance();

        // Is this unnecessary? vvv
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, searchFragment, "SearchFragment");
        fragmentTransaction.commit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
    }

    /**
     * Creates the navigation drawer for navigating between fragments in the activity.
     */
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
        ListView mDrawerList = (ListView) findViewById(R.id.room_left_drawer);

        String[] mDrawerStrings = getResources().getStringArray(R.array.navdrawer_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.generic_list_item, mDrawerStrings));

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                EditText myEditText = (EditText) findViewById(R.id.searchField);
                if (myEditText != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                switch (position) {
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
                dismissProgressDialog();
            }
        });

        // Makes the drawer a lot easier to open
        try {
            Field mDragger = mDrawerLayout.getClass().getDeclaredField("mLeftDragger");
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(mDrawerLayout);

            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);

            // Making the drag margin a little wider
            mEdgeSize.setInt(draggerObj, edge * 5);
        } catch (Exception e) {
            Log.d("Navigation Drawer", "Navigation drawer drag margin setting failed");
        }
    }

    @Override
    protected void onDestroy() {
        RoomManager.removeUserFromRoom(ParseUser.getCurrentUser().getUsername());

        super.onDestroy();
    }

    /**
     * Sets up the fragment for viewing the RoomUsers.
     */
    public void setUpRoomUsersFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, viewRoomUsersFragment, "ViewRoomUsersFragment");
        fragmentTransaction.commit();
    }

    /**
     * Sets up the fragment for searching for ParseTracks.
     */
    public void setUpSearchFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, searchFragment, "SearchFragment");
        fragmentTransaction.commit();
    }

    /**
     * Sets up the fragment for Viewing the ParseQueue.
     */
    public void setUpViewQueueFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.container, viewQueueFragment, "ViewQueueFragment");
        fragmentTransaction.commit();
    }

    /**
     * Sets the callback for the SearchFragment's main button.
     * @param v
     */
    public void onSearchBtnClick(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        showProgressDialog();
        searchFragment.onSearchBtnClick(v, testFlag);
    }

    /**
     * Sets the callback for the ViewQueueFragment's refresh button.
     * @param v
     * @throws Exception
     */
    public void onRefreshClick(View v) throws Exception {
        viewQueueFragment.onRefreshClick(v);
    }

    /**
     * Shows the loading screen.
     */
    public void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    /**
     * Dismisses the loading screen.
     */
    public void dismissProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
