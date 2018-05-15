package com.unos.crescentapp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AppEventsLogger;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.UserInfoChangedCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.BaseActivity.RegQuickBloxTasck;
import com.unos.crescentapp.adapter.UserLoginViewPagerAapter;
import com.unos.crescentapp.asynctask.CreateSessionToken;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.gpstracker.GPSTracker;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends BaseActivity {
	private LocationManager manager;
	private UiLifecycleHelper uiHelper;
	private CirclePageIndicator pagerIndicator;
	int[] dasboard = { R.drawable.discover_potentia_bg,
			R.drawable.onboarding_bg2, R.drawable.onboarding_bg3 };
	private ViewPager viewPager;
	private TextView tv_dasboard_text;
	private LoginButton login_facebook;
	private GraphUser user;
	private ProgressDialog progressDialog;
	private String deviceId;
	private GoogleCloudMessaging gcmObj;
	private GPSTracker gpsTracker;

	final String[] PERMISSIONS = new String[] { "user_location",
			"user_birthday", "public_profile", "email" };
	private QBUser mQbUser;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall,
				Exception error, Bundle data) {
			Log.d("DatingApp", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall,
				Bundle data) {
			Log.d("DatingApp", "Success!");
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if ((exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(UserLoginActivity.this).setTitle("Error")
					.setMessage(exception.getLocalizedMessage())
					.setPositiveButton("Ok", null).show();
		}
		updateUI();
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void updateUI() {
		Session session = Session.getActiveSession();
		boolean enableButtons = (session != null && session.isOpened());
		if (enableButtons && user != null) {
			getDetailsFromFbUser(user);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		gpsTracker = new GPSTracker(this);
		setContentView(R.layout.activity_login_main_viewpager_view);
		pagerIndicator = (CirclePageIndicator) findViewById(R.id.dialog_viewpager_indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		tv_dasboard_text = (TextView) findViewById(R.id.inflate_viewpager_adapter_tv_tittle);
		UserLoginViewPagerAapter viewPagerAdapter = new UserLoginViewPagerAapter(
				this);
		viewPager.setAdapter(viewPagerAdapter);
		pagerIndicator.setViewPager(viewPager);
		deviceId = MySharedPrefs.getString(UserLoginActivity.this, "",
				MySharedPrefs.DEVICE_ID);
		Log.v("deviceID", deviceId);
		login_facebook = (LoginButton) findViewById(R.id.activity_user_login_btn_facebook);
		login_facebook.setPublishPermissions(PERMISSIONS);
		login_facebook
				.setUserInfoChangedCallback(new UserInfoChangedCallback() {

					@Override
					public void onUserInfoFetched(GraphUser user) {
						// TODO Auto-generated method stub
						UserLoginActivity.this.user = user;
						updateUI();
					}

				});

		if (deviceId.equals(""))
			registerInBackground();

		((TextView) findViewById(R.id.activity_user_login_tv_login))
				.setTypeface(Utils.getTypeface(UserLoginActivity.this,
						"fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_user_login_tv_login).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(UserLoginActivity.this,
								NormalUserLoginActivity.class));
						overridePendingTransition(R.anim.grow_from_middle,
								R.anim.shrink_to_middle);
						// finish();
					}
				});

		((TextView) findViewById(R.id.activity_user_login_tv_signup))
				.setTypeface(Utils.getTypeface(UserLoginActivity.this,
						"fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_user_login_tv_signup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(UserLoginActivity.this,
								SignUp1stActivity.class));
						overridePendingTransition(R.anim.grow_from_middle,
								R.anim.shrink_to_middle);
						// finish();
					}
				});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(UserLoginActivity.this, callback);
		uiHelper.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();

		uiHelper.onResume();
		AppEventsLogger.activateApp(UserLoginActivity.this);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
		Session session = Session.getActiveSession();
		if (session != null)
			session.closeAndClearTokenInformation();

	}

	/*
	 * @Override public void onSaveInstanceState(Bundle outState) {
	 * super.onSaveInstanceState(outState);
	 * uiHelper.onSaveInstanceState(outState); }
	 */

	private void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				Log.d("Asyntask", "Entire Class");
				String msg = "";
				try {
					if (gcmObj == null) {
						gcmObj = GoogleCloudMessaging
								.getInstance(UserLoginActivity.this);
					}
					deviceId = gcmObj.register(Constants.GOOGLE_PROJ_ID);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
					// Toast.makeText(getActivity(),
					// getResources().getString(R.string.service_not_available),
					// Toast.LENGTH_LONG).show();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				MySharedPrefs.saveString(UserLoginActivity.this, deviceId,
						MySharedPrefs.DEVICE_ID);
				Log.v("reg_id", "" + msg);
				Log.v("Hello", "Hello");
			}
		}.execute(null, null, null);
	}

	// get Details
	public void getDetailsFromFbUser(GraphUser user) {
		MySharedPrefs.saveString(UserLoginActivity.this, user.getId(),
				MySharedPrefs.FB_USER_ID);

		String email = user.getProperty("email").toString();

		if (email == null || email.equals("")) {
			email = user.getId() + "@facebook.com";
		}
		String pass = "";
		String gender = (String) user.getProperty("gender").toString();
		gender = gender.substring(0, 1).toUpperCase() + gender.substring(1);
		Log.v("email", "" + email);
		String username = user.getFirstName();
		/*
		if (user.getLastName() != null) {
			username = username + " " + user.getLastName();
		}*/
		String dob = "";
		// dob = user.getBirthday().toString();

		Log.v("dob", "" + user.getBirthday());
		String longi = String.valueOf(gpsTracker.getLongitude()); // Constants.Longitude;
		String lati = String.valueOf(gpsTracker.getLatitude()); // Constants.Latitude;

		String imageUrl = Constants.FB_PIC_URL1;
		imageUrl = imageUrl.replace("{facebookId}", MySharedPrefs.getString(
				UserLoginActivity.this, "", MySharedPrefs.FB_USER_ID));
		Log.v("imageurl=", imageUrl);
		String loc = "";
		GraphPlace place = user.getLocation();
		if (place != null) {
			GraphLocation location = place.getLocation();
			if (location != null) {
				loc = location.getState().toString() + ","
						+ location.getCountry().toString();
			}
		}

		Log.v("Location=", "" + loc);

		String deviceType = "android";
		Log.v("deviceID", "" + deviceId);
		hitRegistration("F", email, pass, gender, dob, longi, lati, username,
				loc, imageUrl, deviceId, deviceType);

	}

	private void hitRegistration(String loginType, String email, String pass,
			String gender, String dob, String longi, String lati,
			String username, String location, String imageurl, String deviceId,
			String deviceType) {
		if (!Utils.isNetworkConnected(getApplicationContext())) {
			Toast.makeText(this,
					getResources().getString(R.string.network_not_avail),
					Toast.LENGTH_SHORT).show();

			return;
		}
		if (!Utils.isReachable(UserLoginActivity.this)) {
			Toast.makeText(this,
					getResources().getString(R.string.unable_connect),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// showProgress();
		HashMap<String, String> params = new HashMap<String, String>();
		try {

			params.put("name", username);
			params.put("email", email);
			params.put("password", pass);
			if (imageurl != null) {
				params.put("image", imageurl);

			}

			params.put("phoneno", "");
			params.put("about", "");
			params.put("birthday", dob);
			params.put("gender", gender);
			params.put("latitude", lati);
			params.put("longitude", longi);
			params.put("location", location);
			params.put("login_type", loginType);
			params.put("devicetoken", deviceId);
			params.put("device", deviceType);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new RegistarTask(WebServiceURL.REGISTER_USER, params).execute();

	}

	private class RegistarTask extends AsyncTask<Void, Void, String> {
		private String strUrl, strRes, strMessage;
		private int responceCode;
		private HashMap<String, String> bodyParams;

		public RegistarTask(String strUrl, HashMap<String, String> bodyParams) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(UserLoginActivity.this);
			 * progressDialog.setMessage("Loading..."); progressDialog.show();
			 */
			showProgress();
		}

		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser = new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if (jObject != null)
				try {
					responceCode = jObject.getInt("response");
					strRes = jObject.getString("success");
					strMessage = jObject.getString("message");
					if (strRes.equalsIgnoreCase("true")) {

						JSONObject jobj = jObject.getJSONObject("user");

						if (!jObject.isNull("user")) {
							Gson gson = new Gson();
							mLogUser = gson.fromJson(jobj.toString(),
									LogUser.class);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			return strRes;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopProgress();
			// Log.v("Dating App", ""+result);
			if (result != null && result.equalsIgnoreCase("true")) {
				if (!strMessage.contains("Login")) {
					MySharedPrefs.saveString(UserLoginActivity.this, "yes",
							MySharedPrefs.COACHMARK_SHOW);
					if (mLogUser.getUserId() != null
							&& !mLogUser.getUserId().equalsIgnoreCase("")) {
						MySharedPrefs.saveString(UserLoginActivity.this,
								mLogUser.getUserId(), MySharedPrefs.USER_ID);
						Gson gson = new Gson();
						String userDetails = gson.toJson(mLogUser);
						MySharedPrefs.saveString(UserLoginActivity.this,
								userDetails, MySharedPrefs.USER_DATA);

						Intent intent = new Intent(UserLoginActivity.this,
								SignUp2ndActivity.class);
						intent.putExtra("Facebook", "Facebook");
						startActivity(intent);
						overridePendingTransition(R.anim.enter_from_right,
								R.anim.exit_to_left);
						finish();
					}
				} else {
					MySharedPrefs.saveString(UserLoginActivity.this, "no",
							MySharedPrefs.COACHMARK_SHOW);

					if (mLogUser.getUserId() != null
							&& !mLogUser.getUserId().equalsIgnoreCase("")) {
						MySharedPrefs.saveString(UserLoginActivity.this,
								mLogUser.getUserId(), MySharedPrefs.USER_ID);
						Gson gson = new Gson();
						String userDetails = gson.toJson(mLogUser);
						MySharedPrefs.saveString(UserLoginActivity.this,
								userDetails, MySharedPrefs.USER_DATA);

						if (mLogUser.hasValidQuickbloxId()) {
							Intent intent = new Intent(UserLoginActivity.this,
									WantActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.enter_from_right,
									R.anim.exit_to_left);
							finish();
						} else {
							new RegQuickBloxTasck(mLogUser) {
								@Override
								protected void onPostExecute(String result) {
									super.onPostExecute(result);
									if (mLogUser.hasValidQuickbloxId()) {
										Intent intent = new Intent(
												UserLoginActivity.this,
												WantActivity.class);
										startActivity(intent);
										overridePendingTransition(
												R.anim.enter_from_right,
												R.anim.exit_to_left);
										finish();
									}
								}
							}.execute();

						}
					}

				}
			}
			
		}

	}

}
