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
    /**
     * Sets the Track's name
     * @param trackName: The new track name.
     */
    public void setTrackName(String trackName){
        put("trackName",trackName);
    }

    /**
     * Adds the user's name to the downvotes
     * @param userName: user who downvoted
     */
    public void addDownvoteUser(String userName){
        addUnique("downvote", userName);
    }

    /**
     * Sets the track's artist.
     * @param trackArtist: artist of track.
     */
    public void setTrackArtist(String trackArtist){
        put("trackArtist",trackArtist);
    }

    /**
     * Sets the track's album.
     * @param trackAlbum: Album name of the track.
     */
    public void setTrackAlbum(String trackAlbum){
        put("trackAlbum",trackAlbum);
    }

    /**
     * Sets the ID of the track.
     * @param trackID: ID of track
     */
    public void setTrackID(int trackID){
        put("trackID",trackID);
    }

    /**
     * Sets the submitter of the track.
     * @param submitter: username of who submitted the track.
     */
    public void setSubmitter(String submitter) {
        put("submitter", submitter);
    }

    /**
     * Gets the track name.
     * @return the track name.
     */
    public String getTrackName() { return getString("trackName"); }

    /**
     * Gets list of users who downvoted.
     * @return the list of users who downvoted.
     */
    public List<String> getDownvoteList() { return getList("downvote"); }

    /**
     * Gets the artist of the track.
     * @return the track artist.
     */
    public String getTrackArtist() {
        return getString("trackArtist");
    }

    /**
     * Gets the album name of the track.
     * @return the album name of the track.
     */
    public String getTrackAlbum() {
        return getString("trackAlbum");
    }

    /**
     * Gets the track ID.
     * @return the track ID.
     */
    public int getTrackID() { return getInt("trackID"); }

    /**
     * Gets the submitter of the track.
     * @return the username of the submitter.
     */
    public String getSubmitter() {
        return getString("submitter");
    }

    /**
     * returns the parseQuery associated with the ParseTrack.
     * @return the parse query.
     */
    public static ParseQuery<ParseTrack> getQuery() {
        return ParseQuery.getQuery(ParseTrack.class);
    }

}
