package com.unos.crescentapp.adapter;



import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.unos.crescentapp.AppController;
import com.unos.crescentapp.FriendsProfileActivity;
import com.unos.crescentapp.R;
import com.unos.crescentapp.constant.WebServiceURL;
import com.unos.crescentapp.models.FindingPeople;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.notification.SendNotificationTask;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.JSONPostParser;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CardStackAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class DiscoveryListAdapter extends CardStackAdapter{
	

	private LinearLayout linearLytInterestSub;
	private String userId;
	private int flg;
	private LogUser mLogUser;
	public DiscoveryListAdapter(Context context) {
		super(context);	
		Gson gson = new Gson();
		 String userJson = MySharedPrefs.getString(context, "", MySharedPrefs.USER_DATA);
		 mLogUser = gson.fromJson(userJson, LogUser.class);
		
	}

	

	@Override
	public View getCardView(int position, final FindingPeople mFindingPeople, View convertView, ViewGroup parent) {
		 final ViewHolder holder= new ViewHolder();
		 userId = MySharedPrefs.getString(getContext(), "", MySharedPrefs.USER_ID);
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.adapter_discovery, parent, false);
			//holder = new ViewHolder();
			holder.imgProfile=(ImageView)convertView.findViewById(R.id.adapter_discovery_profile_image);
			holder.txtvwProfileNameAge=(TextView)convertView.findViewById(R.id.adapter_discovery_txtvw_profile_name);
			holder.txtvwLocation=(TextView)convertView.findViewById(R.id.adapter_discovery_txtvw_location);
			holder.txtvwAbout=(TextView)convertView.findViewById(R.id.adapter_discovery_txtvw_about_user);
			holder.linearInterests=(LinearLayout)convertView.findViewById(R.id.adapter_discovery_lnr_lyt_interest);
			holder.imgviewHeart =(ImageView) convertView.findViewById(R.id.adapter_discovery_imgbtn_heart);
			holder.imagelikestatus="0";
			assert convertView != null;
		}
		
			holder.txtvwProfileNameAge.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Semibold.ttf"));
			holder.txtvwLocation.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Regular.ttf"));
			holder.txtvwAbout.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Light.ttf"));
			holder.txtvwProfileNameAge.setText(mFindingPeople.getName()+","
			                           +mFindingPeople.getAge());
			holder.txtvwLocation.setText(mFindingPeople.getLocation());
			if(!mFindingPeople.getAbout().toString().equals(""))
				holder.txtvwAbout.setText("\""+mFindingPeople.getAbout()+"\"");
		
			holder.linearInterests.removeAllViews();
			if (mFindingPeople.getUserimage() != null
					&& !mFindingPeople.getUserimage().equals("")) {
				loadProfileImage(mFindingPeople.getUserimage(),holder.imgProfile);
			}
			
			String strInterest = mFindingPeople.getInterests();
			//Log.v("Interest", strInterest);
			if (strInterest != null
					&& !strInterest.equals("") ) {
				
				String []interest = new String[]{};
				interest = strInterest.split(",");
				
				for(int flag=0;flag<interest.length;flag++){
					
					linearLytInterestSub = new LinearLayout(getContext());
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(20, 20, 20,20);
					linearLytInterestSub.setOrientation(LinearLayout.VERTICAL);
					linearLytInterestSub.setLayoutParams(params);
					linearLytInterestSub.setGravity(Gravity.CENTER_HORIZONTAL);
					linearLytInterestSub.addView(interestImageView(flag, interest[flag]));
					linearLytInterestSub.addView(interestText(flag,interest[flag]));	
					holder.linearInterests.addView(linearLytInterestSub);
					if(flag==4)
						break;
				}
				
			}
			 if(mFindingPeople.getImagelikestatus().equals("0")){
				 holder.imgviewHeart.setImageResource(R.drawable.ic_like_normal);
				 holder.imagelikestatus="0";
			 }
			 else{
				 holder.imgviewHeart.setImageResource(R.drawable.ic_like_selected);
				 holder.imagelikestatus="1";
			 }
			   
			holder.imgviewHeart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ImageView view = (ImageView) v;
						if(holder.imagelikestatus.equals("0"))
							holder.imagelikestatus = "1";
						else
							holder.imagelikestatus = "0";
						
					if(holder.imagelikestatus.equals("1"))
						view.setImageResource(R.drawable.ic_like_selected);
					else
						view.setImageResource(R.drawable.ic_like_normal);
						
					//Toast.makeText(getContext(), ""+mFindingPeople.getUser_id()+" "+holder.imagelikestatus, Toast.LENGTH_SHORT).show();
					new Thread(new Runnable() {
				        public void run() {
				        	//Log.v("friend_id", mFindingPeople.getUser_id());
				        	
				        	HashMap<String, String> bodyParams = new HashMap<>();
							bodyParams.put("userid", userId);
							bodyParams.put("friendsid", mFindingPeople.getUser_id());
							bodyParams.put("islike", holder.imagelikestatus);
							makeJSONImageLikeRequest(WebServiceURL.FRIEND_IMAGE_LIKE_DISLIKE,bodyParams,mFindingPeople);
				        }
				    }).start();
					
				}
			});
			
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Activity activity = (Activity) getContext();
					Intent intent = new Intent(activity,FriendsProfileActivity.class);
					intent.putExtra("friendId", mFindingPeople.getUser_id());
					activity.startActivity(intent);
					activity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
					
				}
			});
			
		return convertView;
	}

	private static class ViewHolder {
		private LinearLayout linearInterests;
		private ImageView imgProfile;
		private TextView txtvwProfileNameAge,txtvwLocation,txtvwAbout;
		private ImageView imgviewHeart;
		private String imagelikestatus;
	}
	
	
	 private ImageView interestImageView(int flag,String interest){
		 LayoutParams imgviewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 ImageView imgView = new ImageView(getContext());
		 imgView.setLayoutParams(imgviewParams);
		 if(interest.equalsIgnoreCase("tech")){
				
			 imgView.setImageResource(R.drawable.icon_tech);
			 }
			 else if(interest.equalsIgnoreCase("travels")|| interest.equalsIgnoreCase("travel"))
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
				
			 else if(interest.equalsIgnoreCase("sports")|| interest.equalsIgnoreCase("sport"))
				 imgView.setImageResource(R.drawable.icon_sports);
				 
			 else if(interest.equalsIgnoreCase("events")|| interest.equalsIgnoreCase("event"))
				 imgView.setImageResource(R.drawable.icon_evnts);
				 
		 return imgView;
	 }
	 private TextView interestText(int flag,String interest){
			LayoutParams TxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			 TextView interestTxt = new TextView(getContext());
			 interestTxt.setId(flag);
			 interestTxt.setText(interest);
			 interestTxt.setLayoutParams(TxtParams);
			 interestTxt.setPadding(5, 5, 5, 5);
			 interestTxt.setGravity(Gravity.CENTER_HORIZONTAL);
			 interestTxt.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Light.ttf"));
			 interestTxt.setTextSize(16f);
	        return interestTxt;
		}
