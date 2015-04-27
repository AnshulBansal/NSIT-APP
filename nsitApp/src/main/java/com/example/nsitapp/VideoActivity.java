package com.example.nsitapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

public class VideoActivity extends SherlockActivity implements
		OnItemClickListener {
	HttpClient client;
	JSONArray jsonArray;
	ArrayList<Video> Videolist = new ArrayList<Video>();
	String URL = "http://gdata.youtube.com/feeds/api/users/UC7c20jlnTG5NukUUdS1ARXw/uploads?v=2&alt=jsonc";
	ListView lvvideo;
	CustomListViewVideoAdapter adapter;
	Helper help;
	MenuDrawer mdrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.videoactivity);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);

		mdrawer.setMenuSize((int) (0.85 * getResources().getDisplayMetrics().widthPixels));

		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		Helper help = new Helper(this);
		lvvideo = (ListView) findViewById(R.id.lvvideo);
		lvvideo.setOnItemClickListener(this);
		lvvideo.setDividerHeight(1);

		ActionBar action = getSupportActionBar();
		action.setTitle("Videos");

		try {
			help.GetsavedVideodata("VideoData", Videolist, 0);
			adapter = new CustomListViewVideoAdapter(VideoActivity.this,
					R.layout.list_item, Videolist);
			lvvideo.setAdapter(adapter);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			mdrawer.toggleMenu();
			break;
		case R.id.preferences:
			Intent i = new Intent("android.intent.action.prefs");
			startActivity(i);
			break;

		}
		return true;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String id = Videolist.get(arg2).getID();

		Intent i = new Intent(VideoActivity.this, PlayVideo.class);
		i.putExtra("VideoId", id);
		startActivity(i);
	}

}
