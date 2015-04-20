package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucaspritz on 3/16/15.
 */
public class ViewQueueFragment extends Fragment {


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewQueueFragment newInstance(String param1, String param2) {
        ViewQueueFragment fragment = new ViewQueueFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public ViewQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    public void refresh(){
        buildTrackList();
    }

    public void onRefreshClick(View v){
        refresh();
    }

    public void buildTrackList(){
        ((RoomActivity) getActivity()).showProgressDialog();
        String roomName = ((RoomActivity)getActivity()).getRoomName();

            RoomManager.getParseRoom(roomName).fetchIfNeededInBackground(new GetCallback<ParseRoom>() {
                public void done(ParseRoom room, ParseException e) {
                    if (e == null) {

                        room.getParseMusicQueue().fetchIfNeededInBackground(new GetCallback<ParseMusicQueue>() {
                            public void done(ParseMusicQueue queue, ParseException e) {
                                if (e == null) {

                                    addTracks(queue);
                                } else {
                                    ((RoomActivity) getActivity()).dismissProgressDialog();
                                    Toast.makeText(getActivity(), "Error occured while fetching room info", Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    } else {
                        ((RoomActivity) getActivity()).dismissProgressDialog();
                        Toast.makeText(getActivity(), "Error occured while fetching room info", Toast.LENGTH_SHORT);
                    }
                }
            });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_queue, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
       /* try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

    public class TrackItemAdder implements Runnable {
        private ArrayList<ViewTrackDisplayItem> tList;
        private ViewQueueFragment fragment;
        private CurrentSongDisplayItem currentSongItem;
        public TrackItemAdder(CurrentSongDisplayItem currentSongItem, ArrayList<ViewTrackDisplayItem> l, ViewQueueFragment frag) {
            this.currentSongItem = currentSongItem;
            this.tList = l;
            this.fragment = frag;
        }

        public void run() {
            LinearLayout viewCurrentSongLayout = (LinearLayout) fragment.getView().findViewById(R.id.current_song);
            viewCurrentSongLayout.removeAllViews();

            if(currentSongItem != null) {
                String roomName = ((RoomActivity)getActivity()).getRoomName();
                ParseRoom currRoom = RoomManager.getParseRoom(roomName);
                String creatorUserName = "";
                try{
                    creatorUserName = currRoom.getCreator().fetchIfNeeded().getUsername();
                } catch (ParseException e ){
                    ((RoomActivity) getActivity()).dismissProgressDialog();
                    return;
                }

                //If I am NOT the creator, Hide the buttons
                if(!creatorUserName.equals(ParseUser.getCurrentUser().getUsername())){
                    currentSongItem.hideButtons();
                }

                viewCurrentSongLayout.addView(currentSongItem);
            }

            LinearLayout viewTrackLayout = (LinearLayout)fragment.getView().findViewById(R.id.view_track_list_layout);
            viewTrackLayout.removeAllViews();
            for(ViewTrackDisplayItem v : tList){
                viewTrackLayout.addView(v);
                registerForContextMenu(v);
            }
            ((RoomActivity) getActivity()).dismissProgressDialog();

        }
    }

    public void addTracks(ParseMusicQueue currQueue){
        ArrayList<ViewTrackDisplayItem> viewList = new ArrayList<ViewTrackDisplayItem>();
        CurrentSongDisplayItem currentSongItem = null;

        if(currQueue.getTrackList() == null || currQueue.getTrackList().isEmpty()){
            Toast.makeText(getActivity(), "Music Queue is Currently Empty", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseTrack currSong = currQueue.getTrackList().get(0);

        // Add the current song to the Fragment as a CurrentSongDisplayItem
        if(currSong != null) {
            currentSongItem = new CurrentSongDisplayItem(this.getActivity(), currSong);

            String currURL = "http://api.soundcloud.com/tracks/" + currSong.getTrackID() + "/stream?client_id="+Constants.API_KEY;

            currentSongItem.setPlayListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    ((RoomActivity)getActivity()).startMediaPlayer();
                }
            });

            currentSongItem.setPauseListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    ((RoomActivity)getActivity()).stopMediaPlayer();
                }
            });


            ((RoomActivity) getActivity()).setCurrentMediaPlayerURL(currURL);


            // Add the rest of the music queue to the Fragment as ViewTrackDisplayItems
            List<ParseTrack> tList = currQueue.getTrackList();
            int tListSize = tList.size();
            for (int count = 1; count < tListSize; count++) {
                ViewTrackDisplayItem tempItem = new ViewTrackDisplayItem(this.getActivity(), tList.get(count), count);
                viewList.add(tempItem);
            }
        }

        (new TrackItemAdder(currentSongItem, viewList, this)).run();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Track Options");
        menu.add(0, v.getId(), 0, "Vote to Skip");
        menu.add(0, v.getId(), 0, "Track Info");
        menu.add(0, v.getId(), 0, "Delete Track");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CharSequence itemTitle = item.getTitle();
        if (itemTitle == "Track Info") {
               //Toast.makeText(getActivity(), "Clicked Track Info", Toast.LENGTH_SHORT).show();
            ViewTrackDisplayItem.TrackDisplayContextMenu c = (ViewTrackDisplayItem.TrackDisplayContextMenu)item.getMenuInfo();
            if (c != null) {
                ParseTrack track = c.trackDisplayItem.getTrack();

                String trackName = track.getTrackName();
                String albumName = track.getTrackAlbum();
                String artistName = track.getTrackArtist();
                String submitter = track.getSubmitter();

                ((RoomActivity) getActivity()).showEditDialog(trackName, albumName, artistName, submitter);
            } else {
                Toast.makeText(getActivity(), "Was Null", Toast.LENGTH_SHORT).show();
            }
        } else if (itemTitle == "Delete Track") {
            //Toast.makeText(getActivity(), "Clicked Delete Track", Toast.LENGTH_SHORT).show();
            ViewTrackDisplayItem.TrackDisplayContextMenu c = (ViewTrackDisplayItem.TrackDisplayContextMenu)item.getMenuInfo();
            ParseTrack toDelete = c.trackDisplayItem.getTrack();

            String roomName = ((RoomActivity)getActivity()).getRoomName();
            RoomManager.deleteTrack(toDelete, roomName, false);
        } else if (itemTitle == "Vote to Skip") {
            ViewTrackDisplayItem.TrackDisplayContextMenu c = (ViewTrackDisplayItem.TrackDisplayContextMenu)item.getMenuInfo();
            ParseTrack toVote = c.trackDisplayItem.getTrack();
        } else {
            return false;
        }
        return true;
    }

}
