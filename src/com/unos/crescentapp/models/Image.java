package com.unos.crescentapp.models;

import java.io.Serializable;

public class Image  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String image;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
