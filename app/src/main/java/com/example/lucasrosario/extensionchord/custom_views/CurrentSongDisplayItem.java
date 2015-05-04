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
 * A custom view for the current song playing
 */
public class CurrentSongDisplayItem extends LinearLayout{

    private ParseTrack track;

    /**
     * Default constructor for when creating the class using xml
     * @param context
     * @param attrs
     */
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

    /**
     * Constructor for when manually adding  a ParseTrack
     * @param context
     * @param t the ParseTrack to be displayed in this display item
     */
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

    /**
     * Getter for the trackname
     * @return track name of the song playing
     */
    public String getTrackName(){
        return ((TextView)this.findViewById(R.id.track_name)).getText().toString();
    }

    /**
     * Getter for the ParseTrack object that this class holds
     * @return ParseTrack object
     */
    public ParseTrack getTrack(){ return track;}

    /**
     * Sets the OnClickListener for the play button
     * @param listener
     */
    public void setPlayListener(OnClickListener listener){
        Button play_button = (Button)this.findViewById(R.id.play_button);
        play_button.setOnClickListener(listener);
    }

    /**
     * Sets the OnClickListener for the pause button
     * @param listener
     */
    public void setPauseListener(OnClickListener listener){
        Button pause_button = (Button)this.findViewById(R.id.pause_button);
        pause_button.setOnClickListener(listener);
    }

    /**
     * Hides the play and pause buttons
     */
    public void hideButtons(){
        Button pause_button = (Button)this.findViewById(R.id.pause_button);
        Button play_button = (Button)this.findViewById(R.id.play_button);

        pause_button.setVisibility(View.GONE);
        play_button.setVisibility(View.GONE);
    }
}
