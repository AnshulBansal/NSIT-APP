package com.example.nsitapp;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewConfiguration;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.widget.SearchView;
import com.anshul.nsitapp.R;
import com.github.amlcurran.showcaseview.*;

public class Latest extends SherlockFragmentActivity {

	ViewPager mviewpager;
	ActionBar actionbar;
	TabsAdapter mtabadpater;
	boolean firstlaunch = true;
	public MenuDrawer mdrawer;

	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancelAll();

		sp = getSharedPreferences("firsttimefile", Context.MODE_PRIVATE);
		firstlaunch = sp.getBoolean("firstlaunch", true);
		mviewpager = new ViewPager(this);
		mviewpager.setId(R.id.viewpagerlatest);
		Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(mviewpager);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);
		mdrawer.setMenuSize((int) (0.85 * getResources().getDisplayMetrics().widthPixels));

		mdrawer.setTouchBezelSize(80);

		getOverflowMenu();

		// setContentView(mviewpager);

		actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);

		mtabadpater = new TabsAdapter(this, mviewpager);
		mtabadpater.addTab(actionbar.newTab().setText("Announcements"),
				Latest_announcements.class, null);
		mtabadpater.addTab(actionbar.newTab().setText("Events"),
				Latest_events.class, null);

	}

	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			java.lang.reflect.Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			mdrawer.openMenu();
			firstlaunch = false;
			sp = getApplicationContext().getSharedPreferences("firsttimefile",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putBoolean("firstlaunch", firstlaunch);
			editor.commit();

			break;

		case R.id.preferences:
			Intent n = new Intent(getApplicationContext(), Prefs.class);
			startActivity(n);
		}
		return true;
	}

	// The code for overriding the menu button in android
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			mdrawer.openMenu();
			return true;
		} else {
			return super.onKeyUp(keyCode, event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		com.actionbarsherlock.view.MenuInflater blowup = getSupportMenuInflater();
		blowup.inflate(R.menu.cool_menu, menu);
		if (firstlaunch == true) {
			Intent intent1 = new Intent(Latest.this, Societies.class);
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			PendingIntent pi = PendingIntent.getActivity(Latest.this, 0,
					intent1, 0);
			String title = "Thank You For Installing Nsit App!";
			Notification notification = new Notification(R.drawable.push_icon,
					title, System.currentTimeMillis());
			notification.setLatestEventInfo(getApplicationContext(), title,
					"Push Notifications are enabled", pi);
			notification.defaults = Notification.DEFAULT_ALL;
			nm.notify(123456, notification);

		}

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		if (null != searchView) {
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(true);
		}

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				Log.d("Search", "Search");
				Latest_announcements.filteradapter(newText);
				Latest_events.filteradapter(newText);

				// this is your adapter that will be filtered
				// adapter.getFilter().filter(newText);
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				Log.d("Search", "Search");
				// this is your adapter that will be filtered
				// adapter.getFilter().filter(query);
				return true;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		return super.onCreateOptionsMenu(menu);
	}
}
