package com.example.nsitapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.anshul.nsitapp.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Society_tech extends SherlockFragment implements
		OnItemClickListener {

	int t;
	SharedPreferences sp;
	FileOutputStream out;
	static ArrayAdapter<RowItem> customAdapter;
	File filePath;
	ArrayList<RowItem> SocityTechfeed = new ArrayList<RowItem>();
	PullToRefreshListView lvsocietytech;
	Helper Help;
	JSONArray techjsonarray;

	String[][] techfeedkey = new String[20][20];

	public String proggroupid = "162982077078900";
	public String ieeeid = "190501900973620";
	public String csiid = "126976547314225";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Help = new Helper(this.getSherlockActivity());
	}
	public static void filteradapter(String string){
		if(customAdapter !=null){
			customAdapter.getFilter().filter(string);
			customAdapter.notifyDataSetChanged();
			}
	}
	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle saveBundle) {
		View view = inflator.inflate(R.layout.society_tech, container, false);
		lvsocietytech = (PullToRefreshListView) view
				.findViewById(R.id.lvsocietytech);
		try {
			setcontent();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;

	}

	private void setcontent() throws FileNotFoundException {
		// TODO Auto-generated method stub

		Help.Getsaveddata("SocietyTech", SocityTechfeed,0);
		customAdapter = new CustomListViewAdapter(this.getActivity(),
				R.layout.list_item, SocityTechfeed);

		lvsocietytech.setAdapter(customAdapter);
		lvsocietytech.getRefreshableView().setOnItemClickListener(this);
		lvsocietytech.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
if(Help.haveNetworkConnection()){
				Log.d("aefs", "Refresh Called");
				new read().execute(csiid);
			}else{
				Toast.makeText(getSherlockActivity(), "Check Your Network Connection", Toast.LENGTH_LONG).show();
				lvsocietytech.onRefreshComplete();
			}
			}
		});
	}

	public void onItemClick(AdapterView<?> parent, View view, int posititon,
			long arg3) {
		// TODO Auto-generated method stub

		RowItem item = (RowItem) customAdapter.getItem(posititon - 1);
		try {
			out = this.getActivity().openFileOutput("image.png",
					Context.MODE_PRIVATE);
			item.getbitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent i = new Intent("android.intent.action.item");
		i.putExtra("position", posititon);
		i.putExtra("name", item.getname());
		i.putExtra("link", item.getlink());
		i.putExtra("description", item.getdescription());

		startActivity(i);

	}

	class read extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.d("AsyncTask", "Called");
			// Retrive Data here

			if (Help.haveNetworkConnection()) {

				try {
					Log.d("dService", "on jsonarray");
					techjsonarray = Helper.getjsonarrayfromfacebookid(
							params[0], true);
					JSONArray ieeejsonarray = Helper
							.getjsonarrayfromfacebookid(ieeeid, false);
					JSONArray proggrpJsonArray = Helper
							.getjsonarrayfromfacebookid(proggroupid, false);
					techjsonarray = Help.combinejsonarray(techjsonarray,
							ieeejsonarray);
					techjsonarray = Help.combinejsonarray(techjsonarray,
							proggrpJsonArray);

					SocityTechfeed = Help.GETmoredata(SocityTechfeed,
							techjsonarray);
					publishProgress(12);
					Help.getimagedata(SocityTechfeed);
					publishProgress(16);
					Help.SaveData("SocietyTech", SocityTechfeed);
					Log.d("SocietyTechdata", "you did it");
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}else{
			Toast.makeText(getActivity(), "Check Your Network Connection",
					Toast.LENGTH_LONG).show();
			publishProgress(20);
		//	lvsocietytech.onRefreshComplete();
			}
			return "zingalala";

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			if(values[0] !=20){
			customAdapter = new CustomListViewAdapter(getActivity(),
					R.layout.list_item, SocityTechfeed);

			lvsocietytech.setAdapter(customAdapter);
			if (values[0] == 16) {
				lvsocietytech.onRefreshComplete();
			}
			}else{
				lvsocietytech.onRefreshComplete();
			}
		}
			

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			// process data here

		}

	}
}
