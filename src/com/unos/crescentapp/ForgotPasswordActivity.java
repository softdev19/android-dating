package com.unos.crescentapp;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgotPasswordActivity extends Activity{
private EditText et_emailId;
private String userEmailId;
private ProgressDialog progressDialog;
private Button btnNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		et_emailId=(EditText) findViewById(R.id.activity_forgot_password_user_et_email);
		//btnNext=(Button) findViewById(R.id.activity_forgot_password_btn_next);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    if(validates()){
		    	makeJsonObjectRequestPost();
		    }
			
			}
		});
	}
	
	
	private boolean validates() {

		if (et_emailId.getText().toString().equals("")) {
			Toast.makeText(ForgotPasswordActivity.this,
					"Please Enter Your Email", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
 	/**
 	 * Method to make json object request where json response starts wtih {
 	 * */
 	private void makeJsonObjectRequestPost() {
 		if (!Utils.isNetworkConnected(getApplicationContext())) {
 			Toast.makeText(this,"Network not Available", Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		userEmailId=et_emailId.getText().toString();
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			params.put("email",userEmailId );	
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		new ForgotPasswordTasck(WebServiceURL.FORGOT_PASSWORD_URL, params).execute();
 	
 	}
 	private class ForgotPasswordTasck extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public ForgotPasswordTasck(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			 progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
			    progressDialog.setMessage("loading...");
			    progressDialog.show();
		
		
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser =new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			try {
				strRes = jObject.getString("success");
				strMessage = jObject.getString("message");
								
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return strRes;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
			if (progressDialog.isShowing()) {
			progressDialog.dismiss();
			
			}
			Toast.makeText(ForgotPasswordActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if(result.equalsIgnoreCase("true")){
				finish();
			}
			
			
		}

		
 		
 	}



}
