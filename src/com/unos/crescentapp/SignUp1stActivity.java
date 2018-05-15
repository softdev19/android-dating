package com.unos.crescentapp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.unos.crescentapp.utilts.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp1stActivity extends Activity {
	private EditText edtxtEmail,edtxtPassword;//edtxtPhone
	private String userEmailId;
	private String userPhoneNO;
	private String userPassword;
	private TextView txtPrivacyPolicy,txtTerms;
	private TextView txtEmail,/*txtPhone,*/txtPassword,txtTitle,txtTerm,txtTerm1,txtTerm2;
	private Button btnNext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_1st);
		txtEmail = (TextView) findViewById(R.id.activity_signup_1st_txtvw_email);
		//txtPhone = (TextView) findViewById(R.id.activity_signup_1st_txtvw_phone);
		txtPassword = (TextView) findViewById(R.id.activity_signup_1st_txtvw_passwd);
		edtxtEmail=(EditText) findViewById(R.id.activity_signup_1st_et_email);
		//edtxtPhone=(EditText) findViewById(R.id.activity_signup_1st_et_phone);
		edtxtPassword=(EditText) findViewById(R.id.activity_signup_1st_et_password);
		txtPrivacyPolicy = (TextView) findViewById(R.id.activity_signup_1st_txtvw_privacy);
		txtTerms = (TextView) findViewById(R.id.activity_signup_1st_txtvw_terms);
		txtTitle = (TextView) findViewById(R.id.activity_signup_1st_txtvw_title);
		txtTerm = (TextView) findViewById(R.id.activity_signup_1st_txtvw_term);
		txtTerm1 = (TextView) findViewById(R.id.activity_signup_1st_txtvw_term1);
		txtTerm2 = (TextView) findViewById(R.id.activity_signup_1st_txtvw_term2);
		txtTitle.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtEmail.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		//txtPhone.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		txtPassword.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Semibold.ttf"));
		
		
		edtxtEmail.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		//edtxtPhone.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		edtxtPassword.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		txtTerm.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtTerm1.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		txtTerm2.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		
		findViewById(R.id.activity_signup_1st_imgbtn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
				 
			}
		});
		btnNext = (Button) findViewById(R.id.activity_signup_1st_btn_next);
		btnNext.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		btnNext.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//startActivity(new Intent(SignUp1stActivity.this, SignUp2ndActivity.class));
					
					if(validates()){
						
						Intent intent=new Intent(SignUp1stActivity.this, SignUp2ndActivity.class);
						userEmailId=edtxtEmail.getText().toString();
						userPassword=edtxtPassword.getText().toString();
						//userPhoneNO=edtxtPhone.getText().toString();
						intent.putExtra("emailId",userEmailId);
						intent.putExtra("passWord",userPassword);
						//intent.putExtra("phoneNo",userPhoneNO);
						startActivity(intent);
						overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
					//finish();
					}
				}
			});
		 txtPrivacyPolicy.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		 txtPrivacyPolicy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				copyReadAssatePrivacyPolicy();
				
			}
		});
		 txtTerms.setTypeface(Utils.getTypeface(SignUp1stActivity.this, "fonts/SourceSansPro-Regular.ttf"));
		 txtTerms.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				copyReadAssetsTerm();
			}
		});
	}
	 private boolean validates(){
		 
			if(this.isEmpty(edtxtEmail.getText().toString())){
				Toast.makeText(SignUp1stActivity.this,"Please Enter Your Email", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			if(!this.isEmail(edtxtEmail.getText().toString())){
				
				Toast.makeText(SignUp1stActivity.this,"Email ID is Invalid", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(this.isEmpty(edtxtPassword.getText().toString())){
				
				Toast.makeText(SignUp1stActivity.this,"Please Enter Your Password", Toast.LENGTH_SHORT).show();
				return false;
			}
			/*if(this.isEmpty(edtxtPhone.getText().toString())){	
				Toast.makeText(SignUp1stActivity.this,"Please Enter Your Phone No", Toast.LENGTH_SHORT).show();
				return false;
			}*/
			return true;
		}
	 
	 private void copyReadAssetsTerm(){
	     
         AssetManager assetManager = getAssets();

         InputStream in = null;
         OutputStream out = null;
         File file = new File(getFilesDir(), "CrescentTerms.pdf");
         try
         {
             in = assetManager.open("CrescentTerms.pdf");
             out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

             copyFile(in, out);
             in.close();
             in = null;
             out.flush();
             out.close();
             out = null;
         } catch (Exception e)
         {
             Log.e("tag", e.getMessage());
         }
         try {
           Intent intent = new Intent(Intent.ACTION_VIEW);
              intent.setDataAndType(
                      Uri.parse("file://" + getFilesDir() + "/CrescentTerms.pdf"),
                      "application/pdf");

              startActivity(intent);
  } catch (ActivityNotFoundException e) {
   Toast.makeText(SignUp1stActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
  }

       
     }
  //For Privacy Policy
  private void copyReadAssatePrivacyPolicy(){
    AssetManager assetManager = getAssets();

          InputStream in = null;
          OutputStream out = null;
          File file = new File(getFilesDir(), "CrescentPrivacyPolicy.pdf");
          try
          {
              in = assetManager.open("CrescentPrivacyPolicy.pdf");
              out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

              copyFile(in, out);
              in.close();
              in = null;
              out.flush();
              out.close();
              out = null;
          } catch (Exception e)
          {
              Log.e("tag", e.getMessage());
          }
          try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
               intent.setDataAndType(
                       Uri.parse("file://" + getFilesDir() + "/CrescentPrivacyPolicy.pdf"),
                       "application/pdf");

               startActivity(intent);
   } catch (ActivityNotFoundException e) {
    Toast.makeText(SignUp1stActivity.this, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
   }

   
  }

     private void copyFile(InputStream in, OutputStream out) throws IOException
     {
         byte[] buffer = new byte[1024];
         int read;
         while ((read = in.read(buffer)) != -1)
         {
             out.write(buffer, 0, read);
         }
     }
	 
	 //Empty checking
	 private boolean isEmpty(String str){
			if(str.trim().length() > 0){
				return false;
			}else{
				return true;
			}
		}
	//validation of email id
	 private boolean isEmail(String email){
			String regExpn ="^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
		                   +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
		                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
		                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
		                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
		     Matcher matcher = pattern.matcher(email);

		     if(matcher.matches())
		        return true;
		     else
		        return false;
		}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
		
	}
	 
	 
}
