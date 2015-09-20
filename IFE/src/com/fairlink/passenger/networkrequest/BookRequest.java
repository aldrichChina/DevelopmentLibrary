package com.fairlink.passenger.networkrequest;

import android.content.Context;

import com.fairlink.passenger.bean.BookInfo;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.JsonUtil;

public class BookRequest extends BaseHttpGetTask {

	public BookRequest(Context context, HttpTaskCallback callback) {
		super(BOOK_RQ_API, HttpUtil.getBookListAll(), null, callback);
	}

	@Override
	protected Object parseJSON(String json) {
		return JsonUtil.parseJsonArray(json, BookInfo.class);
	}
}