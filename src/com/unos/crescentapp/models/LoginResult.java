package com.unos.crescentapp.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class LoginResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogUser LogUser;
	private String Message;
	private String success;

	public String getMessage() {
		return Message;
	}

	public void setMessage(String Message) {
		this.Message = Message;
	}

	public LogUser getLogUser() {
		return LogUser;
	}

	public void setLogUser(LogUser LogUser) {
		this.LogUser = LogUser;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public static class LogUser implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("user_id")
		private String userId;
		private String name;
		private String email;
		private String password;
		private String profImage;
		@SerializedName("userimage")
		private ArrayList<Image> image;
		private String contact;
		private String about;
		private String birthday;
		private String gender;
		private String location;
		private String interests;
		private String show;
		@SerializedName("agerange")
		private String ageRange;
		private String login_type;
		private String is_active;
		private String is_login;
		private String newmatchesnotifystatus;
		private String messagesnotifystatus;
		@SerializedName("create_dt")
		private String createDt;
		private int quickblox_id;
		private String isnewuser;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public String getLogin() {
			if (email != null) {
				return email.replace("@", "").replace(".", "");
			}
			return "";
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getProfImage() {
			return profImage;
		}

		public void setProfImage(String profImage) {
			this.profImage = profImage;
		}

		public String [] getImage() {
			if(image == null) return new String[0];
			String [] ret = new String[image.size()];
			for(int i = 0; i < image.size(); i++){
				ret[i] = image.get(i).getImage();
			}
			return ret;
		}

		public void setImage(ArrayList<Image> image) {
			this.image = image;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getAbout() {
			return about;
		}

		public void setAbout(String about) {
			this.about = about;
		}

		public String getBirthday() {
			return birthday;
		}

		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getInterests() {
			return interests;
		}

		public void setInterests(String interests) {
			this.interests = interests;
		}

		public String getShow() {
			return show;
		}

		public void setShow(String show) {
			this.show = show;
		}

		public String getAgeRange() {
			return ageRange;
		}

		public void setAgeRange(String ageRange) {
			this.ageRange = ageRange;
		}

		public String getLogin_type() {
			return login_type;
		}

		public void setLogin_type(String login_type) {
			this.login_type = login_type;
		}

		public String getIs_active() {
			return is_active;
		}

		public void setIs_active(String is_active) {
			this.is_active = is_active;
		}

		public String getIs_login() {
			return is_login;
		}

		public void setIs_login(String is_login) {
			this.is_login = is_login;
		}

		public String getNewmatchesnotifystatus() {
			return newmatchesnotifystatus;
		}

		public void setNewmatchesnotifystatus(String newmatchesnotifystatus) {
			this.newmatchesnotifystatus = newmatchesnotifystatus;
		}

		public String getMessagesnotifystatus() {
			return messagesnotifystatus;
		}

		public void setMessagesnotifystatus(String messagesnotifystatus) {
			this.messagesnotifystatus = messagesnotifystatus;
		}

		public String getCreateDt() {
			return createDt;
		}

		public void setCreateDt(String createDt) {
			this.createDt = createDt;
		}

		public int getQuickblox_id() {
			return quickblox_id;
		}
		
	

		public boolean hasValidQuickbloxId() {
			
			return this.quickblox_id != 0;
			/*
			if (this.quickblox_id == null) {
				return false;
			}
			if ("".equalsIgnoreCase(this.quickblox_id)) {
				return false;
			}

			if ("0".equalsIgnoreCase(this.quickblox_id)) {
				return false;
			}

			return true;
			*/
		}

		public void setQuickblox_id(int quickblox_id) {
			this.quickblox_id = quickblox_id;
		}

		public String getIsnewuser() {
			return isnewuser;
		}
		public boolean isNewUser() {
			return "1".equalsIgnoreCase(this.isnewuser);
		}

		public void setIsnewuser(String isnewuser) {
			this.isnewuser = isnewuser;
		}

	}
}
