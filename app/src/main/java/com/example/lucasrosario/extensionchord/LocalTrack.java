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

    /**
     * Constructor for a Local Track object. Holds information within the phone as opposed to within parse.
     * @param o: JSON holding the information.
     * @param tName: Track Name
     * @param tArtist: Track Artist
     * @param tAlbum: Track Album
     * @param tID: Track ID
     */
    public LocalTrack(JSONObject o, String tName, String tArtist, String tAlbum, int tID){
        jsonObject = o;
        trackName = tName;
        trackArtist = tArtist;
        trackAlbum = tAlbum;
        trackID = tID;
    }

    /**
     * Gets the track name from the JSON.
     * @param o: JSON to get track name from.
     * @return the track name.
     */
    public static String getTrackFromJSON(JSONObject o){
        String track;
        try{
            track = o.getString("title");
        } catch(JSONException e){
            return null;
        }
        return track;
    }

    /**
     * Gets the artist name from the JSON
     * @param o: Json Object to get track artist from.
     * @return Track Artist.
     */
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

    /**
     * Gets the album name from the JSON
     * @param o: JSON to get album name from.
     * @return The album name.
     */
    public static String getAlbumFromJSON(JSONObject o){
        String album = "";
        try{
            album = o.getString("release");
        } catch(JSONException e){
            return album;
        }
        return album;
    }

    /**
     * Gets the track ID from the JSON
     * @param o: JSON to get track ID from.
     * @return The track ID.
     */
    static public int getTrackIDFromJSON(JSONObject o){
        try{
            return o.getInt("id");
        }
        catch(JSONException e){
            return -1;
        }
    }

    /**
     * Checks if the track has album art.
     * @return whether the track has album art or not.
     */
    public boolean hasAlbumArt(){
        try{
            jsonObject.getString("artwork_url");
        } catch(JSONException e){
            return false;
        }
        return true;
    }

    /**
     * Gets the URL for the album art.
     * @return Album Art URL.
     */
    public String getAlbumArtUrl() {
        try {
            return jsonObject.getString("artwork_url");
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Gets the track name.
     * @return The track name.
     */
    public String getTrackName() { return trackName; }

    /**
     * Gets the track artist.
     * @return the track artist.
     */
    public String getTrackArtist() {
        return trackArtist;
    }

    /**
     * gets the Track album
     * @return The track album.
     */
    public String getTrackAlbum() {
        return trackAlbum;
    }

    /**
     * Gets the Track ID.
     * @return The track ID.
     */
    public int getTrackID() { return trackID; }

}
