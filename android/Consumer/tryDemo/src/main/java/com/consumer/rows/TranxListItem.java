package com.consumer.rows;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public interface TranxListItem {
	public enum ListItemType {
		ROW, Header, Footer
	}
	public int getViewType();
    public View getView(LayoutInflater inflater, View convertView, ViewGroup parent);
    
}


