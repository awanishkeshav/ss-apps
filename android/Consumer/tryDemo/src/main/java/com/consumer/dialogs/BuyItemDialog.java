package com.consumer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.consumer.R;
import com.consumer.utility.DialogUtil;

public class BuyItemDialog extends Dialog implements android.view.View.OnClickListener{

	
	private LinearLayout mCallMeLL, mOrderLL ;
	private Spinner mSpinner;
	private Context mContext;

	public BuyItemDialog(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_item_dialog);
		
		mContext = context;
		controlInitialization();
		
	}

	
	private void controlInitialization(){
		
		mSpinner= (Spinner)findViewById(R.id.colorSpinner);
		mCallMeLL= (LinearLayout)findViewById(R.id.callMeLL);
		mCallMeLL.setOnClickListener(this);
		mOrderLL= (LinearLayout)findViewById(R.id.orderLL);
		mOrderLL.setOnClickListener(this);
		ImageView closeIv = (ImageView)findViewById(R.id.closeIv);
		closeIv.setOnClickListener(this);
		
		String [] colors = {"Silver","Gold"};

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, colors);
		adapter.setDropDownViewResource(R.layout.spinner_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setSelection(0);
		
		TextView bottomTv = (TextView)findViewById(R.id.bottomTv);
		SpannableString styledString = new SpannableString("Your account will be billed $495 for this purchase.");
		styledString.setSpan(new StyleSpan(Typeface.BOLD), 28, 32, 0);
		bottomTv.setText(styledString);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			
		case R.id.closeIv:
			
			dismiss();
			break;
		case R.id.callMeLL:
			DialogUtil.showOkDialog(mContext, "A Privilege Club representative will call you shortly to take your order");
			break;
		case R.id.orderLL:
			DialogUtil.showOkDialog(mContext, "Thank you for your order.\nThe requested item will be shipped to you home address and should arrive in 2-3 business days.");
			break;
		}
		
	}
}
