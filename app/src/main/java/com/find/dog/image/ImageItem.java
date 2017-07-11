package com.find.dog.image;

import java.io.Serializable;

public class ImageItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String path;

	public ImageItem(String p) {
		this.path = p;
	}
}
