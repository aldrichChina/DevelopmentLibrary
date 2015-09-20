package com.fairlink.passenger.setting;
import com.fairlink.passenger.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 自定义dialog
 *
 */
public class SettingMyDialog {

	Context context;
	Dialogcallback dialogcallback;
	Dialog dialog;
	TextView sure;
	TextView textView;
	EditText editText;
	/**
	 * init the dialog
	 * @return
	 */
	public SettingMyDialog(Context con) {
		this.context = con;
		dialog = new Dialog(context,R.style.dialog);
		dialog.setContentView(R.layout.setting_dialog);
		textView = (TextView) dialog.findViewById(R.id.setting_remind);
		sure = (TextView) dialog.findViewById(R.id.setting_confirm);
		editText = (EditText) dialog.findViewById(R.id.setting_edit);

		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogcallback.dialogdo(editText.getText().toString());
				dismiss();

			}
		});

	}
	/**
	 * 设定一个interfack接口,使mydialog可以處理activity定義的事情
	 * @author sfshine
	 *
	 */
	public interface Dialogcallback {
		public void dialogdo(String string);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}

	/**
	 * @category Set The Content of the TextView
	 * */
	public void setContent(String content) {
		textView.setText(content);
	}

	/**
	 * Get the Text of the EditText
	 * */
	public String getText() {
		return editText.getText().toString();
	}

	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();
	}

}