package com.unos.crescentapp.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class CoachMarkPagerAdapter extends PagerAdapter {
	
	private Context context;
	private int[]coachMarkImage;
	public CoachMarkPagerAdapter (Context context,int[]coachMarkImage){
		this.context = context;
		this.coachMarkImage = coachMarkImage;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coachMarkImage.length;
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
		//imageParams.setMargins(1, 1, 1, 1);
		imageView.setLayoutParams(imageParams);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageResource(coachMarkImage[position]);
		//imageView.setBackgroundResource(coachMarkImage[position]);Color.parseColor("#050505")
		imageView.setBackgroundColor(Color.TRANSPARENT);
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

}
