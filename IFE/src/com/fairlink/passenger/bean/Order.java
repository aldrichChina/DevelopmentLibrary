package com.fairlink.passenger.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fairlink.passenger.networkrequest.LoginRequest.Passenger;

public class Order {
	private String orderId;

	private String orderNoOnflight;

	private String orderNoOnland;

	private String goodsMall;

	private String orderType;

	private Date orderTime;

	private String orderUserAddress;

	private String orderReceiveMan;

	private String orderReceivePhone;

	private String orderPostCode;

	private String orderFlightId;

	private Byte orderStatus;

	private Byte memberCardType;

	private String memberCardNumber;

	private String orderRemarks;

	private Float orderGoodsPrice;

	private String orderGoodsInfo;

	private Byte distributionWay;

	private String orderPayWay;

	private Passenger passenger;// 乘务端用

	private List<GoodsItem> goodsList = new ArrayList<GoodsItem>();// 乘务端用

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getOrderNoOnflight() {
		return orderNoOnflight;
	}

	public void setOrderNoOnflight(String orderNoOnflight) {
		this.orderNoOnflight = orderNoOnflight == null ? null : orderNoOnflight.trim();
	}

	public String getOrderNoOnland() {
		return orderNoOnland;
	}

	public void setOrderNoOnland(String orderNoOnland) {
		this.orderNoOnland = orderNoOnland == null ? null : orderNoOnland.trim();
	}

	public String getGoodsMall() {
		return goodsMall;
	}

	public void setGoodsMall(String goodsMall) {
		this.goodsMall = goodsMall == null ? null : goodsMall.trim();
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType == null ? null : orderType.trim();
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderUserAddress() {
		return orderUserAddress;
	}

	public void setOrderUserAddress(String orderUserAddress) {
		this.orderUserAddress = orderUserAddress == null ? null : orderUserAddress.trim();
	}

	public String getOrderReceiveMan() {
		return orderReceiveMan;
	}

	public void setOrderReceiveMan(String orderReceiveMan) {
		this.orderReceiveMan = orderReceiveMan == null ? null : orderReceiveMan.trim();
	}

	public String getOrderReceivePhone() {
		return orderReceivePhone;
	}

	public void setOrderReceivePhone(String orderReceivePhone) {
		this.orderReceivePhone = orderReceivePhone == null ? null : orderReceivePhone.trim();
	}

	public String getOrderPostCode() {
		return orderPostCode;
	}

	public void setOrderPostCode(String orderPostCode) {
		this.orderPostCode = orderPostCode == null ? null : orderPostCode.trim();
	}

	public String getOrderFlightId() {
		return orderFlightId;
	}

	public void setOrderFlightId(String orderFlightId) {
		this.orderFlightId = orderFlightId == null ? null : orderFlightId.trim();
	}

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Byte getMemberCardType() {
		return memberCardType;
	}

	public void setMemberCardType(Byte memberCardType) {
		this.memberCardType = memberCardType;
	}

	public String getMemberCardNumber() {
		return memberCardNumber;
	}

	public void setMemberCardNumber(String memberCardNumber) {
		this.memberCardNumber = memberCardNumber == null ? null : memberCardNumber.trim();
	}

	public String getOrderRemarks() {
		return orderRemarks;
	}

	public void setOrderRemarks(String orderRemarks) {
		this.orderRemarks = orderRemarks == null ? null : orderRemarks.trim();
	}

	public Float getOrderGoodsPrice() {
		return orderGoodsPrice;
	}

	public void setOrderGoodsPrice(Float orderGoodsPrice) {
		this.orderGoodsPrice = orderGoodsPrice;
	}

	public String getOrderGoodsInfo() {
		return orderGoodsInfo;
	}

	public void setOrderGoodsInfo(String orderGoodsInfo) {
		this.orderGoodsInfo = orderGoodsInfo == null ? null : orderGoodsInfo.trim();
	}

	public Byte getDistributionWay() {
		return distributionWay;
	}

	public void setDistributionWay(Byte distributionWay) {
		this.distributionWay = distributionWay;
	}

	public String getOrderPayWay() {
		return orderPayWay;
	}

	public void setOrderPayWay(String orderPayWay) {
		this.orderPayWay = orderPayWay == null ? null : orderPayWay.trim();
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public List<GoodsItem> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<GoodsItem> goodsList) {
		this.goodsList = goodsList;
	}

	public boolean isUnhandled() {
		return (orderStatus == 0 && orderPayWay.equals("2")) || (orderStatus == 1 && orderPayWay.equals("0"));
	}
}