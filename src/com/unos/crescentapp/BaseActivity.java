package com.unos.crescentapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.asynctask.CreateSessionToken;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.view.CrescentProgressDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	Dialog dialog;
	CrescentProgressDialog cresDialog;
	protected LogUser mLogUser;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Gson gson = new Gson();
		String userJson = MySharedPrefs.getString(this, "",
				MySharedPrefs.USER_DATA);
		Log.d("Crescent", "Restore user: " + userJson);
		if("".equalsIgnoreCase(userJson)){
			mLogUser = null;
		} else {
			mLogUser = gson.fromJson(userJson, LogUser.class);
		}
		if (!QBChatService.isInitialized()) {
			QBChatService.init(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!QBChatService.isInitialized()) {
			QBChatService.init(this);
		}
	}

	protected class RegQuickBloxTasck extends AsyncTask<LogUser, Void, String> {
		private LogUser mUser;

		public RegQuickBloxTasck(LogUser user) {
			mUser = user;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress("QuickBlox SignUp");
		}

		@Override
		protected String doInBackground(LogUser... params) {
			QBUser mQBUser;
			mQBUser = new QBUser(mUser.getLogin(), "crescent123",
					mUser.getEmail());
			int qbid = 0;

			try {
				mQBUser = QBUsers.signIn(mQBUser);
				qbid = mQBUser.getId();
			} catch (QBResponseException e1) {
				e1.printStackTrace();
			}
			if (qbid == 0) { // SignIn failed, try signup
				try {
					QBUsers.signUp(mQBUser);
					qbid = mQBUser.getId();
				} catch (QBResponseException e) {
					e.printStackTrace();
				}
			}
			if (qbid == 0) { // Can't create user, break
				return "error";
			}

			JSONPostParser parser = new JSONPostParser();
			HashMap<String, String> userParams = new HashMap<String, String>();
			userParams.put("userid", mLogUser.getUserId());
			userParams.put("quickblox_id", "" + qbid);
			JSONObject jObject = parser.getJSONFromUrl(
					WebServiceURL.UPDATE_MY_PROFILE, userParams);
			String strRes;
			try {
				strRes = jObject.getString("success");
				if (strRes.equalsIgnoreCase("true")) {
					JSONObject jobj = jObject.getJSONObject("user");
					Gson gson = new Gson();
					mLogUser = gson.fromJson(jobj.toString(), LogUser.class);
					MySharedPrefs.saveString(BaseActivity.this, jobj.toString(), MySharedPrefs.USER_DATA);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}

			

			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dismissProgress();
			if("error".equalsIgnoreCase(result)){
				showToast("QuickBlox SignUp Error");
			}
			
			
		}

	}

	public void showToast(final String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	public void showProgress(String msg) {
		if (progressDialog != null)
			progressDialog.dismiss();
		progressDialog = ProgressDialog.show(BaseActivity.this, "", msg);
	}

	public void showProgress() {
		if (progressDialog != null)
			progressDialog.dismiss();
		progressDialog = new CrescentProgressDialog(BaseActivity.this);

		progressDialog.setProgressStyle(R.style.customprogressdialog);
		progressDialog.getWindow().setGravity(
				Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		progressDialog.show();

	}

	public void dismissProgress() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	public Handler getHandler() {
		return handler;
	}

	public void stopProgress() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/*
	 * public void showCrescentProgress(){ if(cresDialog!=null)
	 * cresDialog.cancel(); cresDialog = new
	 * CrescentProgressDialog(BaseActivity.this);
	 * 
	 * cresDialog.show();
	 * 
	 * }
	 */

	public void stopCrescentProgress() {
		if (cresDialog != null && cresDialog.isShowing()) {
			cresDialog.dismiss();
		}
	}

	public Bitmap decodeBitmapFile(File f) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			ExifInterface exif;
			float rotation = 0;
			Log.d("Camera", "Try to read EXIF");
			try {
				exif = new ExifInterface(f.getAbsolutePath());
				rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
						ExifInterface.ORIENTATION_NORMAL);
			} catch (Exception e) {
				Log.e("Camera", "" + e.getLocalizedMessage());
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}

			Log.d("Camera", "image rotation: " + rotation);

			// System.out.println(rotation);

			float rotationInDegrees = exifToDegrees(rotation);
			// System.out.println(rotationInDegrees);
			Log.d("Camera", "image rotationInDegrees: " + rotationInDegrees);

			Matrix matrix = new Matrix();
			matrix.postRotate(rotationInDegrees);

			// The new size we want to scale to
			final int REQUIRED_SIZE = 1200;
			int scale = 1;
			if (rotationInDegrees == 90 || rotationInDegrees == 270) {
				// Find the correct scale value. It should be the power of 2.

				if (o.outHeight > REQUIRED_SIZE) {

					final int halfHeight = o.outHeight / 2;

					// Calculate the largest inSampleSize value that is a power
					// of 2
					// and keeps both
					// height and width larger than the requested height and
					// width.
					while ((halfHeight / scale) > REQUIRED_SIZE) {
						scale *= 2;
					}
				}
			} else {
				// Find the correct scale value. It should be the power of 2.

				if (o.outWidth > REQUIRED_SIZE) {

					final int halfWidth = o.outWidth / 2;

					// Calculate the largest inSampleSize value that is a power
					// of 2
					// and keeps both
					// height and width larger than the requested height and
					// width.
					while ((halfWidth / scale) > REQUIRED_SIZE) {
						scale *= 2;
					}
				}
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inJustDecodeBounds = false;

			// o2.outWidth

			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
					null, o2);
			// int height = 700 * o2.outHeight / o2.outWidth;
			if (rotationInDegrees == 90 || rotationInDegrees == 270) {
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);

			}
			int height = REQUIRED_SIZE * bitmap.getHeight() / bitmap.getWidth();
			return Bitmap.createScaledBitmap(bitmap, REQUIRED_SIZE, height, true);

		} catch (Exception e) {
			Log.e("Camera", "" + e.getLocalizedMessage());
		}
		return null;
	}

	private static float exifToDegrees(float exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}

	void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

}
