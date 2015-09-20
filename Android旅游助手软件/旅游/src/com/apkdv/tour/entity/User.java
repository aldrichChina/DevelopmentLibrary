package com.apkdv.tour.entity;

public class User {
	public int _id;
	public String userName;
	public String password;
	public String myName;
	public String phone;
	public String money;
	public User(int _id, String userName, String password, String myName,
			String phone, String money) {
		this._id = _id;
		this.userName = userName;
		this.password = password;
		this.myName = myName;
		this.phone = phone;
		this.money = money;
	}
	public User() {
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMyName() {
		return myName;
	}
	public void setMyName(String myName) {
		this.myName = myName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "User [_id=" + _id + ", userName=" + userName + ", password="
				+ password + ", myName=" + myName + ", phone=" + phone
				+ ", money=" + money + "]";
	}
	
}
