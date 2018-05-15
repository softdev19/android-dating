package com.unos.crescentapp.utilts;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ServerReachable extends AsyncTask<Void,Void,Boolean>{
	private boolean isReachable=false;
	private Context context;
	public ServerReachable(Context context){
		this.context=context;
	}
	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		 ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnected()) {
		        try {
		            URL url = new URL("http://www.unosinfotech.in/");   
		            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		            urlc.setConnectTimeout(10 * 1000);         
		            urlc.connect();
		            if (urlc.getResponseCode() == 200) {  
		                Log.wtf("Connection", "Success !");
		                isReachable=true;
		                return isReachable;
		            } else {
		            	isReachable=false;
		                return isReachable;
		            }
		        } catch (MalformedURLException e1) {
		            return false;
		        } catch (IOException e) {
		            return false;
		        }
		    }
		    return isReachable;
	}
	
}