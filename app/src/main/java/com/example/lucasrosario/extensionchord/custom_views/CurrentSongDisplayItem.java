package com.example.lucasrosario.extensionchord.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.R;

/**
 * Created by lucaspritz on 4/7/15.
 */
public class CurrentSongDisplayItem extends LinearLayout{

    private ParseTrack track;

    public CurrentSongDisplayItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ViewTrackDisplayItem, 0, 0);
        String track = a.getString(R.styleable.ViewTrackDisplayItem_view_track_name);
        String artist = a.getString(R.styleable.ViewTrackDisplayItem_view_artist_name);
        String album = a.getString(R.styleable.ViewTrackDisplayItem_view_album_name);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_track_display_item, this, true);

        TextView track_view = (TextView)this.findViewById(R.id.track_name);
        track_view.setText(track);

        TextView artist_view = (TextView)this.findViewById(R.id.artist_name);
        artist_view.setText(artist);

        TextView album_view = (TextView)this.findViewById(R.id.album_name);
        album_view.setText(album);
    }

    public CurrentSongDisplayItem(Context context, ParseTrack t) {
        super(context);

        this.track = t;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.current_song, this, true);

        TextView track_view = (TextView)this.findViewById(R.id.track_name);
        track_view.setText(track.getTrackName());

        TextView artist_view = (TextView)this.findViewById(R.id.artist_name);
        artist_view.setText(track.getTrackArtist());

        TextView album_view = (TextView)this.findViewById(R.id.album_name);
        album_view.setText(track.getTrackAlbum());
    }

    public String getTrackName(){
        return ((TextView)this.findViewById(R.id.track_name)).getText().toString();
    }

    public ParseTrack getTrack(){ return track;}

    public void setPlayListener(OnClickListener listener){
        Button play_button = (Button)this.findViewById(R.id.play_button);
        play_button.setOnClickListener(listener);
    }

    public void setPauseListener(OnClickListener listener){
        Button pause_button = (Button)this.findViewById(R.id.pause_button);
        pause_button.setOnClickListener(listener);
    }

    public void hideButtons(){
        Button pause_button = (Button)this.findViewById(R.id.pause_button);
        Button play_button = (Button)this.findViewById(R.id.play_button);

        pause_button.setVisibility(View.GONE);
        play_button.setVisibility(View.GONE);
    }
}
