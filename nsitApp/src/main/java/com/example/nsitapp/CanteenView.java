package com.example.nsitapp;

/**
 * Created with IntelliJ IDEA.
 * User: prancer
 * Date: 17/8/13
 * Time: 4:22 PM
 * To change this template use File | Settings | File Templates.
 */
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CanteenView extends SherlockActivity{
    
	MenuDrawer mdrawer;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.list_item_view);
        
    	Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.list_item_view);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);
		mdrawer.setMenuSize((int) (0.85 *getResources().getDisplayMetrics().widthPixels));
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	
	

        TextView txtProduct = (TextView) findViewById(R.id.canteen_label);
        TextView txtDesc = (TextView) findViewById(R.id.canteen_desc);
        ImageView canteenpic = (ImageView) findViewById(R.id.canteen_pic)   ;

        Intent i = getIntent();

        // getting attached intent data
        final String product = i.getStringExtra("product");
        final String no = i.getStringExtra("no");
        String desc = i.getStringExtra("desc");
        String loc = i.getStringExtra("loc");
        String pic = i.getStringExtra("pic");
        final String menu_pic = pic + "_menu";

        int id = getResources().getIdentifier(pic, "drawable", getPackageName());

        Uri uri = Uri.parse(loc);
        final Intent mapintent = new Intent(Intent.ACTION_VIEW, uri);

        // displaying selected product name
        txtProduct.setText(product);
        txtDesc.setText(desc);
        canteenpic.setImageResource(id);

        final Button sonmap = (Button) findViewById(R.id.sonmap);
        final Button gtmenu = (Button) findViewById(R.id.gtmenu);
        final Button contactnw = (Button) findViewById(R.id.contactnw);

        sonmap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(mapintent);
            }
        });

        gtmenu.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent menuintent = new Intent(CanteenView.this, CanteenMenuView.class);
                menuintent.putExtra("product", product);
                menuintent.putExtra("menu_pic", menu_pic);
                startActivity(menuintent);
            }
        });

        contactnw.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + no));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }
            }
        });

    }
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
mdrawer.openMenu();;
			break;
		case R.id.preferences:
			Intent i = new Intent("android.intent.action.prefs");
			startActivity(i);
			break;

		}
		return true;
	}
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater blowup = getSupportMenuInflater();
		blowup.inflate(R.menu.cool_menu, menu);
		menu.findItem(R.id.menu_search).setVisible(false);
		return true;
	}
}
