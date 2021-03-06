package com.example.lucasrosario.extensionchord.room_fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.LocalTrack;
import com.example.lucasrosario.extensionchord.OnSearchTaskCompleted;
import com.example.lucasrosario.extensionchord.R;
import com.example.lucasrosario.extensionchord.RoomActivity;
import com.example.lucasrosario.extensionchord.RoomManager;
import com.example.lucasrosario.extensionchord.custom_views.SearchTrackDisplayItem;
import com.example.lucasrosario.extensionchord.utility.SoundCloudArtFetcher;
import com.example.lucasrosario.extensionchord.utility.SoundCloudSearch;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements OnSearchTaskCompleted {


    private String mParam1;
    private String mParam2;
    private SoundCloudSearch soundCloudSearch;
    private SoundCloudArtFetcher soundCloudArtFetcher;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Required empty public constructor
     */
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    /**
     * Grabs the text in V and starts a SoundCloud query using the input
     * @param v View that was clicked
     * @param testFlag Flag for testing
     */
    public void onSearchBtnClick(View v, boolean testFlag){

        LinearLayout myLayout = (LinearLayout)getView().findViewById(R.id.search_layout);

        EditText searchField = (EditText)getView().findViewById(R.id.searchField);
        String query = searchField.getText().toString();

        ArrayList<LocalTrack> l = new ArrayList<LocalTrack>();
        if(soundCloudSearch == null) {
            if (!testFlag) {
                //new SoundCloudSearch((RoomActivity) this.getActivity()).execute(query);
                soundCloudSearch = new SoundCloudSearch(this);
                soundCloudSearch.execute(query);
            } else {
                //new SoundCloudSearch((RoomActivity) this.getActivity()).execute("Kanye");
                soundCloudSearch = new SoundCloudSearch(this);
                soundCloudSearch.execute(query);
            }
        }

    }

    /**
     * Creates a new AlbumArtAdder thread and runs it
     * @param imageList
     */
    public void addAlbumArt(Bitmap[] imageList){
        new AlbumArtAdder(imageList).run();
        soundCloudArtFetcher = null;
    }

    /**
     * Method called when a SearchTask is completed. Calls addTracks
     * @param c - ArrayList of LocalTrack objects returned by SoundcloudSearch
     */
    public void onTaskCompleted(Object c){

        ArrayList<LocalTrack> tList = (ArrayList<LocalTrack>)(c);
        addTracks(tList);

        soundCloudSearch = null;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(soundCloudSearch != null){
            soundCloudSearch.cancel(true);
        }
        if(soundCloudArtFetcher != null){
            soundCloudArtFetcher.cancel(true);
        }
    }

    /**
     * Takes in a list of tracks and generates SearchTrackDisplayItems for each
     * Starts a new TrackItemAdder with this generated views
     * @param tList - ArrayList of LocalTracks to add
     */
    public void addTracks(ArrayList<LocalTrack> tList){

        ArrayList<SearchTrackDisplayItem> viewList = new ArrayList<SearchTrackDisplayItem>();
        if(tList != null) {
            for (LocalTrack t : tList) {
                SearchTrackDisplayItem tempItem = new SearchTrackDisplayItem(this.getActivity(), t);
                final LocalTrack tempTrack = t;
                tempItem.setBtnListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currRoom = ((RoomActivity)getActivity()).getRoomName();
                        RoomManager.addTrack(tempTrack, currRoom);
                    }
                });
                viewList.add(tempItem);
            }
        }

        new TrackItemAdder(viewList, this).run();
    }

    /**
     * Helper class that adds items to the view not in the UI thread
     */
    public class TrackItemAdder implements Runnable {
        private ArrayList<SearchTrackDisplayItem> tList;
        private SearchFragment fragment;

        /**
         * Constructor for TrackItemAdder
         * @param l Views to add
         * @param frag Fragment to add views to
         */
        public TrackItemAdder(ArrayList<SearchTrackDisplayItem> l, SearchFragment frag) {
            this.tList = l;
            this.fragment = frag;
        }

        public void run() {
            LinearLayout myLayout = (LinearLayout)fragment.getView().findViewById(R.id.track_list_layout);
            myLayout.removeAllViews();
            for(SearchTrackDisplayItem v : tList){
                myLayout.addView(v);
            }
            ((RoomActivity) getActivity()).dismissProgressDialog();
            fragment.fetchAlbumArt();
        }
    }

    /**
     * Creates a new SoundCloudArtFetcher if one is not currently running
     * Starts SoundCloudArtFetcher
     */
    public void fetchAlbumArt(){
        if(soundCloudArtFetcher == null) {
            soundCloudArtFetcher = new SoundCloudArtFetcher(this);
            soundCloudArtFetcher.execute();
        }
    }

    /**
     * Helper class to add AlbumArt to the UI
     */
    public class AlbumArtAdder implements Runnable {
        private Bitmap[] imageList;

        public AlbumArtAdder(Bitmap[] l) {
            this.imageList = l;

        }

        public void run() {
            LinearLayout myLayout = (LinearLayout)getView().findViewById(R.id.track_list_layout);
            for(int i = 0; i < imageList.length; i++){
                if(imageList[i] != null){
                    ((SearchTrackDisplayItem) myLayout.getChildAt(i)).setAlbumArt(imageList[i]);
                }
            }

        }

    }
}
