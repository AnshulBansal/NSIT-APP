package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import uk.co.senab.photoview.PhotoViewAttacher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.anshul.nsitapp.R;

public class ImageView1 extends SherlockActivity {
	
	ImageView ivimage;
	Intent i;
	int position;
	RowItem item;
	PhotoViewAttacher mattacher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.image_view);
		ivimage = (ImageView) findViewById(R.id.ivimageviewactivity);
		//i = getIntent();
		//position = i.getExtras().getInt("imageposition");
	    Bitmap bm =  null;
		
			try {
				File filePath = getFileStreamPath("image.png");
				FileInputStream fis = new FileInputStream((File) filePath);
				 bm = BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    
			ivimage.setImageBitmap(bm);
		mattacher = new PhotoViewAttacher(ivimage);
		
		com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		LinearLayout rl = (LinearLayout) findViewById (R.id.rlimageview);
	

			

			
		
	
	}
	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()){
		case android.R.id.home:	
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
