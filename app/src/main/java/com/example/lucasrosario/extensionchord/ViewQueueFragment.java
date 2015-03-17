package com.example.lucasrosario.extensionchord;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucaspritz on 3/16/15.
 */
public class ViewQueueFragment extends Fragment {

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

    public void buildTrackList(){
        String roomName = ((RoomActivity)getActivity()).getRoomName();
        ParseRoom currRoom = RoomManager.getParseRoom(roomName);
        ParseMusicQueue currQueue = currRoom.getParseMusicQueue();

        addTracks(currQueue.getTrackList());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        buildTrackList();
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class TrackItemAdder implements Runnable {
        private ArrayList<ViewTrackDisplayItem> tList;
        private ViewQueueFragment fragment;
        public TrackItemAdder(ArrayList<ViewTrackDisplayItem> l, ViewQueueFragment frag) {
            this.tList = l;
            this.fragment = frag;
        }

        public void run() {
            LinearLayout myLayout = (LinearLayout)fragment.getView().findViewById(R.id.view_track_list_layout);
            myLayout.removeAllViews();
            for(ViewTrackDisplayItem v : tList){
                myLayout.addView(v);
            }
        }
    }

    public void addTracks(List<ParseTrack> tList){

        ArrayList<ViewTrackDisplayItem> viewList = new ArrayList<ViewTrackDisplayItem>();
        if(tList != null) {
            for (ParseTrack t : tList) {
                ViewTrackDisplayItem tempItem = new ViewTrackDisplayItem(this.getActivity(), t);
                viewList.add(tempItem);
            }
        }

        (new TrackItemAdder(viewList, this)).run();
    }

}
