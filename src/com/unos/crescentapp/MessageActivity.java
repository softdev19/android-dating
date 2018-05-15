package com.unos.crescentapp;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.adapter.MatchAdapter;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.Matches;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.QuickBloxUtils;
import com.unos.crescentapp.utilts.Utils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends BaseActivity {
	private ListView lvItemSelected;
	private ArrayList<Matches> matchList;
	private MatchAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);

		((TextView) findViewById(R.id.activity_message_txtvw_title))
				.setTypeface(Utils.getTypeface(MessageActivity.this,
						"fonts/SourceSansPro-Regular.ttf"));
		matchList = new ArrayList<>();
		adapter = new MatchAdapter(MessageActivity.this, matchList);
		lvItemSelected = (ListView) findViewById(R.id.activity_message_list_view_contuct);
		lvItemSelected.setAdapter(adapter);

		if (Utils.isNetworkConnected(MessageActivity.this)) {
			if (Utils.isReachable(MessageActivity.this)) {
				// getMatchList(WebServiceURL.GET_MATCH_LIST+mLogUser.getUserId());
				//new createSession().execute();
				new LogToChatTask().execute();
			} else
				showToast(getResources().getString(R.string.unable_connect));
		} else
			showToast(getResources().getString(R.string.network_not_avail));
		/*
		 * if(mLogUser.getName().contains(" ")){ user = new
		 * QBUser(mLogUser.getName().substring(0,
		 * mLogUser.getName().indexOf(" ")), "crescent123"); }else user = new
		 * QBUser(mLogUser.getName(), "crescent123");
		 */

		lvItemSelected.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//if (AppController.getInstance().getCurrentUser() != null) {
					new createChatDialog(
							matchList.get(position).getUserEmail(), matchList
									.get(position).getUserName(), matchList
									.get(position).getImgUrl()).execute();
				//} else
				//	Utils.showAlertDialog("Unable to Create Chat Session",
				//			"Message", MessageActivity.this);
				/*
				 * Intent i=new
				 * Intent(MessageActivity.this,ChatListActivity.class);
				 * startActivity(i);
				 * overridePendingTransition(R.anim.enter_from_right,
				 * R.anim.exit_to_left);
				 */
			}
		});
		findViewById(R.id.activity_message_imgbtn_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
						overridePendingTransition(R.anim.enter_from_left,
								R.anim.exit_to_right);
					}
				});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}

	private void getMatchList(String strUrl) {
		// showProgress();
		JsonObjectRequest jObjRequest = new JsonObjectRequest(strUrl, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if (response.getString("success").equalsIgnoreCase(
									"true")) {
								JSONArray jArray = response
										.getJSONArray("values");
								for (int i = 0; i < jArray.length(); i++) {
									JSONObject jObj = jArray.getJSONObject(i);
									Matches mMatches = new Matches();
									mMatches.setUserId(jObj.getString("userid"));
									mMatches.setImgUrl(jObj
											.getString("imageurl"));
									mMatches.setUserEmail(jObj
											.getString("email"));
									mMatches.setUserName(jObj
											.getString("username"));
									mMatches.setUserLocation(jObj
											.getString("userLocation"));
									matchList.add(mMatches);
								}
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (matchList.size() > 0) {
							adapter.notifyDataSetChanged();
						} else
							Toast.makeText(MessageActivity.this,
									"No Match Found", Toast.LENGTH_LONG).show();

						stopProgress();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						stopProgress();

					}
				});
		AppController.getInstance().addToRequestQueue(jObjRequest);
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
				    QBChatService.init(MessageActivity.this);
				    
				}
				chatService = QBChatService.getInstance();
				if(!chatService.isLoggedIn()){
					chatService.login(mQBUser);
				}
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return e;
			}
		}

		@Override
		protected void onPostExecute(Exception result) {
			stopProgress();
			if(result == null){
	    	getMatchList(WebServiceURL.GET_MATCH_LIST
					+ mLogUser.getUserId());
			} else {
				showToast(result.getLocalizedMessage());
			}
			
		}

	}

	
	/*
	private class createSession extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showProgress();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				session = QBAuth.createSession();
			} catch (QBResponseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (session != null) {
				Log.v("TAG", "session created, token = " + session.getToken());
				try {
					BaseService.getBaseService().setToken(session.getToken());

				} catch (BaseServiceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// stopProgress();
			QBAuth.createSession(user, new QBEntityCallbackImpl<QBSession>() {

				@Override
				public void onSuccess(QBSession session, Bundle args) {
					// Toast.makeText(getApplicationContext(),
					// "Login To Chat Success", Toast.LENGTH_SHORT).show();
					user.setId(session.getUserId());
					((AppController) getApplication()).setCurrentUser(user);
					QuickBloxUtils.loginToChat(AppController.getInstance()
							.getCurrentUser(), MessageActivity.this);
					getMatchList(WebServiceURL.GET_MATCH_LIST
							+ mLogUser.getUserId());
				}

				@Override
				public void onError(List<String> errors) {
					stopProgress();
				}
			});
		}

	}*/

	private class createChatDialog extends AsyncTask<Void, Void, QBDialog> {
		private String strEmail;
		private String userName;
		private String userImage;

		public createChatDialog(String strEmail, String userName,
				String userImage) {
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
				
				
				QBChatService chatService;
				if (!QBChatService.isInitialized()) {
				    QBChatService.init(MessageActivity.this);
				    
				}
				chatService = QBChatService.getInstance();
				QBPrivateChatManager cm = chatService.getPrivateChatManager();
				dialogToCreate = cm.createDialog(selectedUser.getId());
			} catch (Exception e) {
				e.printStackTrace();
				showToast("" + e.getLocalizedMessage());
				
			}

			return dialogToCreate;
		}

	}

	public void startSingleChat(QBDialog dialog, String userName,
			String userImage) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(ChatListActivity.EXTRA_MODE,
				ChatListActivity.Mode.PRIVATE);
		bundle.putSerializable(ChatListActivity.EXTRA_DIALOG, dialog);
		bundle.putString("userName", userName);
		bundle.putString("userImage", userImage);
		ChatListActivity.start(MessageActivity.this, bundle);
	}

}
