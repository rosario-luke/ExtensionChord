package com.example.lucasrosario.extensionchord.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucasrosario.extensionchord.LocalTrack;
import com.example.lucasrosario.extensionchord.R;

/**
 * Created by lucas on 2/19/15.
 * CustomView for the SearchFragment to display tracks and their info
 */
public class SearchTrackDisplayItem extends LinearLayout{

    private LocalTrack track;

    /**
     * Default constructor
     * @param context
     * @param attrs
     */
    public SearchTrackDisplayItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SearchTrackDisplayItem, 0, 0);
        String track = a.getString(R.styleable.SearchTrackDisplayItem_track_name);
        String artist = a.getString(R.styleable.SearchTrackDisplayItem_artist_name);
        String album = a.getString(R.styleable.SearchTrackDisplayItem_album_name);
        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.track_display_item, this, true);

        TextView track_view = (TextView)this.findViewById(R.id.track_name);
        track_view.setText(track);

        TextView artist_view = (TextView)this.findViewById(R.id.artist_name);
        artist_view.setText(artist);

        TextView album_view = (TextView)this.findViewById(R.id.album_name);
        album_view.setText(album);
    }

    /**
     * Constructor when building view manually
     * @param context
     * @param t - LocalTrack item with info to display
     */
    public SearchTrackDisplayItem(Context context, LocalTrack t) {
        super(context);

        this.track = t;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.track_display_item, this, true);

        TextView track_view = (TextView)this.findViewById(R.id.track_name);
        track_view.setText(track.getTrackName());

        TextView artist_view = (TextView)this.findViewById(R.id.artist_name);
        artist_view.setText(track.getTrackArtist());

        TextView album_view = (TextView)this.findViewById(R.id.album_name);
        album_view.setText(track.getTrackAlbum());
    }

    /**
     * Get name of track this view holds
     * @return - track name
     */
    public String getTrackName(){
        return ((TextView)this.findViewById(R.id.track_name)).getText().toString();
    }

    /**
     * Sets onClickListener for button embedded in this view
     * @param listener - listener to call
     */
    public void setBtnListener(OnClickListener listener){
        Button addBtn = (Button)this.findViewById(R.id.add_track_button);
        addBtn.setOnClickListener(listener);
    }

    /**
     * Get the LocalTrack this item stores
     * @return LocalTrack
     */
    public LocalTrack getTrack(){ return track;}

    /**
     * Sets the ImageView embedded in this view
     * @param map - Bitmap to set the ImageView to
     */
    public void setAlbumArt(Bitmap map){
        ImageView image = (ImageView)findViewById(R.id.album_art);
        image.setImageBitmap(map);
    }
}
