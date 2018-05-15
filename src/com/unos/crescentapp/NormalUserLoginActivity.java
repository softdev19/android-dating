package com.unos.crescentapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.result.Result;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.BaseActivity.RegQuickBloxTasck;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CrescentProgressDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NormalUserLoginActivity extends BaseActivity {
	private EditText et_EmailID,et_Password;
	private String login_userEmailId;
	private String login_userPassword,deviceId;
	//private ProgressDialog progressDialog;
	
	private CrescentProgressDialog progressDialog;
	private String email_id_forgotPass;
	private TextView txtForgotPassword,txtTitle,txtEmail,txtPassword;/*,txtHint,txtHint1*/
	private Button btnNext;
	private GoogleCloudMessaging gcmObj;
	private QBUser mQbUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_login_registered_user);
		et_EmailID=(EditText) findViewById(R.id.activity_normal_logi_et_email);
		et_Password=(EditText)findViewById(R.id.activity_normal_login_et_password);
		btnNext=(Button) findViewById(R.id.activity_normal_login_btn_next);
		txtForgotPassword = (TextView)findViewById(R.id.activity_normal_login_tv_forgot_password);
		txtTitle = (TextView) findViewById(R.id.activity_normal_login_txtvw_title);
		//txtHint = (TextView) findViewById(R.id.activity_normal_login_txtvw_hints);
		//txtHint1 = (TextView) findViewById(R.id.activity_normal_login_txtvw_hints1);
		
		txtEmail = (TextView) findViewById(R.id.activity_normal_login_txtvw_email);
		txtPassword = (TextView) findViewById(R.id.activity_normal_login_txtvw_passwd);
		
		txtTitle.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnNext.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtEmail.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtPassword.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		
		et_EmailID.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		et_Password.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		//txtHint.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		//txtHint1.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtForgotPassword.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		SpannableString content = new SpannableString(getString(R.string.forgot_pass));
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		txtForgotPassword.setText(content);
		deviceId = MySharedPrefs.getString(NormalUserLoginActivity.this, "", MySharedPrefs.DEVICE_ID);
		Log.v("Device ID", deviceId);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(validates()){
					makeJsonObjectRequestPost();
				}
				
			}
		});
		txtForgotPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				callAlert(NormalUserLoginActivity.this);
			}
		});
		findViewById(R.id.activity_normal_login_imgbtn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});
	}
	
	private boolean validates(){
		
		if(et_EmailID.getText().toString().equals("")){
			Toast.makeText(NormalUserLoginActivity.this,"Please Enter Your Email", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!isEmail(et_EmailID.getText().toString())){
			Toast.makeText(NormalUserLoginActivity.this,"Email ID is Invalid", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(et_Password.getText().toString().equals("")){
			Toast.makeText(NormalUserLoginActivity.this,"Please Enter Password", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		return true;
	}
	 private boolean isEmail(String email){
			String regExpn ="^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
		                   +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
		                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
		                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
		     Matcher matcher = pattern.matcher(email);

		     if(matcher.matches())
		        return true;
		     else
		        return false;
		}
	 
	 private void registerInBackground() {
			new AsyncTask<Void, Void, String>() {
				@Override
				protected String doInBackground(Void... params) {
					Log.d("Asyntask", "Entire Class");
					String msg = "";
					try {
						if (gcmObj == null) {
							gcmObj = GoogleCloudMessaging
									.getInstance(NormalUserLoginActivity.this);
						}
						deviceId = gcmObj
								.register(Constants.GOOGLE_PROJ_ID);
						
						
					} catch (IOException ex) {
						msg = "Error :" + ex.getMessage();
						//Toast.makeText(getActivity(), getResources().getString(R.string.service_not_available), Toast.LENGTH_LONG).show();
					}
					return msg;
				}

				@Override
				protected void onPostExecute(String msg) {
					MySharedPrefs.saveString(NormalUserLoginActivity.this, deviceId, MySharedPrefs.DEVICE_ID);
						Log.v("reg_id", msg);
						}
			}.execute(null, null, null);
		}
	 
	 
	 
	 	private void makeJsonObjectRequestPost() {
	 		if (!Utils.isNetworkConnected(NormalUserLoginActivity.this) ) {
	 			Toast.makeText(this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
	 			//showToast(getResources().getString(R.string.network_not_avail));
	 			return;
	 		}
	 		if(!Utils.isReachable(NormalUserLoginActivity.this)){
	 			Toast.makeText(NormalUserLoginActivity.this,getResources().getString(R.string.unable_connect), Toast.LENGTH_SHORT).show();
	 			return;
	 		}
	 		
	 		
	 		
	 		login_userEmailId=et_EmailID.getText().toString();
			login_userPassword=et_Password.getText().toString();
	 		
	 		 if(deviceId.equals("")){
	 			 registerInBackground();
	 		 }
	 		 Log.v("device_id", deviceId);
	 		HashMap<String, String> params = new HashMap<String, String>();
	 		try {
	 			
	 			params.put("email",login_userEmailId );
	 			params.put("password",login_userPassword);
	 			params.put("devicetoken", deviceId);
	 			params.put("device","android");
	 			
	 		} catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
	 		new LoginTask(WebServiceURL.LOGIN_URL, params).execute();
	 	
	 	}
	 	private class LoginTask extends AsyncTask<Void,Void,String>{
	 		private String strUrl,strRes,strMessage;
	 		private HashMap<String,String>bodyParams;
	 		public LoginTask(String strUrl,HashMap<String,String>bodyParams){
	 			this.strUrl = strUrl;
	 			this.bodyParams = bodyParams;
	 		}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// progressDialog = new ProgressDialog(NormalUserLoginActivity.this);
				    progressDialog = new CrescentProgressDialog(NormalUserLoginActivity.this);
				    progressDialog.setMessage("Signing In");
				    progressDialog.setProgressStyle(R.style.customprogressdialog);
				    progressDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
				    
				    progressDialog.show();
			
			}
			@Override
			protected String doInBackground(Void... params) {
				JSONPostParser parser =new JSONPostParser();
				JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
				if(jObject!=null){
					try {
						strRes = jObject.getString("success");
						strMessage = jObject.getString("message");
						if (strRes.equalsIgnoreCase("true")) {
							JSONObject jobj=jObject.getJSONObject("user");
							Gson gson = new Gson();
							mLogUser = gson.fromJson(jobj.toString(), LogUser.class);
							
						}
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return strRes;
			}

			@SuppressLint("InlinedApi")
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				
				if (progressDialog.isShowing()) {
				progressDialog.dismiss();
				
				}
				
				if(result.equalsIgnoreCase("true")){
					MySharedPrefs.saveString(NormalUserLoginActivity.this, mLogUser.getUserId(), MySharedPrefs.USER_ID);
					 Gson gson = new Gson();
					 String userDetails = gson.toJson(mLogUser);
					 MySharedPrefs.saveString(NormalUserLoginActivity.this, userDetails, MySharedPrefs.USER_DATA);
						if (mLogUser.hasValidQuickbloxId()) {
							Intent intent =new Intent(NormalUserLoginActivity.this,WantActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
							finish();
						} else {
							new RegQuickBloxTasck(mLogUser) {
								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									if (mLogUser.hasValidQuickbloxId()) {
										Intent intent =new Intent(NormalUserLoginActivity.this,WantActivity.class);
										startActivity(intent);
										overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
										finish();
									}
								}
							}.execute();
	
					}
				}else
					Toast.makeText(NormalUserLoginActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			}	
	 		
	 	}
		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		}
	
		private  void callAlert(final Context context){
		     final Dialog dialog = new Dialog(context);
		    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		     dialog.setContentView(R.layout.activity_forgot_password);
		   
		     dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		        TextView txtTitle = (TextView) dialog.findViewById(R.id.activity_forgot_passwd_txtvw_title);
		        TextView txtEmail = (TextView) dialog.findViewById(R.id.activity_forgot_passwd_txtvw_email);
		        txtTitle.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		        txtEmail.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		        Button button_submit = (Button) dialog.findViewById(R.id.activity_forgot_password_user_btn_submit);
		        button_submit.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		     button_submit.setOnClickListener(new OnClickListener() {          
		         public void onClick(View v) {
		          EditText editEmail=(EditText)dialog.findViewById(R.id.activity_forgot_password_user_et_email);
		          editEmail.setTypeface(Utils.getTypeface(NormalUserLoginActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		          email_id_forgotPass=editEmail.getText().toString().trim();
		          if (email_id_forgotPass !=null && !email_id_forgotPass.equals("")) {
		           makeJsonObjectRequestPostForgotPassword(); 
		           dialog.dismiss();
		          }else
		        	  Toast.makeText(NormalUserLoginActivity.this, "Please Enter your Email", Toast.LENGTH_LONG).show();
		          
		         }
		     });
		     dialog.show();
		 }

	private void makeJsonObjectRequestPostForgotPassword() {
		if (!Utils.isNetworkConnected(getApplicationContext())) {
			Toast.makeText(NormalUserLoginActivity.this, getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if(!Utils.isReachable(NormalUserLoginActivity.this)){
			Toast.makeText(NormalUserLoginActivity.this, getResources().getString(R.string.unable_connect), Toast.LENGTH_SHORT)
			.show();
			return;
		}
		HashMap<String, String> params = new HashMap<String, String>();
		try {

			params.put("email", email_id_forgotPass);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ForgotPasswordTask(WebServiceURL.FORGOT_PASSWORD_URL, params)
				.execute();

	}

	private class ForgotPasswordTask extends AsyncTask<Void, Void, Void> {
		private String strUrl, strRes, strMessage;
		private HashMap<String, String> bodyParams;

		public ForgotPasswordTask(String strUrl,
				HashMap<String, String> bodyParams) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//progressDialog = new ProgressDialog(NormalUserLoginActivity.this);
			progressDialog = new CrescentProgressDialog(
					NormalUserLoginActivity.this);

			progressDialog.setProgressStyle(R.style.customprogressdialog);
			progressDialog.getWindow().setGravity(
					Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			progressDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONPostParser parser = new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);

			// JSONParser parse=new JSONParser();

			try {

				strRes = jObject.getString("success");
				strMessage = jObject.getString("message");
				if (strRes.equalsIgnoreCase("true")) {
					/*
					 * JSONObject jobj=jObject.getJSONObject("user"); userID
					 * =jobj.getString("user_id");
					 */
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();

			}

			Toast.makeText(NormalUserLoginActivity.this, "" + strMessage,
					Toast.LENGTH_LONG).show();

		}

	}
	
	
}
