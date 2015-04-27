package com.example.nsitapp;



import uk.co.senab.photoview.PhotoViewAttacher;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;


  
  
public class CanteenMenuView extends SherlockActivity {
	
	PhotoViewAttacher mattacher;
	MenuDrawer mdrawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.menuview);
        
    	Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.menuview);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		mdrawer.setDropShadowEnabled(true);
		mdrawer.setDropShadowSize(10);
		mdrawer.setMenuSize((int) (0.85 *getResources().getDisplayMetrics().widthPixels));
		
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	
	

        TextView product = (TextView) findViewById(R.id.product);
        ImageView menupic = (ImageView) findViewById(R.id.menupic);

        Intent i = getIntent();

        // getting attached intent data
        String label = i.getStringExtra("product");

        final String menu_pic = i.getStringExtra("menu_pic");

        int id = getResources().getIdentifier(menu_pic, "drawable", getPackageName());

        // displaying selected product name
        product.setText(label);
        menupic.setImageResource(id);
        mattacher = new PhotoViewAttacher(menupic);

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
