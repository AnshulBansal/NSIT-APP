package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.anshul.nsitapp.R;
import com.example.nsitapp.Society_auto.read;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class Latest_announcements extends SherlockFragment implements
		OnItemClickListener {

	int t;
	SharedPreferences sp;
	FileOutputStream out;
	static ArrayAdapter<RowItem> customAdapter;
	File filePath;
	Helper help;
	ArrayList<RowItem> latestanouncementfeed = new ArrayList<RowItem>();
	PullToRefreshListView lvlatestannouncemnts;
	public static String nsitonlinefeedkey[][];

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		int j=0;
		for(j=0;j<latestanouncementfeed.size();j++){
			if(latestanouncementfeed.get(j).getbitmap()!=null){
			latestanouncementfeed.get(j).getbitmap().recycle();}
		}
		super.onDestroy();
		
	}
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle saveBundle) {
		View view = inflator.inflate(R.layout.latest_announcements, container,false);
		lvlatestannouncemnts = (PullToRefreshListView) view
				.findViewById(R.id.lvlatestannouncements);
		help = new Helper(this.getActivity());
		setcontent();
		return view;

	}
	public static void filteradapter(String string){
		if(customAdapter !=null){
			customAdapter.getFilter().filter(string);
			customAdapter.notifyDataSetChanged();
			}
	}

	private void setcontent() {
		// TODO Auto-generated method stub

		try {
			help.Getsaveddata("nsitonlinedata",latestanouncementfeed,0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customAdapter = new CustomListViewAdapter(this.getActivity(),
				R.layout.list_item, latestanouncementfeed);

		lvlatestannouncemnts.setAdapter(customAdapter);
		lvlatestannouncemnts.getRefreshableView().setOnItemClickListener(this);
		lvlatestannouncemnts.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.

				Log.d("aefs", "Refresh Called");
if(help.haveNetworkConnection()){
	Intent i = new Intent(Latest_announcements.this.getActivity()  , UpdaterService.class);
	Latest_announcements.this.getActivity().startService(i);
	Toast.makeText(getActivity(), "Service Started", Toast.LENGTH_LONG).show();
	lvlatestannouncemnts.onRefreshComplete();
	
}else{
	Toast.makeText(getActivity(), "Check Your Network Connection", Toast.LENGTH_LONG).show();
	lvlatestannouncemnts.onRefreshComplete();
}
				
			}
		});
		}



	public void onItemClick(AdapterView<?> parent, View view, int posititon,
			long arg3) {
		// TODO Auto-generated method stub

		RowItem item = (RowItem) customAdapter.getItem(posititon - 1);
		try {
			out = this.getActivity().openFileOutput("image.png",
					Context.MODE_PRIVATE);
			item.getbitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent i = new Intent("android.intent.action.item");
		i.putExtra("position", posititon);
		i.putExtra("name", item.getname());
		i.putExtra("link", item.getlink());
		i.putExtra("description", item.getdescription());

		startActivity(i);

	}

}
