package com.fairlink.passenger.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fairlink.passenger.R;
import com.fairlink.passenger.shop.GoodsCategoryItem;

/**
 * @ClassName ： ShopCategoryAdapter
 * @Description: 左侧栏 list 适配器
 */

public class ShopCategoryAdapter extends BaseExpandableListAdapter {
	private Context mContext = null;

	private List<GoodsCategoryItem> groupList;

	public static interface ShopCategorySelectedListener {
		public void onCategorySelected(String name);
	}

	public ShopCategoryAdapter(Context mContext, List<GoodsCategoryItem> list) {
		this.mContext = mContext;
		groupList = list;

	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public int getGroupCount() {

		return (groupList == null) ? 0 : groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupList.get(groupPosition).goodsType == null ? 0 : groupList.get(groupPosition).goodsType.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupList.get(groupPosition).goodsType.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		TextView text = null;
		if (convertView == null) {
			text = new TextView(mContext);
		} else {
			text = (TextView) convertView;
		}
		String name = groupList.get(groupPosition).name;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60);
		text.setLayoutParams(lp);
		text.setTextSize(18);
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		text.setSingleLine();
		text.setEllipsize(TruncateAt.END);
		text.setPadding(20, 0, 0, 0);
		text.setBackground(mContext.getResources().getDrawable(R.drawable.shop_text_selector));
	//	text.setTextColor(R.drawable.shop_text_selector);
		text.setText(name);
		return text;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		Button text = null;
		if (convertView == null) {
			text = new Button(mContext);
		} else {
			text = (Button) convertView;
		}
		String name = groupList.get(groupPosition).goodsType.get(childPosition).getGoodsTypeName();
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
		text.setLayoutParams(lp);
		text.setTextSize(15);
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		text.setPadding(45, 0, 0, 0);
//		text.setBackground(mContext.getResources().getDrawable(R.drawable.shop_text_selector));
		text.setBackground(mContext.getResources().getDrawable(R.drawable.btn_buy_disable_selector));
	//	text.setTextColor(R.drawable.shop_text_selector);
		text.setText(name);

		return text;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
	}

}
