package com.fairlink.passenger.upgrade;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import android.os.Environment;

import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.BootAnimationRequest;
import com.fairlink.passenger.networkrequest.BootAnimationRequest.BootAnimationInfo;
import com.fairlink.passenger.networkrequest.DownloadTask;
import com.fairlink.passenger.networkrequest.DownloadTask.DownloadListener;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.util.Logger;

public class AnimationUpgradeChecker implements HttpTaskCallback, NetworkRequestAPI {
	private static AnimationUpgradeChecker instance = new AnimationUpgradeChecker();
	private Logger logger = new Logger(this, "Animation");
	static private final String ANIMATION_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/Android/";
	static private final String BOOTANIMATION_VERSION_FILE = "bootAnimationVersion.txt";

	private AnimationUpgradeChecker() {}

	public static AnimationUpgradeChecker getInstance() {
		return instance;
	}

	public void start () {
		new BootAnimationRequest(this).execute((String)null);
	}

	@Override
	public void onGetResult(int requestType, Object result) {
		if (result == null) {
			logger.error("receive incorrect animation upgrade data");
			return;
		}

		if (requestType == BOOT_ANIMATION_API) {
			BootAnimationInfo info = (BootAnimationInfo)result;
			
			if (info.code == false) {
				logger.error("receive animation respond with server error");
				return;
			}
			
			if (info.version.isEmpty() || info.path.isEmpty()) {
				logger.error("no animation version on server, just use current one!");
				return;
			}

			BufferedReader reader = null;
			String localversion = null;
			try {
				File file = new File(ANIMATION_FOLDER + BOOTANIMATION_VERSION_FILE);
				if (file.exists()) {
					reader = new BufferedReader(new FileReader(ANIMATION_FOLDER + BOOTANIMATION_VERSION_FILE));
					localversion = reader.readLine();					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (localversion != null && localversion.equals(info.version)) {
				return;
			}

			new DownloadTask(ANIMATION_FOLDER, new BootAnimationDownloadListner(info)).execute(info.path);
		}
	}
	
	@Override
	public void onError(int requestType) {
		// TODO Auto-generated method stub
		
	}
	
	private class BootAnimationDownloadListner implements DownloadListener {
		BootAnimationInfo bootAnimationInfo;
		public BootAnimationDownloadListner(BootAnimationInfo info) {
			bootAnimationInfo = info;
		}

		@Override
		public void OnFinish(boolean isSuccess, String savePath,
				Object... params) {
			if (!isSuccess) {
				logger.error("download animation file[" + savePath + "] failed");
				return;
			}

			try {
				File file = new File(savePath);
				if(file.exists()) {
					file.renameTo(new File(ANIMATION_FOLDER + "bootanimation.zip"));
				} else {
					logger.error("rename to bootanimation.zip failed");
					return;
				}

				FileWriter fw = new FileWriter(ANIMATION_FOLDER + BOOTANIMATION_VERSION_FILE);
				fw.write(bootAnimationInfo.version);
				fw.flush();
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
}
