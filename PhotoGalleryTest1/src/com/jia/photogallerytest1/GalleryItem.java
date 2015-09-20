package com.jia.photogallerytest1;

public class GalleryItem {
	private String mCaption;
	private String mId;
	private String mUrl;
	public String toString(){
		return mCaption;
	}
	public String getmCaption() {
		return mCaption;
	}
	public void setmCaption(String mCaption) {
		this.mCaption = mCaption;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getmUrl() {
		return mUrl;
	}
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	
}
