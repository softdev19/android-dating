package com.unos.crescentapp.adapter;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import com.google.gson.Gson;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.unos.crescentapp.AppController;
import com.unos.crescentapp.MessageActivity;
import com.unos.crescentapp.R;
import com.unos.crescentapp.models.LoginResult.LogUser;
import com.unos.crescentapp.sharedprefs.MySharedPrefs;
import com.unos.crescentapp.utilts.TimeUtils;
import com.unos.crescentapp.utilts.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class ChatListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	private final List<QBChatMessage> chatMessages;
	private String userName;
	private LogUser mLogUser;
	private String userImage;

	public ChatListAdapter(Context context, List<QBChatMessage> chatMessages, String userName, String userImage) {
		this.context = context;
		this.chatMessages = chatMessages;
		this.userName = userName;
		this.userImage = userImage;
		Gson gson = new Gson();
		String userJson = MySharedPrefs.getString(this.context, "", MySharedPrefs.USER_DATA);
		mLogUser = gson.fromJson(userJson, LogUser.class);
	}

	@Override
	public int getCount() {
		if (chatMessages != null) {
			return chatMessages.size();
		} else {
			return 0;
		}
	}

	@Override
	public QBChatMessage getItem(int position) {
		if (chatMessages != null) {
			return chatMessages.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * @Override public int getViewTypeCount() { // TODO Auto-generated method
	 * stub return 2; }
	 * 
	 * @Override public int getItemViewType(int position) { // TODO
	 * Auto-generated method stub return position % 2; }
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		inflater = LayoutInflater.from(context);
		QBChatMessage chatMessage = getItem(position);
		
		
		
		boolean isOutgoing = chatMessage.getSenderId() == null || chatMessage.getSenderId().equals(mLogUser.getQuickblox_id());
		if (!isOutgoing) {
			if (!chatMessage.getBody().equalsIgnoreCase("null")) {
				// if(chatMessage.getAttachments().size() == 0){
				// if(chatMessage.getBody()!=null){
				// Log.v("Attachment" ,""+chatMessage.getBody());
				convertView = inflater.inflate(R.layout.inflater_chat_receiver, parent, false);
				holder = createRecieverViewHolder(convertView);
				holder.txtMessage.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Regular.ttf"));
				holder.txtMessage.setText(chatMessage.getBody());
				holder.txtTime.setText(userName + "  " + getTimeText(chatMessage));
				Picasso.with(context).load(userImage).into(holder.imgViewUser);
			} else {
				// Log.v("Attachment" ,""+chatMessage.getBody());
				convertView = inflater.inflate(R.layout.inflater_chat_receiver_attach, parent, false);
				holder = createReceiveAttachmentViewHolder(convertView);
				holder.progressBar.setVisibility(View.VISIBLE);
				QBAttachment attachment = (QBAttachment) chatMessage.getAttachments().toArray()[0];
				Picasso.with(context).load(attachment.getUrl())
						.into(holder.imgViewAttach, new ImageLoadedCallback(holder));

				// uploadChatImage(chatMessage, holder);
				holder.txtTime.setText(userName + "  " + getTimeText(chatMessage));
				Picasso.with(context).load(userImage).into(holder.imgViewUser);
			}

		} else {

			if (!chatMessage.getBody().equalsIgnoreCase("null")) {
				// Log.v("Attachment" ,""+chatMessage.getBody());
				convertView = inflater.inflate(R.layout.inflater_chat_sender, parent, false);
				holder = createSenderViewHolder(convertView);
				holder.txtMessage.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Regular.ttf"));
				holder.txtMessage.setText(chatMessage.getBody());
				holder.txtTime.setText("You  " + getTimeText(chatMessage));
				Picasso.with(context).load(mLogUser.getImage()[0]).into(holder.imgViewUser);
			} else {
				// Log.v("Attachment" ,""+chatMessage.getBody());
				convertView = inflater.inflate(R.layout.inflater_chat_sender_attach, parent, false);
				holder = createSenderAttachmentViewHolder(convertView);
				holder.progressBar.setVisibility(View.VISIBLE);
				QBAttachment attachment = (QBAttachment) chatMessage.getAttachments().toArray()[0];
				Picasso.with(context).load(attachment.getUrl())
						.into(holder.imgViewAttach, new ImageLoadedCallback(holder));

				// uploadChatImage(chatMessage, holder);
				holder.txtTime.setText("You  " + getTimeText(chatMessage));
				Picasso.with(context).load(mLogUser.getImage()[0]).into(holder.imgViewUser);
			}

		}
		holder.txtTime.setTypeface(Utils.getTypeface(context, "fonts/SourceSansPro-Light.ttf"));

		return convertView;
	}

	

	public void add(QBChatMessage message) {
		chatMessages.add(message);
	}

	public void add(List<QBChatMessage> messages) {
		chatMessages.addAll(messages);
	}

	private ViewHolder createRecieverViewHolder(View v) {
		ViewHolder holder = new ViewHolder();
		holder.imgViewUser = (ImageView) v.findViewById(R.id.inflater_chat_receiver_imgvw);
		holder.txtMessage = (TextView) v.findViewById(R.id.inflater_receiver_chat_txtvw_msg);
		holder.txtTime = (TextView) v.findViewById(R.id.inflater_chat_receiver_txtvw_time);
		return holder;
	}
	


	private ViewHolder createSenderViewHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.imgViewUser = (ImageView) convertView.findViewById(R.id.inflater_chat_sender_imgvw);
		holder.txtMessage = (TextView) convertView.findViewById(R.id.inflater_chat_sender_txtvw_msg);
		holder.txtTime = (TextView) convertView.findViewById(R.id.inflater_chat_sender_txtvw_time);
		return holder;
	}

	private ViewHolder createSenderAttachmentViewHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.imgViewUser = (ImageView) convertView.findViewById(R.id.inflater_chat_sender_attach_imgvw_user);
		holder.imgViewAttach = (ImageView) convertView.findViewById(R.id.inflater_chat_sender_attach_imgvw_att);
		holder.txtTime = (TextView) convertView.findViewById(R.id.inflater_chat_sender_attach_txtvw_time);
		holder.progressBar = (ProgressBar) convertView.findViewById(R.id.inflater_chat_sender_attach_progressbar);
		return holder;
	}

	private ViewHolder createReceiveAttachmentViewHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.imgViewUser = (ImageView) convertView.findViewById(R.id.inflater_chat_receiver_attach_imgvw_user);
		holder.imgViewAttach = (ImageView) convertView.findViewById(R.id.inflater_chat_receiver_attach_imgvw_att);
		holder.txtTime = (TextView) convertView.findViewById(R.id.inflater_chat_receiver_attach_txtvw_time);
		holder.progressBar = (ProgressBar) convertView.findViewById(R.id.inflater_chat_receiver_attach_progressbar);
		return holder;
	}

	private String getTimeText(QBChatMessage message) {
		return Utils.getDate(message.getDateSent() * 1000, "MMM d, yyyy hh:mm a");
		// return TimeUtils.millisToLongDHMS(message.getDateSent()*1000);
	}

	class ViewHolder {
		private ImageView imgViewUser, imgViewAttach;
		private TextView txtMessage;
		private TextView txtTime;
		private ProgressBar progressBar;
	}
	class ImageLoadedCallback implements Callback {
		ViewHolder mHolder;

		public ImageLoadedCallback(ViewHolder holder) {
			super();
			mHolder = holder;
		}

		@Override
		public void onSuccess() {
			mHolder.progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onError() {
			// TODO Auto-generated method stub

		}
	};

	private void uploadChatImage(final QBChatMessage chatMessage, final ViewHolder holder) {

		if (!Utils.isNetworkConnected(context)) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, context.getString(R.string.network_not_avail), Toast.LENGTH_LONG).show();

				}
			});

			return;
		}
		if (!Utils.isReachable(context)) {
			((Activity) context).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, context.getString(R.string.unable_connect), Toast.LENGTH_LONG).show();
				}
			});

			return;
		}

		if (chatMessage != null && chatMessage.getAttachments() != null) {
			for (QBAttachment attachment : chatMessage.getAttachments()) {
				Integer fileId = Integer.parseInt(attachment.getId());
				QBContent.downloadFileTask(fileId, new QBEntityCallbackImpl<InputStream>() {
					@Override
					public void onSuccess(InputStream inputStream, Bundle params) {
						if (inputStream != null) {
							BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
							Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
							holder.imgViewAttach.setImageBitmap(bmp);
							holder.progressBar.setVisibility(View.GONE);
						}
					}

					@Override
					public void onError(List<String> errors) {
						// errors
					}
				});

			}

		}

	}

}
