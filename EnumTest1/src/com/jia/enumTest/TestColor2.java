package com.jia.enumTest;

public class TestColor2 {
	public static void main(String[]args){
		for(Color c:Color.values()){
			System.out.println(c.ordinal()+"."+c.name()+c.getColor());
		}
	}
}
