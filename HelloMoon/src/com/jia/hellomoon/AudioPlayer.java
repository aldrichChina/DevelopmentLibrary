package com.jia.hellomoon;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AudioPlayer {
	private MediaPlayer mplayer;
	private boolean isPause;
	private SurfaceHolder mSurfaceHolder;

	public void stop() {
		if (mplayer != null) {
			mplayer.release();
			mplayer = null;
			isPause = false;
		}
	}

	public void play(Context c) {
		if (isPause) {
			mplayer.start();
		} else {
			stop();
			mplayer = MediaPlayer.create(c, R.raw.one_small_step);
			mplayer.setDisplay(mSurfaceHolder);
			mplayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					stop();
				}
			});
			mplayer.start();
		}
		isPause = false;
	}

	public void pause() {
		if (mplayer != null) {
			mplayer.pause();
			isPause = true;
		}
	}

	public boolean isPlaying() {
		if (mplayer != null)
			return mplayer.isPlaying();
		else
			return false;
	}

	public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
		mSurfaceHolder = surfaceHolder;
	}
}
