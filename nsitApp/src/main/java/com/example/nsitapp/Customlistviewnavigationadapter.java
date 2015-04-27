package com.example.nsitapp;


import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.anshul.nsitapp.R;

public class Customlistviewnavigationadapter extends ArrayAdapter<RowItem>{

	
	Context Context;
	public Customlistviewnavigationadapter(Context context, int ResourceId,
			List<RowItem> objects) {
		super(context, ResourceId, objects);
		this.Context = context;
	
	}
private class ViewHolder{
	
	ImageView imageview;
	TextView textname;
	
}
	

public View getView(int position, View convertView, ViewGroup parent){
	
	ViewHolder holder;
	RowItem rowitem = getItem(position);
	LayoutInflater mInflater = (LayoutInflater) Context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	
	if (convertView == null) {
		convertView = mInflater.inflate(R.layout.list_item_navigation, null);
		holder = new ViewHolder();
		
		holder.textname = (TextView) convertView.findViewById(R.id.titlenavig);
		holder.imageview = (ImageView) convertView.findViewById(R.id.iconnavig);
		convertView.setTag(holder);
	} else
		holder = (ViewHolder) convertView.getTag();
	
	
	
	holder.textname.setText(rowitem.getname());
	holder.imageview.setImageBitmap(rowitem.getbitmap());
	
	
	switch(position){
	
	case 0:
		holder.imageview.setBackgroundColor(Color.parseColor("#86C031"));
		holder.textname.setBackgroundColor(Color.parseColor("#86C031"));
		break;

	case 1:
		

		holder.imageview.setBackgroundColor(Color.parseColor("#594747"));
		holder.textname.setBackgroundColor(Color.parseColor("#594747"));
		break;

	case 2:

		holder.imageview.setBackgroundColor(Color.parseColor("#628179"));
		holder.textname.setBackgroundColor(Color.parseColor("#628179"));
		break;

	case 3:

		holder.imageview.setBackgroundColor(Color.parseColor("#21AABD"));
		holder.textname.setBackgroundColor(Color.parseColor("#21AABD"));
		break;

	case 4:
		holder.imageview.setBackgroundColor(Color.parseColor("#654B6B"));
		holder.textname.setBackgroundColor(Color.parseColor("#654B6B"));
		break;
	case 5 :
		holder.imageview.setBackgroundColor(Color.parseColor("#BD2A4E"));
		holder.textname.setBackgroundColor(Color.parseColor("#BD2A4E"));
		break;
	case 6 :
		holder.imageview.setBackgroundColor(Color.parseColor("#FE9D01"));
		holder.textname.setBackgroundColor(Color.parseColor("#FE9D01"));
		break;
	case 7:
		holder.imageview.setBackgroundColor(Color.parseColor("#BD2A4E"));
		holder.textname.setBackgroundColor(Color.parseColor("#BD2A4E"));
		break;
	case 8:
		holder.imageview.setBackgroundColor(Color.parseColor("#628179"));
		holder.textname.setBackgroundColor(Color.parseColor("#628179"));
		break;
	case 9 :
		holder.imageview.setBackgroundColor(Color.parseColor("#21AABD"));
		holder.textname.setBackgroundColor(Color.parseColor("#21AABD"));
		break;
	}
	Typeface tf =  Typeface.createFromAsset(getContext().getAssets(), "4863.ttf");
	holder.textname.setTypeface(tf);
	
	return convertView;
	
	
}
	
	
	
	
	
	
	
	
	
	
	
}
