package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;

import com.fairlink.passenger.util.HttpUtil;

public class MovieDetailRequest extends BaseHttpGetTask {

	public static class MovieDetail {
		public String id;
		public String name;
		public String director;
		public String actor;
		public String content;
		public String image;
		public String isMovice;
		public String area;
		public ArrayList<MoviePlayerItem> items;
	}
	
	public static class MoviePlayerItem {
		public String no;
		public String location;
	}
	
	public MovieDetailRequest(Context context, HttpTaskCallback callback,String id) {
		super(MOVIE_TV_DETAIL_API, HttpUtil.getVideoDetail()+id, null, callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}

		JSONTokener parser = new JSONTokener(json);
		MovieDetail tmp = new MovieDetail();
		try {
			JSONObject menu = (JSONObject) parser.nextValue();

			int code = menu.getInt("code");

			if (code != 0) {
				return null;
			}

			JSONObject data = menu.getJSONObject("data");

			tmp.id = data.getString("videoId");
			tmp.name = data.getString("videoName");
			tmp.director = data.getString("videoDirector");
			tmp.actor = data.getString("videoActors");
			tmp.content = data.getString("videoDesc");
			tmp.image = data.getString("videoPoster");
			tmp.isMovice = data.getString("videoIsMovie");
			tmp.area = data.getString("videoArea");

			tmp.items = new ArrayList<MovieDetailRequest.MoviePlayerItem>();
			JSONArray items = data.getJSONArray("itemList");
			int len = items.length();
			MoviePlayerItem item;
			JSONObject index;
			for (int i = 0; i < len; i++) {
				item = new MoviePlayerItem();
				index = (JSONObject) items.get(i);
				item.no = index.getString("videoItemNo");
				item.location = index.getString("videoItemLocation");
				tmp.items.add(item);
			}

			return tmp;
		} catch (JSONException e) {
			logger.error("MovieDetailRequest parse failed with error:" + e.getMessage());
			return null;
		}
	}

}
