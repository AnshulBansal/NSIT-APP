package com.example.nsitapp;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;
import android.util.Log;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Intent i = new Intent(context, UpdaterService.class);
		Log.d("Nsit App", "Service Started");
		context.startService(i);
		
		// react to the event

	}

}
