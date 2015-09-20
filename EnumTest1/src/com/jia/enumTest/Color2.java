package com.jia.enumTest;

public enum Color2 {
	RED{

		@Override
		public String getColor() {
			return "ºìÉ«";
		}},
	BREEN{

		@Override
		public String getColor() {
			return "ÂÌÉ«";
		}},
	BLUE{

		@Override
		public String getColor() {
			return "À¶É«";
		}};
	public abstract String getColor();
}
