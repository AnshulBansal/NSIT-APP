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
import com.example.nsitapp.Society_auto.read;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class Society_auto extends SherlockFragment implements
OnItemClickListener {

int t;
SharedPreferences sp;
FileOutputStream out;
static ArrayAdapter<RowItem> customAdapter;
File filePath;
ArrayList<RowItem> SocityAutofeed = new ArrayList<RowItem>();
PullToRefreshListView lvsocietyAuto;
Helper Help;
JSONArray Autojsonarray;

String[][] cultfeedkey = new String[20][20];

public String motorsportsid = "354302918553";
public String bhrid = "109582689081817";

@Override
public void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
super.onCreate(savedInstanceState);
Help = new Helper(this.getSherlockActivity());
}

public View onCreateView(LayoutInflater inflator, ViewGroup container,
	Bundle saveBundle) {
View view = inflator.inflate(R.layout.society_auto, container, false);
lvsocietyAuto = (PullToRefreshListView) view
		.findViewById(R.id.lvsocietyauto);
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

Help.Getsaveddata("SocietyAuto", SocityAutofeed,0);
customAdapter = new CustomListViewAdapter(this.getActivity(),
		R.layout.list_item, SocityAutofeed);

lvsocietyAuto.setAdapter(customAdapter);
lvsocietyAuto.getRefreshableView().setOnItemClickListener(this);
lvsocietyAuto.setOnRefreshListener(new OnRefreshListener<ListView>() {
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// Do work to refresh the list here.
if(Help.haveNetworkConnection()){
		Log.d("aefs", "Refresh Called");
		new read().execute(bhrid);
	}else{
		Toast.makeText(getSherlockActivity(), "Check Your Network Connection", Toast.LENGTH_LONG).show();
		lvsocietyAuto.onRefreshComplete();
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
public static void filteradapter(String string){
	if(customAdapter !=null){
		customAdapter.getFilter().filter(string);
		customAdapter.notifyDataSetChanged();
		}
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
			Autojsonarray = Helper.getjsonarrayfromfacebookid(
					params[0], false);
			JSONArray ieeejsonarray = Helper
					.getjsonarrayfromfacebookid(motorsportsid, false);
			
			Autojsonarray = Help.combinejsonarray(Autojsonarray,
					ieeejsonarray);
			
			SocityAutofeed = Help.GETmoredata(SocityAutofeed,
					Autojsonarray);
			publishProgress(12);
			Help.getimagedata(SocityAutofeed);
			publishProgress(16);
			Help.SaveData("SocietyAuto", SocityAutofeed);
			Log.d("SocietyAutodata", "you did it");
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

//	lvsocietyAuto.onRefreshComplete();
	}
	return "zingalala";

}

@Override
protected void onProgressUpdate(Integer... values) {
	// TODO Auto-generated method stub
	
	customAdapter = new CustomListViewAdapter(getActivity(),
			R.layout.list_item, SocityAutofeed);

	lvsocietyAuto.setAdapter(customAdapter);
	if (values[0] == 16) {
		lvsocietyAuto.onRefreshComplete();
	}
	
}

@Override
protected void onPostExecute(String result) {
	// TODO Auto-generated method stub

	// process data here

}

}
}

