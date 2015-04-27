package com.example.nsitapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.anshul.nsitapp.R;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class UpdaterService extends Service {

	HttpClient client;
	String message = "zingalala";
	SharedPreferences sp;
	Editor editor;
	int Uniqueid = 101010;
	boolean running = true;
	BufferedReader in;
	JSONArray nsitonlinejsonarray;
	String localadam;
	String nsitonlineid = "109315262061";
	final static String access_token = "CAACTzPZAxblQBAE6tYaEVZBjOIl0A3AZAn0qEWj5oqa08MXNCsjE3BInto2G07sKrcCdxTAMZAZCqHYn2oRjwk0wPfZC5C8U4On1E2OoEZCUkU7XLmSzo5aLuGwhjuisrrUcyna7rrCqsiROioS1KpR9NoDZApZAdoFICtWuLVpCYRMR9Fmw4xyHh";
	final static String URL = "https://graph.facebook.com/";
	ArrayList<RowItem> nsitonlinefeed = new ArrayList<RowItem>();
	ArrayList<CustomEventItem> eventfeed = new ArrayList<CustomEventItem>();
	boolean firsttimelaunch;
	private FileOutputStream out;
	String[][] Nsitonlinefeedkey = new String[20][20];
	public String nsitfreshersid = "186766334824961";
	public String proggroupid = "162982077078900";
	public String collegespaceid = "209095329107081";
	ArrayList<Video> Videolist = new ArrayList<Video>();
	Helper help;
	String[] eventidlist = new String[40];
	JSONArray jsonarray1, jsonarray2, jsonarray3, jsonarray4;

	@Override
	public void onCreate() {
		client = new DefaultHttpClient();
		Log.d("Servisdce", "Created");
		help = new Helper(getApplicationContext());
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		running = false;
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("Servnice", "Started");
		int minutes = 180;

		SharedPreferences getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String values = getprefs.getString("Timedelay", "4");
		if (values == "60") {
			minutes = 60;
		}
		if (values == "180") {
			minutes = 180;
		}
		if (values == "360") {
			minutes = 360;
		}
		if (values == "720") {
			minutes = 720;
		}
		if (values == "1440") {
			minutes = 1440;
		}
		// Change here for other update time

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, UpdaterService.class);
		PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
		am.cancel(pi);
		// by my own convention, minutes <= 0 means notifications are disabled
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + minutes * 60 * 1000,
					minutes * 60 * 1000, pi);
		}
		// by my own convention, minutes <= 0 means notifications are disabled
		if (minutes > 0) {
			am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
					SystemClock.elapsedRealtime() + minutes * 60 * 1000,
					minutes * 60 * 1000, pi);
		}

		// get data from shared prefernces for comaprison
		new read1().execute("id");

		return 1;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	class read1 extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.d("AsyncTask", "Called");
			// Retrive Data here

			if (haveNetworkConnection()) {

				try {
					Log.d("dService", "on jsonarray");
					nsitonlinejsonarray = Helper.getjsonarrayfromfacebookid(
							nsitonlineid, true);

					nsitonlinefeed = help.GETmoredata(nsitonlinefeed,
							nsitonlinejsonarray);
					help.getimagedata(nsitonlinefeed);
					SendAnnouncementNotification();
					help.SaveData("nsitonlinedata", nsitonlinefeed);
					Log.d("Announcementsdata", "you did it");
					jsonarray2 = Helper.getjsonarrayfromfacebookid(proggroupid,
							false);

					jsonarray3 = Helper.getjsonarrayfromfacebookid(
							collegespaceid, false);
					jsonarray4 = Helper.getjsonarrayfromfacebookid(
							nsitfreshersid, false);
					Processeventsdata();
					Videolist.clear();
					Videolist = help
							.GetFeedFromYoutubeChannel("UCu445B5LTXzkNr5eft8wNHg");
					sendvideonotification();
					help.SaveVideoData("VideoData", Videolist);
					Log.d("VideoData", "Done");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return "zingalala";

		}

		@SuppressWarnings("deprecation")
		private void sendvideonotification() throws FileNotFoundException {
			// TODO Auto-generated method stub
			ArrayList<Video> rettempfeed = new ArrayList<Video>();
			help.GetsavedVideodata("VideoData", rettempfeed, 3);
			if (!rettempfeed.isEmpty()) {

				if (!(Videolist.get(0).getID()
						.equals(rettempfeed.get(0).getID()))) {
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

					Intent intent1 = new Intent(UpdaterService.this,
							VideoActivity.class);
					intent1.putExtra("Uniqueid", Uniqueid);
					PendingIntent pi = PendingIntent.getActivity(
							UpdaterService.this, 0, intent1, 0);
					String title = "New Video is available";

		
					Notification notification = new Notification(
							R.drawable.push_icon, title,
							System.currentTimeMillis());
					if (Videolist.get(0).gettitle().length() < 40) {
						notification.setLatestEventInfo(
								getApplicationContext(), title, Videolist
										.get(0).gettitle(), pi);
					} else {
						notification
								.setLatestEventInfo(getApplicationContext(),
										title, Videolist.get(0).gettitle()
												.substring(0, 40), pi);
					}
					notification.defaults = Notification.DEFAULT_ALL;
					SharedPreferences getprefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					boolean push = getprefs.getBoolean("pushexam", true);
					if (push) {
						nm.notify(Uniqueid, notification);
						Uniqueid++;
					}
				}
			}

		}

		private void SendAnnouncementNotification()
				throws FileNotFoundException {
			// TODO Auto-generated method stub

			ArrayList<RowItem> rettempfeed = new ArrayList<RowItem>();
			help.Getsaveddata("nsitonlinedata", rettempfeed, 3);
			if (!rettempfeed.isEmpty()) {

				if (!(nsitonlinefeed.get(0).getname().equals(rettempfeed.get(0)
						.getname()))) {
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

					Intent intent1 = new Intent(UpdaterService.this,
							Latest.class);
					intent1.putExtra("Uniqueid", Uniqueid);
					PendingIntent pi = PendingIntent.getActivity(
							UpdaterService.this, 0, intent1, 0);
					String title = nsitonlinefeed.get(0).getname();

					Notification notification = new Notification(
							R.drawable.push_icon, title,
							System.currentTimeMillis());
					if (nsitonlinefeed.get(0).getdescription().length() < 40) {
						notification.setLatestEventInfo(
								getApplicationContext(), title, nsitonlinefeed
										.get(0).getdescription(), pi);
					} else {
						notification.setLatestEventInfo(
								getApplicationContext(), title,
								nsitonlinefeed.get(0).getdescription()
										.substring(0, 40), pi);
					}
					notification.defaults = Notification.DEFAULT_ALL;
					SharedPreferences getprefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					boolean push = getprefs.getBoolean("pushexam", true);
					if (push) {
						nm.notify(Uniqueid, notification);
						Uniqueid++;
					}
				}
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			stopSelf();

		}

		private void Processeventsdata() {
			// TODO Auto-generated method stub

			try {
				help.geteventids(nsitonlinejsonarray);
				help.geteventids(jsonarray2);
				help.geteventids(jsonarray3);
				help.geteventids(jsonarray4);
				Log.d("goteventidlist", "got it");
				filterandsaveeventsdata();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void filterandsaveeventsdata() throws ClientProtocolException,
				IOException, JSONException {
			// TODO Auto-generated method stub
			SharedPreferences sp = getSharedPreferences("EventsList",
					Context.MODE_PRIVATE);
			List<String> eventlist = new ArrayList<String>();
			eventlist.clear();
			int t = sp.getInt("size", 0);

			String[] eventfeedkey = new String[t];

			for (int k = 0; k < t; k++) {

				eventfeedkey[k] = "Events" + k;
			}
			System.out.print(eventlist);
			System.out.print(eventfeedkey);

			for (int k = 0; k < t; k++) {
				eventlist.add(sp.getString(eventfeedkey[k], "45678"));

			}
			System.out.print(eventlist);
			List<String> filteredeventlist = new ArrayList<String>();
			for (int jl = 0; jl < t; jl++) {
				if (!(filteredeventlist.contains(eventlist.get(jl)))

				&& !(eventlist.get(jl).equals("45678"))) {
					filteredeventlist.add(eventlist.get(jl));
					Log.d("Filteredtaglist", eventlist.get(jl));
				}
			}

			for (int i = 0; i < filteredeventlist.size(); i++) {
				CustomEventItem item = GetEventInformation(filteredeventlist
						.get(i));
				if (item != null) {
					eventfeed.add(item);
				}
			}
			SendEventNotification();

			SaveData("EventsFeed", eventfeed);
			Log.d("EventsData", "Events data has been saved");
			eventfeed.clear();
		}

		private void SendEventNotification() throws FileNotFoundException {
			// TODO Auto-generated method stub

			ArrayList<CustomEventItem> rettempfeed = new ArrayList<CustomEventItem>();
			Getsaveddata("EventsFeed", rettempfeed);
			if (!(rettempfeed.isEmpty())) {
				if (!(eventfeed.get(0).getname().equals(rettempfeed.get(0)
						.getname()))) {
					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

					Intent intent1 = new Intent(UpdaterService.this,
							Latest.class);
					intent1.putExtra("Uniqueid", Uniqueid);
					PendingIntent pi = PendingIntent.getActivity(
							UpdaterService.this, 0, intent1, 0);
					String title = eventfeed.get(0).getname();

					Notification notification = new Notification(
							R.drawable.push_icon, title,
							System.currentTimeMillis());
					if (eventfeed.get(0).getdescription().length() < 40) {
						notification.setLatestEventInfo(
								getApplicationContext(), title, eventfeed
										.get(0).getdescription(), pi);
					} else {
						notification.setLatestEventInfo(
								getApplicationContext(),
								title,
								eventfeed.get(0).getdescription()
										.substring(0, 40), pi);
					}
					notification.defaults = Notification.DEFAULT_ALL;
					SharedPreferences getprefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					boolean push = getprefs.getBoolean("pushexam", true);
					if (push) {
						nm.notify(Uniqueid, notification);
						Uniqueid++;
					}
				}
			}
		}

		public void Getsaveddata(String String,
				ArrayList<CustomEventItem> latesteventsfeed2)
				throws FileNotFoundException {
			// TODO Auto-generated method stub
			latesteventsfeed2.clear();
			Bitmap bm;
			Context activitycontext = getApplicationContext();
			sp = activitycontext.getSharedPreferences(String,
					Context.MODE_PRIVATE);
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

					File filePath = activitycontext
							.getFileStreamPath(filestring);
					FileInputStream fis = new FileInputStream(filePath);
					bm = BitmapFactory.decodeStream(fis);

					CustomEventItem item = new CustomEventItem(bm,
							sp.getString(feedkey[j][1], null), sp.getString(
									feedkey[j][2], null), sp.getString(
									feedkey[j][3], null), sp.getString(
									feedkey[j][4], null), sp.getString(
									feedkey[j][5], null), sp.getString(
									feedkey[j][6], null));
					latesteventsfeed2.add(item);
				}
			}
		}

		private CustomEventItem GetEventInformation(String string) {
			// TODO Auto-generated method stubs
			try {
				String Urlforeventinfo = "https://graph.facebook.com/";
				StringBuilder url = new StringBuilder(Urlforeventinfo);
				url.append(string);
				url.append("?access_token=");
				url.append(access_token);

				HttpGet get = new HttpGet(url.toString());

				HttpResponse r;

				r = client.execute(get);

				int status = r.getStatusLine().getStatusCode();
				if (status == 200) {
					HttpEntity e = r.getEntity();
					String data = EntityUtils.toString(e);
					JSONObject timelin = new JSONObject(data);
					CustomEventItem item = new CustomEventItem(
							BitmapFactory.decodeStream((InputStream) new URL(
									"https://graph.facebook.com/" + string
											+ "/picture").getContent()),
							timelin.optString("name", null), timelin.optString(
									"description", null),
							"https://www.facebook.com/events/" + string + "/",
							string, timelin.optString("start_time", null),
							timelin.optString("end_time", null));

					return item;

				}
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();

			} catch (IllegalArgumentException ti) {
				ti.printStackTrace();

			}

			return null;

		}

		public void SaveData(String string, ArrayList<CustomEventItem> eventfeed)
				throws IOException {
			// TODO Auto-generated method stub

			SharedPreferences sp = getSharedPreferences(string,
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();

			int t = eventfeed.size();
			String[][] feedkey = new String[t][7];
			for (int j = 0; j < t; j++) {
				for (int k = 0; k < 7; k++) {

					feedkey[j][k] = string + j + "-" + k;
				}

			}

			editor.clear();

			int j = 0;

			for (j = 0; j < t; j++) {

				out = getApplicationContext().openFileOutput(
						feedkey[j][0] + ".png", Context.MODE_PRIVATE);
				eventfeed.get(j).getbitmap()
						.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				editor.putString(feedkey[j][1], eventfeed.get(j).getname());
				editor.putString(feedkey[j][2], eventfeed.get(j)
						.getdescription());
				editor.putString(feedkey[j][3], eventfeed.get(j).getlink());
				editor.putString(feedkey[j][4], eventfeed.get(j).getid());
				editor.putString(feedkey[j][5], eventfeed.get(j).getstartat());
				editor.putString(feedkey[j][6], eventfeed.get(j).getstopat());

			}
			editor.putInt("size", t);
			editor.putBoolean("datawassaved", true);
			editor.commit();
		}

		private boolean haveNetworkConnection() {
			boolean haveConnectedWifi = false;
			boolean haveConnectedMobile = false;

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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

	}

}
