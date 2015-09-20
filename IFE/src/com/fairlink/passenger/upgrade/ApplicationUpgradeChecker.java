package com.fairlink.passenger.upgrade;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fairlink.passenger.application.Application;
import com.fairlink.passenger.application.ApplicationManager;
import com.fairlink.passenger.application.ApplicationManager.ApplicationInstallListener;
import com.fairlink.passenger.networkrequest.BaseHttpGetTask;
import com.fairlink.passenger.util.HttpUtil;
import com.fairlink.passenger.util.Logger;

public class ApplicationUpgradeChecker {

	private Logger logger = new Logger(this, "upgrade");

	boolean mIsProgressing;

	public interface ApplicationUpgradeListener {
		public void onFinish();
	}
	
	ApplicationUpgradeListener mListener;
	private LinkedList<Application> applicationDownloadList;

	public static ApplicationUpgradeChecker getInstance() {
		return new ApplicationUpgradeChecker();
	}

	private ApplicationUpgradeChecker() {}

	class ModularCheckTask extends BaseHttpGetTask {

		public ModularCheckTask() {
			super(0, HttpUtil.getApplications(), new HashMap<String, String>(), new HttpTaskCallback() {
				
				private Logger logger = new Logger(this, "upgrade");

				@Override
				public void onGetResult(int requestType, Object result) {
					if (result == null) {
						logger.info("no file on server, application upgrade finished.");
						finish();
						return;
					}

					if (!mIsProgressing)
						return;

					if (!(result instanceof List<?>))
						return;

					List<Application> serverApplicationList = (List<Application>)result;
					if (serverApplicationList.isEmpty()) {
						logger.info("no file on server, application upgrade finished.");
						finish();
						return;
					}

					ApplicationManager.getInstence().disableApplicationNotOnServer(serverApplicationList);
					
					logger.info("found " + serverApplicationList.size() + " applications on server");

					applicationDownloadList = new LinkedList<Application>();
					for (Application downloadApplication : serverApplicationList) {
						check(downloadApplication);
					}
					
					logger.info(applicationDownloadList.size() + " applications need be downloaded");

					startNext();
				}

				private void check(Application downloadApplication){
					for (Application localApplication : ApplicationManager.getInstence().applicationList()) {
						if (!downloadApplication.getId().equals(localApplication.getId())) {
							continue;
						}

						// if the version changed, must update the application. no matter other info changed or not.
						if (!localApplication.getVersion().equals(downloadApplication.getVersion()))
						{
							// Skip the loop, add the application to download list.
							break;
						} else {
							// if just info changed, update the database directly.
							if (!localApplication.getName().equals(downloadApplication.getName())
									|| !localApplication.getCategory().equals(downloadApplication.getCategory())
									|| !localApplication.getType().equals(downloadApplication.getType())
									|| !localApplication.getComponentName().equals(downloadApplication.getComponentName())
									|| !localApplication.getDescription().equals(downloadApplication.getDescription())
									|| !localApplication.getDeveloper().equals(downloadApplication.getDeveloper())
									|| !localApplication.getIsUsing().equals(downloadApplication.getIsUsing())
									|| !localApplication.getOrigin().equals(downloadApplication.getOrigin()))
							{
								if (!ApplicationManager.getInstence().updateApplication(downloadApplication)) {
									logger.error("failed to update [" + localApplication + "] to [" + downloadApplication + "]");
								}
							}
						}

						// goto next downloadApplication
						return;
					}

					// add the application to download list. the game applications to the queue tail, others from insert queue head. 
					if (downloadApplication.getType().equals("game"))
						applicationDownloadList.addLast(downloadApplication);
					else
						applicationDownloadList.addFirst(downloadApplication);
				}

				@Override
				public void onError(int requestType) {
					finish();
					logger.error("can't connect to the server");
				}
			});
		}

		@Override
		protected Object parseJSON(String jsonString) {
			if (jsonString == null) {
				return null;
			}

			JSONObject json = (JSONObject)JSONObject.parse(jsonString);
			return JSONArray.parseArray(json.getString("data"), Application.class);
		}

	}

	public void start() {
		if (mIsProgressing) {
			return;
		}
		mIsProgressing = true;
//		mLocalApplicationList = mDBManager.queryApplications();
		new ModularCheckTask().execute((String) null);
	}

	public void cancel() {
		mIsProgressing = false;
	}

	void finish() {

		mIsProgressing = false;

		if (mListener != null) {
			mListener.onFinish();

		}
	}

	void startNext() {
		if (applicationDownloadList.isEmpty()) {
			logger.info("application upgrade finished.");
			finish();
		} else {
			logger.info("start next application");
			ApplicationManager.getInstence().downloadAndInstall(applicationDownloadList.pollFirst(), 
					new ApplicationInstallListener() {
				@Override
				public void onFinish() {
					startNext();
				}
				@Override
				public void onError() {
					startNext();
				}
			});
		}
	}

	public void setListener(ApplicationUpgradeListener l) {
		mListener = l;
	}
}
