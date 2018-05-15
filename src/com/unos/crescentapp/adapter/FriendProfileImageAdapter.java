package com.unos.crescentapp.adapter;

import com.android.volley.toolbox.ImageLoader;
import com.unos.crescentapp.AppController;
import com.unos.crescentapp.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class FriendProfileImageAdapter extends PagerAdapter {
	private Context context;
	private String imageUrl[];
	//private int images[]={R.drawable.sample_pic3,R.drawable.sample_pic3};
	public FriendProfileImageAdapter(Context context,String imageUrl[]){
		this.context = context;
		this.imageUrl = imageUrl;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imageUrl.length;
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		LinearLayout layout = new LinearLayout(context);
	      layout.setOrientation(LinearLayout.HORIZONTAL);
	      LayoutParams layoutParams = new LayoutParams(
	        LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	      
	      layout.setLayoutParams(layoutParams);
		ImageView imageView = new ImageView(context);
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		imageParams.setMargins(30, 30, 30,  20);
		imageView.setLayoutParams(imageParams);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		loadProfileImage(imageUrl[position], imageView);
		layout.addView(imageView);
		container.addView(layout);
		return layout;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}
	
	@Override
	  public void destroyItem(ViewGroup container, int position, Object object) {
	   container.removeView((LinearLayout)object);
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
