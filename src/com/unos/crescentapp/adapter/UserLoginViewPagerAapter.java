package com.unos.crescentapp.adapter;

import com.unos.crescentapp.R;
import com.unos.crescentapp.utilts.Utils;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserLoginViewPagerAapter extends PagerAdapter{
	int [] dasboard ={ R.drawable.discover_potentia_bg, R.drawable.onboarding_bg2,
			R.drawable.onboarding_bg3,};
	//String [] namePage={"Discover potential\n lode interests","Anonymously show \n interest in them","If you both like each other \n We'll connect you"};
	int [] namePage={R.string.discover_potential,R.string.anonymously_show,R.string.if_you_both_like_each_other};
	
	Context mContext;
    LayoutInflater mLayoutInflater;
 
    public UserLoginViewPagerAapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    @Override
    public int getCount() {
    	
        return dasboard.length;
    }
 
  

	@Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.inflate_viewpager_adapter, container, false);
 
        ImageView imageView = (ImageView) itemView.findViewById(R.id.inflater_viewpager_background_image);
        TextView tvTittle=(TextView)itemView.findViewById(R.id.inflate_viewpager_adapter_tv_tittle);
        imageView.setImageResource(dasboard[position]);
   	 	tvTittle.setText(namePage[position]);
   	 	tvTittle.setTypeface(Utils.getTypeface(mContext, "fonts/SourceSansPro-Regular.ttf"));
       
       
 
        container.addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
	
	

}
