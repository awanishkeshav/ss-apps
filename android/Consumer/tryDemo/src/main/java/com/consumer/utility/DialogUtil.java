package com.consumer.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.consumer.R;

public class DialogUtil {


	// Web service error Messages.
	public static void showOkDialog(final Context context, String message) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		dialog.setContentView(R.layout.ok_dialog);

		TextView titleTv = (TextView) dialog.findViewById(R.id.title_tv);
		titleTv.setVisibility(View.GONE);

		TextView messageTv = (TextView) dialog.findViewById(R.id.message_tv);
		messageTv.setText(message);

		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		okBtn.setTag(dialog);
		okBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}


	public static Dialog showOkCancelDialog(final Context context, int titleResId, String msgResId, OnClickListener okListener) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		dialog.setContentView(R.layout.ok_cancel_dialog);

		TextView titleTv = (TextView) dialog.findViewById(R.id.title_tv);
		if (titleResId > 0) {
			titleTv.setText(titleResId);
		} else {
			titleTv.setVisibility(View.GONE);
		}
		TextView messageTv = (TextView) dialog.findViewById(R.id.message_tv);
		messageTv.setText(msgResId);

		dialog.findViewById(R.id.cancel_btn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		okBtn.setTag(dialog);
		okBtn.setOnClickListener(okListener);

		dialog.show();
		return dialog;
	}
	
	
	public static void showOkListenerDialog(final Context context, String message, OnClickListener okListener) {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		dialog.setContentView(R.layout.ok_dialog);

		TextView titleTv = (TextView) dialog.findViewById(R.id.title_tv);
		titleTv.setVisibility(View.GONE);


		TextView messageTv = (TextView) dialog.findViewById(R.id.message_tv);
		messageTv.setText(message);

		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		okBtn.setTag(dialog);
		okBtn.setOnClickListener(okListener);

		dialog.show();
	}
	

	
	
	
	
}
