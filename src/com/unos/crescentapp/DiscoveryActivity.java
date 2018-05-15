package com.unos.crescentapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.adapter.CoachMarkPagerAdapter;
import com.unos.crescentapp.adapter.DiscoveryListAdapter;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.gpstracker.GPSTracker;
import com.unos.crescentapp.intefaces.OnAdapterEmptyListener;
import com.unos.crescentapp.intefaces.OnCardDismissedListener;
import com.unos.crescentapp.models.FindingPeople;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.models.Orientations.Orientation;
import com.unos.crescentapp.notification.GCMNotificationIntentService;
import com.unos.crescentapp.notification.SendNotificationTask;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.QuickBloxUtils;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CardContainer;
import com.unos.crescentapp.view.CircleImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DiscoveryActivity extends BaseActivity {
	private DrawerLayout drawerLyout;
	private CircleImageView userImageView;
	private TextView txtUser, txtNotificationCount;
	private String[] strImage;
	private int[] coachMarkImage = { R.drawable.coach_marks01, R.drawable.coach_marks02, R.drawable.coach_marks03,
			R.drawable.coach_marks04, R.drawable.coach_marks05 };
	private CardContainer discoveryCardContainer;
	private DiscoveryListAdapter adapter;
	private int totCount = 0, pageCount = 0, pageNo = 1;
	private ViewPager coackMarkViewPager;
	private String userId;
	public static final int PAGE_SIZE = 20;
	private GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gpsTracker =  new GPSTracker(DiscoveryActivity.this);
		
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_discovery);
		drawerLyout = (DrawerLayout) findViewById(R.id.drawer_layout);
		userImageView = (CircleImageView) findViewById(R.id.activity_discovery_imgvw_user);
		txtUser = (TextView) findViewById(R.id.activity_discovery_txtvw_user);
		txtNotificationCount = (TextView) findViewById(R.id.activity_discovery_txtvw_notification_count);
		coackMarkViewPager = (ViewPager) findViewById(R.id.activity_discovery_coach_mark_pager);
		coackMarkViewPager.setAdapter(new CoachMarkPagerAdapter(DiscoveryActivity.this, coachMarkImage));
		userId = MySharedPrefs.getString(DiscoveryActivity.this, "", MySharedPrefs.USER_ID);
		txtUser.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		discoveryCardContainer = (CardContainer) findViewById(R.id.activity_discovery_card_container);
		// coackMarkViewPager.setVisibility(View.VISIBLE);
		if (MySharedPrefs.getString(DiscoveryActivity.this, "", MySharedPrefs.COACHMARK_SHOW).equals("yes"))
			coackMarkViewPager.setVisibility(View.VISIBLE);
		else {
			coackMarkViewPager.setVisibility(View.GONE);

		}
		if (GCMNotificationIntentService.notifyID != 0) {
			txtNotificationCount.setText(String.valueOf(GCMNotificationIntentService.notifyID));
		} else
			txtNotificationCount.setVisibility(View.GONE);
		/*
		if (mLogUser.getName().contains(" ")) {
			user = new QBUser(mLogUser.getName().substring(0, mLogUser.getName().indexOf(" ")), "crescent123");
		} else
			user = new QBUser(mLogUser.getName(), "crescent123");
			*/
		//user = new QBUser(mLogUser.getLogin(), "crescent123", mLogUser.getEmail());
		// user = new QBUser(mLogUser.getName().substring(0,
		// mLogUser.getName().indexOf(" ")), "crescent123");

		if (mLogUser != null) {
			if (mLogUser.getImage().length > 0) {
				strImage = new String[mLogUser.getImage().length];
				strImage = mLogUser.getImage();
				loadProfileImage(strImage[0], userImageView);
			}
			txtUser.setText(mLogUser.getName());
		}
		if (Utils.isNetworkConnected(DiscoveryActivity.this)) {
			if (Utils.isReachable(DiscoveryActivity.this)) {
				makeJsonObjectRequest(WebServiceURL.FINDING_PEOPLE_NEAR + "?" + "userid=" + mLogUser.getUserId()
						+ "&latitude=" + String.valueOf(gpsTracker.getLatitude()) + "&longitude=" + String.valueOf(gpsTracker.getLongitude()) 
						+ "&pageno=" + pageNo + "&pagesize=" + PAGE_SIZE);
				new LogToChatTask().execute();
			} else
				showToast(getResources().getString(R.string.unable_connect));
		} else
			showToast(getResources().getString(R.string.network_not_avail));
		pageNo++;
		findViewById(R.id.activity_discovery_lnr_lyt_profile).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DiscoveryActivity.this, MyProfileActivity.class));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);

			}
		});

		coackMarkViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == coachMarkImage.length - 1) {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {

							if (coackMarkViewPager.getVisibility() == View.VISIBLE) {
								coackMarkViewPager.setVisibility(View.GONE);
							}
						}
					}, 3000);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		findViewById(R.id.activity_discovery_ib_imageChatCount).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(DiscoveryActivity.this, MessageActivity.class));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
			}
		});
		adapter = new DiscoveryListAdapter(DiscoveryActivity.this);

		discoveryCardContainer.setAdapter(adapter);
		discoveryCardContainer.setOrientation(Orientation.Ordered);

		discoveryCardContainer.setmOnAdapterEmptyListener(new OnAdapterEmptyListener() {

			@Override
			public void onAdapterEmpty() {
				if (totCount != 0) {
					if (totCount > PAGE_SIZE) {
						if (totCount % PAGE_SIZE == 0) {
							pageCount = totCount / PAGE_SIZE;
						} else
							pageCount = (totCount / PAGE_SIZE) + 1;
					}
					// showToast(""+pageNo);
					if (pageNo <= pageCount) {

						makeJsonObjectRequest(WebServiceURL.FINDING_PEOPLE_NEAR + "?" + "userid=" + mLogUser.getUserId()
								+ "&latitude" + String.valueOf(gpsTracker.getLatitude()) + "&longitude" + String.valueOf(gpsTracker.getLongitude()) 
								+ "&pageno=" + pageNo + "&pagesize=" + PAGE_SIZE);

						pageNo++;
						if (pageNo > pageCount)
							pageNo = 1;
					} else {
						pageNo = 1;

						makeJsonObjectRequest(WebServiceURL.FINDING_PEOPLE_NEAR + "?" + "userid=" + mLogUser.getUserId()
								+ "&latitude" + String.valueOf(gpsTracker.getLatitude()) + "&longitude" + String.valueOf(gpsTracker.getLongitude()) 
								+ "&pageno=" + pageNo + "&pagesize=" + PAGE_SIZE);

					}

				}
			}
		});

		discoveryCardContainer.setmOnCardDisListener(new OnCardDismissedListener() {

			@Override
			public void onLike(final FindingPeople man) {
				new Thread(new Runnable() {
					public void run() {
						 
						//int totalCount = discoveryCardContainer.getAdapter().getCount();
						//FindingPeople man =  (FindingPeople)discoveryCardContainer.getAdapter().getItem(totalCount - position);
					    Log.v("frient_name", man.getName());
						HashMap<String, String> bodyParams = new HashMap<>();
						bodyParams.put("userid", userId);
						bodyParams.put("friendid", man.getUser_id());
						bodyParams.put("like", "1");
						makeJSONLikeDisLikeRequest(WebServiceURL.LIKE_DISLIKE_FRIEND, bodyParams, man);

					}
				}).start();
				/*
				 * if(findingPeopleList.get(findingPeopleList.size()-position).
				 * getFriendlikestatus().equals("1")){
				 * callAlert(DiscoveryActivity
				 * .this,findingPeopleList.get(findingPeopleList
				 * .size()-position)); }
				 */

			}

			@Override
			public void onDislike(final  FindingPeople man) {
				// Toast.makeText(DiscoveryActivity.this,
				// findingPeopleList.get(position-1).getUser_id(),
				// Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					public void run() {
						//Log.v("childCount", "" + position);
						//Log.v("adapter size", "" + discoveryCardContainer.getAdapter().getCount());
						//int totalCount = discoveryCardContainer.getAdapter().getCount();
						//FindingPeople man =  (FindingPeople)discoveryCardContainer.getAdapter().getItem(totalCount - position);
						Log.v("friend_name", man.getName());
						HashMap<String, String> bodyParams = new HashMap<>();
						bodyParams.put("userid", userId);
						bodyParams.put("friendid", man.getUser_id());
						bodyParams.put("like", "0");
						makeJSONLikeDisLikeRequest(WebServiceURL.LIKE_DISLIKE_FRIEND, bodyParams, man);
					}
				}).start();

			}
		});

		findViewById(R.id.activity_discovery_ib_imagesidebar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (drawerLyout.isDrawerOpen(Gravity.LEFT)) {
					drawerLyout.closeDrawer(Gravity.LEFT);
				} else {
					drawerLyout.openDrawer(Gravity.LEFT);
				}

			}
		});

		((TextView) findViewById(R.id.activity_discovery_tv_notifications)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_tv_notifications).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(((DrawerLayout)
				// findViewById(R.id.drawer_layout)).)
				txtNotificationCount.setVisibility(View.GONE);
				startActivity(new Intent(DiscoveryActivity.this, NotificationsActivity.class));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
			}
		});
		((TextView) findViewById(R.id.activity_discovery_tv_settings)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_tv_settings).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(((DrawerLayout)
				// findViewById(R.id.drawer_layout)).)
				startActivity(new Intent(DiscoveryActivity.this, SettingsActivity.class));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
			}
		});

		((TextView) findViewById(R.id.activity_discovery_tv_Iwant)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_tv_Iwant).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DiscoveryActivity.this, WantActivity.class).putExtra("userDetails", mLogUser)
						.putExtra("discovery", "discovery"));
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
			}
		});
		((TextView) findViewById(R.id.activity_discovery_txtvw_logout)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_txtvw_logout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlert();
				// makeJsonObjectRequestPostAccount(WebServiceURL.LOGOUT);
			}
		});
		((TextView) findViewById(R.id.activity_discovery_tv_contact_cresent)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_tv_contact_cresent).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto","info@crescentapp.com", null));
				String[] recipients = { "info@crescentapp.com" };
				//intent.putExtra(Intent.EXTRA_EMAIL, recipients);
				intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_subject));
				intent.putExtra(Intent.EXTRA_TEXT, "");
				//intent.setType("text/plain");
				startActivity(Intent.createChooser(intent, "Send Email"));
				
			}
		});
		((TextView) findViewById(R.id.activity_discovery_tv_share_cresent)).setTypeface(Utils.getTypeface(
				DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		findViewById(R.id.activity_discovery_tv_share_cresent).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_SEND);

				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "Join Crescent - A Mobile App for Modern Muslim Singles.");
				intent.putExtra(
						Intent.EXTRA_TEXT,
						"Hey, you should should download Crescent - The Muslim Love App to find your Muslim love. Here's the link: http://www.crescentapp.com/");
				startActivity(Intent.createChooser(intent, "SHARE CRESCENT"));

			}
		});
		
	

	}

	public void showAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DiscoveryActivity.this);
		alertDialogBuilder.setTitle("Crescent");
		alertDialogBuilder.setMessage("Would you like to Logout?");
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.cancel();
				makeJsonObjectRequestPostAccount(WebServiceURL.LOGOUT);

				
			}
		});
		alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	@Override
	protected void onStart(){
		super.onStart();
		gpsTracker.getLocation();
		if(!gpsTracker.canGetLocation()){
			gpsTracker.showSettingsAlert();
		}
		/*
		if(gpsTracker != null){
			gpsTracker.stopUsingGPS();
			gpsTracker = null;
		}
		gpsTracker =  new GPSTracker(this);
		if(!gpsTracker.canGetLocation()){
			gpsTracker.showSettingsAlert();
		}
		*/
		/*
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean canGetLocation = false;
        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
        } else {
            canGetLocation = true;
        }
        
        showToast("Location " + (canGetLocation ? "enabled" : "disabled"));
		*/
	}
	


	private void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader.get(url,
				ImageLoader.getImageListener(imageview, R.drawable.add_your_photo, R.drawable.add_your_photo));

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (drawerLyout.isDrawerOpen(Gravity.LEFT))
			drawerLyout.closeDrawer(Gravity.LEFT);
		MySharedPrefs.saveString(DiscoveryActivity.this, "No", MySharedPrefs.COACHMARK_SHOW);
	}

	private void makeJsonObjectRequest(String url) {
		showProgress();
		Log.d("Request URL", url);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
		
				new JSONToPeopleTask(response).execute();
						
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				stopProgress();

			}
		});
		AppController.getInstance().addToRequestQueue(jsonObjReq);
	}

	private class JSONToPeopleTask extends AsyncTask<Void, Void, ArrayList<FindingPeople>> {
		private ArrayList<FindingPeople> people;
		private JSONObject response;
		private String strMessage;
		int mtotCount;

		public JSONToPeopleTask(JSONObject response) {
			this.response = response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			strMessage = "";
		}

		@Override
		protected ArrayList<FindingPeople> doInBackground(Void... params) {
			try {
				if (response.getString("success").equalsIgnoreCase("true")) {

					// discoveryCardContainer.setAdapter(null);
					if (response.has("message"))
						strMessage = response.getString("message");
					if (response.has("totalresult"))
						mtotCount = Integer.parseInt(response.getString("totalresult"));

					if (response.has("values")) {
						JSONArray jArray = response.getJSONArray("values");
						people = new ArrayList<FindingPeople>(jArray.length());
						for (int i = 0; i < jArray.length(); i++) {
							JSONObject jObject = jArray.getJSONObject(i);

							FindingPeople mFindingPeople = new FindingPeople();
							mFindingPeople.setUser_id(jObject.getString("user_id"));
							mFindingPeople.setName(jObject.getString("name"));
							mFindingPeople.setAbout(jObject.getString("about"));
							mFindingPeople.setEmail(jObject.getString("email"));
							mFindingPeople.setContact(jObject.getString("contact"));
							mFindingPeople.setBirthday(jObject.getString("birthday"));
							mFindingPeople.setAge(jObject.getString("age"));
							mFindingPeople.setGender(jObject.getString("gender"));
							mFindingPeople
									.setCardLikeImageDrawable(getResources().getDrawable(R.drawable.button_right));
							mFindingPeople.setCardDislikeImageDrawable(getResources().getDrawable(
									R.drawable.button_delete));
							mFindingPeople.setLocation(jObject.getString("location"));
							mFindingPeople.setUserlikestatus(jObject.getString("userlikestatus"));
							mFindingPeople.setFriendlikestatus(jObject.getString("friendlikestatus"));
							mFindingPeople.setImagelikestatus(jObject.getString("imagelikestatus"));
							mFindingPeople.setUserimage(jObject.getString("userimage"));
							mFindingPeople.setInterests(jObject.getString("interests"));
							mFindingPeople.setIs_login(jObject.getString("is_login"));
							mFindingPeople.setDeviceToken(jObject.getString("devicetoken"));
							mFindingPeople.setDeviceType(jObject.getString("device"));
							people.add(mFindingPeople);
						}
					}
				}

			} catch (JSONException e) {
				if(people == null){
					return new ArrayList<FindingPeople>();
				}
				e.printStackTrace();
			}
			return people;
		}

		@Override
		protected void onPostExecute(ArrayList<FindingPeople> result) {
			super.onPostExecute(result);
			
			if(result == null){
				result =  new ArrayList<FindingPeople>();
			}
			totCount = mtotCount;
			
			Log.v("Arrar Size",""+result.size());
			if (result.size() > 0) {
				adapter.removeAll();
				adapter.addAll(result);
				discoveryCardContainer.setAdapter(adapter);
			} else {
				showToast("No match found");
				//startActivity(new Intent(DiscoveryActivity.this, WantActivity.class));
				//finish();
			}
			if (!strMessage.equals("")) {
				Toast.makeText(DiscoveryActivity.this, strMessage, Toast.LENGTH_LONG).show();
			}
			stopProgress();
			//finish();

		}
	}

	private void makeJsonObjectRequestPostAccount(String strUrl) {
		if (!Utils.isNetworkConnected(DiscoveryActivity.this)) {
			showToast(getResources().getString(R.string.network_not_avail));
			return;
		}
		if (!Utils.isReachable(DiscoveryActivity.this)) {

			showToast(getResources().getString(R.string.unable_connect));

			return;
		}

		HashMap<String, String> params = new HashMap<String, String>();
		try {
			params.put("userid", userId);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new AccountTask(strUrl, params).execute();

	}

	private class AccountTask extends AsyncTask<Void, Void, String> {
		private String strUrl, strRes, strMessage;
		private HashMap<String, String> bodyParams;

		public AccountTask(String strUrl, HashMap<String, String> bodyParams) {
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
			JSONPostParser parser = new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if (jObject != null)
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

			if (result != null && !result.equalsIgnoreCase("")) {
				// showToast(result);
				if (strRes != null && strRes.equalsIgnoreCase("true")) {
					MySharedPrefs.saveString(DiscoveryActivity.this, "", MySharedPrefs.USER_ID);
					MySharedPrefs.saveString(DiscoveryActivity.this, "", MySharedPrefs.MESSAGE_ALERT);
					MySharedPrefs.saveString(DiscoveryActivity.this, "", MySharedPrefs.NEW_MATCH_ALERT);
					MySharedPrefs.saveString(DiscoveryActivity.this, "", MySharedPrefs.USER_DATA);
					Intent intent = new Intent(DiscoveryActivity.this, UserLoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(intent);
					finish();
					overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				}
			}

		}
	}

	private void makeJSONLikeDisLikeRequest(String strUrl, HashMap<String, String> bodyParams,
			FindingPeople mFindingPeople) {
		if (!Utils.isNetworkConnected(DiscoveryActivity.this)) {
			DiscoveryActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showToast(getResources().getString(R.string.network_not_avail));

				}
			});

			return;
		}
		if (!Utils.isReachable(DiscoveryActivity.this)) {
			DiscoveryActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					showToast(getResources().getString(R.string.unable_connect));

				}
			});

			return;
		}

		new LikeDislikeTask(strUrl, bodyParams, mFindingPeople).execute();

	}

	private class LikeDislikeTask extends AsyncTask<Void, Void, String> {
		private String strUrl, strMessage, strRes;
		private HashMap<String, String> bodyParams;
		private FindingPeople mFindingPeople;

		public LikeDislikeTask(String strUrl, HashMap<String, String> bodyParams, FindingPeople mFindingPeople) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
			this.mFindingPeople = mFindingPeople;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			/*
			 * progressDialog = new ProgressDialog(getContext());
			 * progressDialog.setMessage("Loading...");
			 * progressDialog.setCancelable(false); progressDialog.show();
			 */
		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject jObj = new JSONPostParser().getJSONFromUrl(strUrl, bodyParams);
			Log.d("LIKEDISLIKE", "Params: " + bodyParams.toString());
			Log.d("LIKEDISLIKE", "Result: " + jObj.toString());
			if (jObj != null) {
				try {
					strRes = jObj.getString("success");
					strMessage = jObj.getString("message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return strMessage;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			/*
			 * if(progressDialog.isShowing()){ progressDialog.cancel(); }
			 */

			if (result != null && !result.equalsIgnoreCase("")) {
				// String []str = result.split(" ");
				// if(str.length>1){

				if (/* strRes.equalsIgnoreCase("true") && */!result.contains("disliked")) {

					if (mFindingPeople.getFriendlikestatus().equals("1")) {
						HashMap<String, String> params = new HashMap<>();
						params.put("devicetoken", mFindingPeople.getDeviceToken());
						params.put(
								"message",
								String.format(getString(R.string.you_and_other_like_each_other),
										"" + mLogUser.getName()));
						new SendNotificationTask(WebServiceURL.SEND_NOTIFICATION, params).execute();

						/*
						 * HashMap<String, String> params1 = new HashMap<>();
						 * params.put("devicetoken",
						 * MySharedPrefs.getString(DiscoveryActivity.this, "",
						 * MySharedPrefs.DEVICE_ID)); params.put("message",
						 * "You have a new match on Crescent"); new
						 * SendNotificationTask(WebServiceURL.SEND_NOTIFICATION,
						 * params1).execute();
						 */
						//if (mFindingPeople.getFriendlikestatus().equals("1"))
							callAlert(DiscoveryActivity.this, mFindingPeople);
					}

				}
			}
		}

	}

	@SuppressLint("DefaultLocale")
	private void callAlert(final Context context, final FindingPeople findPeople) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_mutual_match);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		lp.x = -50;
		lp.y = -100;
		dialog.getWindow().setAttributes(lp);

		TextView txtLike, txtMutualMatch, txtKnow;
		Button btnMay;
		CircleImageView userImage, friendImage;
		Button btnSendMessage;
		// dialog.getWindow().setBackgroundDrawable(new
		// ColorDrawable(android.graphics.Color.TRANSPARENT));
		Gson gson = new Gson();
		String userJson = MySharedPrefs.getString(DiscoveryActivity.this, "", MySharedPrefs.USER_DATA);
		mLogUser = gson.fromJson(userJson, LogUser.class);
		userImage = (CircleImageView) dialog.findViewById(R.id.activity_mutual_match_imgvw_user);
		friendImage = (CircleImageView) dialog.findViewById(R.id.activity_mutual_match_imgvw_friend);
		txtMutualMatch = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_mutual);
		txtLike = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_like);
		txtKnow = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_know);
		btnMay = (Button) dialog.findViewById(R.id.activity_mutual_match_btn_may);

		btnSendMessage = (Button) dialog.findViewById(R.id.activity_mutual_match_btn_send_message);
		txtKnow.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtMutualMatch.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtLike.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnMay.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setTypeface(Utils.getTypeface(DiscoveryActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setText("SEND " + findPeople.getName().toUpperCase() + " A MESSAGE");
		if (mLogUser.getImage() != null && mLogUser.getImage().length > 0) {
			loadProfileImage(mLogUser.getImage()[0], userImage);
		}

		loadProfileImage(findPeople.getUserimage(), friendImage);
		txtLike.setText(String.format(getString(R.string.you_and_other_like_each_other), "" + findPeople.getName()));
		txtKnow.setText("Now you can know more about " + findPeople.getName());
		txtKnow.setVisibility(View.INVISIBLE);
		btnSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
				if (Utils.isNetworkConnected(DiscoveryActivity.this)) {
					if (Utils.isReachable(DiscoveryActivity.this)) {
						//if (AppController.getInstance().getCurrentUser() != null) {
							new createChatDialog(findPeople.getEmail(), findPeople.getName(), findPeople.getUserimage())
									.execute();
						//} else
						//	Utils.showAlertDialog("Unable to Create Chat Session", "Message", DiscoveryActivity.this);
					} else
						showToast(getResources().getString(R.string.unable_connect));
				} else
					showToast(getResources().getString(R.string.network_not_avail));
				/*
				 * Intent intent = new
				 * Intent(DiscoveryActivity.this,ChatListActivity.class);
				 * startActivity(intent);
				 * overridePendingTransition(R.anim.enter_from_right,
				 * R.anim.exit_to_left);
				 */
			}
		});
		btnMay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();

			}
		});
		try{
			dialog.show();
		} catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private class createChatDialog extends AsyncTask<Void, Void, QBDialog> {
		private String strEmail;
		private String userName;
		private String userImage;

		public createChatDialog(String strEmail, String userName, String userImage) {
			this.strEmail = strEmail;
			this.userName = userName;
			this.userImage = userImage;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected void onPostExecute(QBDialog result) {
			stopProgress();
			if (result != null) {
				startSingleChat(result, userName, userImage);
			}
		}

		@Override
		protected QBDialog doInBackground(Void... params) {
			QBUser selectedUser = null;
			QBDialog dialogToCreate = null;
			try {
				selectedUser = QBUsers.getUserByEmail(strEmail);
				dialogToCreate = QBChatService.getInstance().getPrivateChatManager().createDialog(selectedUser.getId());
				// occupantIds.add(selectedUser.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return dialogToCreate;
		}

	}

	public void startSingleChat(QBDialog dialog, String userName, String userImage) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ChatListActivity.EXTRA_MODE, ChatListActivity.Mode.PRIVATE);
		bundle.putSerializable(ChatListActivity.EXTRA_DIALOG, dialog);
		bundle.putString("userName", userName);
		bundle.putString("userImage", userImage);
		ChatListActivity.start(DiscoveryActivity.this, bundle);
	}
	
	private class LogToChatTask extends AsyncTask<Void, Void, Exception> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected Exception doInBackground(Void... params) {
			try {
				QBUser mQBUser = new QBUser(mLogUser.getLogin(), "crescent123",
						mLogUser.getEmail());
				
				//mQBUser.setId(QBUsers.signIn(mQBUser).getId());
				//mQBUser.setId(mLogUser.getQuickblox_id());
				mQBUser = QBUsers.signIn(mQBUser);
				mQBUser.setPassword("crescent123");
				QBChatService chatService;
				if (!QBChatService.isInitialized()) {
				    QBChatService.init(DiscoveryActivity.this);
				    
				}
				chatService = QBChatService.getInstance();
				if(!chatService.isLoggedIn()){
					chatService.login(mQBUser);
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("QBLogin Error", "" + e.getLocalizedMessage());
				return e;
			}
		}

		@Override
		protected void onPostExecute(Exception result) {
			stopProgress();
			if(result == null){
	    	//getMatchList(WebServiceURL.GET_MATCH_LIST
			//		+ mLogUser.getUserId());
			} else {
				showToast(result.getLocalizedMessage());
			}
			
		}

	}

	
}
