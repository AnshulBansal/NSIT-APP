package com.example.nsitapp;

import java.util.List;

import com.anshul.nsitapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class CustomListViewVideoAdapter extends ArrayAdapter<Video> {

	Context Context;

	public CustomListViewVideoAdapter(Context context, int ResourceId,
			List<Video> objects) {
		super(context, ResourceId, objects);
		this.Context = context;

	}

	private class ViewHolder {

		ImageView imageview;
		TextView textname;
		TextView desc;

	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		Video rowitem = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) Context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_video, null);
			holder = new ViewHolder();

			holder.textname = (TextView) convertView.findViewById(R.id.title);
			holder.imageview = (ImageView) convertView.findViewById(R.id.icon);
			holder.desc = (TextView) convertView.findViewById(R.id.tvdesc);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		holder.textname.setText(rowitem.gettitle());
		Typeface typeFace=Typeface.createFromAsset(Context.getAssets(),"robotolight.ttf");
	
		holder.textname.setTypeface(typeFace);
		holder.imageview.setImageBitmap(rowitem.getbitmap());
		holder.desc.setText(rowitem.getdesc());
		holder.imageview.setScaleType(ScaleType.CENTER_CROP);
		holder.imageview.setCropToPadding(true);

		// convertView.setBackgroundColor(Color.WHITE);

		return convertView;

	}

}
