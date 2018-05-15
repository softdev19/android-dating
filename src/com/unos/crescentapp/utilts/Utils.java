package com.unos.crescentapp.utilts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import com.unos.crescentapp.constant.Constants;

import eu.janmuller.android.simplecropimage.CropImage;

public class Utils {

	private static final String TAG = null;
	private static File mFileTemp;
	public static String imgPath;
	Context mContext;

	public Utils(Context mContext) {
		this.mContext = mContext;
	}

	public static String getDate(long milliSeconds, String dateFormat) {
		// Create a DateFormatter object for displaying date in specified
		// format.
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		// Create a calendar object that will convert the date and time value in
		// milliseconds to date.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliSeconds);
		return formatter.format(calendar.getTime());
	}

	public float getPixelDensity() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.density;
	}

	public float getPixelDensityDpi() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.densityDpi;
	}

	public float getxdpi() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.xdpi;
	}

	public float getydpi() {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.ydpi;
	}

	public static float getheightPixel(Context mContext) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static float getwidthPixel(Context mContext) {
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		return metrics.widthPixels;
	}

	public int getDisplayHeight() {
		return mContext.getResources().getDisplayMetrics().heightPixels;
	}

	public int getDisplayWidth() {

		return mContext.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * checks if internet connection is possible. checks radio signal presence
	 * and airplane mode.
	 * 
	 * @return true if device is connect able.
	 */

	public static float functionNormalize(int max, int min, int value) {
		int intermediateValue = max - min;
		value -= intermediateValue;
		float var = Math.abs((float) value / (float) intermediateValue);
		return Math.abs((float) value / (float) intermediateValue);
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	public static Typeface getTypeface(Context context, String typeface) {
		Typeface mTypeface = Typeface.createFromAsset(context.getAssets(),
				typeface);
		return mTypeface;
	}

	public boolean isConnectionPossible() {

		ConnectivityManager connectivity = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	public void displayAlert(String message) {
		// TODO Auto-generated method stub

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);
		alertDialogBuilder.setMessage(message);
		alertDialogBuilder.setTitle("Confirm");
		alertDialogBuilder.setPositiveButton("Ok",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();

	}

	public static File getOutputMediaFile(Context context) {

		Log.e("In function", "In function");
		File mediaStorageDir;

		mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
				"Crescent");

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.e("Quotes", "failed to create directory");
				return null;
			} else {
				Log.e("Quote App", "Created");
			}
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		// scanFile(context, mediaFile.getPath());
		return mediaFile;
	}

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm != null) {
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
		}
		return false;
	}

	public static boolean isReachable(Context context) {
		boolean isReachable = false;
		try {
			isReachable = new ServerReachable(context).execute().get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return isReachable;
	}

	/*
	public static String giveDateString(String currentFormat,
			String neededFormat, String date) {
		String str = "";
		SimpleDateFormat currentSimpleFormat = new SimpleDateFormat(
				currentFormat);
		SimpleDateFormat neededSimpleFormat = new SimpleDateFormat(neededFormat);
		Date d = new Date();
		try {
			d = currentSimpleFormat.parse(date);
			str = neededSimpleFormat.format(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "Date String after conversion" + str);
		return str;
	}*/

	public static File getPicLocation(Context context) {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			mFileTemp = new File(Environment.getExternalStorageDirectory(),
					InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
		} else {
			mFileTemp = new File(context.getFilesDir(),
					InternalStorageContentProvider.TEMP_PHOTO_FILE_NAME);
		}
		return mFileTemp;
	}

	public static String giveDateAdded(String format, long millesec) {
		String str = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		str = dateFormat.format(new Date(millesec));
		return str;
	}

	public static Uri imageUri;

	// Select Image....
	public static Uri selectImage(final Activity activity) {
		// TODO Auto-generated method stub
		final CharSequence[] options = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setTitle("Add Photo!");

		builder.setItems(options, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {

				if (options[item].equals("Take Photo"))

				{

					try {
						ContentValues values = new ContentValues();
						String fileName = "Camera_Example.jpg";
						values.put(MediaStore.Images.Media.TITLE, fileName);
						values.put(MediaStore.Images.Media.DESCRIPTION,
								"Image capture by camera");
						imageUri = activity.getContentResolver().insert(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								values);
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								imageUri);
						// intent.putExtra(MediaStore.EXTRA_OUTPUT,
						// setImageUri());
						activity.startActivityForResult(intent,
								Constants.PICK_FROM_CAMERA);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					/*
					 * try { Uri mImageCaptureUri = null; String state =
					 * Environment.getExternalStorageState(); if
					 * (Environment.MEDIA_MOUNTED.equals(state)) {
					 * mImageCaptureUri = Uri.fromFile(mFileTemp); } else {
					 * 
					 * 
					 * 
					 * mImageCaptureUri =
					 * InternalStorageContentProvider.CONTENT_URI; }
					 * intent.putExtra(
					 * android.provider.MediaStore.EXTRA_OUTPUT,
					 * mImageCaptureUri); intent.putExtra("return-data", true);
					 * activity.startActivityForResult(intent,
					 * Constants.PICK_FROM_CAMERA);
					 * 
					 * } catch (ActivityNotFoundException e) { String
					 * errorMessage =
					 * "Whoops - your device doesn't support capturing images!";
					 * ((BaseFragment) activity).showToast(errorMessage);
					 * 
					 * }
					 */
				} else if (options[item].equals("Choose from Gallery")) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					activity.startActivityForResult(photoPickerIntent,
							Constants.PICK_FROM_GALLERY);
					/*
					 * Intent intent = new Intent(); // call android default
					 * gallery intent.setType("image/*");
					 * intent.setAction(Intent.ACTION_GET_CONTENT); // ********
					 * code for crop image intent.putExtra("crop", "true");
					 * intent.putExtra("aspectX", 0); intent.putExtra("aspectY",
					 * 0); intent.putExtra("outputX", 200);
					 * intent.putExtra("outputY", 150);
					 * 
					 * try {
					 * 
					 * intent.putExtra("return-data", true);
					 * frag.startActivityForResult(Intent.createChooser(intent,
					 * "Complete action using"), Constants.PICK_FROM_GALLERY);
					 * 
					 * } catch (ActivityNotFoundException e) { // Do nothing for
					 * now }
					 */
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}

			}

		});

		builder.show();
		return imageUri;
	}

	@SuppressWarnings("deprecation")
	public static String convertImageUriToFile(Uri imageUri, Activity activity) {

		Cursor cursor = null;
		int imageID = 0;
		String Path = "";

		try {
			/*********** Which columns values want to get *******/
			String[] proj = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID,
					MediaStore.Images.Thumbnails._ID,
					MediaStore.Images.ImageColumns.ORIENTATION };

			cursor = activity.managedQuery(

			imageUri, // Get data for specific image URI
					proj, // Which columns to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null // Order-by clause (ascending by name)

					);

			// Get Query Data

			int columnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnIndexThumb = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			int file_ColumnIndex = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

			// int orientation_ColumnIndex = cursor.
			// getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);

			int size = cursor.getCount();

			/******* If size is 0, there are no images on the SD Card. *****/

			if (size == 0) {

				// imageDetails.setText("No Image");
			} else {

				int thumbID = 0;
				if (cursor.moveToFirst()) {

					/**************** Captured image details ************/

					/***** Used to show image on view in LoadImagesFromSDCard class ******/
					imageID = cursor.getInt(columnIndex);

					thumbID = cursor.getInt(columnIndexThumb);

					Path = cursor.getString(file_ColumnIndex);

					// String orientation =
					// cursor.getString(orientation_ColumnIndex);

					String CapturedImageDetails = " CapturedImageDetails : \n\n"
							+ " ImageID :"
							+ imageID
							+ "\n"
							+ " ThumbID :"
							+ thumbID + "\n" + " Path :" + Path + "\n";

					// Show Captured Image detail on activity

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return Captured Image ImageID ( By this ImageID Image will load from
		// sdcard )

		return "" + Path;
	}

	public static Uri setImageUri() {
		// Store image in dcim
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Crescent/", "image_" + new Date().getTime() + ".png");
		Uri imgUri = Uri.fromFile(file);
		imgPath = file.getAbsolutePath();
		return imgUri;
	}

	// Crop image....
	public static void startCropImage(Activity activity) {

		Intent intent = new Intent(activity, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
		intent.putExtra(CropImage.SCALE, true);

		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		activity.startActivityForResult(intent, Constants.PIC_CROP);
	}

	public static String getBase64String(Bitmap bitmap) {
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bao);

		byte[] ba = bao.toByteArray();

		String string = Base64.encodeToString(ba, Base64.DEFAULT);
		return string;
	}

	// Write to Sd Card......
	public static void writeToSdcard(String message) {
		try {
			File myFile = new File("/sdcard/mysdfile.txt");
			if (!myFile.exists())
				myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append(message);
			myOutWriter.close();
			fOut.close();

		} catch (Exception e) {

		}
	}

	/*
	 * public static int getAge (int _year, int _month, int _day) {
	 * 
	 * GregorianCalendar cal = new GregorianCalendar(); int y, m, d, a;
	 * 
	 * y = cal.get(Calendar.YEAR); m = cal.get(Calendar.MONTH); d =
	 * cal.get(Calendar.DAY_OF_MONTH); cal.set(_year, _month, _day); a = y -
	 * cal.get(Calendar.YEAR); if ((m < cal.get(Calendar.MONTH)) || ((m ==
	 * cal.get(Calendar.MONTH)) && (d < cal .get(Calendar.DAY_OF_MONTH)))) {
	 * --a; } if(a < 0) throw new IllegalArgumentException("Age < 0"); return a;
	 * }
	 */

	public static int getAge(int year, int month, int day) {
		Calendar dob = Calendar.getInstance();
		Calendar today = Calendar.getInstance();

		dob.set(year, month, day);

		int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

		if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
			age--;
		}

		return age;
	}

	public static void buildAlertMessageNoGps(final Context context) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(
				Html.fromHtml("<b> Allow \" DatingApp\" to access your location while you use the app?</b>")
						+ "\n"
						+ "Location is required to find out where you are")
				.setCancelable(false)
				.setPositiveButton("Allow",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								context.startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("Don't Allow",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								dialog.cancel();
							}
						});

		final AlertDialog alert = builder.create();
		alert.show();
	}

	public static File getFile(Context context, Bitmap btmap) {
		File f = new File(context.getCacheDir(), "IMG_"
				+ System.currentTimeMillis() + ".png");
		try {
			f.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Convert bitmap to byte array

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		btmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, bos);
		byte[] bitmapdata = bos.toByteArray();

		// write the bytes in file
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(bitmapdata);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return f;
	}

	public static Bitmap decodeFile(String path) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, o);
			// The new size we want to scale to
			final int REQUIRED_SIZE = 70;

			// Find the correct scale value. It should be the power of 2.
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_SIZE
					&& o.outHeight / scale / 2 >= REQUIRED_SIZE)
				scale *= 2;

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeFile(path, o2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	public static void showAlertDialog(String msg, String title, Context context) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		alertDialogBuilder.setTitle(title);
		alertDialogBuilder.setMessage(msg)
				// .setIcon(R.drawable.icon_beware)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		alertDialogBuilder.create().show();
	}
}
