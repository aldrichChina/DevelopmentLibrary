package com.fairlink.passenger.bean;

public class Goods {

	private String goodsId;

	private int id;

	private String goodsName;

	private String goodsDesc;

	private String goodsPic;
	
	private String detailDesc;

	private String productId;

	private String goodsMall;

	private String productName;

	private Float goodsRecommendLevel;

	private Float goodsPrice;

	private Float goodsPreferentialPrice;

	private Integer goodsLimitNum;

	private Float goodsCarriageCharge;

	private Integer goodsStockNum;

	private Byte goodsIsOn;

	private Long goodsAddTime;
	private String paymentType;

	private Float goodsExchangeScore;

	private Byte distributionWay;
	
	

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId == null ? null : goodsId.trim();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId == null ? null : productId.trim();
	}

	public String getGoodsMall() {
		return goodsMall;
	}

	public void setGoodsMall(String goodsMall) {
		this.goodsMall = goodsMall == null ? null : goodsMall.trim();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName == null ? null : productName.trim();
	}

	public Float getGoodsRecommendLevel() {
		return goodsRecommendLevel;
	}

	public void setGoodsRecommendLevel(Float goodsRecommendLevel) {
		this.goodsRecommendLevel = goodsRecommendLevel;
	}

	public Float getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(Float goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Float getGoodsPreferentialPrice() {
		return goodsPreferentialPrice;
	}

	public void setGoodsPreferentialPrice(Float goodsPreferentialPrice) {
		this.goodsPreferentialPrice = goodsPreferentialPrice;
	}

	public Integer getGoodsLimitNum() {
		return goodsLimitNum;
	}

	public void setGoodsLimitNum(Integer goodsLimitNum) {
		this.goodsLimitNum = goodsLimitNum;
	}

	public Float getGoodsCarriageCharge() {
		return goodsCarriageCharge;
	}

	public void setGoodsCarriageCharge(Float goodsCarriageCharge) {
		this.goodsCarriageCharge = goodsCarriageCharge;
	}

	public Integer getGoodsStockNum() {
		return goodsStockNum;
	}

	public void setGoodsStockNum(Integer goodsStockNum) {
		this.goodsStockNum = goodsStockNum;
	}

	public Byte getGoodsIsOn() {
		return goodsIsOn;
	}

	public void setGoodsIsOn(Byte goodsIsOn) {
		this.goodsIsOn = goodsIsOn;
	}

	public Long getGoodsAddTime() {
		return goodsAddTime;
	}

	public void setGoodsAddTime(Long goodsAddTime) {
		this.goodsAddTime = goodsAddTime;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType == null ? null : paymentType.trim();
	}

	public Float getGoodsExchangeScore() {
		return goodsExchangeScore;
	}

	public void setGoodsExchangeScore(Float goodsExchangeScore) {
		this.goodsExchangeScore = goodsExchangeScore;
	}

	public Byte getDistributionWay() {
		return distributionWay;
	}

	public void setDistributionWay(Byte distributionWay) {
		this.distributionWay = distributionWay;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName == null ? null : goodsName.trim();
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc == null ? null : goodsDesc.trim();
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic == null ? null : goodsPic.trim();
	}

	public String getDetailDesc() {
		return detailDesc;
	}

	public void setDetailDesc(String detailDesc) {
		this.detailDesc = detailDesc;
	}
}