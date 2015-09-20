package com.apkdv.tour.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentPagerAdapter
extends FragmentPagerAdapter
{
	ArrayList<Fragment> list;

	public MainFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
		super(fm);
		if (list==null)
		{
			this.list=new ArrayList<Fragment>();
		}else
		{
			this.list=list;
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		return list.get(index);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
