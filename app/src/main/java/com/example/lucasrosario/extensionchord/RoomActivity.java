package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;


public class RoomActivity extends Activity {
    private String[] mDrawerStrings;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_room);

        createNavigationDrawer();
    }

    private void createNavigationDrawer() {
        Bundle intent = getIntent().getExtras();

        // Won't need this once we store room with the ParseUser object
        if (intent != null) {
            TextView roomNameTextView = (TextView) findViewById(R.id.drawer_roomname);
            roomNameTextView.setText("Room: " + intent.getString("roomName"));
        }

        if (ParseUser.getCurrentUser() != null) {
            TextView userNameTextView = (TextView) findViewById(R.id.drawer_username);
            userNameTextView.setText("User: " + ParseUser.getCurrentUser().getUsername());
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.room_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.room_left_drawer);

        mDrawerStrings = getResources().getStringArray(R.array.navdrawer_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mDrawerStrings));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener() {});
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(RoomActivity.this, "Clicked item at index: " + position, Toast.LENGTH_LONG).show();
        }
    }
}
