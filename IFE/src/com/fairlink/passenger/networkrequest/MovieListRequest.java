package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;

import android.content.Context;

public class MovieListRequest extends BaseHttpPostTask {

	public static class MovieList {
		public String code;
		public ArrayList<MovieInfo> items;
	}
	
	public static class MovieInfo {
		public String id;
		public String image;
		public String name;
//		public String director;
//		public String area;
//		public String actor;
		public String videoType;
		public String videoIsMovie;
	}
	
	public MovieListRequest(Context context, HttpTaskCallback callback, int type, int pageNo, int rows) {
		super(MOVIE_TV_LIST_API, HttpUtil.getVideoList(), new HashMap<String, String>(), callback);
		mParam.put("type", ""+type);
		mParam.put("pageNo", ""+pageNo);
		mParam.put("rows", ""+rows);

	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}
		MovieList movielist = new MovieList();
		ArrayList<MovieInfo> list = new ArrayList<MovieInfo>();
		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			movielist.code = menu.getString("code");
			movielist.items = list;
			JSONArray items = menu.getJSONArray("data");
			int len = items.length();
			JSONObject item;
			MovieInfo tmp;
			for(int i=0; i<len; i++) {
				item = items.getJSONObject(i);
				tmp = new MovieInfo();
				tmp.id = item.getString("videoId");
				tmp.image = item.getString("videoPoster");
				tmp.name = item.getString("videoName");
				tmp.videoType = item.getString("videoType");
				tmp.videoIsMovie = item.getString("videoIsMovie");
				list.add(tmp);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movielist;
	}
}
