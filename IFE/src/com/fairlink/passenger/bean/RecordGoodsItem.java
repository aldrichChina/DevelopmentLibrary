package com.fairlink.passenger.bean;
import java.io.Serializable;


/**
 * @ClassName ： GoodsOrderRecordItem
 * @Description: 我的订单列表商品数据类
 */
public class RecordGoodsItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5508816869922210596L;
	public RecordGoodsItem(String goodsId, int goodsBuyCount, String productName, String goodsName, double goodsPrice, String pic) {
		this.goodsId = goodsId;
		this.goodsBuyCount = goodsBuyCount;
		this.productName = productName;
		this.goodsName = goodsName;
		this.goodsPrice = goodsPrice;
		this.pic = pic;
	}

	public int goodsBuyCount; // 商品购买数量
	public String goodsName; // 商品
	public String productName; // 商品名称
	public double goodsPrice; // 商品价格
	public String pic; // 商品图片
	public String goodsId;// 商品id
}
