package com.jia.enumTest;

public class TestColor04 {
	public static void main(String[]args){
		Color color=Color.valueOf(Color.class,"RED");
		System.out.println(color);
	}
}
