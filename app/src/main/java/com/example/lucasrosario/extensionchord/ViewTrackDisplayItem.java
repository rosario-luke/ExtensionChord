package com.example.lucasrosario.extensionchord;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Jakub on 3/17/2015.
 */
public class ViewTrackDisplayItem extends LinearLayout{

    private ParseTrack track;
    private TrackDisplayContextMenu cMenu;
    public ViewTrackDisplayItem(Context context, AttributeSet attrs) {
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

        cMenu = new TrackDisplayContextMenu(this);
    }

    public ViewTrackDisplayItem(Context context, ParseTrack t, int count) {
        super(context);

        this.track = t;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_track_display_item, this, true);

        TextView track_view = (TextView)this.findViewById(R.id.track_name);
        track_view.setText(track.getTrackName());

        TextView artist_view = (TextView)this.findViewById(R.id.artist_name);
        artist_view.setText(track.getTrackArtist());

        TextView album_view = (TextView)this.findViewById(R.id.album_name);
        album_view.setText(track.getTrackAlbum());

        TextView track_number = (TextView)this.findViewById(R.id.track_number);
        track_number.setText(count + ".)");

        cMenu = new TrackDisplayContextMenu(this);

    }

    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return cMenu;
    }

    public boolean isContextView(ContextMenu.ContextMenuInfo menuInfo) {
        return menuInfo == (ContextMenu.ContextMenuInfo)cMenu;
    }

    public String getTrackName(){
        return ((TextView)this.findViewById(R.id.track_name)).getText().toString();
    }



    public ParseTrack getTrack(){ return track;}

    public String getSubmitter(){ return track.getSubmitter(); }

    static class TrackDisplayContextMenu implements ContextMenu.ContextMenuInfo {
        protected ViewTrackDisplayItem  trackDisplayItem = null;

        protected TrackDisplayContextMenu (ViewTrackDisplayItem vt) {
            trackDisplayItem = vt;
        }
    }


}
