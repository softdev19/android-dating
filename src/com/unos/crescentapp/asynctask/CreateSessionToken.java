package com.unos.crescentapp.asynctask;

import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.exception.BaseServiceException;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.server.BaseService;
import com.quickblox.users.model.QBUser;

import android.os.AsyncTask;
import android.util.Log;

public class CreateSessionToken extends AsyncTask<Void,Void, QBSession> {
	private QBSession session;
	private QBUser user;
	public CreateSessionToken(){
		
	}
	CreateSessionToken(QBUser user){
		this.user = user;
	}
	@Override
	protected QBSession doInBackground(Void... params) {
		try {
			session  = QBAuth.createSession();
		} catch (QBResponseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   
	    if(session != null){
	        Log.v("TAG", "session created, token = " + session.getToken());
	        try {
				BaseService.getBaseService().setToken(session.getToken());
				
			} catch (BaseServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return session;
	}

}
