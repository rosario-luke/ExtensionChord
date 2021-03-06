package com.example.lucasrosario.extensionchord.room_fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lucasrosario.extensionchord.R;
import com.example.lucasrosario.extensionchord.RoomManager;
import com.example.lucasrosario.extensionchord.parse_objects.RoomUser;
import com.example.lucasrosario.extensionchord.parse_objects.ParseRoom;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewRoomUsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewRoomUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewRoomUsersFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String roomName;
    private View rootView;

    String[] values;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewRoomUsersFragment.
     */
    public static ViewRoomUsersFragment newInstance() {
        ViewRoomUsersFragment fragment = new ViewRoomUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewRoomUsersFragment() {
    }

    /*
     * oncreate function called when fragment is opened,
     * sets the roomname and mParams
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            Bundle b = getArguments();
            roomName = b.getString("room_name");
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_room_users, container, false);
        viewRoomList();
        return rootView;
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

    private void viewRoomList() {
        List<String> users = new ArrayList<String>();
        ParseRoom currRoom = RoomManager.getParseRoom(roomName);
        boolean currUserAdmin;
        try {
            currUserAdmin = (currRoom.getCreator().getUsername().equals(ParseUser.getCurrentUser().getUsername()));
        }
        catch(IllegalStateException e)
        {
            currUserAdmin = false;
        }
        for(RoomUser user: currRoom.getRoomUsers()) {
            users.add(user.getUsername());
            if(currUserAdmin == false && user.getUsername().equals(ParseUser.getCurrentUser().getUsername())) {
                currUserAdmin = user.isAdmin();
            }
        }

        values = users.toArray(new String[users.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.generic_list_item, values);
        ListView lv = (ListView) rootView.findViewById(R.id.roomUsers);

        lv.setAdapter(adapter);

        if(currUserAdmin) {
            registerForContextMenu(lv);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("User Actions");
        menu.add(0, v.getId(), 0, "Promote User");
        menu.add(0, v.getId(), 0, "Boot User");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if(item.getTitle()=="Promote User"){
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            String userName =  values[info.position];
            for(RoomUser user: RoomManager.getParseRoom(roomName).getRoomUsers()) {
                try {
                    user.fetchIfNeeded();
                } catch(ParseException e){
                    continue;
                }
                if(user.getUsername().equals(userName))
                {

                    user.setAdmin(true);
                    Toast.makeText(getActivity(), "Promoted User " + user.getUsername(), Toast.LENGTH_SHORT).show();
                    try {
                        user.save();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        else if(item.getTitle()=="Boot User"){
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            String userName =  values[info.position];

            if(ParseUser.getCurrentUser().getUsername().equals(userName)){
                Toast.makeText(getActivity(), "Cannot Boot Yourself", Toast.LENGTH_SHORT).show();
                return true;
            }
            RoomManager.removeUserFromRoom(userName);
            //kill fragment, go back to main page if boot yourself
            //need to delete room if no users left
        }
        else
        {
            return false;
        }
        return true;
    }
}
