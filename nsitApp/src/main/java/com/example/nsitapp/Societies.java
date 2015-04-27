package com.example.nsitapp;

import java.io.IOException;
import java.util.List;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.widget.SearchView;
import com.anshul.nsitapp.R;

public class Societies extends SherlockFragmentActivity {

	ViewPager mviewpager;
	ActionBar actionbar;
	TabsAdapter mtabadpater;
	MenuDrawer mdrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mviewpager = new ViewPager(this);
		mviewpager.setId(R.id.viewpagersociety);
		mviewpager.setOffscreenPageLimit(7);
	
		Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(mviewpager);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);

		mdrawer.setMenuSize((int) (0.85 *getResources().getDisplayMetrics().widthPixels));
		
		mdrawer.setTouchBezelSize(80);
		
		actionbar = getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setDisplayHomeAsUpEnabled(true);
		mtabadpater = new TabsAdapter(this, mviewpager);
		mtabadpater.addTab(actionbar.newTab().setText("SIG"), Society_sig.class, null);
		mtabadpater.addTab(actionbar.newTab().setText("Tech"), Society_tech.class, null);
		mtabadpater.addTab(actionbar.newTab().setText("Cult"), Society_cult.class, null);
		mtabadpater.addTab(actionbar.newTab().setText("Auto"), Society_auto.class, null);
		mtabadpater.addTab(actionbar.newTab().setText("Social"), Society_social.class, null);
	}

	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		//TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
		mdrawer.openMenu();
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
		
		 SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	        if (null != searchView )
	        {
	            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	            searchView.setIconifiedByDefault(true);   
	        }
	        

	        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() 
	        {
	        	
	            public boolean onQueryTextChange(String newText) 
	            {
	            Log.d("Search", newText);


	          Society_sig.filteradapter(newText);
	          Society_tech.filteradapter(newText);
	          Society_social.filteradapter(newText);
	          Society_auto.filteradapter(newText);
	          Society_cult.filteradapter(newText);


	            

	            // this is your adapter that will be filtered
	            //    adapter.getFilter().filter(newText);
	                return true;
	            }

	            public boolean onQueryTextSubmit(String query) 
	            {
	            	Log.d("Search","Search");
	                // this is your adapter that will be filtered
	              //  adapter.getFilter().filter(query);
	                return true;
	            }
	        };
	        searchView.setOnQueryTextListener(queryTextListener);

	        return super.onCreateOptionsMenu(menu);
	   
	}
	

		
}
