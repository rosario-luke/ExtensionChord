package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.parse.ParseUser;
import com.parse.ParseException;

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
        String roomName = ((RoomActivity)getActivity()).getRoomName();
        ParseRoom currRoom;
        ParseMusicQueue currQueue;
        try{
            // TODO: Change this to be asynchronous
            currRoom = RoomManager.getParseRoom(roomName).fetchIfNeeded();
            currQueue = currRoom.getParseMusicQueue().fetchIfNeeded();
        } catch(ParseException e){
            Toast.makeText(getActivity(), "Error occured while fetching room info", Toast.LENGTH_SHORT);
            return;
        }


        addTracks(currQueue.getTrackList());
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
        }
    }

    public void addTracks(List<ParseTrack> tList){
        ArrayList<ViewTrackDisplayItem> viewList = new ArrayList<ViewTrackDisplayItem>();
        CurrentSongDisplayItem currentSongItem = null;

        if(!tList.isEmpty()) {
            int tListSize = tList.size();
            currentSongItem = new CurrentSongDisplayItem(this.getActivity(), tList.get(0));

            String currURL = "http://api.soundcloud.com/tracks/" + tList.get(0).getTrackID() + "/stream?client_id=3fe96f34e369ae1ef5cf7e8fcc6c8eec";

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

            ((RoomActivity)getActivity()).setCurrentMediaPlayerURL(currURL);

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
        menu.add(0, v.getId(), 0, "Track Info");
        menu.add(0, v.getId(), 0, "Delete Track");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle()=="Track Info"){
               //Toast.makeText(getActivity(), "Clicked Track Info", Toast.LENGTH_SHORT).show();
            ViewTrackDisplayItem.TrackDisplayContextMenu c = (ViewTrackDisplayItem.TrackDisplayContextMenu)item.getMenuInfo();
            if(c != null){
                Toast.makeText(getActivity(), c.trackDisplayItem.getSubmitter(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Was Null", Toast.LENGTH_SHORT).show();

            }

        }
        else if(item.getTitle()=="Delete Track"){
            Toast.makeText(getActivity(), "Clicked Delete Track", Toast.LENGTH_SHORT).show();

        }

        else
        {
            return false;
        }
        return true;
    }

}
