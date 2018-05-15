package com.unos.crescentapp.asynctask;

import android.os.AsyncTask;

import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class GetUserById extends AsyncTask<Void, Void, QBUser>{
		private int userId;
		public GetUserById(int userId){
			this.userId = userId;
		}
	@Override
	protected QBUser doInBackground(Void... params) {
		QBUser user=null;
		try {
			user = QBUsers.getUser(userId);
		} catch (QBResponseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return user;
	}
		
	}