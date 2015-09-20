package com.apkdv.tour.entity;

public class Suites {
	public int _id;
	public String name;
	public int pidId;
	public String money;
	public Suites(int _id, String name, int pidId, String money) {
		this._id = _id;
		this.name = name;
		this.pidId = pidId;
		this.money = money;
	}
	public Suites() {
	}
	@Override
	public String toString() {
		return "Suites [_id=" + _id + ", name=" + name + ", pidId=" + pidId
				+ ", money=" + money + "]";
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPidId() {
		return pidId;
	}
	public void setPidId(int pidId) {
		this.pidId = pidId;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
}
