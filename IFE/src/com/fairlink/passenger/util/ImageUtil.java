package com.fairlink.passenger.util;

import java.util.Map;

import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.PhotoManager;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

public class ImageUtil {
	
	public static final short SMALL = 1;
	public static final short MID = 2;
	public static final short BIG = 3;
	
	//默认为最小模式
	public static int imageSize = 200;
	public static int imageId = R.drawable.default_200;
	
	public static class ImageViewHolder {
		public ImageView imageView;
	}
	
	static Map<ImageView, ImageViewHolder> map;
	
	public static void setImage(String imgUrl, short decodeImageSize, ImageView img,
			ImageViewHolder imageHolder, ProgressBar spinner) {
		String imgPath = PhotoManager.getInstance().getImageFile(imgUrl);
		final ImageViewHolder holder = imageHolder;
		final ImageView image = img;
		final ProgressBar progressBar = spinner;
		
		switch (decodeImageSize) {
		case SMALL:
			imageId = R.drawable.default_200;
			imageSize = 200;
		case MID:
			imageId = R.drawable.default_400;
			imageSize = 400;
		case BIG:
			imageId = R.drawable.default_800;
		    imageSize = 800;
		}
		if (imgPath == null) {
			image.setScaleType(ScaleType.FIT_CENTER);
			image.setImageResource(imageId);
			if (progressBar != null) {
				progressBar.setVisibility(View.VISIBLE);
			}
			PhotoManager.getInstance().downloadImage(imgUrl, new PhotoManager.PhotoDownloadCallback() {
				
				@Override
				public void onPhotoDownloadError(String url, String path) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onPhotoDownload(String url, String path) {
					if(holder == null || holder.imageView != null){
						Bitmap bmp = PhotoManager.getInstance().decodePhoto(path, imageSize, imageSize);
						if (bmp == null) {
							image.setScaleType(ScaleType.FIT_CENTER);
							image.setImageResource(imageId);
							if (progressBar != null) {
								progressBar.setVisibility(View.VISIBLE);
							}
						} else {
							image.setScaleType(ScaleType.FIT_XY);
							image.setImageBitmap(bmp);
							if (progressBar != null) {
								progressBar.setVisibility(View.GONE);
							}
						}
						
					}
				}
			});
			
		} else {
			Bitmap bmp = PhotoManager.getInstance().decodePhoto(imgPath, imageSize, imageSize);
			if (bmp == null) {
				image.setScaleType(ScaleType.FIT_CENTER);
				image.setImageResource(imageId);
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
			} else {
				image.setScaleType(ScaleType.FIT_XY);
				image.setImageBitmap(bmp);
				if (progressBar != null) {
					progressBar.setVisibility(View.GONE);
				}
			}
		}
	}
}
