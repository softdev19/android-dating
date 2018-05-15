package com.unos.crescentapp.utilts;

import java.util.List;

import org.jivesoftware.smack.SmackException;

import android.app.AlertDialog;
import android.content.Context;

import com.quickblox.chat.QBChatService;
import com.quickblox.core.QBEntityCallbackImpl;

import com.quickblox.users.model.QBUser;

public class QuickBloxUtils {
	private static QBChatService chatService;
	public  static void loginToChat(final QBUser user,final Context context){
		if (!QBChatService.isInitialized()) {
            QBChatService.init(context);
        }
		chatService =  QBChatService.getInstance();
		if(chatService!=null){
		chatService.login(user, new QBEntityCallbackImpl() {
            @Override
            public void onSuccess() {

                // Start sending presences
                //
                try {
                	QBChatService.getInstance().startAutoSendPresence(30);
                } catch (SmackException.NotLoggedInException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(List errors) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("chat login errors: " + errors).create().show();
            }
        });
		}
    }
	

	

}
