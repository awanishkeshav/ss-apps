package com.consumer.widget;

import com.consumer.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class WhiteProgressDialog extends Dialog {

	public WhiteProgressDialog(Context context, int msgResId) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.white_progress_dialog);

		TextView messageTv = (TextView) findViewById(R.id.message_tv);

		messageTv.setVisibility(msgResId <= 0 ? View.GONE : View.VISIBLE);
		messageTv.setText(msgResId <= 0 ? R.string.app_name : msgResId);
	}
}
