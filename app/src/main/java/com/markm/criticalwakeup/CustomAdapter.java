package com.markm.criticalwakeup;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private ArrayList<Button> myButtons;
    private int mColumnWidth, mColumnHeight;

    public CustomAdapter(ArrayList<Button> buttons, int columnWidth, int columnHeight){
        myButtons = buttons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    @Override
    public int getCount() {
        return myButtons.size();
    }

    @Override
    public Object getItem(int position) {
        return (Object) myButtons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if(convertView == null){
            button = myButtons.get(position);
        }
        else{
            button = (Button) convertView;
        }

        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);

        button.setLayoutParams(params);

        return button;
    }
}
