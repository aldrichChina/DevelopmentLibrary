package com.fairlink.passenger.networkrequest;

import java.util.HashMap;

import android.content.Context;

import com.fairlink.passenger.bean.BookInfo;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

/**
 * @ClassName ：BookByIdRequest
 * @Description: 获取电子书详情
 */

public class BookByIdRequest extends BaseHttpGetTask {

	public static class Book {
		public String code;
		// public
	}

	public static class BookDetail {
		public int bookPicsNo;
		public String bookPicsPath;
	}

	public BookByIdRequest(Context context, String ebookId, HttpTaskCallback callback) {
		super(BOOK_BY_ID_API, HttpUtil.getBookById(), new HashMap<String, String>(), callback);
		mParam.put("ebookId", ebookId);
	}

	@Override
	protected Object parseJSON(String json) {

		return JsonUtil.parseJsonObjcet(json, BookInfo.class);
	}
}
