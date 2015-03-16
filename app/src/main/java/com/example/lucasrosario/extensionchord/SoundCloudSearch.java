package com.example.lucasrosario.extensionchord;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas on 3/11/15.
 */


public class SoundCloudSearch extends AsyncTask<String, Void, ArrayList<ParseTrack>> {

    final static String client_id = "3fe96f34e369ae1ef5cf7e8fcc6c8eec";
    final static CharSequence spaceSeq = " ";
    final static CharSequence spaceReplace = "%20";
    List<String> str;
    private RoomActivity roomActivity;

    public SoundCloudSearch (RoomActivity frag){
        this.roomActivity = frag;
    }
    @Override
    protected ArrayList<ParseTrack> doInBackground(String... searchString){
        ArrayList<ParseTrack> tracks = new ArrayList<ParseTrack>();


        String query = searchString[0].replace(spaceSeq, spaceReplace);

        String url = "http://api.soundcloud.com/tracks.json?client_id=" + client_id + "&q=" + query + "&limit=50";
        String page = null;
        Log.d("request", url);
        try {
            page = executeHttpGet(url);
        } catch(Exception e) {
            Log.d("Error getting page", e.getMessage());
            return null;
        }
        if(page == null){
            Log.d("get page", "getting page failed");
            return null;
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(page);

            for (int i = 0 ; i < jsonArray.length(); i++ ) {
                JSONObject entry = jsonArray.getJSONObject(i);
                String title = ParseTrack.getTrackFromJSON(entry);
                String artist = ParseTrack.getArtistFromJSON(entry);
                String album = ParseTrack.getAlbumFromJSON(entry);
                int trackID = ParseTrack.getTrackIDFromJSON(entry);
                if(title == null) {
                    continue;
                }
                tracks.add(new ParseTrack(entry, title, artist, album, trackID));
            }


        } catch(JSONException e) {
            Log.d("Error parsing json", e.getMessage());
            e.printStackTrace();
            return null;
        }


        return tracks;


    }




    static String executeHttpGet(String URL) throws Exception
    {
        String response = "";
        StringBuilder sb = new StringBuilder();
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                sb.append(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(ArrayList<ParseTrack> tracks) {
        //super.onPostExecute(events);

        roomActivity.addTracks(tracks);
    }



}
