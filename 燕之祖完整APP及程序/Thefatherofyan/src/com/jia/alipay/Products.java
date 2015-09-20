/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.jia.alipay;

import java.util.ArrayList;

/**
 * demo展示商品列表的商品信息
 * 
 */
public final class Products {

	public class ProductDetail {
		String subject;
		String body;
		String price;
		int resId;
	}

	ArrayList<Products.ProductDetail> mProductlist = new ArrayList<Products.ProductDetail>();

	public ArrayList<ProductDetail> retrieveProductInfo() {
		ProductDetail productDetail = null;

		//
		// x < 50
		productDetail = new ProductDetail();
		productDetail.subject = "123456";
		productDetail.body = "2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红";
		productDetail.price = "一口价:0.01";
		productDetail.resId = 30;
		mProductlist.add(productDetail);

		//
		// x < 50
		productDetail = new ProductDetail();
		productDetail.subject = "魅力香水";
		productDetail.body = "新年特惠 adidas 阿迪达斯走珠 香体止汗走珠 多种香型可选";
		productDetail.price = "一口价:0.01";
		productDetail.resId = 30;
		mProductlist.add(productDetail);
		
		return mProductlist;
	}
}
