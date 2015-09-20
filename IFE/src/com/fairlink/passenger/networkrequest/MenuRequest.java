package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

public abstract class MenuRequest extends BaseHttpPostTask {
	
	public static class Menu {
		public String type;
		public String selectid;
		public String selectname;
	}
	
	public MenuRequest(Context context, HttpTaskCallback callback, String language, int api, String url) {
		super(api, url, new HashMap<String, String>(), callback);
		mParam.put("language", language);
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}
		ArrayList<Menu> list = new ArrayList<Menu>();
		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			JSONArray items = menu.getJSONArray("selectItem");
			int len = items.length();
			JSONObject item;
			Menu tmp;
			for(int i=0; i<len; i++) {
				item = items.getJSONObject(i);
				tmp = new Menu();
				tmp.type = item.getString("type");
				tmp.selectid = item.getString("selectid");
				tmp.selectname = item.getString("selectname");
				list.add(tmp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}

}
