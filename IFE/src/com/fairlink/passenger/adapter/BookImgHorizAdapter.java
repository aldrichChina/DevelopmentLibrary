package com.fairlink.passenger.adapter;

import java.util.ArrayList;

import com.fairlink.passenger.R;
import com.fairlink.passenger.networkrequest.BookDetailRequest.BookDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BookImgHorizAdapter extends BaseAdapter{

	private 	Context 				context;
	private 	ArrayList<BookDetail> 	list;
	DisplayImageOptions options;// jar包声明配置
	
	private BookImgHorizAdapter(Context ctx, ArrayList<BookDetail> lst)
	{
		context = ctx;
		list = lst;
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.bg_default).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 :list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	static class ViewHolder {
		ImageView photoView;
		ProgressBar spinner;
	}
	ViewHolder holder;
	private void setItemControl(View convertView)
	{
		holder = new ViewHolder();
		holder.photoView = (ImageView) convertView.findViewById(R.id.image);
		holder.spinner = (ProgressBar) convertView.findViewById(R.id.loading);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.book_detail_list_item, null);
			setItemControl(convertView);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		String imgUrl = list.get(position).bookPicsPath;
		ImageLoader.getInstance().displayImage(imgUrl, holder.photoView, options, new ImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}

			@Override
			public void onLoadingComplete(String arg0, View arg1,
					Bitmap arg2) {
				holder.spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingFailed(String arg0, View arg1,
					FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
				case IO_ERROR:
					message = "Input/Output error";
					break;
				case DECODING_ERROR:
					message = "Image can't be decoded";
					break;
				case NETWORK_DENIED:
					message = "Downloads are denied";
					break;
				case OUT_OF_MEMORY:
					message = "Out Of Memory error";
					break;
				case UNKNOWN:
					message = "Unknown error";
					break;
				}
				holder.spinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				holder.spinner.setVisibility(View.VISIBLE);
			}
			
		},new ImageLoadingProgressListener(){
			 @Override  
		      public void onProgressUpdate(String imageUri, View view, int current,int total) {     
		      } 
		});
		
		return convertView;
	}
	
	
}
