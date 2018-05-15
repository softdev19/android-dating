package com.unos.crescentapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;

import eu.janmuller.android.simplecropimage.CropImage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EditProfileActivity extends BaseActivity {
	private ProgressDialog progressDialog;
	
	private EditText et_name,et_about;
	private TextView txtBirthDay,txtGender,txtCharCounter;
	private ImageView iv_profile_image_one,iv_profile_image_two,iv_profile_image_three,iv_profile_image_four;
    private String userName,userAbout,userGender,userDOB,userId,userInterests;
    private File mFileTemp;
    private Bitmap mprofileImageBitmap;
    private String[] mProfImage;
    private int imageposition=1;
    private ArrayList<String> interestItem;
	private LinearLayout ll_edit_prof_fashion, ll_edit_prof_sports,
			ll_edit_prof_politics, ll_edit_prof_music, ll_edit_prof_fitness,
			ll_edit_prof_tech, ll_edit_prof_food, ll_edit_prof_business,
			ll_edit_prof_arts, ll_edit_prof_movies, ll_edit_prof_travells,
			ll_edit_prof_events;
	private ImageButton profileImage_delete_ome, profileImage_delete_two,
			profileImage_delete_three, profileImage_delete_four;
	private int count_event_fashion = 1, count_event_sports = 1,
			count_event_politics = 1, count_event_music = 1,
			count_event_fitness = 1, count_event_tech = 1,
			count_event_food = 1, count_event_business = 1,
			count_event_art = 1, count_event_movies = 1,
			count_event_travel = 1, count_event_events = 1,year,month,day;
	private DatePickerDialog datePickerDialog;
    private ArrayList<Bitmap> bitmapArray;
    private String [] interestItemsArray;
    private String[] birthday;
    private ProgressDialog progDialog;
    private Button btnDone,btnCancel;
    private TextView txtTitle,txtName,txtTitleBirthday,txtTitleGender,txtInterests,txtAbout;
    private TextView txtFashion, txtSports, txtPolitics, txtMusic, txtFitness,//txtCount,
	txtTech, txtFood, txtBusiness, txtArts, txtMovies, txtTravels,
	txtEvents;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		interestItem =new ArrayList<String>();
		bitmapArray = new ArrayList<Bitmap>();
		mFileTemp = Utils.getPicLocation(getApplicationContext());
		setContentView(R.layout.activity_edit_my_profile);
		
		btnDone=(Button) findViewById(R.id.activity_edit_my_profile_btn_done);
		btnCancel = (Button) findViewById(R.id.activity_edit_my_profile_btn_cancel);
		
		et_name=(EditText) findViewById(R.id.activity_edit_my_profile_et_Name);
		et_about=(EditText) findViewById(R.id.actovity_edit_my_profile_et_about);
		
		txtBirthDay=(TextView) findViewById(R.id.activity_edit_my_profile_txtvw_birthday);
		txtGender=(TextView) findViewById(R.id.activity_edit_my_profile_txtvw_gender);
		//txtCount = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_count);
		iv_profile_image_one=(ImageView)findViewById(R.id.activity_edit_my_profile_iv_one);
		iv_profile_image_two=(ImageView)findViewById(R.id.activity_edit_my_profile_iv_two);
		iv_profile_image_three=(ImageView)findViewById(R.id.activity_edit_my_profile_iv_three);
		iv_profile_image_four=(ImageView)findViewById(R.id.activity_edit_my_profile_iv_four);
		
		profileImage_delete_ome=(ImageButton)findViewById(R.id.activity_edit_my_profile_one_ib_delete);
		profileImage_delete_two=(ImageButton) findViewById(R.id.activity_edit_my_profile_two_ib_delete);
		profileImage_delete_three=(ImageButton) findViewById(R.id.activity_edit_my_profile_ib_three_delete);
		profileImage_delete_four=(ImageButton) findViewById(R.id.activity_edit_my_profile_ib_four_delete);
		
		
		
		ll_edit_prof_fashion=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_fashion);
		ll_edit_prof_sports=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_sports);
		ll_edit_prof_politics=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_politics);
		ll_edit_prof_music=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_music);
		ll_edit_prof_fitness=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_fitness);
		ll_edit_prof_tech=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_tech);
		ll_edit_prof_food=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_food);
		ll_edit_prof_business=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_business);
		ll_edit_prof_arts=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_artist);
		ll_edit_prof_movies=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_movies);
		ll_edit_prof_travells=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_travel);
		ll_edit_prof_events=(LinearLayout)findViewById(R.id.activity_edit_my_profile_linear_lay_events);
		
		txtTitle =(TextView) findViewById(R.id.activity_edit_my_profile_txtvw_title);
		txtName = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_name);
		txtTitleBirthday=(TextView) findViewById(R.id.activity_edit_my_profile_txtvw_tit_birthday);
		txtTitleGender = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_tit_gender);
		txtAbout = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_about);
		txtInterests = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_interests);
		
		txtFashion = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_fashion);
		txtSports = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_sports);
		txtArts = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_artist);
		txtBusiness = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_business);
		txtEvents = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_events);
		txtFitness = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_fitness);
		txtFood = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_food);
		txtMovies = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_movies);
		txtMusic = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_music);
		txtTech = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_tech);
		txtPolitics = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_polities);
		txtTravels = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_travel);
		txtCharCounter = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_about_char_counter);
		
		btnDone.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnCancel.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtTitle.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtName.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtTitleBirthday.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtTitleGender.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtAbout.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtInterests.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		
		et_name.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		et_about.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		//txtCount.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtBirthDay.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtGender.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtFashion.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtSports.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtArts.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtBusiness.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtEvents.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtFitness.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtFood.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtMovies.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtMusic.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtTech.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtPolitics.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		txtTravels.setTypeface(Utils.getTypeface(EditProfileActivity.this, "fonts/SourceSansPro-Light.ttf"));
		
		
		userName=mLogUser.getName().toString();
		userDOB=mLogUser.getBirthday().toString();
		userAbout=mLogUser.getAbout().toString();
		userGender=mLogUser.getGender().toString();
		userId=mLogUser.getUserId().toString();
		userInterests=mLogUser.getInterests().toString();
		interestItemsArray =userInterests.split(",");
		for (int i = 0; i < interestItemsArray.length; i++) {
			Log.v("",interestItemsArray[i]);
			interestItem.add(interestItemsArray[i]);
			if (interestItemsArray[i].toString().equals("Fashion")) {
				count_event_fashion=count_event_fashion+1;
				ll_edit_prof_fashion.setBackgroundResource(R.drawable.btn_interest_selected);
			}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Sports")) {
				 count_event_sports=count_event_sports+1;
					ll_edit_prof_sports.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Politics")) {
				 count_event_politics=count_event_politics+1;
					ll_edit_prof_politics.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Music")) {
				 count_event_music=count_event_music+1;
					ll_edit_prof_music.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Fitness")) {
				 count_event_fitness=count_event_fitness+1;
					ll_edit_prof_fitness.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Food")) {
				 count_event_food=count_event_food+1;
					ll_edit_prof_food.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Business")) {
				 count_event_business= count_event_business+1;
					ll_edit_prof_business.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Art")) {
				 count_event_art=count_event_art+1;
					ll_edit_prof_arts.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Movies")) {
				 count_event_movies=count_event_movies+1;
					ll_edit_prof_movies.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Travels")||interestItemsArray[i].toString().equalsIgnoreCase("Travel")) {
				 count_event_travel= count_event_travel+1;
					ll_edit_prof_travells.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Events")) {
				 count_event_events= count_event_events+1;
					ll_edit_prof_events.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 if (interestItemsArray[i].toString().equalsIgnoreCase("Tech")) {
				 count_event_tech= count_event_tech+1;
					ll_edit_prof_tech.setBackgroundResource(R.drawable.btn_interest_selected);
				}
			 
		}
		
		
      
		et_about.setText(userAbout);
		/*et_about.addTextChangedListener(txwatcher);
		if(!userAbout.equals("")){
			txtCount.setText(""+userAbout.length());
		}*/
		et_about.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				int charsCount = s.length();
				txtCharCounter.setText("" + charsCount);
				if(charsCount == 200){
					txtCharCounter.setTextColor(Color.RED);
				} else {
					txtCharCounter.setTextColor(Color.DKGRAY);	
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
		txtBirthDay.setText(userDOB);
		txtGender.setText(userGender);
		et_name.setText(userName);
		
		txtBirthDay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  final Calendar c = Calendar.getInstance();
				if(!userDOB.equals("")){
					birthday= userDOB.split("/");
				}
				year = Integer.parseInt(birthday[2]);
				month = Integer.parseInt(birthday[0]);
				day = Integer.parseInt(birthday[1]);
				datePickerDialog = new DatePickerDialog(EditProfileActivity.this,
						pickerListener,year,month,day);
				datePickerDialog.show();
				
				
			}
		});
		
		txtGender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final CharSequence[] options = { "Male", "Female" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						EditProfileActivity.this);
				builder.setTitle("Select your gender");
				builder.setItems(options,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int item) {

								txtGender.setText(options[item]);
								dialog.cancel();
							}
						});
				builder.show();
			}

		});
		
		ll_edit_prof_fashion.setOnClickListener(onClickListener);
		   
		ll_edit_prof_sports.setOnClickListener(onClickListener);
		ll_edit_prof_politics.setOnClickListener(onClickListener);
		ll_edit_prof_music.setOnClickListener(onClickListener);
		ll_edit_prof_fitness.setOnClickListener(onClickListener);
		ll_edit_prof_tech.setOnClickListener(onClickListener);
		ll_edit_prof_food.setOnClickListener(onClickListener);
		ll_edit_prof_business.setOnClickListener(onClickListener);
		ll_edit_prof_arts.setOnClickListener(onClickListener);
		ll_edit_prof_movies.setOnClickListener(onClickListener);
		ll_edit_prof_travells.setOnClickListener(onClickListener);
		ll_edit_prof_events.setOnClickListener(onClickListener);
		if(mLogUser.getImage()!=null && mLogUser.getImage().length>0){
			mProfImage = new String[mLogUser.getImage().length];
			mProfImage = mLogUser.getImage();
			if (mProfImage.length >0) {
				for (int i = 0; i < mProfImage.length; i++) {
					
					switch (i) {
					case 0:
						loadProfileImage(mProfImage[i], iv_profile_image_one);
						profileImage_delete_ome.setVisibility(View.VISIBLE);
						iv_profile_image_one.setEnabled(false);
						break;
					case 1:
						loadProfileImage(mProfImage[i], iv_profile_image_two);
						profileImage_delete_two.setVisibility(View.VISIBLE);
						iv_profile_image_two.setEnabled(false);
						break;
					case 2:
						loadProfileImage(mProfImage[i], iv_profile_image_three);
						profileImage_delete_three.setVisibility(View.VISIBLE);
						iv_profile_image_three.setEnabled(false);
						break;
					case 3:
						loadProfileImage(mProfImage[i], iv_profile_image_four);
						profileImage_delete_four.setVisibility(View.VISIBLE);
						iv_profile_image_four.setEnabled(false);
						break;	
					default:
						break;
					}
					
				
				}
				
				}
			}
		if (Utils.isNetworkConnected(EditProfileActivity.this)
				&& Utils.isReachable(EditProfileActivity.this))
			new GetBitMapFromURL(mProfImage).execute();
		
		
		btnDone.setOnClickListener(
				new OnClickListener() {
                 @Override
					public void onClick(View v) {
                	 if(validates())
						makeJsonObjectRequestPost();
						//startActivity(new Intent(EditProfileActivity.this, DiscoveryActivity.class));
					}
				});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				
			}
		});
	
		 iv_profile_image_one.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.selectImage(EditProfileActivity.this);
				imageposition=1;
			}
		});
		iv_profile_image_two.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.selectImage(EditProfileActivity.this);
				imageposition=2;
			}
		});
		iv_profile_image_three.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.selectImage(EditProfileActivity.this);
				imageposition=3;
				
			}
		});
		iv_profile_image_four.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.selectImage(EditProfileActivity.this);
				imageposition=4;
			}
		});
		
		profileImage_delete_ome
		.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(bitmapArray.size()>=1)
				bitmapArray.remove(0);
				/*iv_profile_image_one
						.setImageResource(R.drawable.picture_add);
				Log.i("Number of found image removing one",
						String.valueOf(bitmapArray
								.size()));*/
				
				reArrangeProfileImageViews();
				

			}
		});
		profileImage_delete_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iv_profile_image_two.setImageResource(R.drawable.picture_add);
				if(bitmapArray.size()>=2)
				bitmapArray.remove(1);
				reArrangeProfileImageViews();
				Log.i("Number of found image removing two",
						String.valueOf(bitmapArray.size()));

			}
		});
		profileImage_delete_three.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv_profile_image_three.setImageResource(R.drawable.picture_add);
				if(bitmapArray.size()>=3)
					bitmapArray.remove(2);
				Log.i("Number of found image removing three",String.valueOf( bitmapArray.size()));
				reArrangeProfileImageViews();
				
				
				
			}
		});
		
	
		
		profileImage_delete_four.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				iv_profile_image_four.setImageResource(R.drawable.picture_add);
				if(bitmapArray.size()>=4)
				bitmapArray.remove(3);
				//reArrangeProfileImageViews();
				Log.i("Number of found image removing four",String.valueOf(bitmapArray.size()));
				profileImage_delete_four.setVisibility(View.GONE);
				iv_profile_image_four.setEnabled(true);
				
			}
		});
		
		
		
	}
	
	private  class GetBitMapFromURL extends AsyncTask<Void, Void, ArrayList<Bitmap>>{

		 private String[] strUrl;
		
		  public GetBitMapFromURL(String[] strUrl){
			  this.strUrl = strUrl;
			 
		  }
		  
			@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopProgress();
		}

			@Override
			protected ArrayList<Bitmap> doInBackground(Void... params) {
				for(int i=0;i<strUrl.length;i++){
					Bitmap myBitmap;
					try {
				        URL url = new URL(strUrl[i]);
				        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				        connection.setDoInput(true);
				        connection.connect();
				        InputStream input = connection.getInputStream();
				        myBitmap = BitmapFactory.decodeStream(input);
				        bitmapArray.add(myBitmap);
				    } catch (IOException e) {
				        // Log exception
				        return null;
				    }
				}
				return bitmapArray;
			}

		}
	
	  

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear,
	                int selectedMonth, int selectedDay) {
	        	 year  = selectedYear;
		            month = selectedMonth;
		            day   = selectedDay;

	            	 txtBirthDay.setText(new StringBuilder().append(month + 1)
		 	                    .append("/").append(day).append("/").append(year)
		 	                    .append(" "));
	            	 
	            		 
	        	}
	        };
	
	//For Profile Image 
	public void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader
				.get(url,
						ImageLoader.getImageListener(imageview,
								R.color.trans, R.color.trans));

	
	} 
	
	
	
	
	 private void showAlert(){
			 if (Utils.getAge(year, month, day)<17) {
            	
        	 new AlertDialog.Builder(EditProfileActivity.this)
        	  .setTitle("Crescent")
        	  .setMessage(Html.fromHtml(getResources().getString(R.string.age_limitation_for_user_dialog_message)))
        	  .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			})
        	  .setCancelable(false).show();
        	
		}
		 }
	 
	 
	 private boolean validates(){
			
			
			if(this.isEmpty(et_name.getText().toString())){
				Toast.makeText(EditProfileActivity.this,"Please Enter Your Name", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			/*if(this.isEmpty(et_about.getText().toString())){
				
				Toast.makeText(EditProfileActivity.this,"Please Enter About Yourself", Toast.LENGTH_SHORT).show();
				return false;
			}*/
			if(this.isEmpty(txtBirthDay.getText().toString())){
				
				Toast.makeText(EditProfileActivity.this,"Please Enter your Birthday", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(this.isEmpty(txtGender.getText().toString())){
				Toast.makeText(EditProfileActivity.this,"Please Enter your Gender", Toast.LENGTH_SHORT).show();
				return false;
			} 
			 if(Utils.getAge(year, month, day)<17){
				 showAlert();
				 return false;
			 }
			if(bitmapArray.size()<=0){
				Toast.makeText(EditProfileActivity.this,"Please Add Your Profile Image", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(interestItem.size()==0){
				Toast.makeText(EditProfileActivity.this, "Please Select Interest", Toast.LENGTH_LONG).show();
				return false;
			}
			
			return true;
		}
     //Empty checking
	 private boolean isEmpty(String str){
			if(str.trim().length() > 0){
				return false;
			}else{
				return true;
			}
		}
	 

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		
	}




	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.PICK_FROM_CAMERA) {
				
			//	mprofileImageBitmap = (Bitmap) data.getExtras().get("data"); 
				
				try {

					/*Bitmap mBitmap = BitmapFactory.decodeFile(Utils
							.convertImageUriToFile(Utils.imageUri,
									EditProfileActivity.this));*/
					File f = new File(Utils
							.convertImageUriToFile(Utils.imageUri,
									EditProfileActivity.this));
					Bitmap mBitmap = decodeBitmapFile(f);
					if (mBitmap == null) {
						Log.e("Camera", "Bitmap decode error");
					} else {
						mprofileImageBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(),mBitmap.getHeight(), true);
						setImage(mprofileImageBitmap);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}/* else if (requestCode == Constants.PIC_CROP) {
				String path = mFileTemp.getPath();
				if (path == null) {

					return;
				}
				mprofileImageBitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
				
						
		      switch (imageposition) {
		        case 1:
		        	
		        	iv_profile_image_one.setImageBitmap(mprofileImageBitmap);
		        	bitmapArray.add(mprofileImageBitmap); 
		        	// Add a bitmap
		        	Log.i("Number of found image adding ", String.valueOf( bitmapArray.size()));
		        	profileImage_delete_ome.setVisibility(View.VISIBLE);
		        	
		        	iv_profile_image_one.setEnabled(false);
						
					
			
			   break;
		        case 2:
		        	
		        	iv_profile_image_two.setImageBitmap(mprofileImageBitmap);
		        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
		    		
		    		Log.i("Number of found image Adding two", String.valueOf( bitmapArray.size()));
		        	profileImage_delete_two.setVisibility(View.VISIBLE);
		        	iv_profile_image_two.setEnabled(false);
			
			   break;
		        case 3:
		        	
		        	iv_profile_image_three.setImageBitmap(mprofileImageBitmap);
		        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
		    		
		    		Log.i("Number of found image add three", String.valueOf( bitmapArray.size()));
		        	profileImage_delete_three.setVisibility(View.VISIBLE);
		        	iv_profile_image_three.setEnabled(false);
			
			   break;
		        case 4:
		        
		        	iv_profile_image_four.setImageBitmap(mprofileImageBitmap);
		        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
		    		
		    		Log.i("Number of found image add four", String.valueOf( bitmapArray.size()));
		        	profileImage_delete_four.setVisibility(View.VISIBLE);
		        	iv_profile_image_four.setEnabled(false);
			
			   break;
		            default:
			        break;
		            }					
			
			 }

			else*/ if (requestCode == Constants.PICK_FROM_GALLERY && data!=null) {
				try {

					InputStream inputStream = getBaseContext()
							.getContentResolver().openInputStream(
									data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(
							mFileTemp);
					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();
					//startActivityForResult(data, Constants.PIC_CROP);
					//Utils.startCropImage(EditProfileActivity.this);
					mprofileImageBitmap = decodeBitmapFile(mFileTemp);//BitmapFactory.decodeFile(mFileTemp.getPath());
					setImage(mprofileImageBitmap);
				} catch (Exception e) {

					Log.e("", "Error while creating temp file", e);
				}
				
				
				
				
			}

		}
	}
	
	private void setImage(Bitmap profileImage){
		switch (imageposition) {
        case 1:
        	
        	iv_profile_image_one.setImageBitmap(mprofileImageBitmap);
        	bitmapArray.add(mprofileImageBitmap); 
        	// Add a bitmap
        	Log.i("Number of found image adding ", String.valueOf( bitmapArray.size()));
        	profileImage_delete_ome.setVisibility(View.VISIBLE);
        	
        	iv_profile_image_one.setEnabled(false);
				
			
	
	   break;
        case 2:
        	
        	iv_profile_image_two.setImageBitmap(mprofileImageBitmap);
        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
    		
    		Log.i("Number of found image Adding two", String.valueOf( bitmapArray.size()));
        	profileImage_delete_two.setVisibility(View.VISIBLE);
        	iv_profile_image_two.setEnabled(false);
	
	   break;
        case 3:
        	
        	iv_profile_image_three.setImageBitmap(mprofileImageBitmap);
        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
    		
    		Log.i("Number of found image add three", String.valueOf( bitmapArray.size()));
        	profileImage_delete_three.setVisibility(View.VISIBLE);
        	iv_profile_image_three.setEnabled(false);
	
	   break;
        case 4:
        
        	iv_profile_image_four.setImageBitmap(mprofileImageBitmap);
        	bitmapArray.add(mprofileImageBitmap); // Add a bitmap
    		
    		Log.i("Number of found image add four", String.valueOf( bitmapArray.size()));
        	profileImage_delete_four.setVisibility(View.VISIBLE);
        	iv_profile_image_four.setEnabled(false);
	
	   break;
            default:
	        break;
            }
	}
	
	public static void copyStream(InputStream input, OutputStream output)
	        throws IOException {

	    byte[] buffer = new byte[1024];
	    int bytesRead;
	    while ((bytesRead = input.read(buffer)) != -1) {
	        output.write(buffer, 0, bytesRead);
	    }
	}
	public void reArrangeProfileImageViews()
	{
		
		
		if(bitmapArray.size()>=0){
			if(bitmapArray.size()==0){
				iv_profile_image_one.setImageResource(R.drawable.picture_add);
				iv_profile_image_one.setEnabled(true);
				profileImage_delete_ome.setVisibility(View.GONE);
			}
			else if(bitmapArray.size()==1){
				iv_profile_image_one.setImageBitmap(bitmapArray.get(0));
				iv_profile_image_one.setEnabled(false);
				 iv_profile_image_two.setImageResource(R.drawable.picture_add);
				 iv_profile_image_two.setEnabled(true);
				 profileImage_delete_two.setVisibility(View.GONE);
				 iv_profile_image_three.setImageResource(R.drawable.picture_add);
				 iv_profile_image_three.setEnabled(true);
				 profileImage_delete_three.setVisibility(View.GONE);
				 iv_profile_image_four.setImageResource(R.drawable.picture_add);
				 iv_profile_image_four.setEnabled(true);
				 profileImage_delete_four.setVisibility(View.GONE);
			}else if(bitmapArray.size()==2){
				iv_profile_image_one.setImageBitmap(bitmapArray.get(0));
				iv_profile_image_one.setEnabled(false);
				 iv_profile_image_two.setImageBitmap(bitmapArray.get(1));
				 iv_profile_image_two.setEnabled(false);
				 iv_profile_image_three.setImageResource(R.drawable.picture_add);
				 profileImage_delete_three.setVisibility(View.GONE);
				 iv_profile_image_three.setEnabled(true);
				 iv_profile_image_four.setImageResource(R.drawable.picture_add);
				 iv_profile_image_four.setEnabled(true);
				 profileImage_delete_four.setVisibility(View.GONE);
			}else if(bitmapArray.size()==3){
				iv_profile_image_one.setImageBitmap(bitmapArray.get(0));
				iv_profile_image_one.setEnabled(false);
				 iv_profile_image_two.setImageBitmap(bitmapArray.get(1));
				 iv_profile_image_two.setEnabled(false);
				 iv_profile_image_three.setImageBitmap(bitmapArray.get(2));
				 iv_profile_image_three.setEnabled(false);
				 iv_profile_image_four.setImageResource(R.drawable.picture_add);
				 iv_profile_image_four.setEnabled(true);
				 profileImage_delete_four.setVisibility(View.GONE);
			}
				
				
		}
		/*for (int i = 0; i <bitmapArray.size(); i++) {
			switch (i) {
			case 0:
				  iv_profile_image_one.setImageBitmap(bitmapArray.get(i));
				break;
			case 1:
				  iv_profile_image_two.setImageBitmap(bitmapArray.get(i));
				break;
			case 2:
				  iv_profile_image_three.setImageBitmap(bitmapArray.get(i));
				break;
			case 3:
				  iv_profile_image_four.setImageBitmap(bitmapArray.get(i));
				break;
			default:
				break;
			}
		}*/
		
		
	}
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			switch (v.getId()) {
			case R.id.activity_edit_my_profile_linear_lay_fashion:
				if (count_event_fashion%2==1) {//selected
					if(interestItem.size()<5){
						interestItem.add("Fashion");
						ll_edit_prof_fashion.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_fashion++;
					  }
				
					}
					else {
						if(interestItem.size()<=5){
							interestItem.remove("Fashion");
							ll_edit_prof_fashion.setBackgroundResource(R.drawable.border_button);
							count_event_fashion--;
						 }
						}
				break;
			case R.id.activity_edit_my_profile_linear_lay_sports:
				
				
			if (count_event_sports%2==1) {
				if(interestItem.size()<5){
						interestItem.add("Sports");
						ll_edit_prof_sports.setBackgroundResource(R.drawable.btn_interest_selected);
						count_event_sports++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Sports");
						ll_edit_prof_sports.setBackgroundResource(R.drawable.border_button);
						count_event_sports--;
					}
				}
			
				break;
			case R.id.activity_edit_my_profile_linear_lay_politics:
				
				if (count_event_politics%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Politics");
					ll_edit_prof_politics.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_politics++;
					}
				}
				else {
					if(interestItem.size()<=5){
						interestItem.remove("Politics");
						ll_edit_prof_politics.setBackgroundResource(R.drawable.border_button);
						count_event_politics--;
					}
				}
	
				
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_music:
				
				if (count_event_music%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Music");
					ll_edit_prof_music.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_music++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Music");
					ll_edit_prof_music.setBackgroundResource(R.drawable.border_button);
					count_event_music--;
					}
				}
		
				
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_fitness:
				
				if (count_event_fitness%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Fitness");
					ll_edit_prof_fitness.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_fitness++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Fitness");
					ll_edit_prof_fitness.setBackgroundResource(R.drawable.border_button);
					count_event_fitness--;
					}
				}
		
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_tech:
				
				if (count_event_tech%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Tech");
					ll_edit_prof_tech.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_tech++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Tech");
					ll_edit_prof_tech.setBackgroundResource(R.drawable.border_button);
					count_event_tech--;
					}
				}
		
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_food:
				
				if (count_event_food%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Food");
					ll_edit_prof_food.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_food++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Food");
					ll_edit_prof_food.setBackgroundResource(R.drawable.border_button);
					count_event_food--;
					}
				}
		
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_business:
				
				if (count_event_business%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Business");
					ll_edit_prof_business.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_business++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Business");
					ll_edit_prof_business.setBackgroundResource(R.drawable.border_button);
					count_event_business--;
					}
				}
			
				
				break;
			case R.id.activity_edit_my_profile_linear_lay_artist:
				
				if (count_event_art%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Art");
					ll_edit_prof_arts.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_art++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Art");
					ll_edit_prof_arts.setBackgroundResource(R.drawable.border_button);
					count_event_art--;
					}
				}
				
            break;
			case R.id.activity_edit_my_profile_linear_lay_movies:
				
				if (count_event_movies%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Movies");
					ll_edit_prof_movies.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_movies++;
					}
				}
				else {
					if(interestItem.size()<=5){
					interestItem.remove("Movies");
					ll_edit_prof_movies.setBackgroundResource(R.drawable.border_button);
					count_event_movies--;
					}
				}
				
			break;
			case R.id.activity_edit_my_profile_linear_lay_travel:
				
				if (count_event_travel%2==1) {//selected
					if(interestItem.size()<5){
					interestItem.add("Travel");
					ll_edit_prof_travells.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_travel++;
					}
				}
				else {
					if(interestItem.size()<=5){
					if(interestItem.contains("Travel"))
						interestItem.remove("Travel");
					else if(interestItem.contains("Travels"))
						 interestItem.remove("Travels");
					ll_edit_prof_travells.setBackgroundResource(R.drawable.border_button);
					count_event_travel--;
					}
				}
				
				
				break;

			  case R.id.activity_edit_my_profile_linear_lay_events:
				
				if (count_event_events%2==1) {//selected
					interestItem.add("Events");
					ll_edit_prof_events.setBackgroundResource(R.drawable.btn_interest_selected);
					count_event_events++;
				}
				else {
					interestItem.remove("Events");
					ll_edit_prof_events.setBackgroundResource(R.drawable.border_button);
					count_event_events--;
				}
				
				break;
				
			}
			
			
		}
	};
	 /*TextWatcher txwatcher = new TextWatcher() {
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         
         }

         public void onTextChanged(CharSequence s, int start, int before, int count) {
          
         
          
         }

         public void afterTextChanged(Editable s) {
        	 txtCount.setText(String.valueOf(s.length()));
         }
      }; */ 
	
	/**
 	 * Method to make json object request where json response starts wtih {
 	 * */
 	private void makeJsonObjectRequestPost() {
 		if (!Utils.isNetworkConnected(EditProfileActivity.this)) {
 			Toast.makeText(this,getResources().getString(R.string.network_not_avail), Toast.LENGTH_SHORT).show();
 			
 			return;
 		}
 		
 		if (!Utils.isReachable(EditProfileActivity.this)) {
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
 		//showProgress();
 		HashMap<String, String> params = new HashMap<String, String>();
 		try {
 			userAbout=et_about.getText().toString();
 			userGender=txtGender.getText().toString();
 			userDOB=txtBirthDay.getText().toString();
 			userName=et_name.getText().toString();
 			Log.d("User Dob",""+userDOB);
          
 			if(bitmapArray.size() > 0){
 			 	for(int i=0;i<bitmapArray.size();i++)
 			 	{
 			 	Bitmap lastbitmap = bitmapArray.get(i);
 			 	params.put("image"+(1+i),Utils.getBase64String(lastbitmap));
 			    }
 			 }
 				
 			
 			params.put("userid",userId);
 			params.put("name",userName);
 			params.put("About",userAbout);
            params.put("gender",userGender);
            params.put("interests",interest);
 			params.put("birthday", userDOB);
 		
 			
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 		new UpdateProfileTasck(WebServiceURL.UPDATE_MY_PROFILE, params).execute();
 	
 	}
 	private class UpdateProfileTasck extends AsyncTask<Void,Void,String>{
 		private String strUrl,strRes,strMessage;
 		private HashMap<String,String>bodyParams;
 		public UpdateProfileTasck(String strUrl,HashMap<String,String>bodyParams){
 			this.strUrl = strUrl;
 			this.bodyParams = bodyParams;
 		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			/* progressDialog = new ProgressDialog(EditProfileActivity.this);
			    progressDialog.setMessage("Loading...");
			    progressDialog.show();*/
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
				if (strRes.equalsIgnoreCase("true")) {
					JSONObject jobj = jObject.getJSONObject("user");
					Gson gson = new Gson();
					mLogUser = gson.fromJson(jobj.toString(), LogUser.class);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return strRes;
		}

		@SuppressLint("InlinedApi")
		@Override
		protected void onPostExecute(String result ) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			stopProgress();
			
			/*if (progressDialog.isShowing()) {
			progressDialog.dismiss();
			
			}*/
			//Toast.makeText(EditProfileActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if(result.equalsIgnoreCase("true")){
				 Gson gson = new Gson();
				 String userDetails = gson.toJson(mLogUser);
				 MySharedPrefs.saveString(EditProfileActivity.this, userDetails, MySharedPrefs.USER_DATA);
				Intent intent = new Intent(EditProfileActivity.this,MyProfileActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
				startActivity(intent);
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		}

		
 		
 	}
	}

