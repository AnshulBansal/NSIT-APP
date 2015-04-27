package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.anshul.nsitapp.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class Latest_events extends SherlockFragment implements
		OnItemClickListener {

	int t;
	SharedPreferences sp;
	FileOutputStream out;
	static ArrayAdapter<CustomEventItem> customAdapter;
	File filePath;
	Helper help;
	ArrayList<CustomEventItem> latesteventsfeed = new ArrayList<CustomEventItem>();
	PullToRefreshListView lvlatestevents;
	public static String nsitonlinefeedkey[][];
	boolean empty = false;

	public void onDestroy() {
		// TODO Auto-generated method stub
		int j = 0;
		for (j = 0; j < latesteventsfeed.size(); j++) {
			if (latesteventsfeed.get(j).getbitmap() != null) {
				latesteventsfeed.get(j).getbitmap().recycle();
			}
		}
		super.onDestroy();

	}

	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle saveBundle) {
		View view = inflator.inflate(R.layout.latest_events, container, false);
		lvlatestevents = (PullToRefreshListView) view
				.findViewById(R.id.lvlatestevents);
		help = new Helper(this.getActivity());
		setcontent();
		return view;

	}

	public static void filteradapter(String string) {
		if (customAdapter != null) {
			customAdapter.getFilter().filter(string);
			customAdapter.notifyDataSetChanged();
		}
	}

	private void setcontent() {
		// TODO Auto-generated method stub

		try {
			Getsaveddata("EventsFeed", latesteventsfeed);
			Collections.sort(latesteventsfeed,
					new Comparator<CustomEventItem>() {
						public int compare(CustomEventItem obj1,
								CustomEventItem obj2) {
							return obj1.getstartat().compareToIgnoreCase(
									obj2.getstartat());
						}
					});
			for (int t = 0; t < latesteventsfeed.size(); t++) {
				String temp = latesteventsfeed.get(t).getstartat();
				latesteventsfeed.get(t).setstartat(gettimestamp(latesteventsfeed.get(t).getstartat(),latesteventsfeed.get(t).getstopat()));
				latesteventsfeed.get(t).setstopat(temp);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Filteroutpassed();
		if (latesteventsfeed.isEmpty()) {
			CustomEventItem item = new CustomEventItem(null,
					"No Upcoming Events", "description", "link", "id", "",
					"stopat");
			latesteventsfeed.add(item);
			empty = true;
		}
		customAdapter = new CustomListvieweventsadapter(this.getActivity(),
				R.layout.list_item, latesteventsfeed);
		lvlatestevents.setAdapter(customAdapter);
		if (empty == true) {
			lvlatestevents.setClickable(false);
		} else {
			lvlatestevents.setClickable(true);
		}
		lvlatestevents.getRefreshableView().setOnItemClickListener(this);
		lvlatestevents.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.

				Log.d("aefs", "Refresh Called");
				if (help.haveNetworkConnection()) {
					Intent i = new Intent(Latest_events.this.getActivity(),
							UpdaterService.class);
					Latest_events.this.getActivity().startService(i);
					Toast.makeText(getActivity(), "Service Started",
							Toast.LENGTH_LONG).show();
					lvlatestevents.onRefreshComplete();
				} else {
					Toast.makeText(getActivity(),
							"Check Your Network Connection", Toast.LENGTH_LONG)
							.show();
					lvlatestevents.onRefreshComplete();
				}

			}
		});
	}

	private void Filteroutpassed() {
		// TODO Auto-generated method stub
		ArrayList<CustomEventItem> tempeventsfeed = new ArrayList<CustomEventItem>();
		int l = 0;
		for (l = 0; l < latesteventsfeed.size(); l++) {
			if (latesteventsfeed.get(l).getstartat() != "Passed"
					&& latesteventsfeed.get(l).getstartat() != " ") {
				tempeventsfeed.add(latesteventsfeed.get(l));
			}

		}

		latesteventsfeed = tempeventsfeed;
	}

	private String gettimestamp(String getstartat, String getstopat) {
		// TODO Auto-generated method stub
		String dtStart = getstartat;
		String dtstop = getstopat;

		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'+0530'");
		String date3 = " ";
		if (!(dtStart == null)) {
			try {
				Date date = format.parse(dtStart);

				if(!(date.after(Calendar.getInstance().getTime()))){
					return "Passed";
				}
				date3 = date.toString();

				date3 = date3.substring(4, 16);
				if (!(dtstop == null)) {
					Date date2 = format.parse(dtstop);
					String date4 = date2.toString();
					date4 = date4.substring(4, 16);

					date3 = date3 + "-" + date4;
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d(date3, date3);
		System.out.println(date3);
		System.out.println(Calendar.getInstance().getTime());
		return date3;

	}

	public void Getsaveddata(String String,
			ArrayList<CustomEventItem> latesteventsfeed2)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		latesteventsfeed2.clear();
		Bitmap bm;
		Context activitycontext = this.getActivity();
		sp = activitycontext.getSharedPreferences(String, Context.MODE_PRIVATE);
		int t = sp.getInt("size", 0);
		if (sp.getBoolean("datawassaved", false) == true) {

			String[][] feedkey = new String[t][7];
			for (int j = 0; j < t; j++) {
				for (int k = 0; k < 7; k++) {

					feedkey[j][k] = String + j + "-" + k;
				}

			}
			int j = 0;

			for (j = 0; j < t; j++) {

				String filestring = feedkey[j][0] + ".png";
				if (null == activitycontext) {
					Log.d("The activity context in null",
							activitycontext.toString());
				}

				filePath = activitycontext.getFileStreamPath(filestring);
				FileInputStream fis = new FileInputStream(filePath);
				bm = BitmapFactory.decodeStream(fis);

				CustomEventItem item = new CustomEventItem(bm, sp.getString(
						feedkey[j][1], null),
						sp.getString(feedkey[j][2], null), sp.getString(
								feedkey[j][3], null), sp.getString(
								feedkey[j][4], null), sp.getString(
								feedkey[j][5], null), sp.getString(
								feedkey[j][6], null));
				latesteventsfeed2.add(item);
			}
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int posititon,
			long arg3) {
		// TODO Auto-generated method stub

		if (empty != true) {
			CustomEventItem item = (CustomEventItem) customAdapter
					.getItem(posititon - 1);
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

			i.putExtra("startat", item.getstartat());
			i.putExtra("stopat", item.getstopat());
			i.putExtra("position", posititon);
			i.putExtra("name", item.getname());
			i.putExtra("link", item.getlink());
			i.putExtra("description", item.getdescription());

			startActivity(i);
		}

	}

}