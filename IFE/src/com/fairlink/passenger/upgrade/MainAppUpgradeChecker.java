package com.fairlink.passenger.upgrade;

import com.alibaba.fastjson.JSONObject;
import com.fairlink.passenger.application.Application;
import com.fairlink.passenger.application.ApplicationManager;
import com.fairlink.passenger.application.ApplicationManager.ApplicationInstallListener;
import com.fairlink.passenger.networkrequest.BaseHttpGetTask;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.Logger;

public class MainAppUpgradeChecker {


	static private MainAppUpgradeChecker instance = new MainAppUpgradeChecker();
	private MainAppUpgradeListener listener;
	private Logger logger = new Logger(this, "upgrade");
	boolean isChecking;

	public interface MainAppUpgradeListener {
		public void onFinish();
	}

	public static MainAppUpgradeChecker getInstance() {
		return instance;
	}

	private MainAppUpgradeChecker() {
	}
	
	class MainApplicationInfo {
		boolean isOK;
		String version;
		String path;
	}

	class GetNewVersionListener implements HttpTaskCallback {

		@Override
		public void onGetResult(int requestType, Object result) {
			if (result == null) {
				logger.error("receive incorrect respond, retry");
				restart();
				return;
			}

			MainApplicationInfo info = (MainApplicationInfo)result;
			if (!info.isOK){
				logger.error("receive respond with server error, retry");
				restart();
				return;
			}
			
			if (info.version == null || info.version.isEmpty()) {
				logger.info("no version on server, just use current one!");
				listener.onFinish();
				return;
			}

			
			String currentVersion = ApplicationManager.getInstence().getMainApplicationVersion();
			if (info.version.equals(currentVersion)) {
				logger.info("main app version check OK!");
				listener.onFinish();
			} else {
				logger.warn("main app version will upgrade " + currentVersion + " -> " + info.version);
				logger.info("start download new main app version");
				Application newMainApplication = new Application();
				newMainApplication.setPath(info.path);
				newMainApplication.setVersion(info.version);
				newMainApplication.setName("MainAPP");
				newMainApplication.setType("MainAPP");
				ApplicationManager.getInstence().downloadAndInstall(newMainApplication, new ApplicationInstallListener() {
					@Override
					public void onFinish() {
						// should nerver go here;
						logger.error("current application did not restart after upgrade! throw run time exception.");
						throw new RuntimeException();
					}
					@Override
					public void onError() {
						logger.error("download and install main application failed, retry");
						restart();
					}
				});
			}
		}

		@Override
		public void onError(int requestType) {
			logger.error("send version request failed, retry");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			restart();			
		}
	}
	
	class CheckUpdateTask extends BaseHttpGetTask {

		public CheckUpdateTask() {
			super(0, HttpUtil.getUpgradeMainApp(), null, new GetNewVersionListener());
		}

		@Override
		protected Object parseJSON(String jsonString) {
			MainApplicationInfo info = new MainApplicationInfo();
			try {
				JSONObject json = (JSONObject)JSONObject.parse(jsonString);
				info.isOK = json.getBoolean("code");
				info.version = json.getString("version");
				info.path = json.getString("path");
				return info;
			} catch (Exception e) {
				logger.error("prase json failed with error:" + e.getMessage());
				info.isOK = false;
				return info;
			}
		}
	}

	public void checkUpdate() {
		if(isChecking){
			return;
		}
		isChecking = true;
		new CheckUpdateTask().execute((String) null);
	}
	
	public void restart() {
		new CheckUpdateTask().execute((String) null);
	}

	public void cancel() {
		isChecking = false;
	}
	
	public void setListener(MainAppUpgradeListener l) {
		listener = l;
	}
}
