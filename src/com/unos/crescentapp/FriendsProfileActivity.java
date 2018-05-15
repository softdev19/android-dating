package com.unos.crescentapp;

import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unos.crescentapp.adapter.FriendProfileImageAdapter;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.FriendProfile;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FriendsProfileActivity extends BaseActivity {
	protected static final String TAG = null;
	
	private ViewPager profileImageViewPager;
	private CirclePageIndicator pagerIndicator;
	
	private Button btnBack;
	private TextView txtNameAge,txtLocation,txtAbout,txtTitle;
	private LinearLayout linear_interest;
	private FriendProfile mFriendProfile;
	private List<String> interestList;
	private int flag;
	private String[] mProfImage;
	private String userId="",friendId="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mFriendProfile=new FriendProfile();
		setContentView(R.layout.activity_friends_profile);
		userId = MySharedPrefs.getString(FriendsProfileActivity.this, "", MySharedPrefs.USER_ID);
		txtTitle = (TextView)findViewById(R.id.activity_friends_profile_tv_friends_name);
		txtNameAge=(TextView)findViewById(R.id.activity_friends_profile_tv_name_age);
		txtLocation=(TextView) findViewById(R.id.activity_friends_profile_tv_location);
		txtAbout=(TextView) findViewById(R.id.activity_friends_profile_txtvw_about);
		linear_interest=(LinearLayout) findViewById(R.id.activity_friends_profile_lnr_lyt_interest);
		profileImageViewPager = (ViewPager) findViewById(R.id.activity_friends_profile_pager);
		pagerIndicator = (CirclePageIndicator)findViewById(R.id.activity_friends_profile_pager_indicator);
		btnBack = (Button) findViewById(R.id.activity_friends_profile_btn_back);
		friendId = getIntent().getStringExtra("friendId");
		if(Utils.isNetworkConnected(FriendsProfileActivity.this))
		{
			if(Utils.isReachable(FriendsProfileActivity.this)){
				getFriendDetailsRequest(WebServiceURL.FRIEND_PROFILE+"?&userid="+userId+"&friendid="+friendId);
			}else
				Toast.makeText(FriendsProfileActivity.this, getResources().getString(R.string.unable_connect), Toast.LENGTH_SHORT).show();
		}else
			Toast.makeText(FriendsProfileActivity.this, getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
			
		txtTitle.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtNameAge.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtLocation.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtAbout.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		btnBack.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});
		
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}



	private TextView interestText(int flag,String interest){
		LayoutParams TxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 TextView interestTxt = new TextView(FriendsProfileActivity.this);
		 interestTxt.setId(flag);
		 interestTxt.setText(interest);
		 interestTxt.setLayoutParams(TxtParams);
		 interestTxt.setPadding(5, 5, 5, 5);
		 interestTxt.setGravity(Gravity.CENTER_HORIZONTAL);
		 interestTxt.setTypeface(Utils.getTypeface(FriendsProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		 interestTxt.setTextSize(16f);
        return interestTxt;
	}
	 private ImageView interestImageView(int flag,String interest){
		 LayoutParams imgviewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 ImageView imgView = new ImageView(FriendsProfileActivity.this);
		 imgView.setLayoutParams(imgviewParams);
		 if(interest.equalsIgnoreCase("tech")){
				
			 imgView.setImageResource(R.drawable.icon_tech);
			 }
			 else if(interest.equalsIgnoreCase("travel")||interest.equalsIgnoreCase("travels"))
				 imgView.setImageResource(R.drawable.icon_travel); 
			 else if(interest.equalsIgnoreCase("fashion"))
				 imgView.setImageResource(R.drawable.icon_fashion);
				 
			 else if(interest.equalsIgnoreCase("fitness"))
				 imgView.setImageResource(R.drawable.icon_fitness);
			 else if(interest.equalsIgnoreCase("food"))
				 imgView.setImageResource(R.drawable.icon_food);
				
			 else if(interest.equalsIgnoreCase("business"))
				 imgView.setImageResource(R.drawable.icon_business);
			 else if(interest.equalsIgnoreCase("art"))
				 imgView.setImageResource(R.drawable.icon_art);
				
			 else if(interest.equalsIgnoreCase("movies"))
				 imgView.setImageResource(R.drawable.icon_movies);
				 
			 else if(interest.equalsIgnoreCase("music"))
				 imgView.setImageResource(R.drawable.icon_music);
				
			 else if(interest.equalsIgnoreCase("politics"))
				 imgView.setImageResource(R.drawable.icon_politics);
				
			 else if(interest.equalsIgnoreCase("sports")||interest.equalsIgnoreCase("sport"))
				 imgView.setImageResource(R.drawable.icon_sports);
				 
			 else if(interest.equalsIgnoreCase("events")||interest.equalsIgnoreCase("event"))
				 imgView.setImageResource(R.drawable.icon_evnts);
				 
		 return imgView;
	 }
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		
	}
	
 private  void getFriendDetailsRequest(String strUrl){
	 showProgress();
	 	JsonObjectRequest jObjrequest = new JsonObjectRequest(strUrl, null, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject response) {
				try {
					if(response.getString("success").equalsIgnoreCase("true")){
						mFriendProfile.setUserId(response.getString("user_id"));
						mFriendProfile.setName(response.getString("name"));
						mFriendProfile.setEmail(response.getString("email"));
						mFriendProfile.setContact(response.getString("contact"));
						mFriendProfile.setAbout(response.getString("about"));
						mFriendProfile.setBirthday(response.getString("birthday"));
						mFriendProfile.setAge(response.getString("age"));
						mFriendProfile.setGender(response.getString("gender"));
						mFriendProfile.setLocation(response.getString("location"));
						mFriendProfile.setUserlikestatus(response.getString("userlikestatus"));
						mFriendProfile.setFriendlikestatus(response.getString("friendlikestatus"));
						
								JSONArray jArr = response
										.getJSONArray("userimage");
								if (jArr != null) {
									String[] str = new String[jArr.length()];
									for (int i = 0; i < jArr.length(); i++) {
										JSONObject jsonImage = jArr
												.getJSONObject(i);

										str[i] = jsonImage.getString("image");
										
									}

									mFriendProfile.setUserimage(str);
								}
						mFriendProfile.setInterests(response.getString("interests"));
						mFriendProfile.setLogin_type(response.getString("login_type"));
						mFriendProfile.setIs_active(response.getString("is_active"));
						mFriendProfile.setIs_login(response.getString("is_login"));
						mFriendProfile.setCreate_dt(response.getString("create_dt"));
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				txtTitle.setText(mFriendProfile.getName()+"'s Profile");
				txtNameAge.setText(mFriendProfile.getName()+","+mFriendProfile.getAge());
				txtLocation.setText(mFriendProfile.getLocation());
				if(!mFriendProfile.getAbout().equals(""))
					txtAbout.setText("\""+mFriendProfile.getAbout()+"\"");
				String interest = mFriendProfile.getInterests();
				if(interest!=null && !interest.equals("")){
					interestList = Arrays.asList(interest.split(","));
					
					/*int rowCount=0;
					int j=0;
					if(interestList.size()>0){
						if(interestList.size()%3==0)
							rowCount = 	interestList.size()/3;
							else
								rowCount = 	(interestList.size()/3)+1;
						for(int i=0;i<rowCount;i++){
							LinearLayout linearLytInterestMain = new LinearLayout(FriendsProfileActivity.this);
							LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
									LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
							linearLytInterestMain.setOrientation(LinearLayout.HORIZONTAL);
							linearLytInterestMain.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
							linearLytInterestMain.setLayoutParams(params2);
							while(j<interestList.size()){
								LinearLayout linearLytInterestSub = new LinearLayout(FriendsProfileActivity.this);
								LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
										100, 100);
								params1.setMargins(10, 10, 10,10);
								linearLytInterestSub.setOrientation(LinearLayout.VERTICAL);
								linearLytInterestSub.setLayoutParams(params1);
								linearLytInterestSub.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
								linearLytInterestSub.addView(interestImageView(j, interestList.get(j)));
								linearLytInterestSub.addView(interestText(j,interestList.get(j)));
								linearLytInterestMain.addView(linearLytInterestSub);
								j++;
								if(j%3==0)
									break;
							}
							linear_interest.addView(linearLytInterestMain);
						}
					}*/
					for(flag=0;flag<interestList.size();flag++){
						LinearLayout linearLytInterestSub = new LinearLayout(FriendsProfileActivity.this);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						params.setMargins(20, 20, 20,20);
						linearLytInterestSub.setOrientation(LinearLayout.VERTICAL);
						linearLytInterestSub.setLayoutParams(params);
						linearLytInterestSub.setGravity(Gravity.CENTER_HORIZONTAL);
						linearLytInterestSub.addView(interestImageView(flag, interestList.get(flag)));
						linearLytInterestSub.addView(interestText(flag,interestList.get(flag)));
						linear_interest.addView(linearLytInterestSub);
						if(flag==4)
							break;
					}
				}
		    //For Image
				if (mFriendProfile.getUserimage()!=null && mFriendProfile.getUserimage().length>0) {
					mProfImage = new String[mFriendProfile.getUserimage().length];
					mProfImage = mFriendProfile.getUserimage();
					profileImageViewPager.setAdapter(new FriendProfileImageAdapter(FriendsProfileActivity.this,mProfImage));
					pagerIndicator.setViewPager(profileImageViewPager);
					final float density = getResources().getDisplayMetrics().density;
			         pagerIndicator.setBackgroundColor(Color.TRANSPARENT);
			         pagerIndicator.setRadius(5 * density);
			         pagerIndicator.setPageColor(Color.GRAY);//0xFF888888
			         pagerIndicator.setFillColor(Color.parseColor("#FC5A42"));//0x880000FF
			         pagerIndicator.setStrokeColor(Color.TRANSPARENT);//0xFF000000
			         pagerIndicator.setStrokeWidth(2 * density);
				}
				
				stopProgress();
				
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				
				Toast.makeText(FriendsProfileActivity.this, ""+error, Toast.LENGTH_SHORT).show();
				stopProgress();
			}
		});
	 	AppController.getInstance().addToRequestQueue(jObjrequest);
			
	    }

	
}
