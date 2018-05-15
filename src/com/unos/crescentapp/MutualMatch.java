package com.unos.crescentapp;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.unos.crescentapp.models.FindingPeople;
import com.unos.crescentapp.models.FriendProfile;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CircleImageView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MutualMatch extends AlertDialog {
	
	private CircleImageView userImage , friendImage;
	private TextView txtLike,txtMutualMatch,txtknow;
	private FindingPeople findPeople;
	private LogUser mLogUser;
	private Button btnSendMessage; 
	public MutualMatch(Context context, FindingPeople findPeople) {
		super(context);
		this.findPeople=findPeople;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mutual_match);
		Gson gson = new Gson();
		 String userJson = MySharedPrefs.getString(getContext(), "", MySharedPrefs.USER_DATA);
		 mLogUser = gson.fromJson(userJson, LogUser.class);
		userImage = (CircleImageView) findViewById(R.id.activity_mutual_match_imgvw_user);
		friendImage = (CircleImageView) findViewById(R.id.activity_mutual_match_imgvw_friend);
		txtMutualMatch = (TextView) findViewById(R.id.activity_mutual_match_txtvw_mutual);
		txtLike = (TextView) findViewById(R.id.activity_mutual_match_txtvw_like);
		txtknow = (TextView) findViewById(R.id.activity_mutual_match_txtvw_know);
		btnSendMessage = (Button) findViewById(R.id.activity_mutual_match_btn_send_message);
		txtMutualMatch.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Semibold.ttf"));
		txtLike.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Regular.ttf"));
		txtknow.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setTypeface(Utils.getTypeface(getContext(), "fonts/SourceSansPro-Regular.ttf"));
		btnSendMessage.setText("SEND "+findPeople.getName()+"A MESSAGE");
		if(mLogUser.getImage()!=null && mLogUser.getImage().length>0){
			loadProfileImage(mLogUser.getImage()[0],userImage);
		}
		loadProfileImage(findPeople.getUserimage(),friendImage);
		txtLike.setText("You and "+findPeople.getName()+" liked each other");
		txtknow.setText("Now you can know more about "+findPeople.getName());
		txtknow.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.cancel();
	}
	private void loadProfileImage(String url, ImageView imageview) {
		ImageLoader imageLoader = AppController.getInstance().getImageLoader();
		// Loading image with placeholder and error image

		imageLoader
				.get(url,
						ImageLoader.getImageListener(imageview,
								R.color.trans, R.color.trans));

	}
	
}
