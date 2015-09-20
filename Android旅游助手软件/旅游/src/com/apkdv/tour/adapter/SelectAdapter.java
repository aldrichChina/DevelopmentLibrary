package com.apkdv.tour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkdv.tour.entity.Suites;
import com.apldv.tour.R;

public class SelectAdapter extends BaseAdapter {
	private ArrayList<Suites>list ;
	private Context context;
	public SelectAdapter(Context context,ArrayList<Suites> sMusics) {
		this.setArray(sMusics);
		this.context = context;
	}
	
	public void changeArray(ArrayList<Suites> hospitals){
		this.setArray(hospitals);
		this.notifyDataSetChanged();
	}
	
	private void setArray(ArrayList<Suites> hospitals){
		if (hospitals!=null) {
			this.list = hospitals;
		}else{
			this.list = new ArrayList<Suites>();
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context, R.layout.shop_item, null);
			viewHolder.tvName = (TextView)convertView.findViewById(R.id.tv_item_name);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.image_item_select);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		//…Ë÷√ ˝æ›
		Suites music = list.get(position);
		viewHolder.tvName.setText(music.getName());
		viewHolder.imageView.setImageResource(music.getPidId());
		return convertView;
	}
	class ViewHolder{
		public TextView tvName;
		public ImageView imageView;
	}

}
