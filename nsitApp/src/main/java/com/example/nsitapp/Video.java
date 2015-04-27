package com.example.nsitapp;

import android.graphics.Bitmap;

public class Video {

	private String ID;
	private String Title;
	private String Desc;
	private Bitmap bm;

	public Video(String ID, String Title, String Desc, Bitmap bm) {

		this.ID = ID;
		this.Title = Title;
		this.Desc = Desc;
		this.bm = bm;

	}

	public String getID() {

		return this.ID;
	}

	public String gettitle() {

		return this.Title;
	}

	public String getdesc() {

		return this.Desc;
	}

	public Bitmap getbitmap() {

		return this.bm;
	}

}
