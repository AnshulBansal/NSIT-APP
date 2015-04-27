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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.anshul.nsitapp.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Society_sig extends SherlockFragment implements
		OnItemClickListener {

	int t;
	SharedPreferences sp;
	FileOutputStream out;
	static ArrayAdapter<RowItem> customAdapter;
	File filePath;
	ArrayList<RowItem> SocietySigfeed = new ArrayList<RowItem>();
	static PullToRefreshListView lvSocietySig;
	Helper Help;
	JSONArray sigJsonArray;
	String junoonid = "481373328585455";
	String nsitqc = "185960271431856";

	String[][] sigfeedkey = new String[20][20];

	public String debsocid = "194466683916784";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Help = new Helper(this.getSherlockActivity());
	}

	public static void filteradapter(String string) {

		if (customAdapter != null) {
			customAdapter.getFilter().filter(string);
			customAdapter.notifyDataSetChanged();
		}

	}

	public View onCreateView(LayoutInflater inflator, ViewGroup container,
			Bundle saveBundle) {
		View view = inflator.inflate(R.layout.society_sig, container, false);
		lvSocietySig = (PullToRefreshListView) view
				.findViewById(R.id.lvsocietyssig);
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

		Help.Getsaveddata("SocietySig", SocietySigfeed,0);
		customAdapter = new CustomListViewAdapter(this.getActivity(),
				R.layout.list_item, SocietySigfeed);

		lvSocietySig.setAdapter(customAdapter);
		lvSocietySig.getRefreshableView().setOnItemClickListener(this);
		lvSocietySig.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Do work to refresh the list here.

				SharedPreferences getprefs = PreferenceManager
						.getDefaultSharedPreferences(getActivity());
				String values = getprefs.getString("List", "1");
				if (Help.haveNetworkConnection()) {
					if (values == "1") {
						Log.d("aefs", "Refresh Called");
						new read().execute(debsocid);

					} else if (values == "2") {
						new read().execute(junoonid);
					} else if (values == "3") {

						new read().execute(nsitqc);
					}
				} else {
					Toast.makeText(getSherlockActivity(),
							"Check Your Network Connection", Toast.LENGTH_LONG)
							.show();
					lvSocietySig.onRefreshComplete();

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
					sigJsonArray = Helper.getjsonarrayfromfacebookid(params[0],
							false);
					SocietySigfeed = Help.GETmoredata(SocietySigfeed,
							sigJsonArray);
					publishProgress(12);
					Help.getimagedata(SocietySigfeed);
					publishProgress(16);
					Help.SaveData("SocietySig", SocietySigfeed);
					Log.d("SocietySigdata", "you did it");
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

			} else {

			}
			return "zingalala";

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub

			customAdapter = new CustomListViewAdapter(getActivity(),
					R.layout.list_item, SocietySigfeed);

			lvSocietySig.setAdapter(customAdapter);
			if (values[0] == 16) {
				lvSocietySig.onRefreshComplete();
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			// process data here

		}

	}
}
