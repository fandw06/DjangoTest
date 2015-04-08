package com.example.djangotest;

import java.util.concurrent.ExecutionException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.http.HttpPackage;
import com.example.http.HttpRequestTask;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText name;
	private EditText data;
	private EditText keyName;	
	private Button upload;
	private Button search;
	private TextView result;

	private String url;

		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		url = "http://inertia.ece.virginia.edu/assist_web/api/v1/data/";
		upload = (Button) findViewById(R.id.btnUpload);
		upload.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String curName = name.getText().toString();
				int curData = Integer.parseInt(data.getText().toString());
				
				String s = "Name: "+ curName+"\n"+"Data: "+curData;
				// Json includes name and data
				JSONObject jData = new JSONObject();
				try {
					jData.put("name", curName);
					jData.put("JSON_data", Integer.toString(curData));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				result.setText(jData.toString());	
				HttpPackage hp = new HttpPackage(url, jData, (byte)1);
				new HttpRequestTask().execute(hp);
			}
			
		});
		
		search = (Button) findViewById(R.id.btnSearch);
		search.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				StringBuffer urln = new StringBuffer(url);
				urln.append(keyName.getText().toString());
				result.setText(urln.toString());
				HttpPackage hp = new HttpPackage(urln.toString(), null, HttpPackage.HTTP_GET);				
				HttpRequestTask ht = new HttpRequestTask();
				ht.execute(hp);
				JSONObject jData = null;
				try {
					jData = ht.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(jData!=null)
					result.setText(jData.toString());
				else
					result.setText("No result");
			}
			
		});
		
		name = (EditText) findViewById(R.id.name);
		data = (EditText) findViewById(R.id.data);
		keyName = (EditText) findViewById(R.id.in_name);
		result = (TextView) findViewById(R.id.result);
	}

	private boolean checkNetwork() {
	    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
	    return  (networkInfo != null && networkInfo.isConnected());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
