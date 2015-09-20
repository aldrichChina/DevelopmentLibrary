package com.fairlink.passenger.bean;

public class GoodsTypeWithRelation {
	private int id; // goodsType 当前节点id
	private int parent; // 父节点
	private String goodsTypeId; // 与goods表中将要新增typeId 字段对应
	private String goodsTypeName; // goodsType 名称

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParent() {
		return parent;
	}

	public void setParent(int parent) {
		this.parent = parent;
	}

	public String getGoodsTypeId() {
		return goodsTypeId;
	}

	public void setGoodsTypeId(String goodsTypeId) {
		this.goodsTypeId = goodsTypeId;
	}

	public String getGoodsTypeName() {
		return goodsTypeName;
	}

	public void setGoodsTypeName(String goodsTypeName) {
		this.goodsTypeName = goodsTypeName;
	}
}