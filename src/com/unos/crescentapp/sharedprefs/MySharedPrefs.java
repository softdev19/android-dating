package com.unos.crescentapp.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefs {
	public static final String MY_PREFS = "myPrefs";
	public static final String COUNTRYID = "countryIds";
	public static final String STATEID = "stateIds";
	public static final String COUNTRY_NAME = "countryName";
	public static final String STATE_NAME = "stateName";
	public static final String BACKUP_ID = "email_backup_id";
	public static final String PIN="securepin";
	public static final String USER_ID="userid";
	public static final String FB_USER_ID="fbuserid";
	public static final String GOOGLE_PLUS_IMAGE_URL = "googleImgUrl";
	public static final String MESSAGE_ALERT="message_alert";
	public static final String NEW_MATCH_ALERT="new_match_alert";
	public static final String USER_DATA="userDetails";
	public static final String COACHMARK_SHOW="coach_mark";
	public static final String DEVICE_ID="device_id";

	public static SharedPreferences getSharedPrefs(Context context) {
		SharedPreferences myPrefs = context.getSharedPreferences(MY_PREFS,
				Context.MODE_PRIVATE);
		return myPrefs;
	}

	public static SharedPreferences.Editor getEditor(Context context) {
		SharedPreferences myprefs = getSharedPrefs(context);
		return myprefs.edit();
	}

	public static void saveInt(Context context, int value, String key) {
		SharedPreferences.Editor myeditor = getEditor(context);
		myeditor.putInt(key, value);
		myeditor.commit();
	}

	public static int getInt(Context context, int defValue, String key) {
		SharedPreferences myprefs = getSharedPrefs(context);
		return myprefs.getInt(key, defValue);
	}

	public static String getString(Context context, String defValue, String key) {
		SharedPreferences myprefs = getSharedPrefs(context);
		return myprefs.getString(key, defValue);
	}

	public static void saveString(Context context, String value, String key) {
		SharedPreferences.Editor myeditor = getEditor(context);
		myeditor.putString(key, value);
		myeditor.commit();
	}

}
