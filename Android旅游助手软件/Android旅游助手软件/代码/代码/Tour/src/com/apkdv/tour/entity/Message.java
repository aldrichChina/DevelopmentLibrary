package com.apkdv.tour.entity;

public class Message {
	public int _id;
	public String message;
	public String userName;
	public Message(int _id, String message, String userName) {
		this._id = _id;
		this.message = message;
		this.userName = userName;
	}
	@Override
	public String toString() {
		return "Message [_id=" + _id + ", message=" + message + ", userName="
				+ userName + "]";
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Message() {
	}
	
	
}
