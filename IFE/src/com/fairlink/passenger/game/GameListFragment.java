package com.fairlink.passenger.game;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fairlink.passenger.BaseFragment;
import com.fairlink.passenger.R;
import com.fairlink.passenger.application.Application;
import com.fairlink.passenger.application.ApplicationManager;
import com.fairlink.passenger.application.InstalledApplication;

public class GameListFragment extends BaseFragment {

	private GridView gameGrid;
	private GameItemAdapter gameAdapter;
	private List<InstalledApplication> mGameList = new ArrayList<InstalledApplication>();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.game_list_fragment, null);

		final Context context = inflater.getContext();
		initGameList(context);

		gameGrid = (GridView)view.findViewById(R.id.game_grid);
		gameAdapter = new GameItemAdapter(context);
		gameGrid.setAdapter(gameAdapter);
		gameGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int index, long arg3) {
					Application gameInfo = mGameList.get(index);
					Intent i = new Intent();
					i = context.getPackageManager().getLaunchIntentForPackage(
									gameInfo.getComponentName());
					startActivity(i);
				}
		});

		return view;
	}

	private void initGameList(Context context) {
		List<InstalledApplication> applicaitonList = ApplicationManager.getInstence().applicationList();
		if(applicaitonList == null) {
			return;
		}
		
		for (InstalledApplication application : applicaitonList) {
			if (application.getType().equals("game") && application.getIsUsing().equals(1))
				mGameList.add(application);
		}
	
	}

	class GameItem {
		ImageView img;
		TextView gameTextName;
		TextView gameTextTime;
	}
	
	class GameItemAdapter extends BaseAdapter {
		private Context mContext;

		GameItemAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return mGameList == null? 0:mGameList.size();
		}

		@Override
		public Object getItem(int position) {
			return mGameList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			InstalledApplication installedGame = (InstalledApplication)getItem(position);
			GameItem gameItem;

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.game_item, null);
				gameItem = new GameItem();
				gameItem.img = (ImageView)convertView.findViewById(R.id.game_icon);
				gameItem.gameTextName = (TextView)convertView.findViewById(R.id.game_txt_name);
				gameItem.img.setImageDrawable(installedGame.getIcon());
				gameItem.gameTextName.setText(installedGame.getName());
				gameItem.gameTextTime = (TextView) convertView.findViewById(R.id.game_txt_time);
				convertView.setTag(gameItem);
			} else {
				gameItem = (GameItem) convertView.getTag();
			}

			return convertView;
		}

	}

}
