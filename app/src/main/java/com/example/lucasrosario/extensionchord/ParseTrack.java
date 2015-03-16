package com.example.lucasrosario.extensionchord;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lucas on 3/11/15.
 */
@ParseClassName("ParseTrack")
public class ParseTrack extends ParseObject{

    private JSONObject jsonObject;

//    private String trackName;
//    private String trackArtist;
//    private String trackAlbum;
//    private int trackID;


//    public ParseTrack(JSONObject o, String tName, String tArtist, String tAlbum, int tID){
//        jsonObject = o;
//        trackName = tName;
//        trackArtist = tArtist;
//        trackAlbum = tAlbum;
//        trackID = tID;
//    }

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
    public void setJSONObject(JSONObject o){
        jsonObject = o;
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

    public String getTrackName() { return getString("trackName"); }

    public String getTrackArtist() {
        return getString("trackArtist");
    }

    public String getTrackAlbum() {
        return getString("trackAlbum");
    }

    public int getTrackID() { return getInt("trackID"); }

}
