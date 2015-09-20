package com.jia.enumTest;

import java.util.EnumSet;
import java.util.Iterator;

public class TestColor06 {
	public static void main(String[]args){
		EnumSet<Color> eSet=EnumSet.allOf(Color.class);
		Iterator<Color>iter=eSet.iterator();
		while(iter.hasNext()){
			System.out.println(iter.next());
		}
	}
}
