package com.example.lucasrosario.extensionchord;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.io.InputStream;
import java.util.List;

/**
 * Created by lucas on 3/11/15.
 */


public class SoundCloudArtFetcher extends AsyncTask<Void, Void, Bitmap[]> {

    List<String> str;
    private SearchFragment fragment;

    public SoundCloudArtFetcher (SearchFragment activity){
        this.fragment =activity;
    }
    @Override
    protected Bitmap[] doInBackground(Void... params){
        LinearLayout myLayout = (LinearLayout) fragment.getView().findViewById(R.id.track_list_layout);

        Bitmap[] imageList = new Bitmap[myLayout.getChildCount()];
        for(int i = 0; i < myLayout.getChildCount(); i++){
            SearchTrackDisplayItem t = (SearchTrackDisplayItem)myLayout.getChildAt(i);
            if(t != null && t.getTrack().hasAlbumArt()){

                Bitmap image = getBitmapFromURL(t.getTrack().getAlbumArtUrl());
                imageList[i] = image;
            } else {
                imageList[i] = null;
            }
        }
        return imageList;

    }


    public Bitmap getBitmapFromURL(String url){
        Bitmap icon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            icon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            if(e.getMessage()!=null && !e.getMessage().isEmpty()) {
            }
        }
        return icon;
    }



    @Override
    protected void onPostExecute(Bitmap[] albumArt) {
        //super.onPostExecute(events);
        fragment.addAlbumArt(albumArt);
    }



}
