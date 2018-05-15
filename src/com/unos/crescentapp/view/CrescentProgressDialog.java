package com.unos.crescentapp.view;


import com.unos.crescentapp.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class CrescentProgressDialog extends ProgressDialog {
	private ImageView imageview;
	private AnimationDrawable mAnimationDrawable;
	public CrescentProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_progress_dialog);
		imageview = (ImageView) findViewById(R.id.custom_progress_imageView);
		imageview.setBackgroundResource(R.drawable.frame_animation);
		mAnimationDrawable = (AnimationDrawable) imageview.getBackground();
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	
		
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			mAnimationDrawable.start();
		} else {
			mAnimationDrawable.stop();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
	}
	
	
}
