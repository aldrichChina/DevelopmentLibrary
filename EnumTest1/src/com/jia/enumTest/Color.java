package com.jia.enumTest;

public enum Color implements Info {
	RED {
		@Override
		public String getColor() {
			return "ºìÉ«";
		}
	},
	GREEN {
		@Override
		public String getColor() {
			return "ÂÌÉ«";
		}
	},
	BLUE {

		@Override
		public String getColor() {
			return "À¶É«";
		}
	};

}
