package com.fairlink.passenger.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.fairlink.passenger.application.dbmanager.DBManager;
import com.fairlink.passenger.networkrequest.DownloadTask;
import com.fairlink.passenger.util.Logger;
import com.fairlink.passenger.util.PackageUtils;

public class ApplicationManager {
	static private final String APPLICATION_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/application/";
	private Context context;
	private DBManager mDBManager;
	private static ApplicationManager instance = new ApplicationManager();
	private List<InstalledApplication> installedApplicationList;
	private Logger logger = new Logger(this, "application");
	static private final String UPGRADE_APP = "com.fairlink.upgrade";

	private ApplicationManager() {
	}

	public static ApplicationManager getInstence() {
		return instance;
	}

	public List<InstalledApplication> applicationList() {
		return installedApplicationList;
	}

	public void setContext(Context context) {
		this.context = context;
		mDBManager = new DBManager(context);
		initializeInstalledApplicationList();
	}
	
	public boolean updateApplication(Application application) {
		for (Application installedApplication : installedApplicationList) {
			if (installedApplication.getId().equals(application.getId())) {
				logger.info("update application from [" + installedApplication.toString() + "] to [" + application.toString() + "]");
				installedApplication.setName(application.getName());
				installedApplication.setCategory(application.getCategory());
				installedApplication.setType(application.getType());
				installedApplication.setComponentName(application.getComponentName());
				installedApplication.setDescription(application.getDescription());
				installedApplication.setDeveloper(application.getDeveloper());
				installedApplication.setIsUsing(application.getIsUsing());
				installedApplication.setOrigin(application.getOrigin());
				installedApplication.setVersion(application.getVersion());
				mDBManager.update(application);
				return true;
			}
		}

		return false;
	}

	public void disableApplicationNotOnServer(List<Application> serverApplicationList) {
		for (Application installedApplication : installedApplicationList) {
			boolean isFound = false;
			for (Application serverApplication : serverApplicationList) {
				if (installedApplication.getId().equals(serverApplication.getId())) {
					isFound = true;
					break;
				}
			}

			if (!isFound) {
				logger.error("can't find installed application [" + installedApplication.getName() + "] on server, so disable it.");
				installedApplication.setIsUsing(0);
				mDBManager.delete(installedApplication);
			}
		}
	}

	public void downloadAndInstall(final Application application, ApplicationInstallListener listener) {
		logger.info("start download application [" + application.getName()
				+ "]");
		new DownloadTask(APPLICATION_FOLDER, new ApplicationDownloadListener(application, listener)).execute(application.getPath());
	}

	public interface ApplicationInstallListener {
		void onFinish();
		void onError();
	}

	class ApplicationDownloadListener implements DownloadTask.DownloadListener {
		Application application;
		ApplicationInstallListener listener;

		public ApplicationDownloadListener(Application application,
				ApplicationInstallListener listener) {
			this.application = application;
			if (listener == null) {
				this.listener = new ApplicationInstallListener() {
					@Override
					public void onFinish() {}
					@Override
					public void onError() {}
				};
			} else {
				this.listener = listener;
			}
		}
		
		class InstallTask implements Runnable {
			private String path;
			public InstallTask(String path) {
				this.path = path;
			}

			@Override
			public void run() {
				logger.info("start install application [" + application.getName() + "]");

				try {
					PackageUtils.installSilent(context, path);
					Thread.sleep(2000);
				} catch (Exception e) {
					logger.error("install application [" + application.getName() + "] failed with error:" + e.getMessage());
					listener.onError();
					new File(path).delete();
					return;
				}

				if (!updateApplication(application)) {
					InstalledApplication installedApplication = new InstalledApplication(application);
					int retry = 0;
					do {
						if (findandSetIcon(installedApplication)) {
							installedApplicationList.add(installedApplication);
							mDBManager.add(application);
							logger.info("install application [" + application.getName() + "] success!");
							listener.onFinish();
							new File(path).delete();
							return;
						}
						try {
							logger.warn("try to find just installed application [" + application.getName() + "] failed. retry");
							Thread.sleep(2000);
						} catch (InterruptedException e) {}
					} while(retry++ < 30);

					logger.error("can't find just installed application [" + installedApplication.getName() + "], the installed failed.");
					new File(path).delete();
					listener.onError();
				}
			}
		}

