package com.unos.crescentapp.utilts;

import android.util.Log;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBPrivateChat;
import com.quickblox.chat.QBPrivateChatManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBMessageListenerImpl;
import com.quickblox.chat.listeners.QBPrivateChatManagerListener;
import com.quickblox.chat.model.QBChatMessage;
import com.unos.crescentapp.ChatListActivity;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

public class PrivateChatManagerImpl extends QBMessageListenerImpl<QBPrivateChat> implements ChatManager, QBPrivateChatManagerListener {

    private static final String TAG = "PrivateChatManagerImpl";

    private ChatListActivity chatActivity;

    private QBPrivateChatManager privateChatManager;
    private QBPrivateChat privateChat;

    public PrivateChatManagerImpl(ChatListActivity chatActivity, Integer opponentID) {
        this.chatActivity = chatActivity;

        privateChatManager = QBChatService.getInstance().getPrivateChatManager();

        privateChatManager.addPrivateChatManagerListener(this);

        // init private chat
        //
        privateChat = privateChatManager.getChat(opponentID);
        if (privateChat == null) {
            privateChat = privateChatManager.createChat(opponentID, this);
        }else{
            privateChat.addMessageListener(this);
        }
    }

    @Override
    public void sendMessage(QBChatMessage message) throws XMPPException, SmackException.NotConnectedException {
        privateChat.sendMessage(message);
    }

    @Override
    public void release() {
        Log.w(TAG, "release private chat");
        privateChat.removeMessageListener(this);
        privateChatManager.removePrivateChatManagerListener(this);
    }

    @Override
    public void processMessage(QBPrivateChat chat, QBChatMessage message) {
        Log.w(TAG, "new incoming message: " + message);
        chatActivity.showMessage(message);
    }

    @Override
    public void processError(QBPrivateChat chat, QBChatException error, QBChatMessage originChatMessage){

    }

    @Override
    public void chatCreated(QBPrivateChat incomingPrivateChat, boolean createdLocally) {
        if(!createdLocally){
            privateChat = incomingPrivateChat;
            privateChat.addMessageListener(PrivateChatManagerImpl.this);
        }

        Log.w(TAG, "private chat created: " + incomingPrivateChat.getParticipant() + ", createdLocally:" + createdLocally);
    }
}
