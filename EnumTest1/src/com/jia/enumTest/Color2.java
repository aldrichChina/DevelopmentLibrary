package com.jia.enumTest;

public enum Color2 {
	RED{

		@Override
		public String getColor() {
			return "��ɫ";
		}},
	BREEN{

		@Override
		public String getColor() {
			return "��ɫ";
		}},
	BLUE{

		@Override
		public String getColor() {
			return "��ɫ";
		}};
	public abstract String getColor();
}
