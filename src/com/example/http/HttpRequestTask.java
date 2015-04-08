// Copyright 2013 Google Inc. All Rights Reserved.

package com.example.http;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/**
 * An AsyncTask that maintains a connected client.
 */
public class HttpRequestTask extends AsyncTask<HttpPackage, Integer, JSONObject> {
	
	@Override
	protected JSONObject doInBackground(HttpPackage... params) {
		byte command = params[0].command;
		JSONObject res = null;
		switch(command){
			case HttpPackage.HTTP_POST:  
				Log.d("NAME", "HTTP_POST");
				HttpRequest hr = new HttpRequest();  		
				hr.sendJSONPost(params[0].url, params[0].data);
				hr.abort();
				break;
				
			case HttpPackage.HTTP_GET:
				Log.d("NAME", "HTTP_GET");
				HttpRequest hr2 = new HttpRequest();  
				res = hr2.sendGet(params[0].url);
				break;
		}
		
		return res;
	}
		
    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(Long result) {

    }
}
