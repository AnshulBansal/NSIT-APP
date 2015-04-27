package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import uk.co.senab.photoview.PhotoViewAttacher;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

public class Timetable extends SherlockActivity implements OnClickListener {

	String timetableurl = "http://dl.dropbox.com/u/89887150/";
	Bitmap bm;
	ImageView ivtimetable;
	String timetableimage = "timetableimage.png";
	SharedPreferences sp;
	FileInputStream fis;
	File filePath;
	TextView tvtimetable;
	Button bupdatetimetable;
	MenuDrawer mdrawer;

	PhotoViewAttacher mattacher;

	Boolean firstlaunch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.timetable);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);
		mdrawer.setMenuSize((int) (0.85 * getResources().getDisplayMetrics().widthPixels));

		bupdatetimetable = (Button) findViewById(R.id.bupdatetimetable);
		bupdatetimetable.isClickable();
		bupdatetimetable.setOnClickListener(this);
		ivtimetable = (ImageView) findViewById(R.id.iVTimeTable);
		sp = getSharedPreferences("firsttimefile", Context.MODE_PRIVATE);
		firstlaunch = sp.getBoolean("firstlaunchtimetable", true);
		if (firstlaunch == true) {
			firstlaunch = false;
			sp = getSharedPreferences("firsttimefile", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putBoolean("firstlaunchtimetable", firstlaunch);
			editor.commit();

		}
		sp = getSharedPreferences("Generaldata", Context.MODE_PRIVATE);
		if (sp.getBoolean("datawassaved", false) != false) {
			try {
				filePath = getFileStreamPath(timetableimage);
				fis = new FileInputStream(filePath);
				bm = BitmapFactory.decodeStream(fis);
				ivtimetable.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// ivtimetable.setOnTouchListener(this);
		mattacher = new PhotoViewAttacher(ivtimetable);
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			mdrawer.openMenu();
			;
			break;
		case R.id.preferences:
			Intent i = new Intent("android.intent.action.prefs");
			startActivity(i);
			break;

		}
		return true;
	}

	class Read2 extends AsyncTask<String, Integer, String> {

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			dialog = new ProgressDialog(Timetable.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			dialog.setMax(100);

			dialog.show();
		}

		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub

			dialog.incrementProgressBy(values[0]);

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
				publishProgress(20);

				bm = BitmapFactory.decodeStream((InputStream) new URL(arg0[0])
						.getContent());
				publishProgress(20);
				FileOutputStream out = openFileOutput(timetableimage,
						Context.MODE_PRIVATE);
				publishProgress(20);
				bm.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
				publishProgress(40);
				dialog.dismiss();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			ivtimetable.setImageBitmap(bm);
			mattacher.update();
			Editor editor = sp.edit();
			editor.putBoolean("datawassaved", true);
			editor.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater blowup = getSupportMenuInflater();
		blowup.inflate(R.menu.cool_menu, menu);
		menu.findItem(R.id.menu_search).setVisible(false);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d("ButtonClicked", "Bupdateclicked");
		SharedPreferences getprefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		timetableurl = "http://dl.dropbox.com/u/89887150/";
		StringBuilder url2 = new StringBuilder(timetableurl);

		url2.append(getprefs.getString("ttbranch", "COE"));
		url2.append("-");
		url2.append(getprefs.getString("ttyear", "1"));
		url2.append("-");
		url2.append(getprefs.getString("ttsection", "1"));
		url2.append(".jpg");
		timetableurl = url2.toString();

		if (haveNetworkConnection()) {
			new Read2().execute(timetableurl);
		} else {
			Toast.makeText(Timetable.this, "Check Your Network Connection",
					Toast.LENGTH_LONG).show();
		}

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
