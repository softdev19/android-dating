package com.unos.crescentapp;
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.core.LogLevel;
import com.quickblox.core.QBSettings;
import com.quickblox.users.model.QBUser;
import com.unos.crescentapp.utilts.LruBitmapCache;
 
public class AppController extends Application {
 
    public static final String TAG = AppController.class
            .getSimpleName();
 
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    //private QBUser currentUser;
    private static AppController mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        QBSettings.getInstance().fastConfigInit("24072", "xOg7mbBdGg9-Tb8", "vxSawJpKhPfBSZK");
        QBSettings.getInstance().setLogLevel(LogLevel.NOTHING);
    }
 
    /*
    public  QBUser getCurrentUser() {
		return currentUser;
	}*/

    /*
	@SuppressWarnings("static-access")
	public void setCurrentUser(QBUser currentUser) {
		this.currentUser = currentUser;
	}*/

	public static synchronized AppController getInstance() {
        return mInstance;
    }
 
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return mRequestQueue;
    }
 
    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }
    
    
    public Integer getOpponentIDForPrivateDialog(QBDialog dialog, int currentUserId){
        Integer opponentID = -1;
        for(Integer userID : dialog.getOccupants()){
            if(!userID.equals(currentUserId)){
                opponentID = userID;
                break;
            }
        }
        return opponentID;
    }
 
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    
    
}