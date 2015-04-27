package com.example.nsitapp;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.anshul.nsitapp.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Help extends SherlockActivity implements OnClickListener {

	Button bsendemail;
	String emailADD;
	TextView	tvdescription;
	
	LinearLayout rl;
	MenuDrawer mdrawer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.help);
		tvdescription = (TextView)findViewById(R.id.textView7);
		Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.help);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		LinearLayout rl =(LinearLayout)findViewById(R.id.layouthelp);
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		bsendemail = (Button) findViewById(R.id.bsendemail);
		bsendemail.setVisibility(View.GONE);
		bsendemail.setOnClickListener(this);
		emailADD = "ans_hul_1994@yahoo.co.in";
		Linkify.addLinks(tvdescription, Linkify.ALL);
	//	tvdescription.setOnClickListener(this);
	
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://www.nsitonline.in/"));
		startActivity(browserIntent);
	}
	
			
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
	mdrawer.openMenu();
		case R.id.preferences:
			Intent i = new Intent("android.intent.action.prefs");
			startActivity(i);
			break;
	
		
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	
	}
	
	

