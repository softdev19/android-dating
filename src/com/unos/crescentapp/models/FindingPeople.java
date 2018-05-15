package com.unos.crescentapp.models;

import java.io.Serializable;

import android.graphics.drawable.Drawable;

public class FindingPeople implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  user_id;
	private String name;
    private String email;
    private String contact;
    private String about;
    private String userimage;
    private String birthday;
    private String age;
    private String gender;
    private String location;
    private String interests;
    private String userlikestatus;
    private String  friendlikestatus;
    private String imagelikestatus;
    private String is_login;
    private String deviceToken;
    private String deviceType;
	private Drawable cardLikeImageDrawable;
	private Drawable cardDislikeImageDrawable;

   

    private OnClickListener mOnClickListener = null;

  

    public interface OnClickListener {
        void OnClickListener();
    }
    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public void setEmail(String email) {
		this.email = email;
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
	public String getUserimage() {
		return userimage;
	}
	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
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
	public String getUserlikestatus() {
		return userlikestatus;
	}
	public void setUserlikestatus(String userlikestatus) {
		this.userlikestatus = userlikestatus;
	}
	public String getFriendlikestatus() {
		return friendlikestatus;
	}
	public void setFriendlikestatus(String friendlikestatus) {
		this.friendlikestatus = friendlikestatus;
	}
	public String getImagelikestatus() {
		return imagelikestatus;
	}
	public void setImagelikestatus(String imagelikestatus) {
		this.imagelikestatus = imagelikestatus;
	}
	public String getIs_login() {
		return is_login;
	}
	public void setIs_login(String is_login) {
		this.is_login = is_login;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Drawable getCardLikeImageDrawable() {
		return cardLikeImageDrawable;
	}
	public void setCardLikeImageDrawable(Drawable cardLikeImageDrawable) {
		this.cardLikeImageDrawable = cardLikeImageDrawable;
	}
	public Drawable getCardDislikeImageDrawable() {
		return cardDislikeImageDrawable;
	}
	public void setCardDislikeImageDrawable(Drawable cardDislikeImageDrawable) {
		this.cardDislikeImageDrawable = cardDislikeImageDrawable;
	}
	
	public OnClickListener getmOnClickListener() {
		return mOnClickListener;
	}
	public void setmOnClickListener(OnClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}
  

}
