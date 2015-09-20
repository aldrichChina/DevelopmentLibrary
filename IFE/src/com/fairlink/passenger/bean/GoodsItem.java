package com.fairlink.passenger.bean;

import java.io.Serializable;

import com.fairlink.passenger.util.ComUtil;

public class GoodsItem implements Serializable {

	private static final long serialVersionUID = -3200860261919208495L;
	private int count;
	private Goods goods;

	public GoodsItem(int id, int count) {
		this.goods = new Goods();
		this.goods.setId(id);
		this.count = count;
	}

	public GoodsItem() {
	}

	public int getCount() {
		return count;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public void addCount(int count) {
		this.count += count;
	}
	
	public RecordGoodsItem getRecordGoodsItem() {
		return new RecordGoodsItem(goods.getId() + "", count, goods.getProductName(), goods.getGoodsName(), goods.getGoodsPreferentialPrice(), ComUtil.getPic(goods.getGoodsPic()));
	}
}
