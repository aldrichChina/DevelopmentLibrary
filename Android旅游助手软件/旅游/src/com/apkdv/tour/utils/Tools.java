package com.apkdv.tour.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.phprpc.PHPRPC_Client;

import com.google.gson.Gson;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;


public class Tools {
	
	public static  boolean showError = true;
	
	public static Gson gson = new Gson();
	
	/**
	 * 判断是否为空
	 * @param string
	 * @return
	 */
	public static boolean isNull(String string){
		if (string !=null && !string.isEmpty()) {
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 错误日志管理
	 * @param e
	 */
	public static void error(Exception e){
		if (showError) {
			Log.i("Exception", e.toString());
		}
	}
	
	public static void log(String string){
		if (showError) {
			Log.i("log", string);
		}
	}
	public static void log(int string){
		if (showError) {
			Log.i("log", string+"");
		}
	}
	public static void log(long string){
		if (showError) {
			Log.i("log", string+"");
		}
	}
	/**
	 * 进度条
	 * @param context
	 * @return
	 */
	public static ProgressDialog showDiglog(Context context){
		ProgressDialog dialog;
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载，请稍后...");
		dialog.setIndeterminate(false);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	/**
	 * 对话框
	 * @param message
	 * @param context
	 * @return
	 */
	public static AlertDialog dialog(String message,Context context){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("提示信息：").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setMessage(message);
		return builder.create();
	}
	
	
	/**
	 * 将view转换为bitmap
	 * @param view
	 * @return
	 */
	public static Bitmap convertViewToBitmap(View view){
	        view.destroyDrawingCache();
	        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
	                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
	        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	        view.setDrawingCacheEnabled(true);
	        Bitmap bitmap = view.getDrawingCache(true);
	        return bitmap;
		}
	/**
	 * 将图片转为byte[]
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmap2byte(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	 /**
     * install app
     * 
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }
    public static PHPRPC_Client getClient(){

		//return new PHPRPC_Client("http://10.223.6.75:8080/LBEService/servlet/mobile");
    	//return new PHPRPC_Client("http://0.0.0.0:8080/LBEService/servlet/mobile");
    	//return new PHPRPC_Client("http://192.168.1.103:8080/LBEService/servlet/mobile");
    	return new PHPRPC_Client("http://10.222.139.163:8080/LBEService/servlet/mobile");
    }
	
}
