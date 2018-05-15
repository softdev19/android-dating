package com.unos.crescentapp.notification;

import java.util.HashMap;

import org.json.JSONObject;
import com.unos.crescentapp.utilts.JSONPostParser;

import android.os.AsyncTask;

public class SendNotificationTask extends AsyncTask<Void,Void,Void> {
	private String strUrl;
	private HashMap<String,String> bodyParams;
	public SendNotificationTask(String strUrl,HashMap<String,String> bodyParams){
		this.strUrl = strUrl;
		this.bodyParams = bodyParams;
		
	}
	@Override
	protected Void doInBackground(Void... params) {
		JSONObject jObj = new JSONPostParser().getJSONFromUrl(strUrl, bodyParams);
		return null;
	}

}
