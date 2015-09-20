package com.fairlink.passenger.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.util.ComUtil;

public class AboutDetailFragment extends BaseFragment {
	private ImageView mLeftImg;
	private ImageView mRightImg;
	private TextView  mAboutDetailContent;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_detail, null);
		mLeftImg   = (ImageView) view.findViewById(R.id.about_left_img);
		mRightImg  = (ImageView) view.findViewById(R.id.about_right_img);
		mAboutDetailContent = (TextView)  view.findViewById(R.id.about_detail_content);
		
		mLeftImg.setImageResource(R.drawable.about_profile2);
		mRightImg.setImageResource(R.drawable.about_profile1);
		mAboutDetailContent.setText(ComUtil.getFromAssets(getActivity(),"about_profile.txt"));
		
		return view;
	}
	

	public void setImage(int resId1,int resId2)
	{
		mLeftImg.setImageResource(resId1);
		mRightImg.setImageResource(resId2);
	}
	
	public void setText(CharSequence text)
	{
		mAboutDetailContent.setText(text);
	}
	
}
