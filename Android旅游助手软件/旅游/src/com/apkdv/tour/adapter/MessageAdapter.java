package com.apkdv.tour.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apkdv.tour.entity.Message;
import com.apldv.tour.R;


public class MessageAdapter extends BaseAdapter {
	public ArrayList<Message> messages = null;
	public Context context = null;

	public MessageAdapter(ArrayList<Message> messages, Context context) {
		this.setArray(messages);
		this.context = context;
	}

	private void setArray(ArrayList<Message> messages) {
		if (!messages.isEmpty() && messages != null) {
			this.messages = messages;
		} else {
			this.messages = new ArrayList<Message>();
		}
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int arg0) {
		return messages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHold viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHold();
			convertView = View.inflate(context, R.layout.message_item, null);
			viewHolder.tvMessage = (TextView) convertView
					.findViewById(R.id.tv_choose_class_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHold) convertView.getTag();
		}
		Message message = messages.get(position);
		
		viewHolder.tvMessage.setText(message.getMessage());
		return convertView;
	}

	class ViewHold {
		public TextView tvMessage;
	}
}
