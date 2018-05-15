package com.unos.crescentapp.utilts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONPostParser {
	static InputStream is = null;
	static JSONObject jobj = null;
	 static String json = "";
	HttpResponse httpResponse=null;
	public JSONPostParser() {
		
	}
	
public JSONObject getJSONFromUrl(String url, HashMap<String,String> bodyParams) {
	   	 
        // Making HTTP request
        try {
        	HttpParams params = new BasicHttpParams();
        	//this how tiny it might seems, is actually absoluty needed. otherwise http client lags for 2sec.
        	HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        	HttpClient httpClient = new DefaultHttpClient(params);
            HttpPost httpPost = new HttpPost(url);
            
           
			httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			httpPost.setHeader("Accept", "application/json");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();  
			if (bodyParams != null) {
				for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
					pairs.add(new BasicNameValuePair(entry.getKey(),
							entry.getValue()));
					
				}
			}
			Log.d("JSONPostParser", "URL: " + url);
			Log.d("JSONPostParser", "Params: " + bodyParams.toString());
			httpPost.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();          
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.d("JSONPostParser", "Responce: " + sb.toString());
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
        	jobj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jobj;
 
    }
}
