package com.fairlink.passenger.shop;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.IFEApplication;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.FlowAdapter;
import com.fairlink.passenger.adapter.GoodsPayTypeAdapter;
import com.fairlink.passenger.bean.Goods;
import com.fairlink.passenger.bean.GoodsItem;
import com.fairlink.passenger.bean.RecordGoodsItem;
import com.fairlink.passenger.cart.GoodsCartItem;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.CartAddGoodsRequest;
import com.fairlink.passenger.networkrequest.GoodsDetailRequest;
import com.fairlink.passenger.networkrequest.GoodsLabelRequest;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderRequest.OrderSave;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ImageUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogEdit;
import com.fairlink.passenger.view.DialogEdit.OnBuyNumListener;
import com.fairlink.passenger.view.DialogLoading;
import com.fairlink.passenger.view.FlowView;
import com.fairlink.passenger.view.GoodsLabelGridView;

public class ShopItemDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, HttpTaskCallback,
		NetworkRequestAPI {

	private ImageView imgGoods;
	private ImageView imgDetail;
	private TextView tvPicNum;
	private TextView tvName;
	private TextView tvPrice;
	private TextView tvDeliveryType;
	private RatingBar rbRecommend;
	private ImageButton mDesNum;
	private ImageButton mAddNum;
	private TextView mNum;
	// private TextView tvFreight;
	private Button btnBuy;
	private Button btnCart;
	// private TextView tvGoodsIntroduce;
	private ScrollView scrShop;
	private GoodsLabelGridView gridViewPayType;
	private GoodsPayTypeAdapter adapterPayType;
	private FlowView flowView;
	private FlowAdapter flowAdapter;
	private String[] listPayType;
	private List<Goods> goodsList;
	private LinearLayout linGoodsLabel;
	private LinearLayout lin_recommend_level;
	private DialogLoading diaLoading;

	private int picsNum; // 图片数目
	private String pics; // 图片字符串列表
	private int id; // 商品主键
	private int payType; // 支付方式
	private int deliveryType; // 配送方式
	private Goods data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_detail);

		diaLoading = new DialogLoading(this);
		id = getIntent().getExtras().getInt("id", 0);
		new GoodsDetailRequest(this, this, id).execute((String) null);
		diaLoading.show();

		initView();
		initAllListeners();
	}

	public void onDestroy() {

		if (diaLoading != null) {
			diaLoading.dismiss();
		}
		super.onDestroy();
	}

	private void initView() {

		imgGoods = (ImageView) findViewById(R.id.img_goods);
		tvPicNum = (TextView) findViewById(R.id.tv_goods_pics_num);
		tvName = (TextView) findViewById(R.id.tv_goods_detail_name);
		tvPrice = (TextView) findViewById(R.id.tv_goods_price_now);
		tvDeliveryType = (TextView) findViewById(R.id.tv_delivery_type);
		rbRecommend = (RatingBar) findViewById(R.id.rb_goods_detail_score_image);
		btnBuy = (Button) findViewById(R.id.btn_buy);
		btnCart = (Button) findViewById(R.id.btn_cart);
		// tvGoodsIntroduce = (TextView)
		// findViewById(R.id.tv_goods_detail_introduce);
		imgDetail = (ImageView) findViewById(R.id.img_detail);
		scrShop = (ScrollView) findViewById(R.id.scr_shop);
		mDesNum = (ImageButton) findViewById(R.id.num_des);
		mAddNum = (ImageButton) findViewById(R.id.num_add);
		mNum = (TextView) findViewById(R.id.tv_buy_num);

		gridViewPayType = (GoodsLabelGridView) findViewById(R.id.grid_pay_type);

		linGoodsLabel = (LinearLayout) findViewById(R.id.lin_goods_label);
		lin_recommend_level = (LinearLayout) findViewById(R.id.lin_recommend_level);
		adapterPayType = new GoodsPayTypeAdapter(this, listPayType);
		gridViewPayType.setAdapter(adapterPayType);
		gridViewPayType.setOnItemClickListener(this);
		gridViewPayType.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

		flowView = (FlowView) findViewById(R.id.grid_label);
		flowAdapter = new FlowAdapter(this, goodsList);

		flowView.setAdapter(flowAdapter);
		flowView.setOnItemClickListener(this);

		btnBuy.setVisibility(View.VISIBLE);
		btnCart.setVisibility(View.VISIBLE);
	}

	private void initAllListeners() {
		imgGoods.setOnClickListener(this);
		btnCart.setOnClickListener(this);
		btnBuy.setOnClickListener(this);
		mDesNum.setOnClickListener(this);
		mAddNum.setOnClickListener(this);

		mNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				DialogEdit dialogBuilder = DialogEdit
						.getInstance(ShopItemDetailActivity.this);

				dialogBuilder.withNumLimit(ShopUtil.MAX_GOODS_NUMBER)
						.withNumBuy(mNum.getText().toString())
						.setmListener(mListener).show();

			}
		});

	}

	private OnBuyNumListener mListener = new OnBuyNumListener() {

		@Override
		public void onBuyNumberChanged(String num) {
			mNum.setText(num);
		}
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.img_goods:
			if (0 == picsNum) {
				DialogCom.DiaCom(this, "商品图片数目为0");
				return;
			}

			Intent intent = new Intent(this, GoodsShowImg.class);
			intent.putExtra("pics", pics);
			startActivity(intent);
			break;

		case R.id.btn_cart:
			addGoodsToCart();
			break;

		case R.id.num_des:
			int numDes = Integer.parseInt(mNum.getText().toString().trim());
			mNum.setText(Integer.toString(numDes > 1 ? numDes -= 1 : 1));
			break;

		case R.id.num_add:
			int numAdd = Integer.parseInt(mNum.getText().toString().trim());
			mNum.setText(Integer
					.toString(numAdd < ShopUtil.MAX_GOODS_NUMBER ? numAdd + 1
							: ShopUtil.MAX_GOODS_NUMBER));
			break;

		case R.id.btn_buy:
			processBuy();
			break;

		default:
			break;
		}

	}

	private void processData(Goods goods) {
		scrShop.smoothScrollTo(0, 0);
		mNum.setText("1");
		this.data = goods;

		if (goods.getGoodsStockNum() == 0) {
			btnCart.setVisibility(View.GONE);
			btnBuy.setEnabled(false);
			btnBuy.setText(R.string.sell_out);
			btnBuy.setBackground(getResources().getDrawable(
					R.drawable.btn_buy_disable_selector));
		} else {
			if (!ShopUtil.FLC_GoodsMall.equals(goods.getGoodsMall())) {
				btnCart.setVisibility(View.GONE);
			} else {
				btnCart.setVisibility(View.VISIBLE);
			}

			btnBuy.setEnabled(true);
			btnBuy.setText(R.string.buy_now);
			btnBuy.setBackground(getResources().getDrawable(
					R.drawable.btn_buy_selector));
		}

		updateShopLabel(goods.getProductId(), goods.getGoodsMall());

		pics = goods.getGoodsPic();
		String picUrl = ComUtil.getPic(pics);

		ImageUtil.setImage(picUrl, ImageUtil.MID, imgGoods, null, null);
		picsNum = ComUtil.getPicNum(pics);
		tvPicNum.setText((picsNum == 0 ? 0 : 1) + "\t/\t" + picsNum);
		tvName.setText(goods.getProductName());

		tvPrice.setText("￥"
				+ String.format("%.2f", goods.getGoodsPreferentialPrice()));

		deliveryType = goods.getDistributionWay();
		tvDeliveryType
				.setText(deliveryType == 0 ? getString(R.string.delivery_up)
						: getString(R.string.delivery_down));
		if (goods.getGoodsRecommendLevel() > 0.0f) {
			lin_recommend_level.setVisibility(View.VISIBLE);
			rbRecommend.setRating(goods.getGoodsRecommendLevel());
		} else
			lin_recommend_level.setVisibility(View.GONE);

		listPayType = goods.getPaymentType().split(",");
		listPayType = filterPayType(listPayType);
		if (listPayType != null) {
			adapterPayType.setList(listPayType);
			gridViewPayType.setItemChecked(0, true);
			if (!listPayType[0].isEmpty())
				payType = Integer.parseInt(listPayType[0]);
			else
				payType = 1;
		}

		// tvGoodsIntroduce.setText(goods.getGoodsDesc());
		String detailUrl = ComUtil.getPic(goods.getDetailDesc());
		ImageUtil.setImage(detailUrl, ImageUtil.BIG, imgDetail, null, null);
	}

	// 修改listPayType类型
	private String[] filterPayType(String[] listpayType) {
		if (IFEApplication.getInstance().getOfflineStatus()) // 判断应用是否在联网
		{
			List<String> newStr = new ArrayList<String>();
//			String[] newStr = new String[listpayType.length];// 创建newStr字符串数组,长度为listpayType类型
			for (int i = 0; i < listpayType.length; ++i)// 循环listpayType字符串数组
			{
				if (!listpayType[i].equalsIgnoreCase("0")) {// 判断listPayType是否包含"0"
					newStr.add(listpayType[i]);
					//newStr[i] = listpayType[i];// 把listpayType赋值给newStr
				}
			}
			
			return newStr.toArray(new String[]{});
		}
		return listpayType;
	}

	// 获取商品标签
	private void updateShopLabel(String productId, String mall) {
		if (ComUtil.isEmpty(productId) || ComUtil.isEmpty(mall))
			return;

		new GoodsLabelRequest(this, productId, mall, this)
				.execute((String) null);
		diaLoading.show();
	}

	// 更新商品详情(id:商品主键)
	public void updateShopDetail(int id) {
		new GoodsDetailRequest(this, this, id).execute((String) null);
		diaLoading.show();
	}

	// 更新商品详情(id:商品主键)
	public void updateShopDetail(Goods goods) {
		processData(goods);
	}

	/** 增加商品到购物车 */
	private void addGoodsToCart() {

		if (mNum.getText().toString().trim().equals("") || null == data) {
			mNum.setText("1");
			return;
		}

		int num = Integer.parseInt(mNum.getText().toString());
		new CartAddGoodsRequest(this, this, data.getId(), num)
				.execute((String) null);
		diaLoading.show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if (arg0.getId() == R.id.grid_label) {
			flowAdapter.setCurrentId(arg2);
			flowAdapter.notifyDataSetChanged();
			updateShopDetail(goodsList.get(arg2));
		} else if (arg0.getId() == R.id.grid_pay_type) {
			gridViewPayType.setItemChecked(arg2, true);

			payType = Integer.parseInt(listPayType[arg2]);
		}

	}

	@Override
	public void onGetResult(int requestType, Object result) {

		diaLoading.hide();
		if (requestType == GOODS_DETAIL_API) {
			if (result == null) {
				return;
			}

			processData((Goods) result);

		} else if (requestType == GOODS_LABEL_API) {
			if (result == null) {
				return;
			}

			if (goodsList != null) {
				goodsList.clear();
			}
			goodsList = (List<Goods>) result;
			flowAdapter.setList(goodsList);
			flowAdapter.notifyDataSetChanged();
			
		} else if (requestType == ORDER_SAVE_API) {
			if (result == null) {
				return;
			}

			OrderSave item = (OrderSave) result;

			if (0 == item.code) {
				ShopUtil.showOrderPay(this, item.orderId);
			} else {
				DialogCom.DiaCom(this, "购买失败");
			}
		} else if (requestType == CART_ADD_GOODS_API) {
			if (result == null) {
				diaLoading.hide();
				return;
			}

			int code = (Integer) result;

			if (0 == code) {
				int num = Integer.parseInt(mNum.getText().toString());

				if (ShopUtil.FLC_GoodsMall.equals(data.getGoodsMall())) {
					GoodsCartItem item = new GoodsCartItem();
					item.setGoodsItem(new GoodsItem());
					item.getGoodsItem().setCount(num);
					item.getGoodsItem().setGoods(data);

					IFEApplication.getInstance().setSingleDataFLC(item);
					ComUtil.toastText(this, "已成功添加到购物车", Toast.LENGTH_SHORT);

					StringBuilder sb = new StringBuilder();
					sb.append("goodsName:" + data.getProductName() + ";");
					sb.append("goodsId:" + data.getId() + ";");
					sb.append("productId:" + data.getProductId());

					Analytics(data.getGoodsMall(), sb.toString());
				}

			} else {
				ComUtil.toastText(this, "添加购物车失败", Toast.LENGTH_SHORT);
			}

			diaLoading.hide();
		}

	}

	/** 处理立即购买操作的数据 */
	private void processBuy() {

		int num = Integer.parseInt(mNum.getText().toString());

		ArrayList<RecordGoodsItem> listData = new ArrayList<RecordGoodsItem>();
		GoodsItem item = new GoodsItem();
		item.setCount(num);
		item.setGoods(data);
		listData.add(item.getRecordGoodsItem());

		ShopUtil.showOrderConfirm(this, data.getGoodsMall(), payType, listData,
				deliveryType, false);
	}

	private void Analytics(String shopType, String goodsName) {
		Analytics.logEvent(this, AnalyticsType.getOperationShop(4), shopType
				.equals(ShopUtil.FLC_GoodsMall) ? AnalyticsType.ORIGIN_MALL_FLC
				: AnalyticsType.ORIGIN_MALL_SSS, AnalyticsType.cperationData(
				null, goodsName));
	}

	@Override
	public void onError(int requestType) {
		diaLoading.hide();
		if (requestType != REDIRECT_API) {
			ComUtil.toastText(this, "连接服务器出错", Toast.LENGTH_SHORT);
		}
	}

	@Override
	public void onBackMainListener(View v) {
		finish();
	}
}
