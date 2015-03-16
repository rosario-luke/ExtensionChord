package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {


    // TODO: Rename and change types of parameters
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void onSearchBtnClick(View v){

        LinearLayout myLayout = (LinearLayout)getView().findViewById(R.id.search_layout);

        EditText searchField = (EditText)getView().findViewById(R.id.searchField);
        String query = searchField.getText().toString();

        ArrayList<ParseTrack> l = new ArrayList<ParseTrack>();
        new SoundCloudSearch((RoomActivity)this.getActivity()).execute(query);

    }

    public void addTracks(ArrayList<ParseTrack> tList){

        ArrayList<TrackDisplayItem> viewList = new ArrayList<TrackDisplayItem>();
        if(tList != null) {
            for (ParseTrack t : tList) {
                TrackDisplayItem tempItem = new TrackDisplayItem(this.getActivity(), t);
                final ParseTrack tempTrack = t;
                tempItem.setBtnListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currRoom = ((RoomActivity)getActivity()).getRoomName();
                        RoomManager.addTrack(tempTrack, currRoom);
                        Toast.makeText(getActivity(), "Successfully Added ParseTrack " + tempTrack.getTrackID(), Toast.LENGTH_LONG).show();
                    }
                });
                viewList.add(tempItem);
            }
        }

        new TrackItemAdder(viewList, this).run();
    }

    public void addAlbumArt(Bitmap[] imageList){
        new AlbumArtAdder(imageList).run();
    }

    public class TrackItemAdder implements Runnable {
        private ArrayList<TrackDisplayItem> tList;
        private SearchFragment fragment;
        public TrackItemAdder(ArrayList<TrackDisplayItem> l, SearchFragment frag) {
            this.tList = l;
            this.fragment = frag;
        }

        public void run() {
            LinearLayout myLayout = (LinearLayout)fragment.getView().findViewById(R.id.track_list_layout);
            myLayout.removeAllViews();
            for(TrackDisplayItem v : tList){
                myLayout.addView(v);
            }
            fragment.fetchAlbumArt();
        }
    }

    public void fetchAlbumArt(){
        new SoundCloudArtFetcher(this).execute();
    }

    public class AlbumArtAdder implements Runnable {
        private Bitmap[] imageList;

        public AlbumArtAdder(Bitmap[] l) {
            this.imageList = l;

        }

        public void run() {
            LinearLayout myLayout = (LinearLayout)getView().findViewById(R.id.track_list_layout);
            for(int i = 0; i < imageList.length; i++){
                if(imageList[i] != null){
                    ((TrackDisplayItem) myLayout.getChildAt(i)).setAlbumArt(imageList[i]);
                }
            }

        }

    }
}
