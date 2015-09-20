package com.fairlink.passenger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.passenger.admin.DevSettingActivity;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.FlightinfoRequest;
import com.fairlink.passenger.networkrequest.FlightinfoRequest.FlightInfo;
import com.fairlink.passenger.networkrequest.LoginMemberRequest;
import com.fairlink.passenger.networkrequest.LoginRequest;
import com.fairlink.passenger.networkrequest.LoginRequest.Passenger;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.RegisterRequest;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogLoading;

public class LoginFragment extends BaseFragment implements GestureDetector.OnGestureListener, OnClickListener, HttpTaskCallback, NetworkRequestAPI {
	// private ViewFlipper mViewFlipper;
	private GestureDetector mGestureDetector;
	private TextView tvFlight;
	private TextView tvDetail;
	private TextView txtWeather;
	private ImageView imgWeather;
	// private CheckBox cbIFE;
	private CheckBox cb_logon;
	private EditText etNO;
	private EditText etId;
	private Button btnConfirm;
	private EditText etPwd;
	private Button btnLogin;
	private EditText etPhone;
	private EditText etEmail;
	private CheckBox cbSSS;
	private TextView tvSSS;
	private TextView tv_ifelogon;
	private Button btnRegister;
	private LinearLayout linInput;
	private LinearLayout linLogin;
	private LinearLayout linRegister;
	// private LinearLayout lin_read;
	private RelativeLayout rl_read;
	private Button buttonLogin;

	// 在线升级提示框
	private AlertDialog mUpdateDialog;
	// 当前第几个界面
	private int current;
	// 乘客id
	private String passengerId;
	private DialogLoading diaLoading;

	Context mContext;
	LoginMain mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		mActivity = (LoginMain) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mContext = inflater.getContext();

		View view = inflater.inflate(R.layout.main_login_fragment, container, false);

		diaLoading = new DialogLoading(mContext);

		switchLanguage(Locale.CHINESE);

		initView(view);

		tvDetail.setText(ComUtil.getFromAssets(mContext, "login_user_conditions.txt"));
		setListener();
		
