package com.unos.crescentapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends BaseActivity {
	private String userId;
	private ToggleButton tgbtnNewMatches,tgbtnMessage;
	private String strNewMatches="0",strMessage="0";
	private Button  btnLogOut,btnDeleteAccount;
	private RelativeLayout rl_privacyPolicy,rl_termsOfService;
	private TextView txtMessage,txtMatches,txtTitle,txtPrivacyPolicy,txtTerms;
	private Button btnDone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		txtTitle = (TextView) findViewById(R.id.activity_settings_txtvw_title);
		txtMatches = (TextView) findViewById(R.id.activity_settings_txtvw_new_matches);
		txtMessage = (TextView) findViewById(R.id.activity_settings_txtvw_message);
		txtPrivacyPolicy = (TextView) findViewById(R.id.activity_settings_text_view_privacy);
		txtTerms = (TextView) findViewById(R.id.activity_settings_text_view_terms);
		tgbtnMessage=(ToggleButton)findViewById(R.id.activity_settings_tgbtn_message);
		tgbtnNewMatches=(ToggleButton)findViewById(R.id.activity_settings_tgbtn_new_matches);
		btnDeleteAccount = (Button) findViewById(R.id.activity_settings_btn_delete);
		btnDone=(Button) findViewById(R.id.activity_settings_btn_done);
		rl_privacyPolicy=(RelativeLayout) findViewById(R.id.activity_settings_rl_privacy_policy);
		rl_termsOfService=(RelativeLayout) findViewById(R.id.activity_settings_rl_terms_services);
		btnLogOut = (Button) findViewById(R.id.activity_settings_btn_logout);
		userId=MySharedPrefs.getString(SettingsActivity.this, "", MySharedPrefs.USER_ID);
		txtTitle.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtMatches.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtMessage.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtPrivacyPolicy.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtTerms.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		btnDeleteAccount.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		btnLogOut.setTypeface(Utils.getTypeface(SettingsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		
		btnLogOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!userId.equals(""))
					showAlert();
			}
		});
		strNewMatches = mLogUser.getNewmatchesnotifystatus();
		if(strNewMatches.equals("1")){
			//strNewMatches = MySharedPrefs.getString(SettingsActivity.this, "", MySharedPrefs.NEW_MATCH_ALERT);
			tgbtnNewMatches.setChecked(true);
		}
		strMessage = mLogUser.getMessagesnotifystatus();
		if(mLogUser.getMessagesnotifystatus().equals("1")){
			//strMessage = MySharedPrefs.getString(SettingsActivity.this, "", MySharedPrefs.MESSAGE_ALERT);
			tgbtnMessage.setChecked(true);
		}
		
		tgbtnNewMatches.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					strNewMatches="1";
				}else{
					strNewMatches="0";
				}
			}
		});
		tgbtnMessage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					strMessage = "1";
				}else{
					strMessage="0";
				}
				
			}
		});
		findViewById(R.id.activity_settings_imgbtn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});
		btnDone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//startActivity(new Intent(SettingsActivity.this, DiscoveryActivity.class));
				updateNotificationStatus(WebServiceURL.UPDATE_NOTIFICATION_SETTINGS);
				/*finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);*/
			}
		});
		
		rl_privacyPolicy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				copyReadAssatePrivacyPolicy();
			}
		});
		rl_termsOfService.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				copyReadAssetsTerm();
			}
		});
		btnDeleteAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				makeJsonObjectRequestPostAccount(WebServiceURL.DELETE_ACCOUNT);
			}
		});
		
	}
	public void showAlert(){
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
	      alertDialogBuilder.setTitle("Crescent");
	      alertDialogBuilder.setMessage("Would you like to Logout");
	      alertDialogBuilder.setPositiveButton("Yes", 
	      new DialogInterface.OnClickListener() {
			
	         @Override
	         public void onClick(DialogInterface dialog, int arg1) {
	        	 dialog.cancel();
	        	 makeJsonObjectRequestPostAccount(WebServiceURL.LOGOUT);
	         }
	      });
	      alertDialogBuilder.setNegativeButton("No", 
	      new DialogInterface.OnClickListener() {
				
	         @Override
	         public void onClick(DialogInterface dialog, int which) {
	        	 dialog.cancel();
			 }
	      });
		    
	      AlertDialog alertDialog = alertDialogBuilder.create();
	      alertDialog.show();
		    
	   }
	
 private void copyReadAssetsTerm(){
	     
         AssetManager assetManager = getAssets();

         InputStream in = null;
         OutputStream out = null;
         File file = new File(getFilesDir(), "CrescentTerms.pdf");
         try
         {
             in = assetManager.open("CrescentTerms.pdf");
             out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

             copyFile(in, out);
             in.close();
             in = null;
             out.flush();
             out.close();
             out = null;
         } catch (Exception e)
         {
             Log.e("tag", e.getMessage());
         }
         try {
           Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setDataAndType(
                      Uri.parse("file://" + getFilesDir() + "/CrescentTerms.pdf"),
                      "application/pdf");

              startActivity(intent);
  } catch (ActivityNotFoundException e) {
   Toast.makeText(SettingsActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
  }

       
     }
  //For Privacy Policy
  private void copyReadAssatePrivacyPolicy(){
    AssetManager assetManager = getAssets();

          InputStream in = null;
          OutputStream out = null;
          File file = new File(getFilesDir(), "CrescentPrivacyPolicy.pdf");
          try
          {
              in = assetManager.open("CrescentPrivacyPolicy.pdf");
              out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
          } catch (Exception e)
          {
              Log.e("tag", e.getMessage());
          }
          try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setDataAndType(
                       Uri.parse("file://" + getFilesDir() + "/CrescentPrivacyPolicy.pdf"),
                       "application/pdf");

               startActivity(intent);
   } catch (ActivityNotFoundException e) {
    Toast.makeText(SettingsActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
   }

   
  }

     private void copyFile(InputStream in, OutputStream out) throws IOException
     {
         byte[] buffer = new byte[1024];
         int read;
         while ((read = in.read(buffer)) != -1)
         {
             out.write(buffer, 0, read);
         }
     }
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}



	/**
 	 * Method to make json object request where json response starts wtih {
 	 * */
 	private void makeJsonObjectRequestPostAccount(String strUrl) {
 		if (!Utils.isNetworkConnected(SettingsActivity.this)) {
 			//Toast.makeText(this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
 			showToast(getResources().getString(R.string.network_not_avail));
 			return;
 		}
 		if(!Utils.isReachable(SettingsActivity.this)){
 			showToast(getResources().getString(R.string.unable_connect));
 			return;
 		}
 		
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			params.put("userid",userId);
 			
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		new AccountTask(strUrl, params).execute();
 	
 	}
 	private class AccountTask extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public AccountTask(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress();
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser =new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if(jObject!=null)
			try {
				strRes = jObject.getString("success");
				strMessage = jObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return strMessage;
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			stopProgress();
			if(result!=null && !result.equalsIgnoreCase("")){
				showToast(result);
				if(strRes!=null && strRes.equalsIgnoreCase("true")){
					MySharedPrefs.saveString(SettingsActivity.this, "", MySharedPrefs.USER_ID);
					MySharedPrefs.saveString(SettingsActivity.this, "", MySharedPrefs.MESSAGE_ALERT);
					MySharedPrefs.saveString(SettingsActivity.this, "", MySharedPrefs.NEW_MATCH_ALERT);
					MySharedPrefs.saveString(SettingsActivity.this, "", MySharedPrefs.USER_DATA);
					Intent intent = new Intent(SettingsActivity.this,UserLoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				}
			}
			
		}
 	}
 	private void updateNotificationStatus(String strUrl){
 		if (!Utils.isNetworkConnected(SettingsActivity.this)) {
 			//Toast.makeText(this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
 			showToast(getResources().getString(R.string.network_not_avail));
 			return;
 		}
 		if(!Utils.isReachable(SettingsActivity.this)){
 			showToast(getResources().getString(R.string.unable_connect));
 			return;
 		}
 		
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			params.put("userid",userId);
 			params.put("newmatches", strNewMatches);
 			params.put("messages", strMessage);
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		Log.v("newmatches", strNewMatches+" "+strMessage);
 		MySharedPrefs.saveString(SettingsActivity.this, strNewMatches, MySharedPrefs.NEW_MATCH_ALERT);
 		MySharedPrefs.saveString(SettingsActivity.this, strMessage, MySharedPrefs.MESSAGE_ALERT);
 		new UpdateNotificationTask(strUrl, params).execute();
 	}
 	private class UpdateNotificationTask extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public UpdateNotificationTask(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress();
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser =new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if(jObject!=null)
			try {
				strRes = jObject.getString("success");
				strMessage = jObject.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return strMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			stopProgress();
			if(result!=null && !result.equalsIgnoreCase("")){
				showToast(result);
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
			
		}
 	}

}
