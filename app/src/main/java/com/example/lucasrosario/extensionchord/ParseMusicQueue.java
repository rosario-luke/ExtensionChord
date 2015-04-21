package com.example.lucasrosario.extensionchord;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Jakub on 3/16/2015.
 */
@ParseClassName("ParseMusicQueue")
public class ParseMusicQueue extends ParseObject {
    public void addTrackToQueue(ParseTrack track){
        add("tracks", track);
    }

    public List<ParseTrack> getTrackList(){
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

    public ParseTrack pop(){
        List<ParseTrack> tracks = getList("tracks");
        ParseTrack first = null;
        if(tracks == null ||!tracks.isEmpty()) {
            first = tracks.get(0);
            List<ParseTrack> firstList = new ArrayList<ParseTrack>();
            firstList.add(first);
            removeAll("tracks", firstList);
        }
        return first;
    }

    public void deleteTrack(ParseTrack toDelete){
        List<ParseTrack> tracks = getList("tracks");
        if(tracks.contains(toDelete)){
            List<ParseTrack> dList = new ArrayList<>();
            dList.add(toDelete);
            removeAll("tracks", dList);
        }
    }

    public boolean checkTrackDownvotes(ParseTrack track, int numUsers)
    {
        if(track != null && (double) (track.getDownvoteList().size()) >= (Constants.SKIP_THRESHOLD * numUsers))
        {
            return true;
        }
        return false;
    }

    public Integer getCurrentTrack(){ return (int)getList("tracks").get(0); }
}
