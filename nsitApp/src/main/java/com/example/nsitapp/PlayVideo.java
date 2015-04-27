package com.example.nsitapp;

import com.anshul.nsitapp.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class PlayVideo extends YouTubeBaseActivity implements
		OnInitializedListener {

	String Videotoplay = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent i = getIntent();
		try {
			Videotoplay = i.getExtras().getString("VideoId");
		} catch (NullPointerException v) {
			v.printStackTrace();
		}

		setContentView(R.layout.playvideo);
		YouTubePlayerFragment youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager()
				.findFragmentById(R.id.youtube_fragment);
		youTubePlayerFragment.initialize(
				"AIzaSyAFoDWAKrrLFOhNFpOcYpRoSxSKuYrg3_Y", this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		arg1.setFullscreen(true);
		arg1.cueVideo(Videotoplay);

	}

}
