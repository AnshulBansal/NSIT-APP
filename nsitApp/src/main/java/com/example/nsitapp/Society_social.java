package com.example.nsitapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.anshul.nsitapp.R;
import com.example.nsitapp.Society_social.read;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class Society_social extends SherlockFragment implements
		OnItemClickListener {

	int t;
	SharedPreferences sp;
	FileOutputStream out;
	static ArrayAdapter<RowItem> customAdapter;
	File filePath;
	ArrayList<RowItem> SocitySocialfeed = new ArrayList<RowItem>();
	PullToRefreshListView lvsocietySocial;
	Helper Help;
	JSONArray Socialjsonarray;

	String[][] cultfeedkey = new String[20][20];

	public String rotoractid = "147372321996486";
	public String lftid = "204181666304550";
	public String teachforindiaid = "133235756696453";

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
		View view = inflator.inflate(R.layout.society_social, container, false);
		lvsocietySocial = (PullToRefreshListView) view
				.findViewById(R.id.lvsocietysocial);
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

		Help.Getsaveddata("SocietySocial", SocitySocialfeed,0);
		customAdapter = new CustomListViewAdapter(this.getActivity(),
				R.layout.list_item, SocitySocialfeed);

		lvsocietySocial.setAdapter(customAdapter);
		lvsocietySocial.getRefreshableView().setOnItemClickListener(this);
		lvsocietySocial.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.
if(Help.haveNetworkConnection()){
				Log.d("aefs", "Refresh Called");
				new read().execute(rotoractid);
			}else{
				Toast.makeText(getSherlockActivity(), "Check Your Network Connection", Toast.LENGTH_LONG).show();
				lvsocietySocial.onRefreshComplete();
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
					Socialjsonarray = Helper.getjsonarrayfromfacebookid(
							params[0], false);
					JSONArray ieeejsonarray = Helper
							.getjsonarrayfromfacebookid(lftid, false);

					Socialjsonarray = Help.combinejsonarray(Socialjsonarray,
							ieeejsonarray);

					SocitySocialfeed = Help.GETmoredata(SocitySocialfeed,
							Socialjsonarray);
					publishProgress(12);
					Help.getimagedata(SocitySocialfeed);
					publishProgress(16);
					Help.SaveData("SocietySocial", SocitySocialfeed);
					Log.d("SocietySocialdata", "you did it");
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
			}
			return "zingalala";

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
		
			customAdapter = new CustomListViewAdapter(getActivity(),
					R.layout.list_item, SocitySocialfeed);

			lvsocietySocial.setAdapter(customAdapter);
			if (values[0] == 16) {
				lvsocietySocial.onRefreshComplete();
			}
		
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			// process data here

		}

	}
}