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
                    ParseQuery<ParseTrack> query = ParseTrack.getQuery();
                    query.whereEqualTo("objectId", track.getObjectId());
                    ParseTrack mTrack = query.getFirst();
                    Log.d("ParseMusicQueue", "Found track with track name: " + mTrack.getTrackName());
                } catch (Exception e) {
                    Log.d("ParseMusicQueue", "Error during ParseTrack query");
                }
            }

        return getList("tracks");
    }

    public void pop(){
        List<ParseTrack> tracks = getList("tracks");
        if(!tracks.isEmpty()) {
            List<ParseTrack> first = new ArrayList<ParseTrack>();
            first.add(tracks.get(0));
            removeAll("tracks", first);
        }
    }

    public void deleteTrack(ParseTrack toDelete){
        List<ParseTrack> tracks = getList("tracks");
        if(tracks.contains(toDelete)){
            List<ParseTrack> dList = new ArrayList<>();
            dList.add(toDelete);
            removeAll("tracks", dList);
        }
    }

    public Integer getCurrentTrack(){ return (int)getList("tracks").get(0); }
}
