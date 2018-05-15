package com.unos.crescentapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CrescentProgressDialog;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InterestActivity extends BaseActivity{
	private CrescentProgressDialog progressDialog;
	private LinearLayout ll_fashion,ll_sports,ll_politics,
    ll_music,ll_fitness,ll_tech,
    ll_food,ll_business,ll_arts,
    ll_movies,ll_travells,ll_events;
	private TextView txtFashion, txtSports, txtPolitics, txtMusic, txtFitness,
			txtTech, txtFood, txtBusiness, txtArts, txtMovies, txtTravels,
			txtEvents,txtTitle,txtWhat;
    private Button btnNext;
    private ArrayList<String> interestItem;
    private int count_event_fashion=0;
    private int count_event_sports=0;
    private int count_event_politics=0;
    private int count_event_music=0;
    private int count_event_fitness=0;
    private  int count_event_tech=0;
    private  int count_event_food=0;
    private  int count_event_business=0;
    private  int count_event_art=0;
    private  int count_event_movies=0;
    private  int count_event_travel=0;
    private  int count_event_events=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interest_choose);
	ll_fashion=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_fashion);
	ll_sports=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_sports);
	ll_politics=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_polities);
	ll_music=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_music);
	ll_fitness=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_fitness);
	ll_tech=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_tech);
	ll_food=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_food);
	ll_business=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_business);
	ll_arts=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_artist);
	ll_movies=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_movies);
	ll_travells=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_travel);
	ll_events=(LinearLayout)findViewById(R.id.activity_interest_chooser_linear_lay_events);
	
	txtFashion = (TextView) findViewById(R.id.activity_interest_choose_txtvw_fashion);
	txtSports = (TextView) findViewById(R.id.activity_interest_choose_txtvw_sports);
	txtArts = (TextView) findViewById(R.id.activity_interest_choose_txtvw_artist);
	txtBusiness = (TextView) findViewById(R.id.activity_interest_choose_txtvw_business);
	txtEvents = (TextView) findViewById(R.id.activity_interest_choose_txtvw_events);
	txtFitness = (TextView) findViewById(R.id.activity_interest_choose_txtvw_fitness);
	txtFood = (TextView) findViewById(R.id.activity_interest_choose_txtvw_food);
	txtMovies = (TextView) findViewById(R.id.activity_interest_choose_txtvw_movies);
	txtMusic = (TextView) findViewById(R.id.activity_interest_choose_txtvw_music);
	txtTech = (TextView) findViewById(R.id.activity_interest_choose_txtvw_tech);
	txtPolitics = (TextView) findViewById(R.id.activity_interest_choose_txtvw_polities);
	txtTravels = (TextView) findViewById(R.id.activity_interest_choose_txtvw_travel);
	txtTitle = (TextView) findViewById(R.id.activity_interest_choose_txtvw_title);
	txtWhat = (TextView) findViewById(R.id.activity_interest_choose_txtvw_what);
	
	btnNext=(Button)findViewById(R.id.activity_interest_btn_next);
	btnNext.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Regular.ttf"));
	interestItem =new ArrayList<String>();
	
    ll_fashion.setOnClickListener(onClickListener);
    ll_sports.setOnClickListener(onClickListener);
    ll_politics.setOnClickListener(onClickListener);
    ll_music.setOnClickListener(onClickListener);
    ll_fitness.setOnClickListener(onClickListener);
    ll_tech.setOnClickListener(onClickListener);
    ll_food.setOnClickListener(onClickListener);
    ll_business.setOnClickListener(onClickListener);
    ll_arts.setOnClickListener(onClickListener);
    ll_movies.setOnClickListener(onClickListener);
    ll_travells.setOnClickListener(onClickListener);
    ll_events.setOnClickListener(onClickListener);
    
    txtWhat.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
    txtTitle.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Regular.ttf"));
    txtFashion.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtSports.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtArts.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtBusiness.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtEvents.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtFitness.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtFood.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtMovies.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtMusic.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtTech.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtPolitics.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
	txtTravels.setTypeface(Utils.getTypeface(InterestActivity.this, "fonts/SourceSansPro-Light.ttf"));
    
    
    btnNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*Intent intent=new Intent(InterestActivity.this,WantActivity.class);
				intent.putExtra("userDetails", mLogUser);
			    startActivity(intent);
			    finish();*/
				
				if(!mLogUser.getUserId().equals("")){
					
				if(interestItem.size()>0)
					makeJsonObjectRequestPost();
				else
					Toast.makeText(InterestActivity.this, "Please Select Interest", Toast.LENGTH_LONG).show();
				}

			}
		});
	}
	private OnClickListener onClickListener=new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.activity_interest_chooser_linear_lay_fashion:
				
				if (count_event_fashion%2==0) {
					if(interestItem.size()<5){
						interestItem.add("Fashion");
						ll_fashion.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_fashion++;
					}
					
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Fashion");
						ll_fashion.setBackgroundResource(R.drawable.border_button);
						count_event_fashion--;
					 }
					}
				
			break;
			case R.id.activity_interest_chooser_linear_lay_sports:
				
				if (count_event_sports%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Sports");
						ll_sports.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_sports++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Sports");
					ll_sports.setBackgroundResource(R.drawable.border_button);
					count_event_sports--;
					}
				}
				//sports="Sports";
				
				break;
			case R.id.activity_interest_chooser_linear_lay_polities:
				
				if (count_event_politics%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Politics");
						ll_politics.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_politics++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Politics");
						ll_politics.setBackgroundResource(R.drawable.border_button);
						count_event_politics--;
					}
				}
	
				
				
				break;
			case R.id.activity_interest_chooser_linear_lay_music:
				
				if (count_event_music%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Music");
						ll_music.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_music++;
					}
					
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Music");
						ll_music.setBackgroundResource(R.drawable.border_button);
						count_event_music--;
					}
				}
		
				
				
				break;
			case R.id.activity_interest_chooser_linear_lay_fitness:
				
				if (count_event_fitness%2==0) {//selected
					if(interestItem.size()<5){
					interestItem.add("Fitness");
					ll_fitness.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_fitness++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Fitness");
					ll_fitness.setBackgroundResource(R.drawable.border_button);
					count_event_fitness++;
					}
				}
		
				
				break;
			case R.id.activity_interest_chooser_linear_lay_tech:
				
				if (count_event_tech%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Tech");
						ll_tech.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_tech++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Tech");
						ll_tech.setBackgroundResource(R.drawable.border_button);
						count_event_tech--;
					}
				}
		
				
				break;
			case R.id.activity_interest_chooser_linear_lay_food:
				
				if (count_event_food%2==0) {//selected
					if(interestItem.size()<5){
					interestItem.add("Food");
					ll_food.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_food++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Food");
					ll_food.setBackgroundResource(R.drawable.border_button);
					count_event_food--;
					}
				}
		
				
				break;
			case R.id.activity_interest_chooser_linear_lay_business:
				
				if (count_event_business%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Business");
						ll_business.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_business++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Business");
						ll_business.setBackgroundResource(R.drawable.border_button);
						count_event_business--;
					}
				}
			
				
				break;
			case R.id.activity_interest_chooser_linear_lay_artist:
				
				if (count_event_art%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Art");
						ll_arts.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_art++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Art");
						ll_arts.setBackgroundResource(R.drawable.border_button);
						count_event_art--;
					}
				}
				
            break;
			case R.id.activity_interest_chooser_linear_lay_movies:
				
				if (count_event_movies%2==0) {//selected
					if(interestItem.size()<5){
						interestItem.add("Movies");
						ll_movies.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_movies++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Movies");
						ll_movies.setBackgroundResource(R.drawable.border_button);
						count_event_movies--;
					}
				}
				
			break;
			case R.id.activity_interest_chooser_linear_lay_travel:
				
				if (count_event_travel%2==0) {//selected
					if(interestItem.size()<5){
					interestItem.add("Travel");
					ll_travells.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_travel++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Travel");
					ll_travells.setBackgroundResource(R.drawable.border_button);
					count_event_travel--;
					}
				}
				
				
				break;

			  case R.id.activity_interest_chooser_linear_lay_events:
				
				if (count_event_events%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Events");
					ll_events.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_events++;
					}
				}
				else {
					if(interestItem.size()<5){
						interestItem.remove("Events");
						ll_events.setBackgroundResource(R.drawable.border_button);
						count_event_events--;
					}
				}
				
				break;
				
			}
			
		}
	};
 	
 	private void makeJsonObjectRequestPost() {
 		if (!Utils.isNetworkConnected(InterestActivity.this)) {
 			Toast.makeText(this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		if(!Utils.isReachable(InterestActivity.this)){
 			Toast.makeText(this,getResources().getString(R.string.unable_connect), Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		
 		String interest="";
			
			for(int i = 0; i <interestItem.size(); i++){
				if(i<interestItem.size()-1){
					interest= interest+interestItem.get(i)+",";
				}else
					interest = interest+interestItem.get(i);
			}
			Log.v("Interest", ""+interest);
			mLogUser.setInterests(interest);
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			
 			
 			params.put("userid",mLogUser.getUserId());
 			params.put("interests",interest);
 			
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		new InterestTask(WebServiceURL.UPDATE_MY_INTEREST, params).execute();
 	
 	}
 	private class InterestTask extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public InterestTask(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new CrescentProgressDialog(InterestActivity.this);
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
			progressDialog.cancel();
			
			}
			
			//Toast.makeText(InterestActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if(strRes.equalsIgnoreCase("true")){
				 Gson gson = new Gson();
				 String userDetails = gson.toJson(mLogUser);
				 MySharedPrefs.saveString(InterestActivity.this, userDetails, MySharedPrefs.USER_DATA);
				Intent intent=new Intent(InterestActivity.this,WantActivity.class);
				intent.putExtra("userDetails", mLogUser);
			    startActivity(intent);
			    finish();
			    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
			}
		}

		
 		
 	}

	
}	
