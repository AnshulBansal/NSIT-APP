package com.example.nsitapp;

import android.graphics.Bitmap;

public class CustomEventItem {

	private Bitmap imagebitmap;
	private String name;
	private String description;
	private String link;
	private String id;
	private String startat;
	private String stopat;
	
	private String picturelink;

	public CustomEventItem(Bitmap imagebitmap, String name,String description,String link, String id,String startat,String stopat) {

		this.imagebitmap = imagebitmap;
		this.name = name;
		this.description = description;
		this.link = link;
		this.id = id;
		this.startat = startat;
		this.stopat = stopat;
	//	this.picturelink = picturelink;
//	this.message = message;
	}

	public String getpicturelink(){
		return picturelink;
		
	}
	public void setpicturelink(String picturelink){
		
		this.picturelink = picturelink;
	}
	public String getstopat(){
		return stopat;
		
	}
	public void setstopat(String message){
		
		this.stopat = message;
	}

	public String getstartat(){
		return startat;
		
	}
	public void setstartat(String message){
		
		this.startat = message;
	}
	public String getid(){
		return id;
	}
	public void setid(String id){
		this.id = id;
		
	}
	public String getlink(){
		return link;
	}
	public void setlink(String link){
		this.link = link;
		
	}
	public String getdescription(){
		return description;
	}
	public void setdiscription(String discription){
		this.description = discription;
		
	}
	public Bitmap getbitmap() {

		return imagebitmap;

	}

	public void setimagebitmap(Bitmap imagebitmap) {

		this.imagebitmap = imagebitmap;

	}

	public String getname() {

		return name;
	}

	public void setname(String name) {

		this.name = name;

	}
	@Override
    public String toString() {
        return name + "\n" + description;
    }

}
