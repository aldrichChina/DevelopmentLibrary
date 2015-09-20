package com.fairlink.passenger.bean;

public class DynamicType {

	private int id;
	private String name;
	private String picture;
	private int resourceId;
	private int slot;
	private String type;

	public static final String MODULE_NEWS = "news";
	public static final String MODULE_CUSTOMER = "customer";
	public static final String MODULE_GAME = "game";
	public static final String MODULE_ABOUT = "about";
	public static final String MODULE_AIRENV = "airenv";
	public static final String MODULE_EBOOK = "ebook";
	public static final String MODULE_MUSIC = "music";
	public static final String MODULE_MOVIE = "movie";

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPicture() {
		return picture;
	}

	public int getResourceId() {
		return resourceId;
	}

	public int getSlot() {
		return slot;
	}

	public String getType() {
		return type;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLaunchByResouceId() {
		if (type == null)
			return false;

		return !getType().endsWith("_activity");
	}

	public boolean isModule(String mod) {
		if (type == null)
			return false;

		return type.startsWith(mod);
	}
}