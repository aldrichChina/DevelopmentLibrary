package com.fairlink.passenger.view;

import android.content.Context;

/**
 * @ClassName  ：  ViewUSing 
 * @Description: 批量管理自定义dialog 调用
 * @author     ：  家雪  
 * @date       ：  2014-5-29 上午9:39:17 

 */

public class DialogCom {

	
	public static void DiaCom(Context context,String msg) {
		ComDialog dialogBuilder = ComDialog.getInstance(context);
		dialogBuilder.withMessage(false, msg)
		.withBtnSureText("确定")
		.show();	
	}
	
	public static void DiaCom(Context context,String msg,String btnText) {
		ComDialog dialogBuilder = ComDialog.getInstance(context);
		dialogBuilder.withMessage(true, msg)
		.withBtnSureText(btnText)
		.show();	
	}
	
	
	/** 春秋协议弹出框 */
	public static void DiaCaluse(Context context) {
		DiaSSSClause diaSSSClause = DiaSSSClause.getInstance(context);
		diaSSSClause.show();
	}

}
