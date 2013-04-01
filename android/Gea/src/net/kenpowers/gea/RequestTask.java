package net.kenpowers.gea;

import java.io.*;
import android.util.*;
import java.net.*;

import android.os.AsyncTask;



public class RequestTask extends AsyncTask<GeaServerRequest, String, String> {
	private RequestTaskCompleteListener callback;
	private GeaServerRequest request;
	
	public RequestTask(RequestTaskCompleteListener callback) {
		this.callback = callback;
	}
	
	protected String doInBackground(GeaServerRequest... request) {
        int statusCode = 0;
        this.request = request[0];
        
        try {
        	URL url = new URL(this.request.getURL());
        	HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        	connection.setRequestMethod(this.request.getRequestMethod());
        	connection.setRequestProperty("Content-length", "0");
        	connection.connect();
        	statusCode = connection.getResponseCode();
        	if (statusCode == 200) {
        		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();
        	} else {
        		Log.e(MainActivity.LOG_TAG, "Error connecting to Gea server: " + statusCode);
        		return null;
        	}
        	
        } catch (MalformedURLException e) {
        	Log.d(MainActivity.LOG_TAG, "malformed URL " + this.request.getURL());
        } catch (IOException e) {
        	e.printStackTrace();
        }        
        
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        callback.onTaskComplete(request, result);
    }
}
