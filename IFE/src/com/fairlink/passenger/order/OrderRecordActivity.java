package com.fairlink.passenger.order;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fairlink.analytics.Analytics;
import com.fairlink.analytics.AnalyticsType;
import com.fairlink.passenger.BaseActivity;
import com.fairlink.passenger.R;
import com.fairlink.passenger.adapter.OrderRecordAdapter;
import com.fairlink.passenger.adapter.OrderRecordAdapter.OnProcessClickListener;
import com.fairlink.passenger.bean.Order;
import com.fairlink.passenger.networkrequest.BaseHttpTask.HttpTaskCallback;
import com.fairlink.passenger.networkrequest.NetworkRequestAPI;
import com.fairlink.passenger.networkrequest.OrderRecordRequest;
import com.fairlink.passenger.util.ComUtil;
import com.fairlink.passenger.util.ShopUtil;
import com.fairlink.passenger.view.DialogCom;
import com.fairlink.passenger.view.DialogLoading;

/**
 * @ClassName ： OrderRecordActivity
 * @Description: 订单记录
 */

public class OrderRecordActivity extends BaseActivity implements HttpTaskCallback, NetworkRequestAPI {

    private TextView tvNoData;
    private Button btn_no_data;

    private ListView listOrderRecord;
    private OrderRecordAdapter orderAdapter;
    private Spinner mallTypesSpinner;
    private final String[] mallTypes = { "全部订单", "购物中心", "绿翼商城" };
    private Spinner orderStatusSpinner;
    private final String[] orderStatus = { "全部状态", "未支付", "已支付", "已取消" };
    private DialogLoading diaLoading;
    private List<Order> allOrderList;
    private List<Order> mallOrderList;
    private List<Order> statusOrderList;

