package com.example.lucasrosario.extensionchord.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucasrosario.extensionchord.parse_objects.ParseTrack;
import com.example.lucasrosario.extensionchord.R;

/**
 * Created by Jakub on 3/17/2015.
 * Custom view for the music queue in the ViewQueueFragment
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

    /**
     * Sets the context menu that comes up when the view is long pressed
     * @return
     */
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return cMenu;
    }

    /**
     * Returns whether the menuInfo passed in is the views context menu
     * @param menuInfo ContextMenuInfo item to compare
     * @return
     */
    public boolean isContextView(ContextMenu.ContextMenuInfo menuInfo) {
        return menuInfo == (ContextMenu.ContextMenuInfo)cMenu;
    }

    /**
     * Getter for the track name
     * @return track name
     */
    public String getTrackName(){
        return ((TextView)this.findViewById(R.id.track_name)).getText().toString();
    }

    /**
     * Returns the ParseTrack object that the view holds
     * @return
     */
    public ParseTrack getTrack(){ return track;}

    /**
     * Custom ContextMenuInfo for this class used for calling the context menu
     */
    public static class TrackDisplayContextMenu implements ContextMenu.ContextMenuInfo {
        public ViewTrackDisplayItem  trackDisplayItem = null;

        public TrackDisplayContextMenu (ViewTrackDisplayItem vt) {
            trackDisplayItem = vt;
        }
    }


}
