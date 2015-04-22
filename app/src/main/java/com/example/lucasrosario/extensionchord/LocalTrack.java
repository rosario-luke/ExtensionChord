package com.example.lucasrosario.extensionchord;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jakub on 3/16/2015.
 */
public class LocalTrack {
    private JSONObject jsonObject;

    private String trackName;
    private String trackArtist;
    private String trackAlbum;
    private int trackID;

    public LocalTrack(JSONObject o, String tName, String tArtist, String tAlbum, int tID){
        jsonObject = o;
        trackName = tName;
        trackArtist = tArtist;
        trackAlbum = tAlbum;
        trackID = tID;
    }

    public static String getTrackFromJSON(JSONObject o){
        String track;
        try{
            track = o.getString("title");
        } catch(JSONException e){
            return null;
        }
        return track;
    }

    public static String getArtistFromJSON(JSONObject o){
        String artist = "";
        try{
            JSONObject uploader = o.getJSONObject("user");
            artist = uploader.getString("username");
        } catch(JSONException e){
            return artist;
        }
        return artist;
    }

    public static String getAlbumFromJSON(JSONObject o){
        String album = "";
        try{
            album = o.getString("release");
        } catch(JSONException e){
            return album;
        }
        return album;
    }

    static public int getTrackIDFromJSON(JSONObject o){
        try{
            return o.getInt("id");
        }
        catch(JSONException e){
            return -1;
        }
    }

    public boolean hasAlbumArt(){
        try{
            jsonObject.getString("artwork_url");
        } catch(JSONException e){
            return false;
        }
        return true;
    }

    public String getAlbumArtUrl() {
        try {
            return jsonObject.getString("artwork_url");
        } catch (JSONException e) {
            return null;
        }
    }

    public String getTrackName() { return trackName; }

    public String getTrackArtist() {
        return trackArtist;
    }

    public String getTrackAlbum() {
        return trackAlbum;
    }

    public int getTrackID() { return trackID; }

}
