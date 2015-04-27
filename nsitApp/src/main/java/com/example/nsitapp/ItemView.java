package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

public class ItemView extends SherlockActivity implements OnClickListener {

	TextView tvname;
	TextView tvlink;
	TextView tvdescription;
	TextView tveventtimings;
	Button bsetalarmforevent;
	ImageView ivicon;
	RowItem item;
	Intent i;
	int position;
	MenuDrawer mdrawer;
	boolean itisaevent = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		super.onCreate(savedInstanceState);
		Helper help = new Helper(this);
		mdrawer = MenuDrawer.attach(this,
				net.simonvt.menudrawer.MenuDrawer.Type.BEHIND, Position.LEFT,
				MenuDrawer.MENU_DRAG_WINDOW);
		mdrawer.setContentView(R.layout.item_view);
		mdrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
		mdrawer.setMenuView(help.getmenulistview());
		tvname = (TextView)findViewById(R.id.tvname);
		tvlink = (TextView)findViewById(R.id.tVlink);
		tveventtimings = (TextView)findViewById(R.id.tveventtimings);
		bsetalarmforevent = (Button)findViewById(R.id.bsetalarm);
		tvdescription = (TextView)findViewById(R.id.tvdescription);
		ivicon = (ImageView)findViewById(R.id.iVicon);
		ivicon.setOnClickListener(this);
		i = getIntent();
		position = i.getExtras().getInt("position");
		if(!(i.getExtras().getString("startat")==null)){
			itisaevent = true;
		}
		if(itisaevent){
			tveventtimings.setVisibility(View.VISIBLE);
			bsetalarmforevent.setVisibility(View.VISIBLE);
		
			tveventtimings.setText(i.getExtras().getString("startat"));
			bsetalarmforevent.setOnClickListener(this);
			
		}
	
		tvname.setText(i.getExtras().getString("name"));
		if(itisaevent == true){
			tvlink.setText(Html.fromHtml("<a href="+i.getExtras().getString("link")+"> Event Facebook Page"));
		    tvlink.setMovementMethod(LinkMovementMethod.getInstance());
				
		}else{
	    tvlink.setText(Html.fromHtml("<a href="+i.getExtras().getString("link")+"> For More Info Click Here"));
	    tvlink.setMovementMethod(LinkMovementMethod.getInstance());
		}
	    Bitmap bm =  null;
		
		try {
			File filePath = getFileStreamPath("image.png");
			FileInputStream fis = new FileInputStream((File) filePath);
			 bm = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
		ivicon.setImageBitmap(bm);
		tvdescription.setText(i.getExtras().getString("description"));
		Linkify.addLinks(tvdescription, Linkify.ALL);
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		LinearLayout rl = (LinearLayout) findViewById (R.id.rlitemview);


			

			


	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		case android.R.id.home:	
mdrawer.openMenu();
		break;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.iVicon:
		Intent i2 = new Intent("android.intent.action.imageview");
		
		i.putExtra("imageposition",position);
		startActivity(i2);
		break;
		case R.id.bsetalarm:
			String dtStart = i.getExtras().getString("stopat");  
			//String dtstop = "2013-07-14T23:55:00+0530";
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+0530'");  
            try {
				Date datestart = format.parse(dtStart);
				Calendar cal = Calendar.getInstance();   
				
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				intent.putExtra("beginTime",datestart.getTime());
			
				intent.putExtra("title",i.getExtras().getString("name") );
				startActivity(intent);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			Log.d("bclicked", "setalarmclicked");
			
			break;
	}



}
}

