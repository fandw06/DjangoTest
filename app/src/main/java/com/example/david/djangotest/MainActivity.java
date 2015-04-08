package com.example.david.djangotest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.http.HttpPackage;
import com.example.david.http.HttpRequestTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends ActionBarActivity {

    private EditText name;
    private EditText data;
    private EditText keyName;
    private Button upload;
    private Button search;
    private TextView result;

    private ConnectivityManager connMgr;
    private static final String url = "http://inertia.ece.virginia.edu/assist_web/api/v1/data/";
    private static final String TAG = "ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* A manager to check if the network is available. */
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        upload = (Button) findViewById(R.id.btnUpload);
        upload.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // Check network availability
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Perform the upload function
                    JSONObject jData = new JSONObject();
                    try {
                        jData.put("name", name.getText().toString());
                        jData.put("JSON_data", data.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    result.setText(jData.toString());
                    HttpPackage hp = new HttpPackage(url, jData, HttpPackage.HTTP_POST);
                    new HttpRequestTask().execute(hp);

                } else {
                    Toast.makeText(MainActivity.this, "Network unavailable!", Toast.LENGTH_SHORT);
                }
            }

        });

        search = (Button) findViewById(R.id.btnSearch);
        search.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                // Check network availability
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    StringBuilder urln = new StringBuilder(url);
                    urln.append(keyName.getText().toString());
                    //    urln.append("?format=json");
                    result.setText(urln.toString());
                    Log.d(TAG, urln.toString());

                    JSONObject jData = null;
                    HttpPackage hp = new HttpPackage(urln.toString(), null, HttpPackage.HTTP_GET);
                    try {
                        jData = new HttpRequestTask().execute(hp).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(jData!=null)
                        result.setText(jData.toString());
                    else
                        result.setText("No result");
                } else {
                    Toast.makeText(MainActivity.this, "Network unavailable!", Toast.LENGTH_SHORT);
                }
            }

        });

        name = (EditText) findViewById(R.id.name);
        data = (EditText) findViewById(R.id.data);
        keyName = (EditText) findViewById(R.id.in_name);
        result = (TextView) findViewById(R.id.result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
