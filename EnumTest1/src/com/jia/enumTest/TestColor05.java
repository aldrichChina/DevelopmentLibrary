package com.jia.enumTest;

import java.util.EnumMap;
import java.util.Map;

public class TestColor05 {
	public static void main(String args[]){
		EnumMap<Color, String>eMap=new EnumMap<Color, String>(Color.class);
		eMap.put(Color.RED, "��ɫ");
		eMap.put(Color.GREEN, "��ɫ");
		eMap.put(Color.BLUE, "��ɫ");
		for(Map.Entry<Color, String>me:eMap.entrySet()){
			System.out.println(me.getKey()+"----->"+me.getValue());
		}
	}
}
