package com.fairlink.passenger.util;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

	private static Logger logger = new Logger(null, "JsonUtil");

	public static <T> List<T> parseJsonArray(String json, Class<T> clazz) {
		try {
			return (List<T>) JSON.parseArray(getDataField(json, clazz).getJSONArray("data").toString(), clazz);
		} catch (JSONException e) {
			logger.error("parse json with error" + e.getMessage() + ". when parse class [" + clazz.getName()
					+ "], json string [" + json + "]");
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static <T> T parseJsonObjcet(String json, Class<T> clazz) {
		try {
			return JSON.parseObject(getDataField(json, clazz).getJSONObject("data").toString(), clazz);
		} catch (JSONException e) {
			logger.error("parse json array with error" + e.getMessage() + ". when parse class [" + clazz.getName()
					+ "], json string [" + json + "]");
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	private static <T> JSONObject getDataField(String json, Class<T> clazz) throws Exception {
		if (json == null || json.isEmpty()) {
			logger.error("json string is null");
			throw new Exception();
		}

		JSONTokener parser = new JSONTokener(json);
		JSONObject menu = (JSONObject) parser.nextValue();
		int code = menu.getInt("code");
		if (code != 0) {
			logger.error("receive error code " + code + " when parse class [" + clazz.getName() + "], json string ["
					+ json + "]");
			throw new Exception();
		}

		return menu;
	}
}
