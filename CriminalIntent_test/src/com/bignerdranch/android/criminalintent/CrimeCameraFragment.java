package com.bignerdranch.android.criminalintent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author Administrator 初始相机fragment类
 */
public class CrimeCameraFragment extends Fragment {
	public static final String EXTRA_PHOTO_ORIENTATION="com.bignerdranch.android.criminalintent.photo_orientation";
	
	private static final String TAG = "CrimeCameraFragment";
	//新增照片文件名extra
	public static final String EXTRA_PHOTO_FILENAME="com.bignerdranch.test.criminalintent.photo_filename";
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private View mProgressContainer;
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {

		public void onShutter() {
			// Display the progress indicator
			mProgressContainer.setVisibility(View.VISIBLE);
		}
	};
	private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {

		public void onPictureTaken(byte[] data, Camera camera) {
			// 创建一个文件名
			String filename = UUID.randomUUID().toString() + ".jpg";
			// 保存数据
			FileOutputStream os = null;
			boolean success = true;
			try {
				os = getActivity().openFileOutput(filename,Context.MODE_PRIVATE);
			} catch (Exception e) {
				Log.e("jia", "Error writing to file" + filename, e);
				success = false;
			} finally {
				try {
					if (os != null)
						os.close();
				} catch (IOException e) {
					Log.e("jia", "Error closing file" + filename, e);
					success = false;
				}
			}
			if (success) {
				Log.i("jia", "JPEG save at" + filename);
				//Set the photo filename on the result intent
				if(success){
					Intent i=new Intent();
					i.putExtra(EXTRA_PHOTO_FILENAME, filename);
					i.putExtra(EXTRA_PHOTO_ORIENTATION, getActivity().getResources().getConfiguration().orientation);
					
					getActivity().setResult(Activity.RESULT_OK,i);
				}else{
					getActivity().setResult(Activity.RESULT_CANCELED);
				}
			}
			getActivity().finish();
		}
	};

	@Override
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime_camera, container,
				false);
		// 配置使用FrameLayout视图
		mProgressContainer = v
				.findViewById(R.id.crime_camera_progressContainer);
		mProgressContainer.setVisibility(View.INVISIBLE);//
		Button takePictureButton = (Button) v
				.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// getActivity().finish();
				// 实现takePicture()按钮单击事件方法
				if (mCamera != null) {
					mCamera.takePicture(mShutterCallback, null, mJpegCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView) v
				.findViewById(R.id.crime_camera_surfaceView);
		// 获取surfaceholder实例
		SurfaceHolder holder = mSurfaceView.getHolder();
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		// 实现SurfaceHolder.Callback接口
		holder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				if (mCamera != null) {
					mCamera.stopPreview();
				}
			}

			public void surfaceCreated(SurfaceHolder holder) {
				try {
					if (mCamera != null) {
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					Log.d("jia", "Error setting up preview display", e);
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				if (mCamera == null)
					return;
				Camera.Parameters parameters = mCamera.getParameters();
				// Size s = null;
				// 调用getBestSupportedSize()方法
				Size s = getBestSupportedSize(
						parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPreviewSize(s.width, s.height);
				// 调用getBestSupportedSize()方法设置图片尺寸
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(),
						width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				if(getActivity().getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
					mCamera.setDisplayOrientation(90);
				}
				try {
					mCamera.startPreview();
				} catch (Exception e) {
					Log.d("jia", "Could not start preview", e);
					mCamera.release();
					mCamera = null;
				}
			}
		});
		return v;
	}

	// 在onResume()方法中打开相机
	@TargetApi(9)
	@Override
	public void onResume() {

		super.onResume();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			mCamera = Camera.open(0);
		} else {
			mCamera = Camera.open();
		}
	}

	// 实现生命周期方法onPause()
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

	// 找出设备支持的最佳尺寸
	private Size getBestSupportedSize(List<Size> sizes, int width, int height) {
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for (Size s : sizes) {
			int area = s.width * s.height;
			if (area > largestArea) {
				bestSize = s;
				largestArea = area;
			}
		}

		return bestSize;

	}
}
