package com.example.nsitapp;

import android.graphics.Bitmap;

public class RowItem {

	private Bitmap imagebitmap;
	private String name;
	private String description;
	private String link;
	private String id;
	private String message;
	private String picturelink;

	public RowItem(Bitmap imagebitmap, String name,String description,String link, String id,String message,String picturelink) {

		this.imagebitmap = imagebitmap;
		this.name = name;
		this.description = description;
		this.link = link;
		this.id = id;
		this.picturelink = picturelink;
	this.message = message;
	}

	public RowItem(Bitmap imagebitmap, String name,String description,String link, String id,String message) {
		// TODO Auto-generated constructor stub
		this.imagebitmap = imagebitmap;
		this.name = name;
		this.description = description;
		this.link = link;
		this.id = id;
		this.picturelink = "Done";
		
	this.message = message;
	}

	public String getpicturelink(){
		return picturelink;
		
	}
	public void setpicturelink(String picturelink){
		
		this.picturelink = picturelink;
	}
	public String getmessage(){
		return message;
		
	}
	public void setmessage(String message){
		
		this.message = message;
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
