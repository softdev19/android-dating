package com.unos.crescentapp;

import com.quickblox.auth.model.QBSession;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.asynctask.CreateSessionToken;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				if (Utils.isNetworkConnected(SplashActivity.this)) {
					if (Utils.isReachable(SplashActivity.this)) {
						new CreateSessionToken() {
							@Override
							protected void onPostExecute(QBSession result) {
								super.onPostExecute(result);
								if (result != null) {
									if (mLogUser == null) {
										startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
										finish();
										overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
									} else {
										if (mLogUser.hasValidQuickbloxId()) {
											startActivity(new Intent(SplashActivity.this, WantActivity.class));
											overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
										} else {
											new RegQuickBloxTasck(mLogUser) {
												@Override
												protected void onPostExecute(String result) {
													super.onPostExecute(result);
													if (mLogUser.hasValidQuickbloxId()) {
														startActivity(new Intent(SplashActivity.this,
																WantActivity.class));
														overridePendingTransition(R.anim.enter_from_right,
																R.anim.exit_to_left);
													} else {
														MySharedPrefs.saveString(SplashActivity.this, "",
																MySharedPrefs.USER_DATA);
														finish();
													}
												}
											}.execute();
										}
									}

								}
							}
						}.execute();
					} else
						showToast(getResources().getString(R.string.unable_connect));
				} else
					showToast(getResources().getString(R.string.network_not_avail));
			}
		}, 3000);
	}
}
