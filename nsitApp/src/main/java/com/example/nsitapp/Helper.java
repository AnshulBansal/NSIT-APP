package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.PreviewCallback;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;

import com.anshul.nsitapp.R;

public class Helper extends SherlockActivity {
	static List<String> namelist = new ArrayList<String>();
	static List<String> Descriptionlist = new ArrayList<String>();
	static List<String> Linklist = new ArrayList<String>();
	static List<Bitmap> Bitmaplist = new ArrayList<Bitmap>();
	static List<String> Picturelist = new ArrayList<String>();
	static List<String> IDlist = new ArrayList<String>();
	static List<String> Messagelist = new ArrayList<String>();
	static List<String> Captionlist = new ArrayList<String>();
	static int posttype;
	private Context activitycontext;
	FileInputStream fis;
	private FileOutputStream out;
	static int i;
	private SharedPreferences sp;
	static Editor editor;
	File filePath;
	JSONArray jsonArray;
	ArrayList<Video> Videolist = new ArrayList<Video>();

	static HttpClient client = new DefaultHttpClient();
	final static String access_token = "CAACTzPZAxblQBAE6tYaEVZBjOIl0A3AZAn0qEWj5oqa08MXNCsjE3BInto2G07sKrcCdxTAMZAZCqHYn2oRjwk0wPfZC5C8U4On1E2OoEZCUkU7XLmSzo5aLuGwhjuisrrUcyna7rrCqsiROioS1KpR9NoDZApZAdoFICtWuLVpCYRMR9Fmw4xyHh";
	final static String URL = "https://graph.facebook.com/";

	public Helper(Context context) {

		activitycontext = context;

	}

	public Helper() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Video> GetFeedFromYoutubeChannel(String ChannelId)
			throws ClientProtocolException, IOException, JSONException {

		String URL = "http://gdata.youtube.com/feeds/api/users/" + ChannelId
				+ "/uploads?v=2&alt=jsonc";
		client = new DefaultHttpClient();
		HttpGet get = new HttpGet(URL);
		HttpResponse r;

		r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) {
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject timeline = new JSONObject(data);
			timeline = timeline.getJSONObject("data");
			jsonArray = timeline.getJSONArray("items");
		}
		JSONObject js = null;
		ArrayList<Video> previousvideofeed = new ArrayList<Video>();
		GetsavedVideodata("VideoData", previousvideofeed, 0);

