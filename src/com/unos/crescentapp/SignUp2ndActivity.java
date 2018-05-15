package com.unos.crescentapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.BaseActivity.RegQuickBloxTasck;
import com.unos.crescentapp.asynctask.CreateSessionToken;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.gpstracker.GPSTracker;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CircleImageView;
import com.unos.crescentapp.view.CrescentProgressDialog;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SignUp2ndActivity extends BaseActivity {

	private CrescentProgressDialog progressDialog;
	private static final int CAMERA_REQUEST = 1888;
	private static int RESULT_LOAD_IMAGE = 1;
	private Bitmap profileImageBitmap, tempImage;
	private File mFileTemp;
	private CircleImageView iv_imageProfile;
	private ImageView iv_imageProfileBackground;
	private EditText et_userName, et_aboutUser, edtxtLocation;
	private String userFullName;
	private String userDetails;
	private String userDOB;
	private String userGender;
	private String userEmailid;
	private String userPassword;
	private String userPhoneNo, deviceId, loginType;
	private TextView txtGender, txtBirthDay, txtTitleGender, txtTitleBirthday,
			txtName, txtAbout, txtAddPhoto, txtLocation, txtCharCounter;
	private String location;
	static final int DATE_PICKER_ID = 1111;
	protected static final String TAG = null;
	private int year;
	private int month;
	private int day;
	private DatePickerDialog datePickerDialog;
	private Button btnDone;
	private GoogleCloudMessaging gcmObj;
	private CrescentProgressDialog progDialog;
	private GPSTracker gpsTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gpsTracker = new GPSTracker(this);

		setContentView(R.layout.activity_signup2);

		iv_imageProfile = (CircleImageView) findViewById(R.id.activity_signup2_iv_add_photo);
		iv_imageProfileBackground = (ImageView) findViewById(R.id.activity_signup2nd_iv_background);
		txtName = (TextView) findViewById(R.id.activity_signup2_txtvw_name);
		txtAbout = (TextView) findViewById(R.id.activity_signup2_txtvw_about);
		txtBirthDay = (TextView) findViewById(R.id.activity_signup2_txtvw_birthday);
		txtGender = (TextView) findViewById(R.id.activity_signup2_txtvw_gender);
		txtAddPhoto = (TextView) findViewById(R.id.activity_signup2_txtvw_add_photo);
		txtTitleGender = (TextView) findViewById(R.id.activity_signup2_txtvw_title_gender);
		txtTitleBirthday = (TextView) findViewById(R.id.activity_signup2_txtvw_title_birthday);
		txtLocation = (TextView) findViewById(R.id.activity_signup2_txtvw_location);
		txtCharCounter = (TextView) findViewById(R.id.activity_edit_my_profile_txtvw_about_char_counter);

		// txtCount = (TextView)
		// findViewById(R.id.activity_signup2_tv_about_txt_count);
		btnDone = (Button) findViewById(R.id.activity_signup2_btn_done);

		edtxtLocation = (EditText) findViewById(R.id.activity_signup2_et_location);
		et_userName = (EditText) findViewById(R.id.activity_signup2_et_name);
		et_aboutUser = (EditText) findViewById(R.id.activity_signup2_et_about);

		txtTitleGender.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));
		txtTitleBirthday.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));
		txtName.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));
		txtAbout.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));
		txtAddPhoto.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));
		txtLocation.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Semibold.ttf"));

		edtxtLocation.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		et_userName.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		et_aboutUser.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		txtBirthDay.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		txtGender.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		// txtCount.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
		// "fonts/SourceSansPro-Regular.ttf"));
		// et_aboutUser.addTextChangedListener(txwatcher);
		et_aboutUser.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				int charsCount = s.length();
				txtCharCounter.setText("" + charsCount);
				if (charsCount == 200) {
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

		profileImageBitmap = null;
		deviceId = MySharedPrefs.getString(SignUp2ndActivity.this, "",
				MySharedPrefs.DEVICE_ID);
		Intent from1st = getIntent();

		if (getIntent().getStringExtra("Facebook") != null) {
			Gson gson = new Gson();
			String userJson = MySharedPrefs.getString(SignUp2ndActivity.this,
					"", MySharedPrefs.USER_DATA);
			mLogUser = gson.fromJson(userJson, LogUser.class);
			loginType = "F";
			et_userName.setText(mLogUser.getName());
			// userEmailid = mLogUser.getEmail();
			// userPassword="1234W5678";
			edtxtLocation.setText(mLogUser.getLocation());
			txtGender.setText(mLogUser.getGender());
			((ImageButton) findViewById(R.id.signup2_imgbtn_back))
					.setVisibility(View.GONE);
			if (mLogUser.getImage().length > 0) {
				if (Utils.isNetworkConnected(SignUp2ndActivity.this)
						&& Utils.isReachable(SignUp2ndActivity.this)) {
					loadProfileImage(mLogUser.getImage()[0], iv_imageProfile);
					if (iv_imageProfile != null) {
						try {
							profileImageBitmap = new GetBitMapFromURL(
									mLogUser.getImage()[0]).execute().get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (profileImageBitmap != null) {
							tempImage = profileImageBitmap;
							blur(tempImage, iv_imageProfileBackground, 25.0f);
						}
					}
				}
			}

		} else {
			userEmailid = from1st.getStringExtra("emailId");
			userPassword = from1st.getStringExtra("passWord");
			loginType = "N";
		}

		// userPhoneNo=from1st.getStringExtra("phoneNo");

		/*
		 * et_birthDay.setText(new StringBuilder() // Month is 0 based, just add
		 * 1 .append(month + 1).append("/").append(day).append("/")
		 * .append(year).append(" "));
		 */
		txtBirthDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Calendar c = Calendar.getInstance();

				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				datePickerDialog = new DatePickerDialog(SignUp2ndActivity.this,
						pickerListener, year, month, day);
				datePickerDialog.show();

			}
		});

		mFileTemp = Utils.getPicLocation(getApplicationContext());

		iv_imageProfile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utils.selectImage(SignUp2ndActivity.this);

			}
		});
		findViewById(R.id.signup2_imgbtn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.enter_from_left,
								R.anim.exit_to_right);
					}
				});
		btnDone.setTypeface(Utils.getTypeface(SignUp2ndActivity.this,
				"fonts/SourceSansPro-Regular.ttf"));
		btnDone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * startActivity(new Intent(SignUp2ndActivity.this,
				 * InterestActivity.class));
				 */
				if (validates()) {
					if (getIntent().getStringExtra("Facebook") != null) {
						makeJsonObjectUpdate();
					} else
						makeJsonObjectRequestPost();
				}

			}
		});
		txtGender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (year != 0 && month != 0 && year != 0) {
					if (Utils.getAge(year, month, day) < 17) {
						showAlert();
					} else {
						final CharSequence[] options = { "Male", "Female" };
						AlertDialog.Builder builder = new AlertDialog.Builder(
								SignUp2ndActivity.this);
						builder.setTitle("Select your gender");
						builder.setItems(options,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int item) {

										txtGender.setText(options[item]);
										dialog.cancel();
									}
								});
						builder.show();
					}
				} else
					Toast.makeText(SignUp2ndActivity.this,
							"Please Enter your Date of Birth",
							Toast.LENGTH_LONG).show();
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap tempBitMap = null;
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.PICK_FROM_CAMERA) {
				// startActivityForResult(data, Constants.PIC_CROP);
				// Utils.startCropImage(SignUp2ndActivity.this);

				try {

					File f = new File(Utils.convertImageUriToFile(
							Utils.imageUri, SignUp2ndActivity.this));
					Bitmap mBitmap = decodeBitmapFile(f);
					if (mBitmap == null) {
						Log.e("Camera", "Bitmap decode error");
					} else {
						profileImageBitmap = Bitmap.createScaledBitmap(mBitmap,
								mBitmap.getWidth(),
								mBitmap.getHeight(), true);
						iv_imageProfile.setImageBitmap(profileImageBitmap);
						tempBitMap = profileImageBitmap;
						blur(tempBitMap, iv_imageProfileBackground, 25.0f);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
				 * }else if (requestCode == Constants.PIC_CROP) { String path =
				 * data.getStringExtra("data"); if (path == null) {
				 * 
				 * return; }
				 * 
				 * profileImageBitmap = BitmapFactory.decodeFile(mFileTemp
				 * .getPath());
				 * 
				 * iv_imageProfile.setImageBitmap(profileImageBitmap); //For
				 * Image Blur Effect if (iv_imageProfile!=null) {
				 * iv_imageProfileBackground.setImageBitmap(profileImageBitmap);
				 * final RenderScript rs = RenderScript.create(this); final
				 * Allocation input = Allocation.createFromBitmap(rs,
				 * profileImageBitmap, Allocation.MipmapControl.MIPMAP_NONE,
				 * Allocation.USAGE_SCRIPT); final Allocation output =
				 * Allocation.createTyped( rs, input.getType() ); final
				 * ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs,
				 * Element.U8_4( rs ) ); float myBlurRadius=8.0f;
				 * script.setRadius(myBlurRadius); script.setInput( input );
				 * script.forEach( output ); output.copyTo( profileImageBitmap
				 * );
				 * iv_imageProfileBackground.setImageBitmap(profileImageBitmap);
				 * } }
				 */

			} else if (requestCode == Constants.PICK_FROM_GALLERY
					&& data != null) {
				try {

					InputStream inputStream = getBaseContext()
							.getContentResolver().openInputStream(
									data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(
							mFileTemp);
					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();
					// startActivityForResult(data, Constants.PIC_CROP);
					// Utils.startCropImage(EditProfileActivity.this);
					profileImageBitmap = decodeBitmapFile(mFileTemp);// BitmapFactory.decodeFile(mFileTemp.getPath());

					iv_imageProfile.setImageBitmap(profileImageBitmap);
					tempBitMap = profileImageBitmap;
					blur(tempBitMap, iv_imageProfileBackground, 25.0f);
					// Utils.startCropImage(SignUp2ndActivity.this);

				} catch (Exception e) {

					Log.e(TAG, "Error while creating temp file", e);
				}

			}

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

	private class GetBitMapFromURL extends AsyncTask<Void, Void, Bitmap> {

		private String strUrl;

		public GetBitMapFromURL(String strUrl) {
			this.strUrl = strUrl;

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progDialog = new CrescentProgressDialog(SignUp2ndActivity.this);

			progDialog.setMessage("Signing In");
			progDialog.setProgressStyle(R.style.customprogressdialog);
			progDialog.getWindow().setGravity(
					Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			progDialog.show();

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (progDialog.isShowing())
				progDialog.cancel();
		}

		@Override
		protected Bitmap doInBackground(Void... params) {
			Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.add_your_photo);
			if (!strUrl.equals("")) {

				try {
					URL url = new URL(strUrl);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					myBitmap = BitmapFactory.decodeStream(input);

				} catch (IOException e) {
					// Log exception
					return null;
				}
			}
			return myBitmap;
		}
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
								.getInstance(SignUp2ndActivity.this);
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
				MySharedPrefs.saveString(SignUp2ndActivity.this, deviceId,
						MySharedPrefs.DEVICE_ID);
				Log.v("reg_id", msg);
			}
		}.execute(null, null, null);
	}

	private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			txtBirthDay.setText(new StringBuilder().append(month + 1)
					.append("/").append(day).append("/").append(year)
					.append(" "));
		}
	};

	/*
	 * TextWatcher txwatcher = new TextWatcher() { public void
	 * beforeTextChanged(CharSequence s, int start, int count, int after) {
	 * 
	 * }
	 * 
	 * public void onTextChanged(CharSequence s, int start, int before, int
	 * count) {
	 * 
	 * txtCount.setText(String.valueOf(s.length()));
	 * 
	 * }
	 * 
	 * public void afterTextChanged(Editable s) {
	 * 
	 * } };
	 */
	// Validator......
	private boolean validates() {

		if (this.isEmpty(et_userName.getText().toString())) {
			Toast.makeText(SignUp2ndActivity.this, "Please Enter Your Name",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		/*
		 * if(this.isEmpty(et_aboutUser.getText().toString())){
		 * 
		 * Toast.makeText(SignUp2ndActivity.this,"Please Enter About Yourself",
		 * Toast.LENGTH_SHORT).show(); return false; }
		 */
		if (this.isEmpty(txtBirthDay.getText().toString())) {

			Toast.makeText(SignUp2ndActivity.this,
					"Please Enter your Birthday", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (this.isEmpty(txtGender.getText().toString())) {
			Toast.makeText(SignUp2ndActivity.this, "Please Enter your Gender",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (this.isEmpty(edtxtLocation.getText().toString())) {
			Toast.makeText(SignUp2ndActivity.this,
					"Please Enter Your Location", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (profileImageBitmap == null) {
			Toast.makeText(SignUp2ndActivity.this,
					"Please Add Your Profile Image", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (Utils.getAge(year, month, day) < 17) {
			showAlert();
			return false;
		}
		return true;
	}

	// Empty checking
	private boolean isEmpty(String str) {
		if (str.trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private void showAlert() {
		if (Utils.getAge(year, month, day) < 17) {

			new AlertDialog.Builder(SignUp2ndActivity.this)
					.setTitle("Crescent")
					.setMessage(
							Html.fromHtml(getResources()
									.getString(
											R.string.age_limitation_for_user_dialog_message)))
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();

								}
							}).setCancelable(false).show();

		}
	}

	private void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader.get(url, ImageLoader.getImageListener(imageview,
				R.color.trans, R.color.trans));

	}

	private void makeJsonObjectRequestPost() {
		if (!Utils.isNetworkConnected(getApplicationContext())) {
			Toast.makeText(this,
					getResources().getString(R.string.network_not_avail),
					Toast.LENGTH_SHORT).show();

			return;
		}
		if (!Utils.isReachable(SignUp2ndActivity.this)) {
			Toast.makeText(SignUp2ndActivity.this,
					getResources().getString(R.string.unable_connect),
					Toast.LENGTH_SHORT).show();
			return;
		}

		/*
		 * String deviceId = Secure.getString(this.getContentResolver(),
		 * Secure.ANDROID_ID);
		 */
		if (deviceId.equals(""))
			registerInBackground();
		// showProgress();
		HashMap<String, String> params = new HashMap<String, String>();
		try {
			userFullName = et_userName.getText().toString();
			userDetails = et_aboutUser.getText().toString();
			userDOB = txtBirthDay.getText().toString();
			userGender = txtGender.getText().toString();
			location = edtxtLocation.getText().toString();
			params.put("name", userFullName);
			params.put("email", userEmailid);
			params.put("password", userPassword);
			if (profileImageBitmap != null) {
				params.put("image", Utils.getBase64String(profileImageBitmap));

			}

			params.put("phoneno", "");
			params.put("about", userDetails);
			params.put("birthday", userDOB);
			params.put("gender", userGender);
			params.put("latitude", String.valueOf(gpsTracker.getLatitude()));
			params.put("longitude", String.valueOf(gpsTracker.getLongitude()));
			params.put("location", location);
			params.put("devicetoken", deviceId);
			params.put("device", "android");
			params.put("login_type", loginType);
			Log.d("REGISTER USER", "USER PARAMS: " + params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Registartasck(WebServiceURL.REGISTER_USER, params).execute();

	}

	private class Registartasck extends AsyncTask<Void, Void, String> {
		private String strUrl, strRes, strMessage;
		private HashMap<String, String> bodyParams;


		public Registartasck(String strUrl, HashMap<String, String> bodyParams) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new CrescentProgressDialog(SignUp2ndActivity.this);
			progressDialog.setProgressStyle(R.style.customprogressdialog);
			progressDialog.getWindow().setGravity(
					Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			progressDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser = new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if (jObject != null)
				try {
					strRes = jObject.getString("success");
					strMessage = jObject.getString("message");
					if (strRes.equalsIgnoreCase("true")) {

						JSONObject jobj = jObject.getJSONObject("user");
						Gson gson = new Gson();
						mLogUser = gson
								.fromJson(jobj.toString(), LogUser.class);
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

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();

			}

			if (result.equalsIgnoreCase("true")) {
				MySharedPrefs.saveString(SignUp2ndActivity.this,
						mLogUser.getUserId(), MySharedPrefs.USER_ID);
				MySharedPrefs.saveString(SignUp2ndActivity.this, "yes",
						MySharedPrefs.COACHMARK_SHOW);
				Gson gson = new Gson();
				String userDetails = gson.toJson(mLogUser);
				MySharedPrefs.saveString(SignUp2ndActivity.this, userDetails,
						MySharedPrefs.USER_DATA);

				new RegQuickBloxTasck(mLogUser) {
					@Override
					protected void onPostExecute(String result) {
						super.onPostExecute(result);
						if (mLogUser.hasValidQuickbloxId()) {
							Intent intent = new Intent(SignUp2ndActivity.this,
									InterestActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);
							startActivity(intent);
							finish();
							overridePendingTransition(R.anim.enter_from_right,
									R.anim.exit_to_left);
						}
					}
				}.execute();
			}

		}
	}

	private void makeJsonObjectUpdate() {
		if (!Utils.isNetworkConnected(SignUp2ndActivity.this)) {
			Toast.makeText(this,
					getResources().getString(R.string.network_not_avail),
					Toast.LENGTH_SHORT).show();

			return;
		}

		if (!Utils.isReachable(SignUp2ndActivity.this)) {
			Toast.makeText(this,
					getResources().getString(R.string.unable_connect),
					Toast.LENGTH_SHORT).show();

			return;
		}

		String interest = "";

		HashMap<String, String> params = new HashMap<String, String>();

		try {
			String userAbout = et_aboutUser.getText().toString();
			String userGender = txtGender.getText().toString();
			String userDOB = txtBirthDay.getText().toString();
			String userName = et_userName.getText().toString();
			Log.d("User Dob", "" + userDOB);
			/*
			 * if(bitmapArray.size() > 0){ for(int i=0;i<bitmapArray.size();i++)
			 * { Bitmap lastbitmap = bitmapArray.get(i);
			 * params.put("image"+(1+i),Utils.getBase64String(lastbitmap)); } }
			 */

			params.put("image1", Utils.getBase64String(profileImageBitmap));
			params.put("userid", mLogUser.getUserId());
			params.put("name", userName);
			params.put("About", userAbout);
			params.put("gender", userGender);
			params.put("interests", interest);
			params.put("birthday", userDOB);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new UpdateProfileTasck(WebServiceURL.UPDATE_MY_PROFILE, params)
				.execute();

	}

	private class UpdateProfileTasck extends AsyncTask<Void, Void, String> {
		private String strUrl, strRes, strMessage;
		private HashMap<String, String> bodyParams;

		public UpdateProfileTasck(String strUrl,
				HashMap<String, String> bodyParams) {
			this.strUrl = strUrl;
			this.bodyParams = bodyParams;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new CrescentProgressDialog(SignUp2ndActivity.this);

			progressDialog.setProgressStyle(R.style.customprogressdialog);
			progressDialog.getWindow().setGravity(
					Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			progressDialog.show();

		}

		@Override
		protected String doInBackground(Void... params) {
			JSONPostParser parser = new JSONPostParser();
			JSONObject jObject = parser.getJSONFromUrl(strUrl, bodyParams);
			if (jObject != null)
				try {
					strRes = jObject.getString("success");
					strMessage = jObject.getString("message");
					if (strRes.equalsIgnoreCase("true")) {

						JSONObject jobj = jObject.getJSONObject("user");
						Gson gson = new Gson();
						mLogUser = gson
								.fromJson(jobj.toString(), LogUser.class);
					}

				} catch (JSONException e) {
					e.printStackTrace();
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
			// Toast.makeText(EditProfileActivity.this,""+strMessage,Toast.LENGTH_LONG).show();
			if (result.equalsIgnoreCase("true")) {
				MySharedPrefs.saveString(SignUp2ndActivity.this,
						mLogUser.getUserId(), MySharedPrefs.USER_ID);
				Gson gson = new Gson();
				String userDetails = gson.toJson(mLogUser);
				MySharedPrefs.saveString(SignUp2ndActivity.this, userDetails,
						MySharedPrefs.USER_DATA);
				
				if (mLogUser.hasValidQuickbloxId()) {
						MySharedPrefs.saveString(SignUp2ndActivity.this, "yes",
								MySharedPrefs.COACHMARK_SHOW);
						Intent intent = new Intent(SignUp2ndActivity.this,
								InterestActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.enter_from_right,
								R.anim.exit_to_left);
				} else {
					new RegQuickBloxTasck(mLogUser) {
						@Override
						protected void onPostExecute(String result) {
							super.onPostExecute(result);
							if (mLogUser.hasValidQuickbloxId()) {
									MySharedPrefs.saveString(
											SignUp2ndActivity.this, "yes",
											MySharedPrefs.COACHMARK_SHOW);
									Intent intent = new Intent(
											SignUp2ndActivity.this,
											InterestActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									finish();
									overridePendingTransition(
											R.anim.enter_from_right,
											R.anim.exit_to_left);
							}
						}
					}.execute();
				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		finish();
	}

	private void blur(Bitmap bkg, ImageView view, float radius) {
		/*
		 * Bitmap overlay = Bitmap.createBitmap( view.getMeasuredWidth(),
		 * view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		 */
		// Bitmap overlay =
		// Bitmap.createBitmap(bkg.getWidth(),bkg.getHeight(),bkg.getConfig());
		// Bitmap overlay = bkg.copy(bkg.getConfig(), true);
		// Canvas canvas = new Canvas(overlay);
		//
		// canvas.drawBitmap(bkg, -view.getLeft(),
		// -view.getTop(), null);
		//
		// RenderScript rs = RenderScript.create(SignUp2ndActivity.this);
		//
		// Allocation overlayAlloc = Allocation.createFromBitmap(
		// rs, overlay);
		//
		// ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
		// rs, Element.U8_4(rs));
		//
		// blur.setInput(overlayAlloc);
		//
		// blur.setRadius(radius);
		//
		// blur.forEach(overlayAlloc);
		//
		// overlayAlloc.copyTo(overlay);
		Bitmap overlay = bkg.copy(bkg.getConfig(), true);
		Canvas canvas = new Canvas(overlay);
		canvas.drawBitmap(bkg, -view.getLeft(), -view.getTop(), null);
		final RenderScript rs = RenderScript.create(this);
		final Allocation input = Allocation.createFromBitmap(rs, overlay,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));
		float myBlurRadius = 8.0f;
		script.setRadius(myBlurRadius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(overlay);

		/*
		 * view.setBackground(new BitmapDrawable( getResources(), overlay));
		 */
		view.setImageBitmap(overlay);
		rs.destroy();
	}

	/*
	 * private class CreateSessionToken extends AsyncTask<Void,Void, Void> {
	 * private QBSession session; private QBUser user;
	 * 
	 * public CreateSessionToken(QBUser user){ this.user = user; }
	 * 
	 * @Override protected Void doInBackground(Void... params) { try { session =
	 * QBAuth.createSession(); } catch (QBResponseException e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); }
	 * 
	 * if(session != null){ Log.v("TAG", "session created, token = " +
	 * session.getToken()); try {
	 * BaseService.getBaseService().setToken(session.getToken());
	 * 
	 * } catch (BaseServiceException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result);
	 * QBUsers.signIn(user, new QBEntityCallbackImpl<QBUser>() {
	 * 
	 * @Override public void onSuccess(QBUser qbUser, Bundle bundle) {
	 * AppController.getInstance().setCurrentUser(qbUser);
	 * //Toast.makeText(SignUp2ndActivity.this, "Login To Quickblox Success",
	 * Toast.LENGTH_SHORT).show(); }
	 * 
	 * @Override public void onError(List<String> arg0) {
	 * //Toast.makeText(SignUp2ndActivity.this, "Login To Quickblox Failed",
	 * Toast.LENGTH_SHORT).show(); } }); }
	 * 
	 * }
	 */

}
