package com.fairlink.passenger;

public class RecommendGetRecently {

	private static String[] info = new String[5];
	
	public RecommendGetRecently(String type,String selectid, String sid, String image,String vid)
	{		
		info[0] = type;
		info[1] = selectid;
		info[2] = sid;
		info[3] = image;
		info[4] = vid;
	}
	
	public static String[] GetInformation(){
		return info;
	}
	
	
}
