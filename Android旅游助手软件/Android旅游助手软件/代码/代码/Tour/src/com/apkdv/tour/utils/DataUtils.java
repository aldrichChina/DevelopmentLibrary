package com.apkdv.tour.utils;

import java.util.ArrayList;
import java.util.Random;

import com.apkdv.tour.entity.Suites;
import com.apldv.tour.R;

public class DataUtils {
	static Random random = new Random();
	static int[] suitesId = new int[] { R.drawable.su0, R.drawable.su1,
			R.drawable.su2, R.drawable.su3, R.drawable.su4, R.drawable.su5,
			R.drawable.su6, R.drawable.su7, R.drawable.su8, R.drawable.su9,
			R.drawable.su10, R.drawable.su11, R.drawable.su12, R.drawable.su13,
			R.drawable.su14, R.drawable.su15, R.drawable.su16, R.drawable.su17,
			R.drawable.su18, R.drawable.su19, R.drawable.su20, R.drawable.su21,
			R.drawable.su22, R.drawable.su23, R.drawable.su24, R.drawable.su25,
			R.drawable.su26, R.drawable.su27, R.drawable.su28, R.drawable.su29,
			R.drawable.su30, R.drawable.su31, R.drawable.su32, R.drawable.su33,
			R.drawable.su34, R.drawable.su35, R.drawable.su36, R.drawable.su37,
			R.drawable.su38 };
	static int[] susId = new int[] { R.drawable.su0, R.drawable.su1,
			R.drawable.su2, R.drawable.su3, R.drawable.su4, R.drawable.su5,
			R.drawable.su6, R.drawable.su7, R.drawable.su8, R.drawable.su9,
			R.drawable.su10, R.drawable.su11, R.drawable.su12, R.drawable.su13,
			R.drawable.su14, R.drawable.su15, R.drawable.su16, R.drawable.su17,
			R.drawable.su18, R.drawable.su19, R.drawable.su20, R.drawable.su21,
			R.drawable.su22, R.drawable.su23, R.drawable.su24, R.drawable.su25,
			R.drawable.su26, R.drawable.su27, R.drawable.su28, R.drawable.su29,
			R.drawable.su30, R.drawable.su31, R.drawable.su32, R.drawable.su33,
			R.drawable.su34, R.drawable.su35, R.drawable.su36, R.drawable.su37,
			R.drawable.su38 };
		static String [] suName = new String []{"香港盛逸酒店","青岛海景花园大酒店","铂尔曼度假酒店","长沙茉莉花大酒店","金源大饭店",
				"北京神农庄园酒店","三亚华宇皇冠假日酒店","杭州西湖温德姆豪廷大酒店","厦门温德姆和平国际大酒店","瑞麟湾温泉度假酒店","北京伯豪瑞廷酒店",
				"北京市裕龙大酒店","上海宝隆美爵酒店","香港青逸酒店","香港丽都酒店","诺富特","上海神旺大酒店","厦门海悦山庄酒店","上海艾福敦酒店","雅乐轩酒店",
				"上海宝京精品酒店","朗廷酒店","豪生大酒店",
				"金陵饭店","华天大酒店","东莞凯景酒店","南京玄武饭店","深圳百合酒店","君悦酒店","瑞吉酒店","香港半岛酒店","北京万豪酒店","香港如心海景酒店",
				"迪拜酒店","北京千禧大酒店","珠海德翰大酒店","事赢宾馆","万家宾城","丽都宾城"};
		static String [] JName = new String []{"香港盛逸","青岛海景花园大景点","铂尔曼度假景点","长沙茉莉花大景点","金源大饭店",
			"北京神农庄园景点","三亚华宇皇冠假日景点","杭州西湖温德姆豪廷大景点","厦门温德姆和平国际大景点","瑞麟湾温泉度假景点","北京伯豪瑞廷景点",
			"北京市裕龙大景点","上海宝隆美爵景点","香港青逸景点","香港丽都景点","诺富特","上海神旺大景点","厦门海悦山庄景点","上海艾福敦景点","雅乐轩景点",
			"上海宝京精品景点","朗廷景点","豪生大景点",
			"金陵饭店","华天大景点","东莞凯景景点","南京玄武饭店","深圳百合景点","君悦景点","瑞吉景点","香港半岛景点","北京万豪景点","香港如心海景景点",
			"迪拜景点","北京千禧大景点","珠海德翰大景点","事赢景点","万家景点","丽都景点"};
		public static ArrayList<Suites> getSuites(){
			ArrayList<Suites> suites = new ArrayList<Suites>();
			for (int i = 0; i < suitesId.length; i++) {
				Suites suites2 = new Suites();
				suites2.setPidId(suitesId[i]);
				suites2.setName(suName[i]);
				suites2.setMoney(((random.nextInt(1000))-500)+500+"");
				suites.add(suites2);
			}
			return suites;
		}
		public static ArrayList<Suites> gettour(){
			ArrayList<Suites> suites = new ArrayList<Suites>();
			for (int i = 0; i < suitesId.length; i++) {
				Suites suites2 = new Suites();
				suites2.setPidId(suitesId[i]);
				suites2.setName(JName[i]);
				suites2.setMoney(((random.nextInt(1000))-500)+500+"");
				suites.add(suites2);
			}
			return suites;
		}
	
}