public void loadProfileImage(String url,ImageView imageview){
		
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image
		imageLoader.get(url, ImageLoader.getImageListener(
		                imageview, R.color.trans, R.color.trans));
	}
	
private void makeJSONImageLikeRequest(String strUrl,HashMap<String, String> bodyParams,FindingPeople mFindingPeople){
	if(!Utils.isNetworkConnected(getContext())){
		((Activity)getContext()).runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(
						getContext(),
						getContext().getResources().getString(
								R.string.network_not_avail), Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		return;
	}
	if(!Utils.isReachable(getContext())){
	((Activity)getContext()).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(
							getContext(),
							getContext().getResources().getString(
									R.string.unable_connect), Toast.LENGTH_SHORT)
							.show();
				}
			});
	return;
	}
	
	new LikeDislikeTask(strUrl, bodyParams,mFindingPeople).execute();
	
}

private class LikeDislikeTask extends AsyncTask<Void, Void, String>{
	private String strUrl,strMessage,strName,strRes;
	private HashMap<String, String> bodyParams;
	private ProgressDialog progressDialog;
	private FindingPeople mFindingPeople;
	public LikeDislikeTask(String strUrl,HashMap<String, String> bodyParams,FindingPeople mFindingPeople){
		this.strUrl = strUrl;
		this.bodyParams = bodyParams;
		this.mFindingPeople = mFindingPeople;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		/*progressDialog =  new ProgressDialog(getContext());
		progressDialog.setMessage("Loading...");
		progressDialog.setCancelable(false);
		progressDialog.show();*/
	}

	@Override
	protected String doInBackground(Void... params) {

		JSONObject jObj = new JSONPostParser().getJSONFromUrl(strUrl, bodyParams);
		if(jObj!=null){
			try {
				strRes = jObj.getString("success");
				strMessage = jObj.getString("message");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return strMessage;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		/*if(progressDialog.isShowing()){
			progressDialog.cancel();
		}*/
		if(strRes.equalsIgnoreCase("true")){
			if(result!=null && !result.equalsIgnoreCase("")){
				
				//Toast.makeText(getContext(), "You "+result+" "+mFindingPeople.getName()+"'s Photo", Toast.LENGTH_SHORT).show();
				if(result.equalsIgnoreCase("liked")){
					if(!mFindingPeople.getDeviceToken().equals("") && mFindingPeople.getDeviceType().equals("android")){
					HashMap<String, String> params = new HashMap<>();
					params.put("devicetoken", mFindingPeople.getDeviceToken());
					params.put("message", mLogUser.getName()+" Likes Your Photo");
					new SendNotificationTask(WebServiceURL.SEND_NOTIFICATION, params).execute();
					}
				}
				
			}
		}
			
	}
	
	}

}


