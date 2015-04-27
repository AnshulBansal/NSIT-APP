package com.example.nsitapp;

import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.anshul.nsitapp.R;

public class CustomListvieweventsadapter extends ArrayAdapter<CustomEventItem>{

	
	Context Context;
	public CustomListvieweventsadapter(Context context, int ResourceId,
			List<CustomEventItem> objects) {
		super(context, ResourceId, objects);
		this.Context = context;
	
	}

private class ViewHolder{
	
	ImageView imageview;
	TextView textname;
	TextView texttime;
	
	
}
	

public View getView(int position, View convertView, ViewGroup parent){
	
	ViewHolder holder;
	CustomEventItem rowitem = getItem(position);
	LayoutInflater mInflater = (LayoutInflater) Context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	
	if (convertView == null) {
		convertView = mInflater.inflate(R.layout.list_item, null);
		holder = new ViewHolder();
		
		holder.textname = (TextView) convertView.findViewById(R.id.title);
		holder.imageview = (ImageView) convertView.findViewById(R.id.icon);
		holder.texttime = (TextView) convertView.findViewById(R.id.tvtimestamp);
		convertView.setTag(holder);
	} else
		holder = (ViewHolder) convertView.getTag();
	
	
	
	holder.textname.setText(rowitem.getname());
	holder.imageview.setImageBitmap(rowitem.getbitmap());
	holder.texttime.setText(rowitem.getstartat());
	
	
	
	
	
	return convertView;
	
	
}
}
