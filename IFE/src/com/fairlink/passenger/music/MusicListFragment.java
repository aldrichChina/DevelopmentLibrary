package com.fairlink.passenger.music;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.music.MusicPlayerService.MusicStateCallback;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.MusicListRequest;
import com.fairlink.passenger.networkrequest.MusicListRequest.MusicInfo;
import com.fairlink.passenger.networkrequest.MusicListRequest.MusicList;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.PhotoManager;
import com.fairlink.passenger.networkrequest.PhotoManager.PhotoDownloadCallback;
import com.fairlink.passenger.util.ComUtil;

public class MusicListFragment extends BaseFragment implements OnItemClickListener, OnClickListener, HttpTaskCallback,
		PhotoDownloadCallback, MusicStateCallback, OnSeekBarChangeListener, NetworkRequestAPI {

	private static final int MSG_UPDATE_PLAYING = 2;

	private View mListContent;
	private TextView mEmpty;

	private TextView mTitle;
	private ImageButton mPrev, mPlay, mNext;
	private ImageView mImage;
	private ListView mMusicList;
	private SeekBar mSeek;

	private TextView mInfo;
	private MusicAdapter mMusicAdapter;

	private ArrayList<MusicInfo> mData;
	private MusicInfo mCurrentMusic;
	private String currentMusicId;

	private boolean mPaused = true;
	private MusicPlayerService mService;
	private TextView mCurrentTime, mEndTime;
	private ImageView mRecycle;
	private boolean noNet;

	@SuppressLint("HandlerLeak")
	private Handler mH = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPDATE_PLAYING:
				mSeek.setProgress(mService.getCurrentPercent());
				mCurrentTime.setText(getCurrentTimeText());
				mH.sendEmptyMessageDelayed(MSG_UPDATE_PLAYING, 1000);
				break;
			}
		}
	};

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((MusicPlayerService.LocalBinder) service).getService();
			mService.addCallback(MusicListFragment.this);
			mPlay.setEnabled(true);
			mSeek.setEnabled(true);
			mSeek.setProgress(mService.getCurrentPercent());
			if (mService.isPlaying()) {
				mH.removeMessages(MSG_UPDATE_PLAYING);
				mH.sendEmptyMessage(MSG_UPDATE_PLAYING);
				mListener.MusicTypeSelected(mService.getCurrentType(), true);
			}
			mRecycle.setImageResource(getRecycleImage());
			// 等服务启动后 在进行网络请求

			setMusicCategory(1);
		}
	};

	public static interface MusListListener {
		public void MusicTypeSelected(String type, boolean playing);

		/**
		 * 
		 * @param typeMus
		 *            音乐类型
		 * @param musName
		 *            音乐名称
		 * @param type
		 *            操作类型 4: play 5:pause
		 */
		public void Analytics(String typeMus, String musName, int type);
	}

	private MusListListener mListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		activity.startService(new Intent(activity, MusicPlayerService.class));
		activity.bindService(new Intent(activity, MusicPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
		mListener = (MusListListener) activity;

		mData = new ArrayList<MusicInfo>();
	}

	public void onDetach() {
		mService.removeCallback(this);
		mH.removeMessages(MSG_UPDATE_PLAYING);
		getActivity().unbindService(mConnection);
		super.onDetach();
	}

	private void showEmpty(boolean show) {
		if (show) {
			if (noNet)
				mEmpty.setText(getString(R.string.no_net));
			else
				mEmpty.setText(getString(R.string.empty_list));

			mEmpty.setVisibility(View.VISIBLE);
			mListContent.setVisibility(View.INVISIBLE);
		} else {
			mEmpty.setVisibility(View.INVISIBLE);
			mListContent.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.music_list_layout, null);

		mListContent = view.findViewById(R.id.list_content);
		mEmpty = (TextView) view.findViewById(R.id.empty);

		mTitle = (TextView) mListContent.findViewById(R.id.title);
		mPrev = (ImageButton) mListContent.findViewById(R.id.back);
		mPlay = (ImageButton) mListContent.findViewById(R.id.play);
		mNext = (ImageButton) mListContent.findViewById(R.id.forward);
		mPrev.setOnClickListener(this);
		mPlay.setOnClickListener(this);
		mNext.setOnClickListener(this);

		mMusicList = (ListView) mListContent.findViewById(R.id.music_list);
		mMusicAdapter = new MusicAdapter();
		mMusicList.setAdapter(mMusicAdapter);
		mMusicList.setOnItemClickListener(this);

		mImage = (ImageView) mListContent.findViewById(R.id.image);
		mSeek = (SeekBar) mListContent.findViewById(R.id.progress);

		mSeek.setOnSeekBarChangeListener(this);
		mInfo = (TextView) mListContent.findViewById(R.id.info);

		if (mService != null) {
			mPlay.setEnabled(true);
			mSeek.setEnabled(true);
		} else {
			mPlay.setEnabled(false);
			mSeek.setEnabled(false);
		}

		mCurrentTime = (TextView) mListContent.findViewById(R.id.current_time);
		mEndTime = (TextView) mListContent.findViewById(R.id.end_time);
		mRecycle = (ImageView) mListContent.findViewById(R.id.recycle);
		mRecycle.setOnClickListener(this);

		showEmpty(true);

		return view;
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public class MusicAdapter extends BaseAdapter implements OnClickListener {

		private LayoutInflater mInflater;

		public MusicAdapter() {
			mInflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			MusicInfo info = (MusicInfo) getItem(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.music_item, null);
				holder.currentIndicator = (ImageView) convertView.findViewById(R.id.current_indicator);
				holder.index = (TextView) convertView.findViewById(R.id.index);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.singer = (TextView) convertView.findViewById(R.id.singer);
				holder.duration = (TextView) convertView.findViewById(R.id.duration);
				holder.favorite = (ImageView) convertView.findViewById(R.id.favorite);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MusicInfo current = mService.getCurrentMusic();
			if (MusicInfo.equals(info, current)) {
				holder.currentIndicator.setVisibility(View.VISIBLE);
				initView(current);
			} else {
				holder.currentIndicator.setVisibility(View.INVISIBLE);
			}
			holder.index.setText(String.valueOf(position + 1));
			holder.name.setText(info.name);
			holder.singer.setText(info.actor);
			holder.duration.setText(info.playtimes);

			holder.name.setTag(info);

			if (MyRadioManager.getInstance(getActivity()).inRadioList(info)) {
				holder.favorite.setImageResource(R.drawable.music_favorite);
			} else {
				holder.favorite.setImageResource(R.drawable.music_not_favorite);
			}

			holder.favorite.setTag(info);
			holder.favorite.setOnClickListener(this);

			return convertView;
		}

		@Override
		public void onClick(View v) {
			MusicInfo info = (MusicInfo) v.getTag();
			if (MyRadioManager.getInstance(getActivity()).inRadioList(info)) {
				MyRadioManager.getInstance(getActivity()).removeMusic(info);
			} else {
				MyRadioManager.getInstance(getActivity()).addMusic(info);
			}
			notifyDataSetChanged();
			getActivity().sendBroadcast(new Intent("com.fairlink.passenger.musictime"));

		}

	}

	static class ViewHolder {
		ImageView currentIndicator;
		TextView index;
		TextView name;
		TextView singer;
		TextView duration;
		ImageView favorite;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ViewHolder holder = (ViewHolder) arg1.getTag();
		MusicInfo info = (MusicInfo) holder.name.getTag();
		initCurrentMusic(info);
		mListener.MusicTypeSelected(mService.getCurrentType(), true);
		mListener.Analytics(mService.getCurrentType(), info.name, 4);
	}

	public void setMusicCategory(int category) {

		if (category == -1) {
			ArrayList<MusicInfo> mTemp = MyRadioManager.getInstance(getActivity()).getRadioList();
			if (mTemp.size() > 0) {
				mData.clear();
				for (MusicInfo info : mTemp) {
					mData.add(info);
				}

				initView(mData.get(0));
				mMusicAdapter.notifyDataSetChanged();
				mService.setPlayList(mData);
				showEmpty(false);
			} else {
				showEmpty(true);
			}
		} else {
			mData.clear();
			showEmpty(true);
			new MusicListRequest(getActivity(), this, category).execute((String) null);
		}
	}

	public void setTempCurrentMusicId(String musId) {
		currentMusicId = musId;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play:
			MusicInfo current = mService.getCurrentMusic();
			if (mPaused) {
				boolean newsong = false;
				if (!MusicInfo.equals(current, mCurrentMusic)) {
					mService.setUri(mCurrentMusic);
					newsong = true;
				}
				if (!newsong) {
					mService.start();
					mPlay.setImageResource(R.drawable.music_pause);
				}
				mListener.MusicTypeSelected(mService.getCurrentType(), true);
				mListener.Analytics(mService.getCurrentType(), mService.getCurrentMusic().name, 4);
				mH.removeMessages(MSG_UPDATE_PLAYING);
				mH.sendEmptyMessage(MSG_UPDATE_PLAYING);
				mPrev.setEnabled(true);
				mNext.setEnabled(true);
				mSeek.setEnabled(true);
			} else {
				mService.pause();
				mListener.MusicTypeSelected(mService.getCurrentType(), false);
				mListener.Analytics(mService.getCurrentType(), current.name, 5);

				mH.removeMessages(MSG_UPDATE_PLAYING);
				mPlay.setImageResource(R.drawable.music_play);

			}
			mPaused = !mPaused;
			mMusicAdapter.notifyDataSetChanged();
			break;
		case R.id.forward:
			int index1 = findIndexOfCurrentMusic(mCurrentMusic);
			if (index1 == -1) {
				index1 = 0;
			} else {
				index1 = (index1 + 1) % mData.size();
			}
			initCurrentMusic(mData.get(index1));
			mListener.Analytics(mService.getCurrentType(), mData.get(index1).name, 4);
			break;
		case R.id.back:
			int index2 = findIndexOfCurrentMusic(mCurrentMusic);

			if (index2 == -1) {
				index2 = mData.size() - 1;
			} else {
				index2 = (index2 + mData.size() - 1) % mData.size();
			}

			initCurrentMusic(mData.get(index2));
			mListener.Analytics(mService.getCurrentType(), mData.get(index2).name, 4);
			break;
		case R.id.recycle:
			mService.setNextPlayMode();
			mRecycle.setImageResource(getRecycleImage());
			break;
		}

	}

	private int findIndexOfCurrentMusic(MusicInfo info) {
		MusicInfo item;
		for (int i = 0; i < mData.size(); i++) {
			item = mData.get(i);
			if (MusicInfo.equals(item, info)) {
				return i;
			}
		}
		return -1;
	}

	private int getRecycleImage() {
		int mode = mService.getPlayMode();
		if (mode == MusicPlayerService.PLAY_RECYCLE) {
			return R.drawable.recycle;
		} else {
			return R.drawable.recycle_one;
		}
	}

	private void initCurrentMusic(MusicInfo info) {
		MusicInfo current = mService.getCurrentMusic();
		if (MusicInfo.equals(current, info)) {
			return;
		}
		mService.setUri(info);
		initView(info);
		mMusicAdapter.notifyDataSetChanged();
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		MusicList list = (MusicList) result;
		if (list != null) {

			noNet = false;
			mData = list.list;
			mMusicAdapter.notifyDataSetChanged();
			if (mData.size() > 0) {
				if (ComUtil.isEmpty(currentMusicId)) {
					boolean find = false;
					for (MusicInfo info : mData) {
						if ((info.id).equals(currentMusicId)) {
							initView(info);
							find = true;
							break;
						}
					}
					currentMusicId = null;
					if (!find) {
						initView(mData.get(0));
					}
				} else {
					initView(mData.get(0));
				}
				showEmpty(false);
			} else {
				showEmpty(true);
			}
			mService.setPlayList(mData);
		} else {
			noNet = true;
			showEmpty(true);
		}
	}

	private void initView(MusicInfo info) {
		mCurrentMusic = info;
		mTitle.setText(mCurrentMusic.name + "--" + mCurrentMusic.actor);
		String image = PhotoManager.getInstance().getImageFile(mCurrentMusic.image);
		if (image != null) {
			mImage.setImageBitmap(PhotoManager.getInstance().decodePhoto(image, mImage.getWidth(), mImage.getHeight()));
			// mImage.setImageURI(Uri.fromFile(new File(image)));
		} else {
			PhotoManager.getInstance().downloadImage(mCurrentMusic.image, this);
		}

		StringBuilder sb = new StringBuilder();
		sb.append(mCurrentMusic.name + "\n").append(mCurrentMusic.actor);
		mInfo.setText(sb.toString());
		mEndTime.setText(mCurrentMusic.playtimes);
		updatePlayState();
	}

	private void updatePlayState() {
		MusicInfo current = mService.getCurrentMusic();
		if (!MusicInfo.equals(current, mCurrentMusic)) {
			mSeek.setProgress(0);
			mSeek.setEnabled(false);
			mH.removeMessages(MSG_UPDATE_PLAYING);
			mPaused = true;
			mCurrentTime.setText("00:00");
			mPlay.setImageResource(R.drawable.music_play);
		} else {
			mSeek.setProgress(mService.getCurrentPercent());
			mSeek.setEnabled(true);
			if (mService.isPlaying()) {
				mPaused = false;
				mH.removeMessages(MSG_UPDATE_PLAYING);
				mH.sendEmptyMessage(MSG_UPDATE_PLAYING);
				mPlay.setImageResource(R.drawable.music_pause);
			} else {
				mPaused = true;
				mH.removeMessages(MSG_UPDATE_PLAYING);
				mPlay.setImageResource(R.drawable.music_play);
			}
			mCurrentTime.setText(getCurrentTimeText());
		}
	}

	private String getCurrentTimeText() {
		int sec = mService.getCurrentPosition() / 1000;
		int minutes = sec / 60;
		int secs = sec % 60;
		String m, s;
		if (minutes < 10) {
			m = "0" + minutes;
		} else {
			m = String.valueOf(minutes);
		}

		if (secs < 10) {
			s = "0" + secs;
		} else {
			s = String.valueOf(secs);
		}
		return m + ":" + s;
	}

	@Override
	public void onPhotoDownload(String url, final String path) {
		if (path == null) {
			return;
		}
		if (url.equals(mCurrentMusic.image)) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mImage.setImageURI(Uri.fromFile(new File(path)));
				}
			});
		}
	}
	
	@Override
	public void onPhotoDownloadError(String url, String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(int errorcode) {
		if (getActivity() == null) {
			return;
		}
		if (errorcode != REDIRECT_API) {
			ComUtil.toastText(getActivity(), "此歌曲当前无法播放，请检查您的网络或者收听其他歌曲!", Toast.LENGTH_LONG);
		}
	}

	@Override
	public void onPrepared() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updatePlayState();

			}
		});

	}

	@Override
	public void onUpdated(int progress) {

	}

	@Override
	public void onCompelted() {
		mSeek.setProgress(0);
		if (mService.getPlayMode() == MusicPlayerService.PLAY_RECYCLE_ONE) {
			mPaused = false;
			mPlay.setImageResource(R.drawable.music_pause);
		} else {
			mPaused = true;
			mH.removeMessages(MSG_UPDATE_PLAYING);
			mCurrentTime.setText("00:00");
			mMusicAdapter.notifyDataSetChanged();
			MusicInfo info = mService.getCurrentMusic();
			initView(info);
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mService.seekToPercent(seekBar.getProgress());

	}

	@Override
	public void onStopPlaying() {

	}

	public void onTimeStop() {
		if (!mPaused) {
			mPlay.setImageResource(R.drawable.music_play);
			mMusicAdapter.notifyDataSetChanged();
			mPaused = !mPaused;
		}

	}

}
