package com.unos.crescentapp;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.unos.crescentapp.adapter.ProfileImageAdapter;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.AppController;
import com.viewpagerindicator.CirclePageIndicator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

	public class MyProfileActivity extends BaseActivity{
	private TextView tv_profileName,tv_aboutUser,tv_userLocation,txtTitle;
	private String userProfileName,userAbout,userLocation;
	private String[] mProfImage;
	private CirclePageIndicator pagerIndicator;
	private ViewPager profileImageViewPager;
	private LinearLayout linearLytInterest;
	private String strInterest;
	private int flag;
	private List<String> interestList;
	private Button btnEdit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_profile);
		btnEdit = (Button) findViewById(R.id.activity_my_profile_btn_edit);
		txtTitle = (TextView) findViewById(R.id.activity_my_profile_txtvw_title);
		
		tv_profileName=(TextView) findViewById(R.id.activity_my_profile_tv_name_age);
		tv_userLocation=(TextView) findViewById(R.id.activity_my_profile_tv_location);
		tv_aboutUser=(TextView) findViewById(R.id.activity_my_profile_txtvw_about);
		profileImageViewPager = (ViewPager) findViewById(R.id.activity_my_profile_pager);
		pagerIndicator = (CirclePageIndicator)findViewById(R.id.activity_my_profile_pager_indicator);
		linearLytInterest = (LinearLayout) findViewById(R.id.activity_my_profile_lnr_lyt_interest);
		
		txtTitle.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		tv_profileName.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		tv_userLocation.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		tv_aboutUser.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		
		
		userProfileName=mLogUser.getName();
		userAbout=mLogUser.getAbout().toString();
	    userLocation=mLogUser.getLocation().toString();
		tv_profileName.setText(userProfileName);
		if(!userAbout.equals(""))
		tv_aboutUser.setText("\""+userAbout+"\"");
		tv_userLocation.setText(userLocation);
		interestList= new ArrayList<String>();
		strInterest = mLogUser.getInterests();
		
		if(strInterest!=null && !strInterest.equals("")){
			interestList = Arrays.asList(strInterest.split(","));
			/*int rowCount=0;
			int j=0;
			if(interestList.size()>0){
				if(interestList.size()%3==0)
					rowCount = 	interestList.size()/3;
					else
						rowCount = 	(interestList.size()/3)+1;
				for(int i=0;i<rowCount;i++){
					LinearLayout linearLytInterestMain = new LinearLayout(MyProfileActivity.this);
					LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					
					linearLytInterestMain.setOrientation(LinearLayout.HORIZONTAL);
					linearLytInterestMain.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
					
					linearLytInterestMain.setLayoutParams(params2);
					while(j<interestList.size()){
						LinearLayout linearLytInterestSub = new LinearLayout(MyProfileActivity.this);
						LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
								100, 100);
						params1.setMargins(10, 10, 10,0);
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
					linearLytInterest.addView(linearLytInterestMain);
				}
			}*/
			
			for(flag=0;flag<interestList.size();flag++){
				LinearLayout linearLytInterestSub = new LinearLayout(MyProfileActivity.this);
				LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				params1.setMargins(20, 20, 20,20);
				linearLytInterestSub.setOrientation(LinearLayout.VERTICAL);
				linearLytInterestSub.setLayoutParams(params1);
				linearLytInterestSub.setGravity(Gravity.CENTER_HORIZONTAL);
				linearLytInterestSub.addView(interestImageView(flag, interestList.get(flag)));
				linearLytInterestSub.addView(interestText(flag,interestList.get(flag)));
				linearLytInterest.addView(linearLytInterestSub);
				if(flag==4)
					break;
			}
			
			
			
				
		}
		findViewById(R.id.activity_my_profile_imgbtn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyProfileActivity.this,DiscoveryActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});
		
		btnEdit.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intentUser=new Intent(MyProfileActivity.this,EditProfileActivity.class);
						
						startActivity(intentUser);
						overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
						//startActivity(new Intent(MyProfileActivity.this, EditProfileActivity.class));
					}
				});
		//For Image......
		if(mLogUser.getImage()!=null && mLogUser.getImage().length>0){
			mProfImage = new String[mLogUser.getImage().length];
			mProfImage = mLogUser.getImage();
			ProfileImageAdapter adapter = new ProfileImageAdapter(MyProfileActivity.this, mProfImage);
			profileImageViewPager.setAdapter(adapter);
			pagerIndicator.setViewPager(profileImageViewPager);
			final float density = getResources().getDisplayMetrics().density;
	         pagerIndicator.setBackgroundColor(Color.TRANSPARENT);
	         pagerIndicator.setRadius(5 * density);
	         pagerIndicator.setPageColor(Color.GRAY);//0xFF888888
	         pagerIndicator.setFillColor(Color.parseColor("#FC5A42"));//0x880000FF
	         pagerIndicator.setStrokeColor(Color.TRANSPARENT);//0xFF000000
	         pagerIndicator.setStrokeWidth(2 * density);
		}
	}
	
	private TextView interestText(int flag,String interest){
		LayoutParams TxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 TextView interestTxt = new TextView(MyProfileActivity.this);
		 interestTxt.setId(flag);
		 interestTxt.setText(interest);
		 interestTxt.setLayoutParams(TxtParams);
		 interestTxt.setPadding(5, 5, 5, 5);
		 interestTxt.setGravity(Gravity.CENTER_HORIZONTAL);
		 interestTxt.setTypeface(Utils.getTypeface(MyProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		 interestTxt.setTextSize(16f);
        return interestTxt;
	}
	 private ImageView interestImageView(int flag,String interest){
		 LayoutParams imgviewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 ImageView imgView = new ImageView(MyProfileActivity.this);
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
	
	public void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader.get(url,
						ImageLoader.getImageListener(imageview,
								R.color.trans, R.color.trans));

		// imageLoader//
		// .get(url,
		// ImageLoader.getImageListener(imageview,
		// R.drawable.img_1, R.drawable.img_1));
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = new Intent(MyProfileActivity.this,DiscoveryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}
	 
	
}
