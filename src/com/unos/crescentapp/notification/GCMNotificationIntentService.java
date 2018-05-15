package com.unos.crescentapp.notification;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.unos.crescentapp.DiscoveryActivity;
import com.unos.crescentapp.NotificationsActivity;
import com.unos.crescentapp.R;
import com.unos.crescentapp.SplashActivity;
import com.unos.crescentapp.constant.Constants;

public class GCMNotificationIntentService extends IntentService {
	
	
	private int numMessages = 0;

	// Sets an ID for the notification, so it can be updated
	public static int notifyID = 0;
	NotificationCompat.Builder builder;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification(""
						+ extras.getString(Constants.MSG_KEY));
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	@SuppressLint("InlinedApi")
	private void sendNotification(String msg) {
	  
		Intent resultIntent;
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	        
	        mNotifyBuilder = new NotificationCompat.Builder(this)
	        .setContentTitle("Crescent")
            .setSmallIcon(R.drawable.ic_small);
	        mNotifyBuilder .setContentText(""+msg);
	       
	       // if(msg.equalsIgnoreCase("photo")){
		        resultIntent = new Intent(this, SplashActivity.class);
		        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				stackBuilder.addParentStack(SplashActivity.class);
				stackBuilder.addNextIntent(resultIntent);
				
	       /* }else{
	        	resultIntent = new Intent(this, DiscoveryActivity.class);
	        	resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				stackBuilder.addParentStack(DiscoveryActivity.class);
				stackBuilder.addNextIntent(resultIntent);
	        }*/
	        	
			
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mNotifyBuilder.setContentIntent(resultPendingIntent);
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification 
	       // mNotifyBuilder.setContentText(""+msg);
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        notifyID++;
	        mNotifyBuilder.setNumber(notifyID);
	       
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	        
	
	}
	
	
	
	
}
