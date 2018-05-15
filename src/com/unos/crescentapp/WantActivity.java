package com.unos.crescentapp;




import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.view.CrescentProgressDialog;
import com.unos.crescentapp.view.RangeSeekBar;
import com.unos.crescentapp.view.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WantActivity extends BaseActivity  {
	
	
	private TextView txtshow,txtRange,txtTitleShow,txtAge,txtTitle;
	private Button btnNext;
	
	private String userId;
    private LinearLayout lnrLytAgeRange;
	private CrescentProgressDialog progressDialog;
	private ImageButton imgbtnBack;
	private String strShow="";
	private String[] strRange;
	private String strDiscovery;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_want);
		txtTitle = (TextView) findViewById(R.id.activity_want_txtvw_title);
		btnNext =  (Button) findViewById(R.id.activity_want_btn_next);
		txtshow = (TextView) findViewById(R.id.activity_want_txtvw_show_gender);
		txtRange = (TextView) findViewById(R.id.activity_want_txtvw_age_range);
		imgbtnBack = (ImageButton) findViewById(R.id.activity_want_imgbtn_back);
		lnrLytAgeRange = (LinearLayout) findViewById(R.id.activity_want_lnr_lyt_seekbar_age);
		txtTitleShow = (TextView) findViewById(R.id.activity_want_txtvw_show);
		txtAge = (TextView) findViewById(R.id.activity_want_txtvw_age);
		
		txtTitle.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnNext.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtTitleShow.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtshow.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtAge.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtRange.setTypeface(Utils.getTypeface(WantActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		//mLogUser = (LogUser) getIntent().getSerializableExtra("userDetails");
		 Log.v("Name", mLogUser.getName());
		RangeSeekBar<Number> rangeSeekBar = new RangeSeekBar<Number>(17, 59, WantActivity.this);
		lnrLytAgeRange.addView(rangeSeekBar);
			if(!mLogUser.getAgeRange().equals("")){
				txtRange.setText(mLogUser.getAgeRange());
				strRange= mLogUser.getAgeRange().split("-");
				rangeSeekBar.setSelectedMinValue(Integer.parseInt(strRange[0]));
				rangeSeekBar.setSelectedMaxValue(Integer.parseInt(strRange[1]));
			}
			if(!mLogUser.getShow().equals("")){
				if(mLogUser.getShow().equalsIgnoreCase("Men"))
				txtshow.setText("Only "+mLogUser.getShow());
				else
					txtshow.setText(mLogUser.getShow());
				strShow=mLogUser.getShow();
			}else{
				txtshow.setText("Men & Women");
				strShow="Men & Women";
			}
				
				
		
		if(getIntent().getStringExtra("discovery")!= null){
			imgbtnBack.setVisibility(View.VISIBLE);
			strDiscovery = getIntent().getStringExtra("discovery");
		}else
			imgbtnBack.setVisibility(View.GONE);
		txtshow.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//startActivity(new Intent(WantActivity.this, SearchActivity.class));
						final String options[]={"Only Men","Only Women","Men & Women","Cancel"};
						 new AlertDialog.Builder(WantActivity.this)
						 .setCancelable(false)
				            .setTitle("Show").setItems(options, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									 if(!options[which].equalsIgnoreCase("cancel")){
										txtshow.setText(options[which]);
									    if(options[which].equals("Only Men"))
									    	strShow = "Men";
									    else
									    	strShow = options[which];
									 }
										dialog.cancel();
								}
				            })
				            .show();
					}
				});
		
		rangeSeekBar.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Number>() {

			@Override
			public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar,
					Number minValue, Number maxValue) {
				txtRange.setText(minValue+"-"+maxValue);
			}
		});
		
		imgbtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!txtshow.getText().toString().trim().equals("") && !strShow.equals("") && strShow!=null)
					makeJsonObjectRequestPost();
					//Toast.makeText(WantActivity.this, ""+strShow, Toast.LENGTH_LONG).show();
				else
					Toast.makeText(WantActivity.this, "Please Enter Show", Toast.LENGTH_LONG).show();
			}
		});
	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		if(strDiscovery!=null && strDiscovery.equals("discovery")){
			finish();
		    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		}else
			super.onBackPressed();
	}


	private void makeJsonObjectRequestPost() {
 		if (!Utils.isNetworkConnected(WantActivity.this)) {
 			Toast.makeText(WantActivity.this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		if(!Utils.isReachable(WantActivity.this)){
 			Toast.makeText(WantActivity.this,getResources().getString(R.string.unable_connect), Toast.LENGTH_SHORT).show();
 			return;
 		}
 		
 		
 		
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			params.put("userid",mLogUser.getUserId());
 			params.put("show",strShow);
 			params.put("agerange", txtRange.getText().toString());
 			
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		new DiscoverTask(WebServiceURL.UPDATE_DISCOVER, params).execute();
 	
 	}
 	private class DiscoverTask extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public DiscoverTask(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new CrescentProgressDialog(WantActivity.this);
		    progressDialog.setMessage("Signing In");
		    progressDialog.setProgressStyle(R.style.customprogressdialog);
		    progressDialog.getWindow().setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
		    
		    progressDialog.show();
		}
		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser =new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			try {
				strRes = jObject.getString("success");
				strMessage = jObject.getString("message");
				
			} catch (Exception e) {
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
			
			//Toast.makeText(WantActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if("true".equalsIgnoreCase(strRes)){
				/*if(strDiscovery!=null && strDiscovery.equals("discovery")){
					finish();
				    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				}else{*/
					mLogUser.setShow(strShow);
					mLogUser.setAgeRange(txtRange.getText().toString());
					Gson gson = new Gson();
					String userDetails = gson.toJson(mLogUser);
					MySharedPrefs.saveString(WantActivity.this, userDetails,
							MySharedPrefs.USER_DATA);
					Intent intent = new Intent(WantActivity.this,
							LocationActivity.class);
					//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					//finish();
					overridePendingTransition(R.anim.enter_from_right,
							R.anim.exit_to_left);
				} else {
					if(strMessage != null && !"".equalsIgnoreCase(strMessage)){
						showToast(strMessage);
					}
					Intent intent = new Intent(WantActivity.this,
							UserLoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			
		}

		
 		
 	}
}
