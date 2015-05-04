package com.example.lucasrosario.extensionchord.parse_objects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by lucas on 3/11/15.
 */
@ParseClassName("ParseTrack")
public class ParseTrack extends ParseObject{

    /*
     * All setters for ParseTrack variables
     */
    public void setTrackName(String trackName){
        put("trackName",trackName);
    }
    public void addDownvoteUser(String userName){
        addUnique("downvote", userName);
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

    /*
     * All getters for ParseTrack variables
     */
    public String getTrackName() { return getString("trackName"); }
    public List<String> getDownvoteList() { return getList("downvote"); }
    public String getTrackArtist() {
        return getString("trackArtist");
    }
    public String getTrackAlbum() {
        return getString("trackAlbum");
    }
    public int getTrackID() { return getInt("trackID"); }
    public String getSubmitter() { return getString("submitter"); }
    public static ParseQuery<ParseTrack> getQuery() {
        return ParseQuery.getQuery(ParseTrack.class);
    }

}
