package com.apkdv.tour.view;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.apkdv.tour.model.MyApplication;

public class BaseActivity extends AbActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.activities.add(this);
	}

}
