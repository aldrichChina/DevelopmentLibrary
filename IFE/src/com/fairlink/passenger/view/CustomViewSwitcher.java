package com.fairlink.passenger.view;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ViewSwitcher;

import com.fairlink.passenger.LaunchModuleListener;
import com.fairlink.passenger.R;
import com.fairlink.passenger.bean.DynamicType;
import com.fairlink.passenger.networkrequest.PhotoManager;

public class CustomViewSwitcher {

	private ImageSwitcher imageSwitcher;
	private List<DynamicType> types;
	private int currentIndex = 0;
	private boolean isBig = false;
	private LaunchModuleListener listener;

	public CustomViewSwitcher(final Fragment parent, ImageSwitcher imageSwitcher) {
		this.imageSwitcher = imageSwitcher;

		ViewSwitcher.ViewFactory viewFactory = new ViewSwitcher.ViewFactory() {

			@Override
			public View makeView() {
				ImageView i = new ImageView(parent.getActivity());
				i.setBackgroundColor(0x00000000);
				i.setScaleType(ImageView.ScaleType.FIT_XY);
				i.setLayoutParams(new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT,
						ImageSwitcher.LayoutParams.MATCH_PARENT));
				i.setBackground(parent.getActivity().getResources().getDrawable(R.drawable.movie_forground));
				return i;
			}

		};

		imageSwitcher.setFactory(viewFactory);
		Animation animation = AnimationUtils.loadAnimation(parent.getActivity(), android.R.anim.fade_in);
		animation.setDuration(1000);
		imageSwitcher.setInAnimation(animation);

		animation = AnimationUtils.loadAnimation(parent.getActivity(), android.R.anim.fade_out);
		animation.setDuration(1000);
		imageSwitcher.setOutAnimation(animation);

		listener = (LaunchModuleListener) parent;

		imageSwitcher.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (types == null || types.isEmpty())
					return;

				listener.launchModule(types.get(currentIndex));
			}
		});
	}

	public void setImageUrl(String url) {
		final ImageView image = (ImageView) imageSwitcher.getNextView();
		image.setBackgroundResource(R.color.transparent_20_white);
		String imagePath = PhotoManager.getInstance().getImageFile(url);
		if (imagePath == null) {
			image.setScaleType(ScaleType.FIT_CENTER);
			if (isBig) {
				image.setImageResource(R.drawable.default_400);
			} else {
				image.setImageResource(R.drawable.default_200);
			}
			PhotoManager.getInstance().downloadImage(url, new PhotoManager.PhotoDownloadCallback() {
				
				@Override
				public void onPhotoDownloadError(String url, String path) {
				}
				
				@Override
				public void onPhotoDownload(String url, String path) {
					Bitmap bmp = PhotoManager.getInstance().decodePhoto(path, 600, 600);
					if (bmp != null) {
						image.setScaleType(ScaleType.FIT_XY);
						image.setImageBitmap(bmp);
					} else {
						image.setScaleType(ScaleType.FIT_CENTER);
						if (isBig) {
							image.setImageResource(R.drawable.default_400);
						} else {
							image.setImageResource(R.drawable.default_200);
						}
					}
				}
			});
		} else {
			Bitmap bmp = PhotoManager.getInstance().decodePhoto(imagePath, 600, 600);
			if (bmp != null) {
				image.setScaleType(ScaleType.FIT_XY);
				image.setImageBitmap(bmp);
			} else {
				image.setScaleType(ScaleType.FIT_CENTER);
				if (isBig) {
					image.setImageResource(R.drawable.default_400);
				} else {
					image.setImageResource(R.drawable.default_200);
				}
			}
			
		}
		
		imageSwitcher.showNext();
	}

	public void setTypes(List<DynamicType> types, boolean isBig) {
		this.isBig = isBig;
		this.types = types;

		if (types == null || types.size() == 0)
			return;

		setImageUrl(types.get(0).getPicture());
	}

	public void showNext() {
		if (types == null || types.isEmpty())
			return;

		currentIndex = (++currentIndex) % types.size();
		setImageUrl(types.get(currentIndex).getPicture());
	}
}
