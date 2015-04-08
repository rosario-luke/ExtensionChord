package com.example.lucasrosario.extensionchord;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 3/11/15.
 */
@ParseClassName("ParseTrack")
public class ParseTrack extends ParseObject{
    public void setTrackName(String trackName){
        put("trackName",trackName);
    }
    public void setTrackArtist(String trackArtist){
        put("trackArtist",trackArtist);
    }
    public void setTrackAlbum(String trackAlbum){
        put("trackAlbum",trackAlbum);
    }
    public void setTrackID(int trackID){
        put("trackID",trackID);
    }
    public void setSubmitter(String submitter) {
        put("submitter", submitter);
    }

    public String getTrackName() { return getString("trackName"); }

    public String getTrackArtist() {
        return getString("trackArtist");
    }

    public String getTrackAlbum() {
        return getString("trackAlbum");
    }

    public int getTrackID() { return getInt("trackID"); }

    public String getSubmitter() {
        return getString("submitter");
    }

    public static ParseQuery<ParseTrack> getQuery() {
        return ParseQuery.getQuery(ParseTrack.class);
    }

}
