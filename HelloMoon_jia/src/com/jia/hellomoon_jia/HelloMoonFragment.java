package com.jia.hellomoon_jia;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class HelloMoonFragment extends Fragment {
	private AudioPlayer mPlayer = new AudioPlayer();
	private Button mPlayButton;
	private Button mStopButton;
	private SurfaceView mSurfaceView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_hello_moon, container,
				false);
		mPlayButton = (Button) v.findViewById(R.id.hellomoon_playButton);
		mPlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mPlayer.isPlaying()) {
					mPlayer.pause();
					mPlayButton.setText(R.string.hellomoon_play);
				} else {
					mPlayer.play(getActivity());
					mPlayButton.setText(R.string.hellomoon_pause);
				}
			}
		});
		mStopButton = (Button) v.findViewById(R.id.hellomoon_stopButton);
		mStopButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.stop();
				mPlayButton.setText(R.string.hellomoon_play);
			}
		});
		mSurfaceView = (SurfaceView) v.findViewById(R.id.hellomoon_surfaceView);
//		mPlayer.setSurfaceHolder(mSurfaceView.getHolder());
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
	}
}
