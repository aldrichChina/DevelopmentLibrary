package com.fairlink.passenger.admin;


import android.R;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.PackageUtils;

public class DevSettingActivity extends ListActivity {

    private String apkPath = "/sdcard/update/IFE.apk";
    private String uninstallApkPkg = "package:com.fairlink.passenger";

    private Boolean isMyHome = true;
    private Boolean isControlBar = true;
    private Boolean isControlNBar = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				new String[] { "Upgrade", "Third Party App Launcher", "Unlock Home", "Unlock Statusbar", "Show Navigationbar", /*"Install App", "Unstall App",*/ "Clean Recent Task", }));
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String i = (String)l.getItemAtPosition(position);

        if(i.equals("Upgrade")){
        	ComUtil.toastText(this, "Upgrade", Toast.LENGTH_LONG);
			PackageUtils.installSilent(this, apkPath);
        }

        if(i.equals("Third Party App Launcher")){
			startActivity(new Intent(this, ThirdPartyAppLauncher.class));
        }

        if(i.equals("Unlock Home")){
            if (!isMyHome) {
                PackageUtils.launchAsHome(this, true);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Unlock Home");
                isMyHome = true;
            } else {
                PackageUtils.launchAsHome(this, false);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Lock Home");
                isMyHome = false;
            }
        }

        if(i.equals("Unlock Statusbar")){
            if (!isControlBar) {
                PackageUtils.lockStatusbar(this, true);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Unlock Statusbar");
                isControlBar = true;
            } else {
                PackageUtils.lockStatusbar(this, false);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Lock Statusbar");
                isControlBar = false;
            }
        }

        if(i.equals("Show Navigationbar")){
            if (!isControlNBar) {
                PackageUtils.hideNavigationbar(this, true);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Show Navigationbar");
                isControlNBar = true;
            } else {
                PackageUtils.hideNavigationbar(this, false);
                TextView text = (TextView)v.findViewById(R.id.text1);
                text.setText("Hide Navigationbar");
                isControlNBar = false;
            }
        }

        if(i.equals("Install App")){
            PackageUtils.installSilent(this, apkPath);
        }
        
        if(i.equals("Unstall App")){
            PackageUtils.installSilent(this, uninstallApkPkg);
        }
        
        if(i.equals("Clean Recent Task")){
            Intent cleanTasks = new Intent();
            cleanTasks.setAction("customer.clean.tasks");
            cleanTasks.putExtra("mtaskid", getTaskId());
            sendBroadcast(cleanTasks);
        }
        
    }
	
	
}