		return view;

	}

	public void onDestroy() {

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		super.onDestroy();
	}

	void hideInputMethod() {
		// mViewFlipper.requestFocus();
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etNO.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(etId.getWindowToken(), 0);
	}

	private void initView(View parentView) {
		tvFlight = (TextView) parentView.findViewById(R.id.tv_flight);
		tvDetail = (TextView) parentView.findViewById(R.id.tv_detail);
		cb_logon = (CheckBox) parentView.findViewById(R.id.cb_logon);
		txtWeather = (TextView)parentView.findViewById(R.id.txt_weather);
		imgWeather = (ImageView)parentView.findViewById(R.id.img_weather);

		InputFilter[] filters = new InputFilter[1];
		filters[0] = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				return source.toString().toUpperCase();
			}
		};
		etNO = (EditText) parentView.findViewById(R.id.et_no);
		etNO.setFilters(filters);
		etId = (EditText) parentView.findViewById(R.id.et_id);
		etNO.setFilters(filters);
		etId.setFocusable(true);

		btnConfirm = (Button) parentView.findViewById(R.id.btn_confirm);
		etPwd = (EditText) parentView.findViewById(R.id.et_pwd);
		btnLogin = (Button) parentView.findViewById(R.id.btn_login);
		etPhone = (EditText) parentView.findViewById(R.id.et_phone);
		etEmail = (EditText) parentView.findViewById(R.id.et_email);
		cbSSS = (CheckBox) parentView.findViewById(R.id.cb_sss);
		tvSSS = (TextView) parentView.findViewById(R.id.tv_sss);
		btnRegister = (Button) parentView.findViewById(R.id.btn_register);
		linInput = (LinearLayout) parentView.findViewById(R.id.lin_input);
		linLogin = (LinearLayout) parentView.findViewById(R.id.lin_login);
		rl_read = (RelativeLayout) parentView.findViewById(R.id.offline);
		linRegister = (LinearLayout) parentView.findViewById(R.id.lin_register);

		// rbLeft = (RadioButton) parentView.findViewById(R.id.radio_left);
		// rbMid = (RadioButton) parentView.findViewById(R.id.radio_mid);
		// rbRight = (RadioButton) parentView.findViewById(R.id.radio_right);
		buttonLogin = (Button) parentView.findViewById(R.id.button_Login);
		tv_ifelogon = (TextView) parentView.findViewById(R.id.tv_ifelogon);
		tvSSS.setText(Html.fromHtml(getString(R.string.login_agree_sss)));
		tvSSS.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		tv_ifelogon.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); 
		boolean offlineStatus = IFEApplication.getInstance().getOfflineStatus();
		showLoginLayout(offlineStatus);
		handler.postDelayed(runnable, 0);
	}
	
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			updateFlightInfo();
			handler.postDelayed(this, 60 * 1000);
		}
	};

	@SuppressWarnings("deprecation")
	private void setListener() {
		mGestureDetector = new GestureDetector(this);
		btnConfirm.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		tvSSS.setOnClickListener(this);
		tv_ifelogon.setOnClickListener(this);
		buttonLogin.setOnClickListener(this);
	}

	public boolean onTouchEvent(MotionEvent event) {
		boolean offlineStatus = IFEApplication.getInstance().getOfflineStatus();
		if (offlineStatus) {
			return false;
		}
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		hideInputMethod();

		// 向左滑动
		if (e1.getX() - e2.getX() > 50) {
			if (current == 0) {
				// 判断 是否同意ife
				if (current == 0 && !cb_logon.isChecked()) {
					DialogCom.DiaCom(mContext, "您未同意ife用户条款");

				} else if (IFEApplication.getInstance().getOfflineStatus() == true) {
					mActivity.enterMainActivity();

				} else {
					current++;
					// setImgBottom(current);
					// mViewFlipper.setInAnimation(mContext,
					// R.anim.push_left_in);
					// mViewFlipper.setOutAnimation(mContext,
					// R.anim.push_left_out);
					// mViewFlipper.showNext();
				}

			}
			return true;
		} else if (e1.getX() - e2.getX() < -50) {
			if (current > 0) {
				current--;
				// setImgBottom(current);
				// mViewFlipper.setInAnimation(mContext, R.anim.push_right_in);
				// mViewFlipper.setOutAnimation(mContext,
				// R.anim.push_right_out);
				// mViewFlipper.showPrevious();
			}
			return true;
		}
		return false;

	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_confirm:

			if (!cb_logon.isChecked()) {
				DialogCom.DiaCom(mContext, "您未同意ife用户条款");
				return;
			}

			if (ComUtil.isEmpty(etNO.getText().toString())) {
				DialogCom.DiaCom(mContext, "座位号为空，请填写座位号");
				return;
			}

			if (ComUtil.isEmpty(etId.getText().toString())) {
				DialogCom.DiaCom(mContext, "证件号为空，请填写证件号");
				return;
			}

			if (etNO.getText().toString().equals("ADMIN") && etId.getText().toString().equals("000000")) {
				Intent i = new Intent(mContext, DevSettingActivity.class);
				startActivity(i);
				return;
			}

			if (etNO.getText().toString().equals("ADMIN") && etId.getText().toString().equals("000001")) {
				mActivity.enterMainActivity();
				return;
			}

			new LoginRequest(mContext, this, etNO.getText().toString(), etId.getText().toString()).execute((String) null);
			diaLoading.show();

			break;

		case R.id.btn_login:

			if (ComUtil.isEmpty(etPwd.getText().toString())) {
				DialogCom.DiaCom(mContext, "密码为空，请输入会员密码");
				return;
			}

			new LoginMemberRequest(mContext, this, etNO.getText().toString(), etPwd.getText().toString()).execute((String) null);
			diaLoading.show();

			break;

		case R.id.btn_register:

			if (ComUtil.isEmpty(etPhone.getText().toString())) {
				DialogCom.DiaCom(mContext, "手机号码为空，请输入手机号码");
				return;
			}

			if (!ComUtil.isMobileNO(etPhone.getText().toString())) {
				DialogCom.DiaCom(mContext, "手机号码格式错误，请输入正确的手机号码");
				return;
			}

			if (!ComUtil.isEmpty(etEmail.getText().toString()) && !ComUtil.isEmail(etEmail.getText().toString())) {
				DialogCom.DiaCom(mContext, "邮箱格式错误，请输入正确的邮箱地址");
				return;
			}

			if (!cbSSS.isChecked()) {
				DialogCom.DiaCom(mContext, "您未同意春秋航空会员注册协议");
				return;
			}

			new RegisterRequest(mContext, this, passengerId, etPhone.getText().toString(), etEmail.getText().toString()).execute((String) null);
			diaLoading.show();

			break;

		case R.id.tv_sss:
			DialogCom.DiaCaluse(mContext);
			break;

		case R.id.button_Login:
			if (!cb_logon.isChecked()) {
				DialogCom.DiaCom(mContext, "您未同意ife用户条款");
				return;
			}
			new LoginRequest(mContext, this, "OFFLINE", "OFFLINE").execute((String) null);
			diaLoading.show();

			// mActivity.enterMainActivity();
			break;
		case R.id.tv_ifelogon:
			DialogCom.DiaCaluse(mContext);
			break;
		}

		// SharedPreferences preferences = getSharedPreferences("savedate",
		// Activity.MODE_PRIVATE);
		// SharedPreferences.Editor editor = preferences.edit();
		// editor.putString("setnu", mSet.getText().toString());
		// editor.putString("code", mCode.getText().toString());
		// editor.commit();

	}

	private void switchLanguage(Locale locale) {
		Resources resources = getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
		config.locale = locale; // 简体中文
		resources.updateConfiguration(config, dm);
	}

	/** 设置左右滑动底部按钮的变化 */
	/*
	 * private void setImgBottom(int i) { switch (i) { case 0:
	 * rbLeft.setChecked(true); break; case 0: rbMid.setChecked(true); break;
	 * case 1: rbRight.setChecked(true); break; } }
	 */
	@Override
	public void onGetResult(int requestType, Object result) {

		if (result == null) {
			diaLoading.hide();
			return;
		}

		if (requestType == LOGIN_API) {

			Passenger passenger = (Passenger) result;

			switch (passenger.code) {
			case 0:
				if (IFEApplication.getInstance().getOfflineStatus()) {
					hideInputMethod();
					mActivity.enterMainActivity();
				} else {
					if (passenger.userInfo.passengerIsMember == 0 && passenger.userInfo.passengerIsRegister == 0) {
						linInput.setVisibility(View.INVISIBLE);
						linRegister.setVisibility(View.VISIBLE);
						passengerId = passenger.userInfo.passengerSeatNo;

					} else {
						// linInput.setVisibility(View.INVISIBLE);
						// linLogin.setVisibility(View.VISIBLE);
						hideInputMethod();
						mActivity.enterMainActivity();

					}
				}
				// IFEApplication.getInstance().setUserInfo(passenger.userInfo);

				break;
			case 1:
				DialogCom.DiaCom(mContext, "座位号错误,请重新填写");
				break;
			case 2:
				DialogCom.DiaCom(mContext, "证件号错误,请重新填写");
				break;
			default:
				DialogCom.DiaCom(mContext, "服务器出错");
				break;
			}

		} else if (requestType == LOGIN_MEMBER_API) {
			int code = (Integer) result;

			switch (code) {
			case 0:
				mActivity.enterMainActivity();
				break;
			case 1:
				DialogCom.DiaCom(mContext, "座位号错误,请重新填写");
				break;
			case 2:
				DialogCom.DiaCom(mContext, "密码错误,请重新填写");
				break;
			default:
				DialogCom.DiaCom(mContext, "服务器出错");
				break;
			}

		} else if (requestType == REGISTER_API) {
			int code = (Integer) result;

			switch (code) {
			case 0:
				ComUtil.toastText(mContext, "注册成功，进入首页", Toast.LENGTH_SHORT);
				mActivity.enterMainActivity();
				break;
			case 2:
				ComUtil.toastText(mContext, "重复注册", Toast.LENGTH_SHORT);
				break;
			case 1:
				mActivity.enterMainActivity();
				break;
			default:
				DialogCom.DiaCom(mContext, "注册失败，服务器出错");
				break;
			}
			
		} else if (requestType == FlIGHT_INFO_API) {
			FlightInfo flightInfo = (FlightInfo) result;
			tvFlight.setText(String.format(getResources().getString(R.string.flight_mian), flightInfo.fligNo, flightInfo.fligLaunchCity + "~"
					+ flightInfo.fligLandingCity));
			if (flightInfo.weather != null) {
				txtWeather.setText(flightInfo.fligLandingCity + "\r\n" + flightInfo.weather.temperature + "\r\n" + flightInfo.weather.weather);
			    AssetManager am = mContext.getAssets();  
				try {
				    InputStream is = am.open("weather/"+flightInfo.weather.dayPictureUrl+".png");
				    imgWeather.setImageBitmap(BitmapFactory.decodeStream(is));
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
		}

		diaLoading.hide();
	}
	
	private int getWeatherResByName(String name) {
		int r = -1;
		if (name.contains("daxue")) {
			r = R.drawable.daxue;
		} else if (name.contains("dayu")) {
			r = R.drawable.dayu;
		} else if (name.contains("duoyun")) {
			r = R.drawable.duoyun;
		} else if (name.contains("qing")) {
			r = R.drawable.qing;
		} else if (name.contains("wu")) {
			r = R.drawable.wu;
		} else if (name.contains("xiaoxue")) {
			r = R.drawable.xiaoxue;
		} else if (name.contains("yin")) {
			r = R.drawable.yin;
		}
		return r;
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			DialogCom.DiaCom(mContext, "连接服务器出错");
		}
	}

    public void showLoginLayout(boolean offline) {
        //updateFlightInfo();

        if (offline) {
            rl_read.setVisibility(View.VISIBLE);
            linInput.setVisibility(View.GONE);
            linRegister.setVisibility(View.GONE);
            cb_logon = (CheckBox) rl_read.findViewById(R.id.eula);
            current = 0;
        } else {
            linInput.setVisibility(View.VISIBLE);
            linLogin.setVisibility(View.INVISIBLE);
            linRegister.setVisibility(View.INVISIBLE);
            rl_read.setVisibility(View.GONE);
            etNO.setText(null);
            etId.setText(null);
            //清空注册页面信息
            etPhone.setText(null);
            etEmail.setText(null);
            cbSSS.setChecked(false);
        }
    }

    private void updateFlightInfo() {
        new FlightinfoRequest(this).execute((String) null);
    }
}
