package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;


/**
 * @ClassName  ：  BookDetailRequest 
 * @Description: 获取电子书图片详情
 */

public class BookDetailRequest extends BaseHttpGetTask {

	public static class BookList {
		public String code;
		public ArrayList<BookDetail> items;
	}

	public static class BookDetail {
		public int bookPicsNo;
		public String bookPicsPath;
	}
	
	public BookDetailRequest(Context context,String ebookId, HttpTaskCallback callback) {
		super(BOOK_DETAIL_API, HttpUtil.getBookListPic(), new HashMap<String, String>(), callback);
		mParam.put("ebookId", ebookId);
	}

	
	@Override
	protected Object parseJSON(String json) {


		if(json == null) {
			return null;
		}
		BookList Booklist = new BookList();
		
		try {
			
			JSONTokener parser = new JSONTokener(json);
			JSONObject menu = (JSONObject) parser.nextValue();
			int code = menu.getInt("code");
			
			if(0 != code) {
				
				return null;
			}
			
			ArrayList<BookDetail> list = new ArrayList<BookDetail>();

			Booklist.items = list;
			
			JSONArray items = menu.getJSONObject("data").getJSONArray("picsList");
			int len = items.length();
			JSONObject item;
			BookDetail tmp;
			for(int i=0; i<len; i++) {
				item = items.getJSONObject(i);
				tmp = new BookDetail();
				tmp.bookPicsNo = item.getInt("bookPicsNo");
				tmp.bookPicsPath = item.getString("bookPicsPath");
				list.add(tmp);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Booklist;
	}

}
