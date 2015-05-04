package com.example.lucasrosario.extensionchord.parse_objects;

import android.util.Log;

import com.example.lucasrosario.extensionchord.Constants;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 3/16/2015.
 */
@ParseClassName("ParseMusicQueue")
public class ParseMusicQueue extends ParseObject {
    public void addTrackToQueue(ParseTrack track) {
        add("tracks", track);
    }

    /**
     * Gets the track list from the queue
     * @return a List with all the ParseTracks in the queue
     */
    public List<ParseTrack> getTrackList() {
        List<ParseTrack> tracks = getList("tracks");
        if (tracks != null)
            for (ParseTrack track : tracks) {
                try {
                    //ParseQuery<ParseTrack> query = ParseTrack.getQuery();
                    //query.whereEqualTo("objectId", track.getObjectId());
                    //ParseTrack mTrack = query.getFirst();
                    track.fetchIfNeeded();
                } catch (Exception e) {
                    Log.d("ParseMusicQueue", "Error during ParseTrack query");
                }
            }

        return getList("tracks");
    }

    /**
     * pop() function for the queue
     * @return the top ParseTrack on the queue and deletes it from the queue
     */
    public ParseTrack pop() {
        List<ParseTrack> tracks = getList("tracks");
        ParseTrack first = null;
        if (tracks == null || !tracks.isEmpty()) {
            first = tracks.get(0);
            List<ParseTrack> firstList = new ArrayList<ParseTrack>();
            Log.d("Pop", first.getTrackName());
            firstList.add(first);
            removeAll("tracks", firstList);
            saveInBackground();
        }
        return first;
    }

    /**
     * Deletes a ParseTrack from the Queue
     * @param toDelete the ParseTrack to delete from the queue
     */
    public void deleteTrack(ParseTrack toDelete) {
        List<ParseTrack> tracks = getList("tracks");
        if (tracks.contains(toDelete)) {
            List<ParseTrack> dList = new ArrayList<>();
            dList.add(toDelete);
            removeAll("tracks", dList);
        }
    }

    /**
     * Checks whether or not the track has enough downvotes to be deleted.
     *
     * @param track    ParseTrack being downvoted
     * @param numUsers Number of users in the room
     * @return True if the track meets deletion criteria, otherwise false
     */
    public boolean checkTrackDownvotes(ParseTrack track, int numUsers) {
        if (track != null && (double) (track.getDownvoteList().size()) >= (Constants.SKIP_THRESHOLD * numUsers)) {
            return true;
        }
        return false;
    }
}
