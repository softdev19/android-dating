package com.unos.crescentapp.utilts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class GetBitMapFromURL extends AsyncTask<Void, Void, ArrayList<Bitmap>>{

 private String[] strUrl;
 private ArrayList<Bitmap>arrayBitmap;
  public GetBitMapFromURL(String[] strUrl){
	  this.strUrl = strUrl;
	  arrayBitmap =  new ArrayList<Bitmap>();
  }
	@Override
	protected ArrayList<Bitmap> doInBackground(Void... params) {
		for(int i=0;i<strUrl.length;i++){
			Bitmap myBitmap;
			try {
		        URL url = new URL(strUrl[i]);
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoInput(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        myBitmap = BitmapFactory.decodeStream(input);
		        arrayBitmap.add(myBitmap);
		    } catch (IOException e) {
		        // Log exception
		        return null;
		    }
		}
		return arrayBitmap;
	}

}
