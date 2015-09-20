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
		static String [] suName = new String []{"���ʢ�ݾƵ�","�ൺ������԰��Ƶ�","�������ȼپƵ�","��ɳ���򻨴�Ƶ�","��Դ�󷹵�",
				"������ũׯ԰�Ƶ�","���ǻ���ʹڼ��վƵ�","���������µ�ķ��͢��Ƶ�","�����µ�ķ��ƽ���ʴ�Ƶ�","��������Ȫ�ȼپƵ�","����������͢�Ƶ�",
				"������ԣ����Ƶ�","�Ϻ���¡�����Ƶ�","������ݾƵ�","��������Ƶ�","ŵ����","�Ϻ�������Ƶ�","���ź���ɽׯ�Ƶ�","�Ϻ������ؾƵ�","�������Ƶ�",
				"�Ϻ�������Ʒ�Ƶ�","��͢�Ƶ�","������Ƶ�",
				"���극��","�����Ƶ�","��ݸ�����Ƶ�","�Ͼ����䷹��","���ڰٺϾƵ�","���þƵ�","�𼪾Ƶ�","��۰뵺�Ƶ�","��������Ƶ�","������ĺ����Ƶ�",
				"�ϰݾƵ�","����ǧ����Ƶ�","�麣�º���Ƶ�","��Ӯ����","��ұ���","��������"};
		static String [] JName = new String []{"���ʢ��","�ൺ������԰�󾰵�","�������ȼپ���","��ɳ���򻨴󾰵�","��Դ�󷹵�",
			"������ũׯ԰����","���ǻ���ʹڼ��վ���","���������µ�ķ��͢�󾰵�","�����µ�ķ��ƽ���ʴ󾰵�","��������Ȫ�ȼپ���","����������͢����",
			"������ԣ���󾰵�","�Ϻ���¡��������","������ݾ���","�����������","ŵ����","�Ϻ������󾰵�","���ź���ɽׯ����","�Ϻ������ؾ���","����������",
			"�Ϻ�������Ʒ����","��͢����","�����󾰵�",
			"���극��","����󾰵�","��ݸ��������","�Ͼ����䷹��","���ڰٺϾ���","���þ���","�𼪾���","��۰뵺����","�����������","������ĺ�������",
			"�ϰݾ���","����ǧ���󾰵�","�麣�º��󾰵�","��Ӯ����","��Ҿ���","��������"};
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
