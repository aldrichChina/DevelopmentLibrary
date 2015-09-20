package com.fairlink.passenger.bean;

public class ResourceType {

	private String id;
	private String name;
	private String picture;
	private String resourceId;
	private int slot;
	private String type;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPicture() {
		return picture;
	}

	public String getResourceId() {
		return resourceId;
	}

	public int getSlot() {
		return slot;
	}

	public String getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public void setType(String type) {
		this.type = type;
	}
}