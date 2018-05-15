package com.unos.crescentapp.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.unos.crescentapp.R;
import com.unos.crescentapp.models.NotificationData;
import com.unos.crescentapp.utilts.Utils;
import com.unos.crescentapp.view.CircleImageView;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<NotificationData> notificationList;
	
	public NotificationAdapter(Context context,ArrayList<NotificationData> notificationList){
		this.context = context;
		this.notificationList = notificationList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return notificationList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return notificationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		Resources res = context.getResources();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.inflate_notificatonlist, null);
			viewHolder.txtMessage = (TextView) convertView.findViewById(R.id.inflate_notification_txtvw_message);
			viewHolder.txtTime = (TextView) convertView.findViewById(R.id.inflate_notification_txtvw_time);
			viewHolder.userImage = (CircleImageView) convertView.findViewById(R.id.inflate_notification_imgvw);
			convertView.setTag(viewHolder);
			
		}else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.txtMessage.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Semibold.ttf"));
		viewHolder.txtTime.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Light.ttf"));
		if(notificationList.get(position).getNotificationType().equals("1"))
			viewHolder.txtMessage.setText(notificationList.get(position).getUserName()+" Likes Your Photo");
		else if(notificationList.get(position).getNotificationType().equals("3"))
			viewHolder.txtMessage.setText(String.format(res.getString(R.string.you_and_other_like_each_other), "" + notificationList.get(position).getUserName()));
		else if(notificationList.get(position).getNotificationType().equals("4"))
			viewHolder.txtMessage.setText(notificationList.get(position).getUserName()+" Likes Your Profile");
		
		SimpleDateFormat simpleFormat = new SimpleDateFormat("MMM d, yyyy hh:mm a");
		viewHolder.txtTime.setText(simpleFormat.format(notificationList.get(position).getNotifyTime()));
		//viewHolder.txtTime.setText(Utils.giveDateString("MM/dd/yyyy HH:mm:ss", "MMM/dd/yyyy hh:mm aa", notificationList.get(position).getNotifyTime()));
		if(!notificationList.get(position).getImageUrl().equals(""))
			Picasso.with(context).load(notificationList.get(position).getImageUrl()).into(viewHolder.userImage);
		return convertView;
	}

	private static class ViewHolder {
		private TextView txtMessage,txtTime;
		private CircleImageView userImage;
	}
}
