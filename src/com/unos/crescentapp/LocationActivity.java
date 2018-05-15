package com.unos.crescentapp;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.gpstracker.GPSTracker;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.view.CircleImageView;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.RippleBackground;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends BaseActivity {

	private CircleImageView imgprofileImage;
	private String[] strImage;
	private LocationManager manager;
	private GPSTracker gpsTracker;
	private ProgressDialog progDialog;
	private RippleBackground rippleBackground;
	private TextView txtFindPeople;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		imgprofileImage = (CircleImageView) findViewById(R.id.activity_location_imgvw_user);
		txtFindPeople = (TextView) findViewById(R.id.activity_location_txtvw_find_people);
		txtFindPeople.setTypeface(Utils.getTypeface(LocationActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		if (mLogUser.getImage().length > 0) {
			strImage = new String[mLogUser.getImage().length];
			strImage = mLogUser.getImage();
			loadProfileImage(strImage[0], imgprofileImage);
		}
		rippleBackground = (RippleBackground) findViewById(R.id.activity_location_ripple_background);
		rippleBackground.startRippleAnimation();
		/*
		 * findViewById(R.id.activity_location_btn_allow_my_location).
		 * setOnClickListener( new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { startActivity(new
		 * Intent(LocationActivity.this,MyProfileActivity.class)); } });
		 */

		/*
		 * manager = (LocationManager) this.getSystemService(
		 * Context.LOCATION_SERVICE ); if ( !manager.isProviderEnabled(
		 * LocationManager.GPS_PROVIDER ) ) {
		 * Utils.buildAlertMessageNoGps(LocationActivity.this); }else{
		 * gpsTracker = new GPSTracker(LocationActivity.this);
		 * Constants.Latitude = String.valueOf(gpsTracker.getLatitude());
		 * Constants.Longitude = String.valueOf(gpsTracker.getLongitude()); }
		 */

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(LocationActivity.this, DiscoveryActivity.class));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
				finish();
			}
		}, 3000);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		rippleBackground.stopRippleAnimation();

	}

	private void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader.get(url,
				ImageLoader.getImageListener(imageview, R.drawable.add_your_photo, R.drawable.add_your_photo));

	}

	private void makeJsonObjectRequestPost() {
		if (!Utils.isNetworkConnected(LocationActivity.this)) {
			Toast.makeText(LocationActivity.this, "Network not Available", Toast.LENGTH_SHORT).show();

			return;
		}
		if (!Utils.isReachable(LocationActivity.this)) {
			Toast.makeText(LocationActivity.this, "Server Error Occured", Toast.LENGTH_SHORT).show();
			return;
		}

		HashMap<String, String> params = new HashMap<String, String>();
		try {
			params.put("userid", mLogUser.getUserId());
			/*
			 * params.put("show",txtshow.getText().toString());
			 * params.put("agerange", txtRange.getText().toString());
			 */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// new DiscoverTask(WebServiceURL.UPDATE_DISCOVER, params).execute();

	}

	private class SendLocationTask extends AsyncTask<Void, Void, String> {
		private String strUrl, strRes, strMessage;
		private HashMap<String, String> bodyParams;

		public SendLocationTask(String strUrl, HashMap<String, String> bodyParams) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser = new JSONPostParser();
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

			// Toast.makeText(WantActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if (strRes.equalsIgnoreCase("true")) {
				Intent intent = new Intent(LocationActivity.this, DiscoveryActivity.class);
				startActivity(intent);
				finish();
			} else
				Toast.makeText(LocationActivity.this, "" + strMessage, Toast.LENGTH_LONG).show();
		}

	}

}
