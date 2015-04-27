package com.example.nsitapp;

/**
 * Created with IntelliJ IDEA.
 * User: prancer
 * Date: 17/8/13
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */


import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//public class ListViewActivity extends ListActivity {
public class CanteenList extends SherlockActivity {
  
    
    MenuDrawer mdrawer;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.canteen);
    	Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.canteen);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);
		mdrawer.setMenuSize((int) (0.85 *getResources().getDisplayMetrics().widthPixels));
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	

        // storing string resources into Array
        final String[] canteens= getResources().getStringArray(R.array.canteens);
        final String[] canteens_desc= getResources().getStringArray(R.array.canteens_desc);
        final String[] map_locs= getResources().getStringArray(R.array.map_locs);
        final String[] canteen_pics= getResources().getStringArray(R.array.canteen_pics);
        final String[] list_desc= getResources().getStringArray(R.array.list_desc);
        final String[] contact_no= getResources().getStringArray(R.array.contact_no);

        // Binding resources Array to ListAdapter
        /*this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, canteens));
        ListView lv = getListView();
        lv.setDivider(null);
        lv.setClickable(true);*/

        //Setting the list with adapter.
        ListView lv = (ListView) findViewById(R.id.CListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item_sid , R.id.label, canteens);
        lv.setAdapter(adapter);
        lv.setClickable(true);
        lv.setDivider(null);

        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String product = canteens[position];
                String desc = canteens_desc[position];
                String loc = map_locs[position];
                String pic = canteen_pics[position];
                String no = contact_no[position];

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), CanteenView.class);
                // sending data to new activity
                i.putExtra("product", product);
                i.putExtra("desc",desc);
                i.putExtra("loc",loc);
                i.putExtra("pic",pic);
                i.putExtra("no",no);
                startActivity(i);

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
