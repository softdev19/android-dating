package com.unos.crescentapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.unos.crescentapp.adapter.NotificationAdapter;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.models.FindingPeople;
import com.unos.crescentapp.models.NotificationData;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.QuickBloxUtils;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CircleImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationsActivity extends BaseActivity {
	
	private ListView mNotificationListView;
	private ArrayList<NotificationData> notificationList;
	private NotificationAdapter adapter;
	private QBUser mQbUser;
	private QBSession session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.activity_noficationlist);
		notificationList = new ArrayList<>();
		 adapter = new NotificationAdapter(NotificationsActivity.this, notificationList);
		((TextView) findViewById(R.id.activity_notification_title))
				.setTypeface(Utils.getTypeface(NotificationsActivity.this,
						"fonts/SourceSansPro-Regular.ttf"));

		mQbUser = new QBUser(mLogUser.getLogin(), "crescent123", mLogUser.getEmail());
		
		if (Utils.isNetworkConnected(NotificationsActivity.this)) {
			if(Utils.isReachable(NotificationsActivity.this)){
				makeJSONObjectrequest();
				new LogToChatTask().execute();
			}else
				showToast(getResources().getString(R.string.unable_connect));
		}else
			showToast(getResources().getString(R.string.network_not_avail));
		
		findViewById(R.id.activity_notification_imgbtn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});
		mNotificationListView=(ListView) findViewById(R.id.activity_notification_list_view);
		mNotificationListView.setAdapter(adapter);
		
		
		mNotificationListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(notificationList.get(position).getNotificationType().equals("1")){
					Intent intent = new Intent(NotificationsActivity.this,FriendsProfileActivity.class);
					intent.putExtra("friendId", notificationList.get(position).getUserId());
					startActivity(intent);
					overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
				}else if(notificationList.get(position).getNotificationType().equals("3")){
					callAlert(NotificationsActivity.this, notificationList.get(position));
				}else{
					Intent intent = new Intent(NotificationsActivity.this,FriendsProfileActivity.class);
					intent.putExtra("friendId", notificationList.get(position).getUserId());
					startActivity(intent);
					overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
				}
					
					
				
			}
		});

	}
	@SuppressLint("DefaultLocale")
	private  void callAlert(final Context context, final NotificationData mNData){
	     final Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     dialog.setContentView(R.layout.activity_mutual_match);
	     WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	     lp.copyFrom(dialog.getWindow().getAttributes());
	     lp.width = WindowManager.LayoutParams.MATCH_PARENT;
	     lp.height = WindowManager.LayoutParams.MATCH_PARENT;
	     lp.x = -50;
	     lp.y = -100;
	     dialog.getWindow().setAttributes(lp);
	     
	     TextView txtLike,txtMutualMatch,txtKnow;
	     Button btnMay;
	     CircleImageView userImage , friendImage;
	      Button btnSendMessage; 
	     //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
	    
		userImage = (CircleImageView) dialog.findViewById(R.id.activity_mutual_match_imgvw_user);
		friendImage = (CircleImageView) dialog.findViewById(R.id.activity_mutual_match_imgvw_friend);
		txtMutualMatch = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_mutual);
		txtLike = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_like);
		txtKnow = (TextView) dialog.findViewById(R.id.activity_mutual_match_txtvw_know);
		btnMay = (Button) dialog.findViewById(R.id.activity_mutual_match_btn_may);
		
		btnSendMessage = (Button) dialog.findViewById(R.id.activity_mutual_match_btn_send_message);
		txtKnow.setTypeface(Utils.getTypeface(NotificationsActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtMutualMatch.setTypeface(Utils.getTypeface(NotificationsActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtLike.setTypeface(Utils.getTypeface(NotificationsActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnMay.setTypeface(Utils.getTypeface(NotificationsActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setTypeface(Utils.getTypeface(NotificationsActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setText("SEND "+mNData.getUserName().toUpperCase()+" A MESSAGE");
		if(mLogUser.getImage()!=null && mLogUser.getImage().length>0){
			Picasso.with(context).load( mLogUser.getImage()[0]).into(userImage);
		}
		if(!mNData.getImageUrl().equals(""))
		Picasso.with(context).load( mNData.getImageUrl()).into(friendImage);
		
		txtLike.setText(String.format(getString(R.string.you_and_other_like_each_other), "" + mNData.getUserName()));
		txtKnow.setText("Now you can know more about "+mNData.getUserName());  
		btnSendMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				if (Utils.isNetworkConnected(NotificationsActivity.this)) {
					if(Utils.isReachable(NotificationsActivity.this)){
						//if(AppController.getInstance().getCurrentUser()!=null){
							new createChatDialog(mNData.getUserEmail(), mNData.getUserName(), mNData.getImageUrl()).execute();
						//}else
						//	Utils.showAlertDialog("Unable to Create Chat Session", "Message", NotificationsActivity.this);
				}else
					showToast(getResources().getString(R.string.unable_connect));
				}else
					showToast(getResources().getString(R.string.network_not_avail));
				/*Intent intent = new Intent(NotificationsActivity.this,ChatListActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);*/
			}
		});
		btnMay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				
			}
		});
	     dialog.show();
	 }
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}

	private void makeJSONObjectrequest(){
		showProgress();
		JsonObjectRequest jObjectRequest = new JsonObjectRequest(
				WebServiceURL.NOTIFICATION_LIST + "?userid="
						+ mLogUser.getUserId(), null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							if(response!=null && response.getString("success").equals("true")){
								JSONArray jArray = response.getJSONArray("values");
								for(int i=0;i<jArray.length();i++){
									JSONObject jObj = jArray.getJSONObject(i);
									NotificationData mNotificationData = new NotificationData();
									mNotificationData.setNotificationType(jObj.getString("notificationtype"));
									mNotificationData.setUserId(jObj.getString("userid"));
									mNotificationData.setUserName(jObj.getString("username"));
									mNotificationData.setNotifyTime(new Date(jObj.getLong("notifytime") * 1000));
									mNotificationData.setUserEmail(jObj.getString("email"));
									mNotificationData.setImageUrl(jObj.getString("imageurl"));
									notificationList.add(mNotificationData);
								}
								
								
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if(notificationList.size()>0)
							adapter.notifyDataSetChanged();
						
						stopProgress();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						
						stopProgress();
					}
				});
		AppController.getInstance().addToRequestQueue(jObjectRequest);
		
		}
	private class createChatDialog extends AsyncTask<Void,Void,QBDialog>{
		private String strEmail;
		private String userName;
		private String userImage;
		public createChatDialog(String strEmail,String userName,String userImage){
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
			if(result!=null){
				startSingleChat(result, userName,userImage);
			}
		}

		@Override
		protected QBDialog doInBackground(Void... params) {
			QBUser selectedUser=null;
			QBDialog dialogToCreate = null;
			try {
				selectedUser= QBUsers.getUserByEmail(strEmail);
				dialogToCreate=QBChatService.getInstance().getPrivateChatManager().createDialog(selectedUser.getId());
				//occupantIds.add(selectedUser.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return dialogToCreate;
		}
		
	}
	public void startSingleChat(QBDialog dialog,String userName,String userImage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ChatListActivity.EXTRA_MODE, ChatListActivity.Mode.PRIVATE);
        bundle.putSerializable(ChatListActivity.EXTRA_DIALOG, dialog);
        bundle.putString("userName", userName);
        bundle.putString("userImage", userImage);
        ChatListActivity.start(NotificationsActivity.this, bundle);
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
				    QBChatService.init(NotificationsActivity.this);
				    
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
	    	//getMatchList(WebServiceURL.GET_MATCH_LIST
			//		+ mLogUser.getUserId());
			} else {
				showToast(result.getLocalizedMessage());
			}
			
		}

	}
	
	/*
private class createSession extends AsyncTask<Void,Void,Void>{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//showProgress();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				session  = QBAuth.createSession();
			} catch (QBResponseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		   
		    if(session != null){
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
			//stopProgress();
				QBAuth.createSession(mQbUser, new QBEntityCallbackImpl<QBSession>() {
					
					 @Override
			            public void onSuccess(QBSession session, Bundle args) {
			              //  Toast.makeText(getApplicationContext(), "Login To Chat Success", Toast.LENGTH_SHORT).show();
			                mQbUser.setId(session.getUserId());
			                ((AppController)getApplication()).setCurrentUser(mQbUser);
			                QuickBloxUtils.loginToChat(AppController.getInstance().getCurrentUser(), NotificationsActivity.this);
			                
			            }
	
			            @Override
			            public void onError(List<String> errors) {

			            }
				});
			}
		
		}
		*/
	
}
