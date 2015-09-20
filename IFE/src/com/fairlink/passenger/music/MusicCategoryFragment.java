package com.fairlink.passenger.music;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.music.DialogMusic.TimeDialogcallback;
import com.fairlink.passenger.networkrequest.ADNewPicRequest;
import com.fairlink.passenger.networkrequest.ADNewPicRequest.ADNEW;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.VolumeDialog;
import com.fairlink.passenger.view.VolumeDialog.Dialogcallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MusicCategoryFragment extends BaseFragment implements
	 OnItemClickListener, OnClickListener, HttpTaskCallback,NetworkRequestAPI  {

	public static interface MusicCategorySelectedListener {
		public void onMusicCategorySelected(int current);

	}

	private MusicCategorySelectedListener mListener;
	private ListView mList;
	private ImageView mRadiolist;  // 音乐收藏
	private ImageView mSleep;      // 音乐播放停止时间
	private ImageView mVolume;     // 播放声音
	private ImageView adSideBar;   // 侧栏底部广告
	private CategoryAdapter mAdapter;
	private String[] menuTitles;
	private int mCurrentCategory = 1;
	
	private int currentPlaying = -1;
	
	private String 	adsExternalUrl = "";
	private int 	adsRelatedType;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		new ADNewPicRequest(this, ADNewPicRequest.ADS_TYPE_BANNER).execute((String) null);
		
		mListener = (MusicCategorySelectedListener) activity;
		menuTitles = getResources().getStringArray(R.array.type_music);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.music_category_fragment, null);
		ImageView title = (ImageView) view.findViewById(R.id.title);
		title.setImageResource(R.drawable.category_music);
		TextView title1 = (TextView) view.findViewById(R.id.title1);
		title1.setText(R.string.index_music_str);
		title1.setTextSize(getResources().getDimension(R.dimen.category_little_size));
		mList = (ListView) view.findViewById(R.id.category_list);
		
		adSideBar = (ImageView) view.findViewById(R.id.img_ad_side_bar);
		
		mAdapter = new CategoryAdapter(inflater);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(this);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setItemChecked(0, true);
		
		mSleep = (ImageView) view.findViewById(R.id.music_sleep);
		mSleep.setOnClickListener(this);
		mVolume = (ImageView) view.findViewById(R.id.music_volume);
		mVolume.setOnClickListener(this);
		MusicTime();

		mRadiolist = (ImageView) view.findViewById(R.id.myradio_list);
		mRadiolist.setOnClickListener(this);
		ShowMyRadio();
		adSideBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!ComUtil.isEmpty(adsExternalUrl)) {
					ShopUtil.showShop(getActivity(), adsRelatedType,	adsExternalUrl);
				}
			}
		});
		return view;
	}

	public void MusicTime() {
		if (AlarmData.AlarmDataGetEnable()) {
			mSleep.setImageResource(R.drawable.time_true);
		} else {
			mSleep.setImageResource(R.drawable.time_normal);
		}

	}

	public void ShowVolumeImg(int currentVolume) {
				
		if (currentVolume == 0) {
			mVolume.setImageResource(R.drawable.player_volume_img_0);
		} else if (currentVolume > 0 && currentVolume <= 4) {
			mVolume.setImageResource(R.drawable.player_volume_img_1);
		} else if (currentVolume > 4 && currentVolume <= 9) {
			mVolume.setImageResource(R.drawable.player_volume_img_small);
		} else if (currentVolume > 9 && currentVolume <= 14) {
			mVolume.setImageResource(R.drawable.player_volume_img_2);
		} else if (currentVolume == 15) {
			mVolume.setImageResource(R.drawable.player_volume_img_big);
		}
	}

	public void ShowMyRadio() {

		int size = MyRadioManager.getInstance(getActivity()).getRadioListSize();

		if (size > 0) {
			mRadiolist.setImageResource(R.drawable.music_favorite);
		} else {
			mRadiolist.setImageResource(R.drawable.music_not_favorite);
		}

	}

	class CategoryAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CategoryAdapter(LayoutInflater inflater) {
			mInflater = inflater;
		}

		@Override
		public int getCount() {
			return menuTitles == null ? 0 : menuTitles.length;
		}

		@Override
		public String getItem(int position) {
			return menuTitles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String menu = (String) getItem(position);
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.menu_item_music, null);
				holder = new ViewHolder();
				holder.menu = (TextView) convertView.findViewById(R.id.name);
				holder.currentPlaying = (ImageView) convertView.findViewById(R.id.current_playing);
				holder.currentindicator = (ImageView) convertView
						.findViewById(R.id.current_indicator);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.menu.setText(menu);
			if ((mCurrentCategory-1) == position) {
				holder.menu.setTextColor(Color.WHITE);
				holder.currentindicator.setVisibility(View.VISIBLE);
			} else {
				holder.menu.setTextColor(0x99FFFFFF);
				holder.currentindicator.setVisibility(View.INVISIBLE);
			}
			if(currentPlaying == position){
				holder.currentPlaying.setVisibility(View.VISIBLE);
			}else{
				holder.currentPlaying.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

	}

	static class ViewHolder {
		TextView menu;
		ImageView currentindicator;
		ImageView currentPlaying;
	}



	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mCurrentCategory = (arg2+1);
		mListener.onMusicCategorySelected(mCurrentCategory);
		mList.setItemChecked(arg2, true);
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.myradio_list:
			mCurrentCategory = -1;
			mListener.onMusicCategorySelected(mCurrentCategory);
			mList.setItemChecked(mCurrentCategory, false);
			break;
			
		case R.id.music_sleep:
			DialogMusic();
			break;
			
		case R.id.music_volume:
			DialogVolume();
			break;
			
		}
		

	}

	public void DialogVolume() {
		VolumeDialog n = new VolumeDialog(getActivity(), R.style.dialog);
		n.setDialogCallback(volcallback);
		n.setCancelable(true);
		n.show();
	}

	public void DialogMusic() {
		DialogMusic m = new DialogMusic(getActivity(), R.style.dialog);
		m.setCancelable(true);
		m.setDialogCallback(timecallback);
		m.show();
	}

	public void setCurrentMenu(int current) {
		
		if(current < menuTitles.length) {
			mList.setItemChecked(current, true);
			mAdapter.notifyDataSetChanged();
		}

	}
	
	Dialogcallback volcallback = new Dialogcallback() {

		@Override
		public void dialogdo(int i) {
			ShowVolumeImg(i);
		}
	};
	
	TimeDialogcallback timecallback = new TimeDialogcallback() {

		@Override
		public void timedialogdo() {
			MusicTime();
		}
	};
	
	public void MusicShowPlaying(String type,boolean playing){
		
		if(playing){
			currentPlaying = Integer.parseInt(type)-1;
		}else{
			currentPlaying = -1;
		}
		
		mAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if(requestType == ADS_PIC_API) {
			if(result == null) {
				return;
			}
			
			ADNEW data = (ADNEW) result;
			String pic = data.adsPath;
			if (pic != null) {
				adSideBar.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(pic, adSideBar);
			}
			adsExternalUrl = data.adsExternalUrl;
			adsRelatedType = data.adsRelatedType;
		}
		
	}

	@Override
	public void onError(int requestType) {
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}
}
