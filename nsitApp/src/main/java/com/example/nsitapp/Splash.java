package com.example.nsitapp;



import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.anshul.nsitapp.R;


public class Splash extends Activity{


protected void onCreate(Bundle savedInstance) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstance);
	//fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//fullscreen
	setContentView(R.layout.splash);
	Log.d("Service", "Lauching");
	Intent i = new Intent(getApplicationContext(), UpdaterService.class);
	startService(i);
	
	
	
	Thread timer = new Thread(){
		public void run(){
			try{
				
				sleep(1500);
				
			}catch(InterruptedException e){
				e.printStackTrace();
				
			}finally{
				Intent openmainactivity = new Intent ("android.intent.action.latest");
				startActivity(openmainactivity);
				
			}
		}
			
	};
	
	timer.start(); 
}

@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	finish();
}
}
