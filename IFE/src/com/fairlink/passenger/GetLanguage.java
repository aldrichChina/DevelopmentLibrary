package com.fairlink.passenger;

public class GetLanguage {

	private static String items = "CN";
	
	public GetLanguage(int temp){
		if(temp == 0){
			items = "CN";
		
		}
		else if(temp ==1){
			items = "TW";
		}
		else if(temp ==2){
			items = "en";
		}
		else if(temp ==3){
			items = "ja";
		}
		else if(temp ==4){
			items = "ko_KR";
		}
	}
	
	public static String ChooseLan(){

		return items;
		
	}
}
