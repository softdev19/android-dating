package com.unos.crescentapp.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;

import com.unos.crescentapp.R;
import com.unos.crescentapp.models.FindingPeople;
import com.unos.crescentapp.utilts.Utils;


import java.util.ArrayList;
import java.util.Collection;

public abstract class CardStackAdapter extends BaseCardStackAdapter {
	private final Context mContext;

	
	private final Object mLock = new Object();
	private ArrayList<FindingPeople> mData;

	public CardStackAdapter(Context context) {
		mContext = context;
		this.mData = new ArrayList<FindingPeople>();
	}

	public CardStackAdapter(Context context, Collection<? extends FindingPeople> items) {
		mContext = context;
		mData = new ArrayList<FindingPeople>(items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FrameLayout wrapper = (FrameLayout) convertView;
		FrameLayout innerWrapper;
		View cardView;
		View convertedCardView;
		if (wrapper == null) {
			wrapper = new FrameLayout(mContext);
			wrapper.setBackgroundResource(R.drawable.bg_discovery);
			if (shouldFillCardBackground()) {
				innerWrapper = new FrameLayout(mContext);
				innerWrapper.setBackgroundColor(Color.WHITE);//mContext.getResources().getColor(Color.WHITE)
				wrapper.addView(innerWrapper);
			} else {
				innerWrapper = wrapper;
			}
			cardView = getCardView(position, getFindingPeople(position), null, parent);
			innerWrapper.addView(cardView);
		} else {
			if (shouldFillCardBackground()) {
				innerWrapper = (FrameLayout) wrapper.getChildAt(0);
			} else {
				innerWrapper = wrapper;
			}
			cardView = innerWrapper.getChildAt(0);
			convertedCardView = getCardView(position, getFindingPeople(position), cardView, parent);
			if (convertedCardView != cardView) {
				wrapper.removeView(cardView);
				wrapper.addView(convertedCardView);
			}
			
		}
		/* LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams((int)Utils.getwidthPixel(getContext()), (int)Utils.getheightPixel(getContext()));
		 wrapper.setLayoutParams(layoutparams);*/
		 wrapper.setBackgroundColor(Color.WHITE);
		 wrapper.setTag(getFindingPeople(position));
		return wrapper;
	}

	protected abstract View getCardView(int position, FindingPeople model, View convertView, ViewGroup parent);

	public boolean shouldFillCardBackground() {
		return true;
	}

	public void add(FindingPeople item) {
		synchronized (mLock) {
			mData.add(item);
		}
		notifyDataSetChanged();
	}
	public void removeAll(){
		synchronized (mLock) {
			mData.removeAll(mData);
		}
		notifyDataSetChanged();
	}
	
	public void addAll(Collection <? extends FindingPeople> collection){
		synchronized (mLock) {
			mData.addAll(collection);
		}
		notifyDataSetChanged();
	}

	public FindingPeople pop() {
		FindingPeople model;
		synchronized (mLock) {
			model = mData.remove(mData.size() - 1);
		}
		notifyDataSetChanged();
		return model;
	}

	@Override
	public Object getItem(int position) {
		return getFindingPeople(position);
	}

	public FindingPeople getFindingPeople(int position) {
		synchronized (mLock) {
			return mData.get(mData.size() - 1 - position);
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).hashCode();
	}

	public Context getContext() {
		return mContext;
	}
}
