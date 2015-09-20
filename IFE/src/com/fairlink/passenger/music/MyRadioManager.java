package com.fairlink.passenger.music;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;

import com.fairlink.passenger.networkrequest.MusicListRequest.MusicInfo;

public class MyRadioManager {
	private static final String RADIOLIST_FILE = "radio_list.txt";
	
	private static MyRadioManager sInstance;
	private ArrayList<MusicInfo> mMyRadioList = new ArrayList<MusicInfo>();
	private Context mContext;
	
	private MyRadioManager(Context context) {
		mContext = context;
	}
	
	public static synchronized MyRadioManager getInstance(Context context) {
		if(sInstance == null) {
			sInstance = new MyRadioManager(context);
//			sInstance.loadRadioList();
		}
		return sInstance;
	}
	
	
	public ArrayList<MusicInfo> getRadioList() {
		return mMyRadioList;
	}
	
	
	public int getRadioListSize() {
		
		return mMyRadioList==null?0:mMyRadioList.size();
	}
	
	public boolean inRadioList(MusicInfo info) {
		for(MusicInfo item:mMyRadioList) {
			if(item.id.equals(info.id)) {
				return true;
			}
		}
		return false;
	}
	
	public void addMusic(MusicInfo info) {
		if(inRadioList(info)) {
			return;
		}
		MusicInfo newItem = new MusicInfo(info);
		mMyRadioList.add(newItem);
	}
	
	
	public void removeMusic(MusicInfo info) {
		Iterator<MusicInfo> it = mMyRadioList.iterator();
		MusicInfo item;
		while(it.hasNext()) {
			item = it.next();
			if(item.id.equals(info.id)) {
				it.remove();
				return;
			}
		}
	}
	
	public void saveRadioList() {
		File f = new File(mContext.getFilesDir(), RADIOLIST_FILE);
		f.delete();
		FileWriter write = null;
		try {
			f.createNewFile();
			write = new FileWriter(f);
			StringBuilder sb = new StringBuilder();
			for(MusicInfo item: mMyRadioList) {
				sb.append(item.id+"|"+item.name+"|"+item.location+"|"+item.image+"|"+item.actor+"\n");
			}
			write.write(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(write != null) {
				try {
					write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void loadRadioList() {
		mMyRadioList.clear();
		File f = new File(mContext.getFilesDir(), RADIOLIST_FILE);
		BufferedReader read = null;
		try {
			read = new BufferedReader(new FileReader(f));
			String line;
			String[] strs;
			MusicInfo info;
			while((line = read.readLine()) != null) {
				strs = line.split("|");
				info = new MusicInfo();
				info.id = strs[0];
				info.name = strs[1];
				info.location = strs[2];
				info.image = strs[3];
				info.actor = strs[4];
				mMyRadioList.add(info);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
