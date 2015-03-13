package com.example.lucasrosario.extensionchord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 3/11/15.
 */
public class Track {

    private JSONObject jsonObject;

    private String trackName;
    private String trackArtist;
    private String trackAlbum;


    public Track(JSONObject o, String tName, String tArtist, String tAlbum){
        jsonObject = o;
        trackName = tName;
        trackArtist = tArtist;
        tAlbum = tAlbum;
    }

    static String getTrackFromJSON(JSONObject o){
        String track;
        try{
            track = o.getString("title");
        } catch(JSONException e){
            return null;
        }
        return track;
    }

    static String getArtistFromJSON(JSONObject o){
        String artist = "";
        try{
            JSONObject uploader = o.getJSONObject("user");
            artist = uploader.getString("username");
        } catch(JSONException e){
            return artist;
        }
        return artist;
    }

    static String getAlbumFromJSON(JSONObject o){
        String album = "";
        try{
            album = o.getString("release");
        } catch(JSONException e){
            return album;
        }
        return album;
    }

    public boolean hasAlbumArt(){
        try{
            jsonObject.getString("artwork_url");
        } catch(JSONException e){
            return false;
        }
        return true;
    }

    public String getAlbumArtUrl(){
        try{
            return jsonObject.getString("artwork_url");
        } catch(JSONException e){
            return null;
        }
    }

    public String getTrackName() {
        return trackName;
    }

    public String getTrackArtist() {
        return trackArtist;
    }

    public String getTrackAlbum() {
        return trackAlbum;
    }



}
