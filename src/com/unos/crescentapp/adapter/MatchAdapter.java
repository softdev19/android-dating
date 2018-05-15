package com.unos.crescentapp.adapter;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.unos.crescentapp.AppController;
import com.unos.crescentapp.R;
import com.unos.crescentapp.models.Matches;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
    private ArrayList<Matches> matchList;
	public MatchAdapter(Context context,ArrayList<Matches> matchList) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.matchList = matchList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return matchList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return matchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView==null) {
			holder = new ViewHolder();
			convertView=inflater.inflate(R.layout.inflate_message, null);
			holder.imgViewUser = (CircleImageView) convertView.findViewById(R.id.inflate_message_profile_image);
			holder.imgOnline = (ImageView) convertView.findViewById(R.id.inflate_message_profile_online);
			holder.txtName  = (TextView) convertView.findViewById(R.id.inflater_message_user_name);
			holder.txtMessage = (TextView) convertView.findViewById(R.id.inflater_message_txtvw_details);
			//holder.txtTime = (TextView) convertView.findViewById(R.id.inflater_message_txtvw_time);
			convertView.setTag(holder);
		}else
			holder = (ViewHolder) convertView.getTag();
		if(position==2){
			holder.imgOnline.setVisibility(View.VISIBLE);
		}
		holder.txtName.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Semibold.ttf"));
		holder.txtMessage.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Regular.ttf"));
		//holder.txtTime.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Light.ttf"));
		holder.txtName.setText(matchList.get(position).getUserName());
		holder.txtMessage.setText(matchList.get(position).getUserLocation());
		loadProfileImage(matchList.get(position).getImgUrl(), holder.imgViewUser);
		return convertView;
	}
	private static class ViewHolder{
		private CircleImageView imgViewUser;
		private ImageView imgOnline;
		private TextView txtName,txtMessage,txtTime;
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
