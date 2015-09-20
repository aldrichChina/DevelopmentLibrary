package com.fairlink.passenger.util;

/**
 * @ClassName ： Constant
 * @Description: 应用程序配置类：用于保存用户相关信息及设置
 */

public class Constant {

	public static final int AD_TIME = 5;

	public static class Action {
		// 首页推荐
		public final static String ACTION_RECOMMEND = "com.fairlink.passenger.ACTION_RECOMMEND";
		// 视频播放，停止音乐播放
		public final static String ACTION_PLAY_VIDEO = "com.fairlink.passenger.ACTION_PLAY_VIDEO";
		// 通知广播
		public final static String ACTION_INFORM = "com.fairlink.passenger.inform";
	}

	public static final int PAYTYPE_CASH = 0;
	public static final int PAYTYPE_SCORE = 1;
	public static final int PAYTYPE_CREDIT_CARD = 2;
}
