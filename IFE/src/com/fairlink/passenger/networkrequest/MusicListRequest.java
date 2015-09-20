package com.fairlink.passenger.networkrequest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fairlink.passenger.util.HttpUtil;

import android.content.Context;

public class MusicListRequest extends BaseHttpGetTask {

	public static class MusicInfo {
		public String id;    
		public String name;
		public String location;
		public String actor;
		public String image;
		public String playtimes;
		public String type;
		
		public MusicInfo(MusicInfo other) {
			id = other.id;
			name = other.name;
			location = other.location;
			actor = other.actor;
			image = other.image;
			playtimes = other.playtimes;
			type = other.type;
		}
		
		public MusicInfo() {}
		
		public static boolean equals(MusicInfo info1, MusicInfo info2) {
			if(info1 == null) {
				return false;
			}
			if(info2 == null) {
				return false;
			}
			return info1.id.equals(info2.id);
		}
	}
	
	public static class MusicList {
		public String code;
		public ArrayList<MusicInfo> list;
	}
	
	public MusicListRequest(Context context, HttpTaskCallback callback, int type) {
		super(MUSIC_LIST_API, HttpUtil.getMusList()+type, null, callback);
	}

	@Override
	protected Object parseJSON(String json) {
		if(json == null) {
			return null;
		}
		
		MusicList list = new MusicList();
		JSONTokener parser = new JSONTokener(json);
		try {
			JSONObject menu = (JSONObject) parser.nextValue();
			list.code = menu.getString("code");
			
			list.list = new ArrayList<MusicListRequest.MusicInfo>();
			JSONArray items = menu.getJSONArray("data");
			int len = items.length();
			JSONObject item;
			MusicInfo tmp;
			for(int i=0; i<len; i++) {
				item = items.getJSONObject(i);
				tmp = new MusicInfo();
				tmp.id = item.getString("musicId");
				tmp.image = item.getString("musicPosterUrl");
				tmp.name = item.getString("musicName");
				tmp.location = item.getString("musicUrl");
				tmp.actor = item.getString("musicSinger");
				tmp.playtimes = item.getString("musicTime");
				tmp.type = item.getString("musicType");
				list.list.add(tmp);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
