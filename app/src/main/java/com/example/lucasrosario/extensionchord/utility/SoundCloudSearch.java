package com.example.lucasrosario.extensionchord.utility;

import android.os.AsyncTask;
import android.util.Log;

import com.example.lucasrosario.extensionchord.Constants;
import com.example.lucasrosario.extensionchord.LocalTrack;
import com.example.lucasrosario.extensionchord.OnSearchTaskCompleted;


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


public class SoundCloudSearch extends AsyncTask<String, Void, ArrayList<LocalTrack>> {

    final static CharSequence spaceSeq = " ";
    final static CharSequence spaceReplace = "%20";
    List<String> str;
    private OnSearchTaskCompleted listener;

    public SoundCloudSearch (OnSearchTaskCompleted l){
        this.listener = l;
    }
    @Override
    protected ArrayList<LocalTrack> doInBackground(String... searchString){
        ArrayList<LocalTrack> tracks = new ArrayList<LocalTrack>();


        String query = searchString[0].replace(spaceSeq, spaceReplace);

        String url = "http://api.soundcloud.com/tracks.json?client_id=" + Constants.SOUNDCLOUD_API_KEY + "&q=" + query + "&limit=50";
        String page = null;
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
                String title = LocalTrack.getTrackFromJSON(entry);
                String artist = LocalTrack.getArtistFromJSON(entry);
                String album = LocalTrack.getAlbumFromJSON(entry);
                int trackID = LocalTrack.getTrackIDFromJSON(entry);
                if(title == null) {
                    continue;
                }
                tracks.add(new LocalTrack(entry, title, artist, album, trackID));
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
    protected void onPostExecute(ArrayList<LocalTrack> tracks) {
        //super.onPostExecute(events);
        listener.onTaskCompleted(tracks);
        //roomActivity.addTracks(tracks);
    }



}