		for (int i = 0; i < jsonArray.length(); i++) {
			js = jsonArray.getJSONObject(i);

			Video v = new Video(js.optString("id", null), js.optString("title",
					null), js.optString("desc", null),
					BitmapFactory.decodeStream((InputStream) new java.net.URL(
							(String) js.getJSONObject("thumbnail").get(
									"hqDefault")).getContent()));
			Videolist.add(v);

		
		if (Videolist.isEmpty() == false
				&& previousvideofeed.isEmpty() == false) {
			Log.d("staticfeed", Videolist.get(Videolist.size()-1).getID());
			Log.d("tempdownfeed", previousvideofeed.get(0).getID());

			if (Videolist.get(Videolist.size()-1).getID()
					.equals(previousvideofeed.get(0).getID())) {
			Videolist.remove(Videolist.size()-1);
				//	 tempdownfeed.remove(tempdownfeed.size() - 1);
				// Log.d("Comparator", "Loop Entered");
				i = jsonArray.length();

				Videolist.addAll(previousvideofeed);

			}
		}
		}
		return Videolist;

	}

	public void GetsavedVideodata(String String, List<Video> rettempfeed,
			int feedsize) throws FileNotFoundException {
		// TODO Auto-generated method stub
		rettempfeed.clear();
		Bitmap bm;
		sp = activitycontext.getSharedPreferences(String, Context.MODE_PRIVATE);
		int t = sp.getInt("size", 0);
		if (feedsize != 0 && t > feedsize) {
			t = feedsize;
		}
		if (sp.getBoolean("datawassaved", false) == true) {

			String[][] feedkey = new String[t][7];
			for (int j = 0; j < t; j++) {
				for (int k = 0; k < 6; k++) {

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

				Video item = new Video(sp.getString(feedkey[j][3], null),
						sp.getString(feedkey[j][1], null), sp.getString(
								feedkey[j][2], null), bm);
				rettempfeed.add(item);
			}
		}
	}

	public void SaveVideoData(String string, List<Video> tempdownfeed)
			throws IOException {
		// TODO Auto-generated method stub

		SharedPreferences sp = activitycontext.getSharedPreferences(string,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		int t = tempdownfeed.size();
		String[][] feedkey = new String[t][7];
		for (int j = 0; j < t; j++) {
			for (int k = 0; k < 6; k++) {

				feedkey[j][k] = string + j + "-" + k;
			}

		}

		editor.clear();

		int j = 0;

		for (j = 0; j < t; j++) {
			try {
				out = activitycontext.openFileOutput(feedkey[j][0] + ".png",
						Context.MODE_PRIVATE);
				tempdownfeed.get(j).getbitmap()
						.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				editor.putString(feedkey[j][1], tempdownfeed.get(j).gettitle());
				editor.putString(feedkey[j][2], tempdownfeed.get(j).getdesc());
				editor.putString(feedkey[j][3], tempdownfeed.get(j).getID());

			} catch (NullPointerException r1) {
				r1.printStackTrace();
				tempdownfeed.remove(j);
				t = tempdownfeed.size();
			} catch (IllegalStateException li) {
				li.printStackTrace();
				tempdownfeed.remove(j);
				t = tempdownfeed.size();
			}
		}
		editor.putInt("size", t);
		editor.putBoolean("datawassaved", true);
		editor.commit();
	}

	public static JSONArray getjsonarrayfromfacebookid(String params, boolean b)
			throws ClientProtocolException, IOException, JSONException {
		// TODO Auto-generated method stub
		JSONArray jsonArray;

		StringBuilder url = new StringBuilder(URL);
		url.append(params);
		if (b == true) {

			url.append("/posts?access_token=");
		} else {
			url.append("/feed?access_token=");
		}
		url.append(access_token);

		HttpGet get = new HttpGet(url.toString());
		HttpResponse r = client.execute(get);
		int status = r.getStatusLine().getStatusCode();
		if (status == 200) {
			HttpEntity e = r.getEntity();
			String data = EntityUtils.toString(e);
			JSONObject timeline = new JSONObject(data);
			jsonArray = timeline.getJSONArray("data");

			return jsonArray;
		}

		return null;
	}

	public View getmenulistview() {

		LayoutInflater mInflater = (LayoutInflater) activitycontext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		View v = mInflater.inflate(R.layout.slideout, null);
		Customlistviewnavigationadapter adapter;

		ArrayList<RowItem> navigationitems = new ArrayList<RowItem>();
		RowItem item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.about2),
				"ABOUT NSIT", null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.societies),
				"SOCIETIES", null, null, null, null);
		navigationitems.add(item);

		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.newspaper2),
				"LATEST UPDATES", null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.timetable2),
				"TIMETABLE", null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.help2), "CANTEENS",
				null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.videos), "VIDEOS",
				null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.directory2),
				"DIRECTORY", null, null, null, null);
		navigationitems.add(item);
		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.settings2),
				"PREFRENCES", null, null, null, null);
		navigationitems.add(item);

		item = new RowItem(BitmapFactory.decodeResource(
				activitycontext.getResources(), R.drawable.help2),
				"CONTACT DEVELOPER", null, null, null, null);
		navigationitems.add(item);

		adapter = new Customlistviewnavigationadapter(activitycontext,
				R.layout.list_item_navigation, navigationitems);

		ListView lvnavigation = (ListView) v.findViewById(R.id.lvnavigation);
		lvnavigation.setAdapter(adapter);
		lvnavigation.setDivider(null);
		lvnavigation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				switch (arg2) {
				case 0:
					Intent iabout = new Intent(activitycontext, AboutNsit.class);

					activitycontext.startActivity(iabout);
					break;
				case 1:
					Intent l1 = new Intent("android.intent.action.Societies"); // launches
																				// news
																				// activity

					activitycontext.startActivity(l1);

					break;
				case 2:

					Intent i = new Intent("android.intent.action.latest");
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activitycontext.startActivity(i);
					break;

				case 3:
					Intent ijn = new Intent("android.intent.action.timetable");

					activitycontext.startActivity(ijn);
					break;

				case 5:
					Intent i31 = new Intent(
							"android.intent.action.videoactivity");

					activitycontext.startActivity(i31);

					break;
				case 4:
					Intent i312 = new Intent(activitycontext, CanteenList.class);

					activitycontext.startActivity(i312);

					break;
				case 6:
					Intent i3 = new Intent("android.intent.action.directory");

					activitycontext.startActivity(i3);
					break;
				case 7:
					Intent i4 = new Intent("android.intent.action.prefs");

					activitycontext.startActivity(i4);
					break;
				case 8:
					Intent i5 = new Intent("android.intent.action.help");

					activitycontext.startActivity(i5);
					break;
				}

			}

		});

		return v;

	}

	public ArrayList<RowItem> GETmoredata(List<RowItem> tempdownfeed,
			JSONArray jsonArray) throws MalformedURLException, IOException,
			JSONException {

		// WE HAVE JSONARRAY
		List<RowItem> staticfeed = new ArrayList<RowItem>();
		staticfeed.addAll(tempdownfeed);
		tempdownfeed.clear();

		namelist.clear();
		Descriptionlist.clear();
		Bitmaplist.clear();
		Linklist.clear();
		IDlist.clear();
		Picturelist.clear();
		Messagelist.clear();
		Captionlist.clear();
		i = 0;
		if (jsonArray != null) {

			for (i = 0; i < jsonArray.length(); i++) {
				posttype = 0;
				namelist.add(jsonArray.getJSONObject(i).optString("name", null));
				Descriptionlist.add(jsonArray.getJSONObject(i).optString(
						"description", null));
				Linklist.add(jsonArray.getJSONObject(i).optString("link", null));
				Picturelist.add(jsonArray.getJSONObject(i).optString("picture",
						null));
				IDlist.add(jsonArray.getJSONObject(i).optString("id", null));
				Messagelist.add(jsonArray.getJSONObject(i).optString("message",
						null));
				Captionlist.add(jsonArray.getJSONObject(i).optString("caption",
						null));
				if (namelist.get(i) != null && Descriptionlist.get(i) != null
						&& Linklist.get(i) != null
						&& Picturelist.get(i) != null) {
					// It is a page post
					posttype = 1;

				}
				if (namelist.get(i) == null && Descriptionlist.get(i) == null
						&& Linklist.get(i) != null
						&& Picturelist.get(i) == null
						&& Captionlist.get(i) == null) {
					// it is a event
					posttype = 2;

				}
				if (namelist.get(i) == null && Descriptionlist.get(i) == null
						&& Linklist.get(i) == null
						&& Messagelist.get(i) != null) {
					// it is a message
					posttype = 3;

				}
				if (Messagelist.get(i) != null
						&& Descriptionlist.get(i) == null
						&& Picturelist.get(i) != null
						&& Linklist.get(i) != null) {
					// It is a timeline update without caption
					posttype = 4;

				}
				if (namelist.get(i) != null && Descriptionlist.get(i) == null
						&& Linklist.get(i) != null
						&& Picturelist.get(i) != null
						&& Captionlist.get(i) != null) {
					// it is a timeline update with caption
					posttype = 5;

				}
				switch (posttype) {
				case 0:

					break;
				case 1:

					Bitmaplist.add(BitmapFactory.decodeResource(
							activitycontext.getResources(),
							com.anshul.nsitapp.R.drawable.loading_icon));
					// Bitmaplist.add(BitmapFactory
					// .decodeStream((InputStream) new URL(Picturelist
					// .get(i)).getContent()));
					RowItem item = new RowItem(
							Bitmaplist.get(Bitmaplist.size() - 1),
							namelist.get(namelist.size() - 1),
							Descriptionlist.get(Descriptionlist.size() - 1),
							Linklist.get(Linklist.size() - 1),
							IDlist.get(IDlist.size() - 1),
							Messagelist.get(Messagelist.size() - 1),
							Picturelist.get(Picturelist.size() - 1));
					tempdownfeed.add(item);
					break;

				case 2:
					// Check for errors in future because the values are hard
					// coded
					break;

				case 3:
					namelist.remove(i);
					Descriptionlist.remove(i);
					Picturelist.remove(i);
					Linklist.remove(i);
					Linklist.add(jsonArray.getJSONObject(i)
							.getJSONArray("actions").getJSONObject(0)
							.optString("link", null));

					namelist.add(getnamefromstring(Messagelist.get(i)));

					Descriptionlist.add(Messagelist.get(i));
					Picturelist
							.add("http://profile.ak.fbcdn.net/static-ak/rsrc.php/v2/yE/r/tKlGLd_GmXe.png");
					Bitmaplist.add(BitmapFactory.decodeResource(
							activitycontext.getResources(),
							com.anshul.nsitapp.R.drawable.loading_icon));

					RowItem item3 = new RowItem(Bitmaplist.get(Bitmaplist
							.size() - 1), namelist.get(namelist.size() - 1),
							Descriptionlist.get(Descriptionlist.size() - 1),
							Linklist.get(Linklist.size() - 1),
							IDlist.get(IDlist.size() - 1),
							Messagelist.get(Messagelist.size() - 1),
							Picturelist.get(Picturelist.size() - 1));
					tempdownfeed.add(item3);
					break;
				case 4:
					gettimelineupdateinfo();
					RowItem item4 = new RowItem(Bitmaplist.get(Bitmaplist
							.size() - 1), namelist.get(namelist.size() - 1),
							Descriptionlist.get(Descriptionlist.size() - 1),
							Linklist.get(Linklist.size() - 1),
							IDlist.get(IDlist.size() - 1),
							Messagelist.get(Messagelist.size() - 1),
							Picturelist.get(Picturelist.size() - 1));
					tempdownfeed.add(item4);
					break;
				case 5:

					getupdatewithcaptioninfo();
					RowItem item5 = new RowItem(Bitmaplist.get(Bitmaplist
							.size() - 1), namelist.get(namelist.size() - 1),
							Descriptionlist.get(Descriptionlist.size() - 1),
							Linklist.get(Linklist.size() - 1),
							IDlist.get(IDlist.size() - 1),
							Messagelist.get(Messagelist.size() - 1),
							Picturelist.get(Picturelist.size() - 1));
					tempdownfeed.add(item5);
					break;
				}

				// WE HAVE ROW items in a list now
				if (tempdownfeed.isEmpty() == false
						&& staticfeed.isEmpty() == false) {
					Log.d("staticfeed", staticfeed.get(0).getdescription());
					Log.d("tempdownfeed",
							tempdownfeed.get(tempdownfeed.size() - 1)
									.getdescription());

					if (tempdownfeed.get(tempdownfeed.size() - 1)
							.getdescription()
							.equals(staticfeed.get(0).getdescription())) {
						tempdownfeed.remove(tempdownfeed.size() - 1);
						Log.d("Comparator", "Loop Entered");
						i = jsonArray.length();

						tempdownfeed.addAll(staticfeed);

					}
				}
			}
		}
		return (ArrayList<RowItem>) tempdownfeed;
	}

	private void getupdatewithcaptioninfo() throws JSONException,
			MalformedURLException, IOException {
		// TODO Auto-generated method stub
		namelist.remove(i);
		Descriptionlist.remove(i);
		// link will remain as it is
		Descriptionlist.add(Captionlist.get(i));

		namelist.add(getnamefromstring(Captionlist.get(i)));

		Bitmaplist.add(BitmapFactory.decodeResource(
				activitycontext.getResources(),
				com.anshul.nsitapp.R.drawable.loading_icon));
	}

	private void gettimelineupdateinfo() throws MalformedURLException,
			IOException, JSONException {
		// TODO Auto-generated method stub
		namelist.remove(i);
		Descriptionlist.remove(i);

		String l = Picturelist.get(i);
		if (l.contains(".jpg")) {
			l = l.substring(0, l.indexOf(".jpg") - 1);
			l = l + "n.jpg";
			Picturelist.set(i, l);

		}

		namelist.add(getnamefromstring(Messagelist.get(i)));

		Descriptionlist.add(Messagelist.get(i));

		Bitmaplist.add(BitmapFactory.decodeResource(
				activitycontext.getResources(),
				com.anshul.nsitapp.R.drawable.loading_icon));
	}

	private static String getnamefromstring(String string) {
		String processedstring;
		int index = 40;
		int lock = 40;
		if (string.length() > 40) {
			for (index = 40; index > 20; index--) {
				if (string.charAt(index) == 32) {
					lock = index;
					index = 1;
				}

			}
			processedstring = string.substring(0, lock);
			return processedstring;
		} else {
			return string;
		}

	}

	public JSONArray combinejsonarray(JSONArray jsonarray1, JSONArray jsonarray2)
			throws JSONException {
		// TODO Auto-generated method stub
		// AAA sorting the json arrays and storing the output in jsonarray
		// global variable
		JSONArray jsonarray4 = new JSONArray();
		int j1 = 0, j2 = 0, f = 0;

		while (j1 < jsonarray1.length() && j2 < jsonarray2.length()
				&& jsonarray4.length() < 30) {
			f = jsonarray1
					.getJSONObject(j1)
					.optString("created_time")
					.compareTo(
							jsonarray2.getJSONObject(j2).optString(
									"created_time"));
			if (f > 0) {
				// jsonarray2 has less created at time
				jsonarray4.put(jsonarray1.getJSONObject(j1));
				j1++;

			} else {

				jsonarray4.put(jsonarray2.getJSONObject(j2));
				j2++;

			}

		}

		return jsonarray4;

	}

	public void Getsaveddata(String String, List<RowItem> rettempfeed,
			int feedsize) throws FileNotFoundException {
		// TODO Auto-generated method stub
		rettempfeed.clear();
		Bitmap bm;
		sp = activitycontext.getSharedPreferences(String, Context.MODE_PRIVATE);
		int t = sp.getInt("size", 0);
		if (feedsize != 0 && t > feedsize) {
			t = feedsize;
		}
		if (sp.getBoolean("datawassaved", false) == true) {

			String[][] feedkey = new String[t][7];
			for (int j = 0; j < t; j++) {
				for (int k = 0; k < 6; k++) {

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

				RowItem item = new RowItem(bm,
						sp.getString(feedkey[j][1], null), sp.getString(
								feedkey[j][2], null), sp.getString(
								feedkey[j][3], null), sp.getString(
								feedkey[j][4], null), sp.getString(
								feedkey[j][5], null));
				rettempfeed.add(item);
			}
		}
	}

	public void SaveData(String string, List<RowItem> tempdownfeed)
			throws IOException {
		// TODO Auto-generated method stub

		SharedPreferences sp = activitycontext.getSharedPreferences(string,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		int t = tempdownfeed.size();
		String[][] feedkey = new String[t][7];
		for (int j = 0; j < t; j++) {
			for (int k = 0; k < 6; k++) {

				feedkey[j][k] = string + j + "-" + k;
			}

		}

		editor.clear();

		int j = 0;

		for (j = 0; j < t; j++) {
			try {
				out = activitycontext.openFileOutput(feedkey[j][0] + ".png",
						Context.MODE_PRIVATE);
				tempdownfeed.get(j).getbitmap()
						.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				editor.putString(feedkey[j][1], tempdownfeed.get(j).getname());
				editor.putString(feedkey[j][2], tempdownfeed.get(j)
						.getdescription());
				editor.putString(feedkey[j][3], tempdownfeed.get(j).getlink());
				editor.putString(feedkey[j][4], tempdownfeed.get(j).getid());
				editor.putString(feedkey[j][5], tempdownfeed.get(j)
						.getmessage());
			} catch (NullPointerException r1) {
				r1.printStackTrace();
				tempdownfeed.remove(j);
				t = tempdownfeed.size();
			} catch (IllegalStateException li) {
				li.printStackTrace();
				tempdownfeed.remove(j);
				t = tempdownfeed.size();
			}
		}
		editor.putInt("size", t);
		editor.putBoolean("datawassaved", true);
		editor.commit();
	}

	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) activitycontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public void getimagedata(ArrayList<RowItem> Tempfeed) {
		// TODO Auto-generated method stub
		int t = 0;
		t = Tempfeed.size();
		for (int i = 0; i < t; i++) {
			try {
				if (!Tempfeed.get(i).getpicturelink().equals("Done")) {
					Tempfeed.get(i).setimagebitmap(
							BitmapFactory.decodeStream((InputStream) new URL(
									Tempfeed.get(i).getpicturelink())
									.getContent()));
					Tempfeed.get(i).setpicturelink("Done");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Log.d("Picture Task", "Complete");
	}

	public void geteventids(JSONArray jsonarray) throws JSONException {
		// TODO Auto-generated method stub
		int i = 0;
		String eventid = new String();
		for (i = 0; i < jsonarray.length(); i++) {
			if (jsonarray.getJSONObject(i).optString("link")
					.contains("www.facebook.com/events/")) {
				String temp = new String();
				temp = jsonarray.getJSONObject(i).optString("link");
				temp = temp.substring(temp.indexOf("events/") + 7);
				eventid = temp.substring(0, temp.indexOf("/"));
				Log.d("eventidfromlink", eventid);
				addtoeventidstack(eventid);

			}

			String message = jsonarray.getJSONObject(i).optString("message");
			while (message.contains("www.facebook.com/events/")) {
				message = message.substring(message
						.indexOf("www.facebook.com/events/"));
				String temp2 = new String();
				temp2 = message;
				temp2 = temp2.substring(temp2.indexOf("events/") + 7);
				if (temp2.length() > 14 && temp2.contains("/")) {
					eventid = temp2.substring(0, temp2.indexOf("/"));

					addtoeventidstack(eventid);
					Log.d("eventidfrommsg", eventid);
				}
				message = message.substring(message
						.indexOf("www.facebook.com/events/") + 25);
			}

		}
	}

	private void addtoeventidstack(String eventid) {
		// TODO Auto-generated method stub

		SharedPreferences sp = activitycontext.getSharedPreferences(
				"EventsList", Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		List<String> eventlist = new ArrayList<String>();
		eventlist.clear();
		int t = sp.getInt("size", 0);

		String[] eventfeedkey = new String[t + 1];

		for (int k = 0; k < t; k++) {

			eventfeedkey[k] = "Events" + k;
		}

		for (int k = 0; k < t; k++) {
			eventlist.add(sp.getString(eventfeedkey[k], null));

		}
		eventlist.add(eventid);
		eventfeedkey[t] = "Events" + t;
		editor.putString(eventfeedkey[t], eventid);
		editor.putInt("size", t + 1);
		editor.commit();

	}

}