		@Override
		public void OnFinish(boolean isSuccess, final String savePath, Object... params) {
			if (!isSuccess) {
				logger.error("download application file[" + application.getName() + "] failed");
				listener.onError();
				return;
			}

			logger.info("end download application [" + application.getName() + "]");
			
			if (application.getType().equals("MainAPP")) {
				Intent intent = context.getPackageManager().getLaunchIntentForPackage(UPGRADE_APP);
				intent.putExtra("APKPATH", savePath);
				intent.putExtra("VERSION", application.getVersion());
				intent.putExtra("PACKNAME", "com.fairlink.passenger");
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			} else {
				new Thread(new InstallTask(savePath), "install " + application.getName()).start();
			}
		}
	}

	private ArrayList<InstalledApplication> getSystemInstalledApplications() {
		PackageManager pm = context.getPackageManager();
	    ArrayList<InstalledApplication> res = new ArrayList<InstalledApplication>();
	    List<PackageInfo> packs = pm.getInstalledPackages(0);
	    for(PackageInfo p: packs) {
	    	InstalledApplication newInfo = new InstalledApplication();
	        if((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0)
	        	continue;

	        newInfo.setName(p.applicationInfo.loadLabel(pm).toString());
	        newInfo.setComponentName(p.packageName);
	        newInfo.setVersion(p.versionName);
	        newInfo.setIcon(p.applicationInfo.loadIcon(pm));
	        res.add(newInfo);
	    }
	    return res; 
	}
	
	private void initializeInstalledApplicationList() {
		installedApplicationList = new ArrayList<InstalledApplication>();

		List<InstalledApplication> systemInstalledApplications = getSystemInstalledApplications();
		List<Application> savedApplications = mDBManager.queryApplications();

		for (InstalledApplication systemInstalledApplication : systemInstalledApplications) {
			for (Application savedApplication : savedApplications) {
				if (savedApplication.getComponentName().equals(systemInstalledApplication.getComponentName())) {
					if (!savedApplication.getName().equals(systemInstalledApplication.getName())) {
						logger.warn("installed application name [" + systemInstalledApplication.getName()
								+ "] is different from saved application name [" + savedApplication.getName() + "]");
					}

					if (!savedApplication.getVersion().equals(systemInstalledApplication.getVersion())) {
						logger.warn("installed application version [" + systemInstalledApplication.getVersion()
								+ "] is different from saved application version [" + savedApplication.getVersion() + "]");
					}

					systemInstalledApplication.setId(savedApplication.getId());
					systemInstalledApplication.setName(savedApplication.getName());
					systemInstalledApplication.setCategory(savedApplication.getCategory());
					systemInstalledApplication.setType(savedApplication.getType());
					systemInstalledApplication.setDescription(savedApplication.getDescription());
					systemInstalledApplication.setDeveloper(savedApplication.getDeveloper());
					systemInstalledApplication.setIsUsing(savedApplication.getIsUsing());
					systemInstalledApplication.setOrigin(savedApplication.getOrigin());
					installedApplicationList.add(systemInstalledApplication);
					break;
				}
			}
		}
	}
	
	private boolean findandSetIcon(InstalledApplication installedApplication) {
		List<InstalledApplication> systemInstalledApplications = getSystemInstalledApplications();
		for (InstalledApplication systemInstalledApplication : systemInstalledApplications) {
			if (installedApplication.getComponentName().equals(systemInstalledApplication.getComponentName())) {
				if (!installedApplication.getName().equals(systemInstalledApplication.getName())) {
					logger.warn("installed application name [" + systemInstalledApplication.getName()
							+ "] is different from saved application name [" + installedApplication.getName() + "]");
				}

				if (!installedApplication.getVersion().equals(systemInstalledApplication.getVersion())) {
					logger.warn("installed application version [" + systemInstalledApplication.getVersion()
							+ "] is different from saved application version [" + installedApplication.getVersion() + "]");
				}

				installedApplication.setIcon(systemInstalledApplication.getIcon());
				return true;
			}
		}

		return false;
	}

	public String getMainApplicationVersion() {
		return PackageUtils.getAppVersionCode(context);
	}
}
