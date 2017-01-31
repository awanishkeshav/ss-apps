package com.merchant.dialogs;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.merchant.R;
import com.merchant.common.Constants;
import com.merchant.common.PreferanceUtil;
import com.merchant.common.SingletonClass;
import com.merchant.models.Card;
import com.merchant.models.Error;
import com.merchant.models.Merchant;
import com.merchant.services.APIService;
import com.merchant.services.APIService.APIServiceListener;
import com.merchant.utility.ValidationUtil;
import com.merchant.SplashActivity;

public class CardEntryDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText mMIDEt, mCodeEt;
	private Button mRegisterBtn;
	private String mMIDNum, mCode;

	private Context mContext;

	public CardEntryDialog(Context context) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.card_entry_dialog);

		// setCancelable(false);
		setWidth();

		mContext = context;
		controlInitialization();
	}

	private void controlInitialization() {

		mMIDEt = (EditText) findViewById(R.id.mIDEt);
		mMIDEt.setText(Constants.MERCHANT_UUID);
		//mMIDEt.setInputType(android.text.InputType.TYPE_CLASS_TEXT
				//| android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		//String merchantUUID = new PreferanceUtil(mContext).getMerchantUUID();
		//mMIDEt.setText(merchantUUID);
		// mMIDEt.setEnabled(false);
		mCodeEt = (EditText) findViewById(R.id.codeEt);
		//mCodeEt.setInputType(android.text.InputType.TYPE_CLASS_TEXT
				//| android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE);

		mRegisterBtn = (Button) findViewById(R.id.registerBtn);

		mRegisterBtn.setOnClickListener(this);
	}

	private void setWidth() {
		// Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = this.getWindow();
		lp.copyFrom(window.getAttributes());
		// This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}

	private boolean checkValidation() {

		mMIDNum = mMIDEt.getText().toString().trim();
		mCode = mCodeEt.getText().toString().trim();

		Toast toast = null;
		if (mMIDNum.isEmpty()) {
			toast = Toast.makeText(mContext,
					"Please enter the valid merchant id", Toast.LENGTH_SHORT);

		} else if (mCode.isEmpty()) {
			toast = Toast.makeText(mContext,
					"Please enter the valid access code", Toast.LENGTH_SHORT);

		}

		if (toast == null)
			return true;
		else {
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
			return false;
		}
	}

	private void clearFocus() {
		mMIDEt.clearFocus();
		mCodeEt.clearFocus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerBtn:
			// Toast.makeText(mContext, "Register Clicked",
			// Toast.LENGTH_SHORT).show();
			if (checkValidation()) {
				clearFocus();
				SingletonClass.hideSoftKeyboard(mCodeEt);
				registerCardService();
			}

			break;
		}
	}

	/** ...///.... WEB SERVICE....///... */

	private void registerCardService() {

		APIService service = APIService.registerMerchnat(mContext, mMIDNum,
				mCode);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub

				if (error == null) {

					if (data instanceof JSONObject) {
						JSONObject jsonObject = (JSONObject) data;
                        Merchant merchant = new Merchant(jsonObject);
                        new PreferanceUtil(mContext).setMerchantName(merchant.name);
					}
					new PreferanceUtil(mContext).setMerchantUUID(mMIDNum);
					dismiss();

					((SplashActivity) mContext).reloadUI();

				} else {
					Toast toast = Toast.makeText(mContext, error.getMessage(),
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 30);
					toast.show();
				}

			}
		});
		service.getData(true);
	}

}
