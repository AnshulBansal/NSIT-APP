package com.example.nsitapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.anshul.nsitapp.R;

public class Prefs extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

	

	
	
	
	
	
}