package com.fairlink.passenger.admin;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;

public class ThirdPartyAppLauncher extends BaseActivity implements Runnable ,OnItemClickListener {

	private List<Map<String, Object>> list = null;
	private ListView softlist = null;
	private ProgressDialog pd;
	private Context mContext;
	private PackageManager mPackageManager;
	private List<ResolveInfo> mAllApps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_party_app_list);

		setTitle("Third Party Apps");

		mContext = this;
		mPackageManager = getPackageManager();

		softlist = (ListView) findViewById(R.id.softlist);

		pd = ProgressDialog.show(this, "Waiting...", "Information collection", true, false);
		Thread thread = new Thread(this);
		thread.start();

	}


	
	private void bindMsg(){
    	Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);
    	softlist.setAdapter(new MyAdapter(mContext, mAllApps));
    	softlist.setOnItemClickListener(this);
        Collections.sort(mAllApps, new ResolveInfo.DisplayNameComparator(mPackageManager));
        
	}
	
	@Override
	public void run() {
		bindMsg();
	    handler.sendEmptyMessage(0);

	}
	private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            pd.dismiss();
        }
    };
    
    class MyAdapter extends BaseAdapter{

    	private List<ResolveInfo> resInfo;
    	private ResolveInfo res;
		private LayoutInflater infater=null;   
    	
    	public MyAdapter(Context context, List<ResolveInfo> resInfo) {			
			this.resInfo = resInfo;
			 infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		}

		@Override
		public int getCount() {
			
			return resInfo.size();
		}

		@Override
		public Object getItem(int arg0) {
			
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
		//	View view = null;  
	        ViewHolder holder = null;  
	        if (convertView == null || convertView.getTag() == null) {  
	        	convertView = infater.inflate(R.layout.soft_row, null);  
	            holder = new ViewHolder(convertView);  
	            convertView.setTag(holder);  
	        }   
	        else{  
	       //     view = convertView ;  
	            holder = (ViewHolder) convertView.getTag() ;  
	        }  
	        res = resInfo.get(position);
	        holder.appIcon.setImageDrawable(res.loadIcon(mPackageManager));  
	        holder.tvAppLabel.setText(res.loadLabel(mPackageManager).toString());
	        holder.tvPkgName.setText(res.activityInfo.packageName+'\n'+res.activityInfo.name);
			return convertView;
			
		}
    }
    class ViewHolder {  
        ImageView appIcon;  
        TextView tvAppLabel;  
        TextView tvPkgName;  
  
        public ViewHolder(View view) {  
            this.appIcon = (ImageView) view.findViewById(R.id.img);  
            this.tvAppLabel = (TextView) view.findViewById(R.id.name);  
            this.tvPkgName = (TextView) view.findViewById(R.id.desc);  
        }  
    }  
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
		ResolveInfo res = mAllApps.get(position);
		String pkg = res.activityInfo.packageName;
		String cls = res.activityInfo.name;
		
		ComponentName componet = new ComponentName(pkg, cls);
		
		Intent i = new Intent();
		i.setComponent(componet);
		startActivity(i);
		
	}
	
}
