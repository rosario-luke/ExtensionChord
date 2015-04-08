package com.example.lucasrosario.extensionchord;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class TrackInfoDialogFragment extends DialogFragment {
    public TrackInfoDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_info_dialog, container);
        getDialog().setTitle("Track Info");

        Bundle dialogBundle = getArguments();
        String submitter = dialogBundle.getString("submitter");
        String albumName = dialogBundle.getString("albumName");
        String artistName = dialogBundle.getString("artistName");
        String trackName = dialogBundle.getString("trackName");

        TextView submitterText = (TextView) view.findViewById(R.id.trackinfo_submitter);
        TextView albumNameText = (TextView) view.findViewById(R.id.trackinfo_albumname);
        TextView trackNameText = (TextView) view.findViewById(R.id.trackinfo_trackname);
        TextView artistNameText = (TextView) view.findViewById(R.id.trackinfo_artistname);

        trackNameText.setText("Track: " + trackName);
        artistNameText.setText("Artist: " + artistName);
        albumNameText.setText("Album: " + albumName);
        submitterText.setText("Submitter: " + submitter);

        return view;
    }
}
