package com.example.lucasrosario.extensionchord.custom_views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lucasrosario.extensionchord.R;

/**
 * Created by lucas on 2/19/15.
 * This class is a custom view. It is a combination of a rounded button on top on a label.
 * This class allows for the insertion of a custom OnClickListener for the button
 */
public class RoomListItemView extends LinearLayout{

    /**
     * Constructor used when built from XML
     * @param context - Context to insert view into
     * @param attrs - Custom attributes to set the label text
     */
    public RoomListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.RoomListOptions, 0, 0);
        String titleText = a.getString(R.styleable.RoomListOptions_room_name);

        a.recycle();

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.room_list_item, this, true);

        TextView title = (TextView) getChildAt(1);
        title.setText(titleText);



    }

    /**
     * Constructor called when constructing view manually
     * @param context  - Current context that view is inserted into
     * @param roomName - Name for label
     */
    public RoomListItemView(Context context, String roomName) {
        super(context);



        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.room_list_item, this, true);

        TextView title = (TextView) getChildAt(1);
        title.setText(roomName);



    }

    /*
        Sets OnClickListener for plus button. Allows to set up
     */
    public void setButtonListener(Button.OnClickListener listener) {
        Button myButton = (Button) getChildAt(0);
        myButton.setOnClickListener(listener);
    }

    /**
     * Sets if the rounded button is clickable
     * @param isClickable - boolean to set clickable to
     */
    public void setButtonClickable(Boolean isClickable) {
        Button myButton = (Button) getChildAt(0);
        myButton.setClickable(isClickable);
    }


}
