package com.fairlink.passenger.view;


import com.fairlink.passenger.R;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumeDialog extends Dialog implements OnSeekBarChangeListener{
	private SeekBar seekVolumeBar;
	private AudioManager audioManager;
	private int maxVolume, currentVolume;
	private Dialogcallback volcallback;

	public VolumeDialog(Context context, int theme) {
		super(context,theme);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_volume);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		
		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		seekVolumeBar = (SeekBar) findViewById(R.id.pupup_volume_seek);
		seekVolumeBar.setOnSeekBarChangeListener(this);
	}
	

	private void setVolum() {
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		seekVolumeBar.setMax(maxVolume);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		seekVolumeBar.setProgress(currentVolume);
		volcallback.dialogdo(currentVolume);
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
		if (fromUser) {
			volcallback.dialogdo(progress);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					progress, 0);
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
	
	
	public interface Dialogcallback {
		public void dialogdo(int i);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.volcallback = dialogcallback;
		setVolum();
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
            	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_RAISE,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            	setVolum();
            	
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            	audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_LOWER,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            	setVolum();
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }


	
}