    // 订单状态
    private final static int OS_WAIT_PAY = 0;
    private final static int OS_ALREADY_CANCEL = 2;
    private final static int OS_FINISH = 3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_record);

        diaLoading = new DialogLoading(this);

        initView();
        setListener();
        getOrderRecord();
    }

    public void onDestroy() {
        super.onDestroy();

        if (diaLoading != null) {
            diaLoading.dismiss();
        }

    }

    /** 获取我的订单记录 */
    private void getOrderRecord() {

        new OrderRecordRequest(this).execute((String) null);
        diaLoading.show();
    }

    private void initView() {
        tvNoData = (TextView) findViewById(R.id.tv_no_data);
        btn_no_data = (Button) findViewById(R.id.btn_no_data);
        listOrderRecord = (ListView) findViewById(R.id.list_order_record);

        mallTypesSpinner = (Spinner) findViewById(R.id.sp_goods_mall);
        orderStatusSpinner = (Spinner) findViewById(R.id.sp_order_status);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.show(getFragmentManager().findFragmentById(R.id.cart_tip_button));
        transaction.hide(getFragmentManager().findFragmentById(R.id.order_tip_button));
        transaction.commitAllowingStateLoss();

        Button btnBack = (Button) findViewById(R.id.com_back_main).findViewById(R.id.btn_back);
        btnBack.setBackgroundResource(R.drawable.icon_back_pre);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mallTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mallTypesSpinner.setAdapter(adapter);
        mallTypesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (orderAdapter == null) {
                    return;
                }
                List<Order> tmp;
                switch (position) {
                case 0:
                    // 全部订单
                    tmp = statusOrderList;
                    mallOrderList = allOrderList;
                    break;
                case 1:
                    // 购物中心
                    tmp = getOrdersByMallType(statusOrderList, ShopUtil.FLC_GoodsMall);
                    mallOrderList = getOrdersByMallType(allOrderList, ShopUtil.FLC_GoodsMall);
                    break;
                case 2:
                    // 绿翼商城
                    tmp = getOrdersByMallType(statusOrderList, ShopUtil.Spring_GoodsMall);
                    mallOrderList = getOrdersByMallType(allOrderList, ShopUtil.Spring_GoodsMall);
                    break;
                default:
                    tmp = statusOrderList;
                    mallOrderList = allOrderList;
                    break;
                }

                orderAdapter.setOrderList(tmp);
                orderAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatusSpinner.setAdapter(adapter);
        orderStatusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (orderAdapter == null) {
                    return;
                }
                List<Order> tmp;
                switch (position) {
                case 0:
                    // 全部状态
                    tmp = mallOrderList;
                    statusOrderList = allOrderList;
                    break;
                case 1:
                    // 未支付
                    tmp = getOrdersByOrderStatus(mallOrderList, OS_WAIT_PAY);
                    statusOrderList = getOrdersByOrderStatus(allOrderList, OS_WAIT_PAY);
                    break;
                case 2:
                    // 已支付
                    tmp = getOrdersByOrderStatus(mallOrderList, OS_FINISH);
                    statusOrderList = getOrdersByOrderStatus(allOrderList, OS_FINISH);
                    break;
                case 3:
                    // 已取消
                    tmp = getOrdersByOrderStatus(mallOrderList, OS_ALREADY_CANCEL);
                    statusOrderList = getOrdersByOrderStatus(allOrderList, OS_ALREADY_CANCEL);
                    break;
                default:
                    tmp = mallOrderList;
                    statusOrderList = allOrderList;
                    break;
                }

                orderAdapter.setOrderList(tmp);
                orderAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setListener() {

    }

    public void onBackPreListener(View v) {
        finish();

    }

    private OnProcessClickListener mListener = new OnProcessClickListener() {

        @Override
        public void onPay(String orderId) {
            ShopUtil.showOrderPay(OrderRecordActivity.this, orderId);

            Analytics(3, "orderId" + orderId);

            finish();
        }

    };

    private void setNoData(int size) {
        if (size > 0) {
            btn_no_data.setVisibility(View.GONE);
            tvNoData.setVisibility(View.GONE);
        } else {
            btn_no_data.setVisibility(View.VISIBLE);
            // tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetResult(int requestType, Object result) {

        if (requestType == ORDER_RECORD_API) {

            tvNoData.setVisibility(View.VISIBLE);

            diaLoading.hide();
            if (result == null) {
                tvNoData.setText("订单记录信息返回出错!");
                return;
            }

            allOrderList = (List<Order>) result;
            setNoData(allOrderList.size());
            mallOrderList = allOrderList;
            statusOrderList = allOrderList;

            orderAdapter = new OrderRecordAdapter(this, allOrderList);
            orderAdapter.setListener(mListener);
            listOrderRecord.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();

        } else if (requestType == ORDER_CANCEL_API) {
            if (result == null) {
                diaLoading.hide();
                return;
            }

            int code = (Integer) result;

            if (0 == code) {
                ComUtil.toastText(this, "订单取消成功", Toast.LENGTH_SHORT);
                getOrderRecord();

            } else {
                DialogCom.DiaCom(this, "取消订单失败");
            }

            diaLoading.hide();

        } else if (requestType == ORDER_RETURN_GOODS_API) {
            if (result == null) {
                diaLoading.hide();
                return;
            }

            int code = (Integer) result;

            if (0 == code) {
                ComUtil.toastText(this, "退货成功", Toast.LENGTH_SHORT);
                getOrderRecord();

            } else {
                DialogCom.DiaCom(this, "退货失败");
            }

            diaLoading.hide();

        }

    }

    private void Analytics(int type, String name) {

        Analytics.logEvent(this, AnalyticsType.getOperationOrder(type), AnalyticsType.ORIGIN_CART, AnalyticsType.cperationData(null, name));

    }

    @Override
    public void onError(int requestType) {
        diaLoading.hide();
        if (requestType != REDIRECT_API) {
            tvNoData.setVisibility(View.VISIBLE);

            tvNoData.setText("订单记录信息返回出错!");

        }
    }

    private List<Order> getOrdersByMallType(List<Order> orders, String mall) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getGoodsMall().equals(mall)) {
                list.add(order);
            }
        }
        return list;
    }

    private List<Order> getOrdersByOrderStatus(List<Order> orders, int status) {
        List<Order> list = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderStatus() == status) {
                list.add(order);
            }
        }
        return list;
    }
}
