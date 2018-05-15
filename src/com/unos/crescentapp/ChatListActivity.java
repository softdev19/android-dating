package com.unos.crescentapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;

import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBAttachment;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialog;
import com.quickblox.content.QBContent;
import com.quickblox.content.model.QBFile;
import com.quickblox.core.QBEntityCallbackImpl;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;
import com.unos.crescentapp.asynctask.GetUserById;
import com.unos.crescentapp.adapter.ChatListAdapter;
import com.unos.crescentapp.constant.Constants;
import com.unos.crescentapp.utilts.ChatManager;
import com.unos.crescentapp.utilts.PrivateChatManagerImpl;
import com.unos.crescentapp.utilts.TouchImageView;
import com.unos.crescentapp.utilts.Utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChatListActivity extends BaseActivity {
	private static final String TAG = ChatListActivity.class.getSimpleName();
	private ChatListAdapter chatAdapter;
	ArrayList<QBChatMessage> chatHistory = new ArrayList<>();

	public static enum Mode {
		PRIVATE, GROUP
	}

	public Mode mode = Mode.PRIVATE;
	public static final String EXTRA_MODE = "mode";
	public static final String EXTRA_DIALOG = "dialog";
	private final String PROPERTY_SAVE_TO_HISTORY = "save_to_history";
	private TouchImageView fullScreenImage;
	private View fullScreenView;
	private EditText messageEditText;
	private ListView messagesContainer;
	private ImageView sendButton, sendAttachment;
	private ProgressBar progressBar;
	private ChatManager chat;
	private String oppUserName, oppUserImage;
	private QBDialog dialog;
	private Bitmap chatBitmap;

	public static void start(Context context, Bundle bundle) {
		Intent intent = new Intent(context, ChatListActivity.class);
		intent.putExtra("Chat_Details", bundle);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chatlist);
		((TextView) findViewById(R.id.activity_chatlist_txtvw_title)).setTypeface(Utils.getTypeface(
				ChatListActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		messageEditText = (EditText) findViewById(R.id.activity_chatlist_edtxt_send);
		messageEditText.setTypeface(Utils.getTypeface(ChatListActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle = intent.getBundleExtra("Chat_Details");
		messagesContainer = (ListView) findViewById(R.id.activity_chatlist_listview);
		sendAttachment = (ImageView) findViewById(R.id.activity_chatlist_btn_attach);
		sendButton = (ImageView) findViewById(R.id.activity_chatlist_imgvw_send);
		dialog = (QBDialog) bundle.getSerializable(EXTRA_DIALOG);
		mode = (Mode) bundle.getSerializable(EXTRA_MODE);
		oppUserName = bundle.getString("userName");
		oppUserImage = bundle.getString("userImage");
		fullScreenImage = (TouchImageView) findViewById(R.id.fullscreenImage);
		fullScreenView = findViewById(R.id.fullscreenLayout);
		((TextView) findViewById(R.id.activity_chatlist_txtvw_title)).setText(oppUserName);
		findViewById(R.id.activity_chatlist_imgbtn_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
			}
		});

		if (Utils.isNetworkConnected(ChatListActivity.this)) {
			if (Utils.isReachable(ChatListActivity.this)) {
				Integer opponentID = AppController.getInstance().getOpponentIDForPrivateDialog(dialog, mLogUser.getQuickblox_id());
				chat = new PrivateChatManagerImpl(ChatListActivity.this, opponentID);
				QBUser user = null;
				try {
					user = new GetUserById(opponentID).execute().get();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// oppUserName = user.getLogin();
				showProgress();
				loadChatHistory();
			} else
				showToast(getString(R.string.unable_connect));
		} else
			showToast(getString(R.string.network_not_avail));

		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String messageText = messageEditText.getText().toString();
				if (TextUtils.isEmpty(messageText)) {
					return;
				}

				// Send chat message
				//
				QBChatMessage chatMessage = new QBChatMessage();
				chatMessage.setBody(messageText);
				chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
				chatMessage.setDateSent(new Date().getTime() / 1000);

				try {
					chat.sendMessage(chatMessage);
				} catch (XMPPException e) {
					Log.e(TAG, "failed to send a message", e);
				} catch (SmackException sme) {
					Log.e(TAG, "failed to send a message", sme);
				}

				messageEditText.setText("");

				if (mode == Mode.PRIVATE) {
					showMessage(chatMessage);
				}
			}
		});

		sendAttachment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.selectImage(ChatListActivity.this);

			}
		});
		
		messagesContainer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				QBChatMessage chatMessage = chatAdapter.getItem(position);
				if(chatMessage == null)return;
					if (chatMessage.getBody().equalsIgnoreCase("null")) {
			
						fullScreenView.setVisibility(View.VISIBLE);
						QBAttachment attachment = (QBAttachment) chatMessage.getAttachments().toArray()[0];
						Picasso.with(ChatListActivity.this).load(attachment.getUrl())
								.into(fullScreenImage);

					}
				
			}
		});

	}
	
	public void closeFS(View v){
		fullScreenView.setVisibility(View.GONE);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.PICK_FROM_CAMERA) {
				try {

					File f = new File(Utils.convertImageUriToFile(Utils.imageUri, ChatListActivity.this));
					Bitmap mBitmap = decodeBitmapFile(f);

					chatBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2,
							true);
					showProgress();
					attachImageToChat(chatBitmap);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*
				 * try { chatBitmap = (Bitmap) data.getExtras().get("data");
				 * attachImageToChat( chatBitmap); } catch (Exception e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 */

			} else if (requestCode == Constants.PICK_FROM_GALLERY && data != null) {
				File mFileTemp = Utils.getPicLocation(getApplicationContext());
				try {

					InputStream inputStream = getBaseContext().getContentResolver().openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
					copyStream(inputStream, fileOutputStream);
					fileOutputStream.close();
					inputStream.close();
					chatBitmap = decodeBitmapFile(mFileTemp);

					showProgress();
					attachImageToChat(chatBitmap);

				} catch (Exception e) {

					Log.e(TAG, "Error while creating temp file", e);
				}
			}

		}

	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public void showMessage(QBChatMessage message) {
		chatAdapter.add(message);

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				chatAdapter.notifyDataSetChanged();
				scrollDown();
			}
		});
	}

	private void attachImageToChat(Bitmap btmap) {
		File filePhoto = Utils.getFile(ChatListActivity.this, btmap);
		QBContent.uploadFileTask(filePhoto, true, null, new QBEntityCallbackImpl<QBFile>() {
			@Override
			public void onSuccess(QBFile file, Bundle params) {

				// create a message
				QBChatMessage chatMessage = new QBChatMessage();
				chatMessage.setProperty("save_to_history", "1"); // Save a
																	// message
																	// to
																	// history
				chatMessage.setBody("null");
				// attach a photo
				/*
				 * QBAttachment attachment = new QBAttachment("photo");
				 * attachment.setId(file.getId().toString());
				 * attachment.setUrl(file.getPublicUrl());
				 * chatMessage.setBody("null");
				 * chatMessage.addAttachment(attachment); //
				 * chatMessage.setAttachments(chatMessage.getAttachments());
				 */
				QBAttachment attachment = new QBAttachment(QBAttachment.PHOTO_TYPE);
				attachment.setId(file.getUid());
				attachment.setName(file.getName());
				attachment.setContentType(file.getContentType());
				attachment.setUrl(file.getPublicUrl());
				attachment.setSize(file.getSize());
				chatMessage.addAttachment(attachment);

				chatMessage.setProperty(PROPERTY_SAVE_TO_HISTORY, "1");
				chatMessage.setDateSent(new Date().getTime() / 1000);
				try {
					chat.sendMessage(chatMessage);
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (mode == Mode.PRIVATE) {
					showMessage(chatMessage);
				}
				stopProgress();

			}

			@Override
			public void onError(List<String> errors) {
				stopProgress();
			}
		});
	}

	private void loadChatHistory() {
		QBRequestGetBuilder customObjectRequestBuilder = new QBRequestGetBuilder();
		customObjectRequestBuilder.setPagesLimit(100);
		customObjectRequestBuilder.sortDesc("date_sent");

		QBChatService.getDialogMessages(dialog, customObjectRequestBuilder,
				new QBEntityCallbackImpl<ArrayList<QBChatMessage>>() {
					@Override
					public void onSuccess(ArrayList<QBChatMessage> messages, Bundle args) {
						chatHistory = messages;

						chatAdapter = new ChatListAdapter(ChatListActivity.this, new ArrayList<QBChatMessage>(),
								oppUserName, oppUserImage);
						messagesContainer.setAdapter(chatAdapter);

						for (int i = messages.size() - 1; i >= 0; --i) {
							QBChatMessage msg = messages.get(i);
							showMessage(msg);
						}

						stopProgress();
					}

					@Override
					public void onError(List<String> errors) {
						stopProgress();
						AlertDialog.Builder dialog = new AlertDialog.Builder(ChatListActivity.this);
						dialog.setMessage("load chat history errors: " + errors).create().show();
					}
				});
	}

	private void scrollDown() {
		messagesContainer.setSelection(messagesContainer.getCount() - 1);
	}
}
