package com.example.lucasrosario.extensionchord;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 3/16/2015.
 */
@ParseClassName("ParseMusicQueue")
public class ParseMusicQueue extends ParseObject {
    public void addTrackToQueue(ParseTrack track){ add("tracks", track);}

    public List<ParseTrack> getTrackList(){
        return getList("tracks");
    }

    public Integer getCurrentTrack(){ return (int)getList("tracks").get(0); }
}
