package com.example.lucasrosario.extensionchord;

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

/**
 * Created by lucas on 2/19/15.
 */
public class RoomListItemView extends LinearLayout{

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
    public void setButtonListener(OnClickListener listener){
        Button myButton = (Button) getChildAt(0);
        myButton.setOnClickListener(listener);
    }

    public void setButtonClickable(Boolean isClickable){
        Button myButton = (Button) getChildAt(0);
        myButton.setClickable(isClickable);
    }


}
