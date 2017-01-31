package com.consumer.dialogs;

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

import com.consumer.SplashActivity;
import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.Card;
import com.consumer.models.Error;
import com.consumer.services.APIService;
import com.consumer.services.APIService.APIServiceListener;
import com.consumer.utility.ValidationUtil;

public class CardEntryDialog extends Dialog implements android.view.View.OnClickListener{

	private EditText mPhoneEt, mCardNumEt, mCodeEt;
	private Button mRegisterBtn ;
	
	private String mPhoneNum, mCardNum, mCode;

	private Card mCard;
	private Context mContext;
	
	public CardEntryDialog(Context context) {
		super(context);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.card_entry_dialog);
		//setCancelable(false);
		setWidth();
		
		
		mContext = context;
		controlInitialization();
	}

	private void controlInitialization(){
		
		mPhoneEt 	= (EditText)findViewById(R.id.phoneNumEt);
		mCardNumEt 	= (EditText)findViewById(R.id.cardNumEt);
		mCodeEt 	= (EditText)findViewById(R.id.codeEt);
		mRegisterBtn= (Button)findViewById(R.id.registerBtn);
		
		mRegisterBtn.setOnClickListener(this);
	}

	private void setWidth(){
		//Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = this.getWindow();
		lp.copyFrom(window.getAttributes());
		//This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}
	
	private boolean checkValidation(){
		
		mPhoneNum	=	mPhoneEt.getText().toString().trim();
		mCardNum 	=	mCardNumEt.getText().toString().trim();
		mCode		=	mCodeEt.getText().toString().trim();
		
		Toast toast = null;
		if(!ValidationUtil.isValidMobile(mPhoneNum)){
			toast = Toast.makeText(mContext, "Please enter the valid mobile number", Toast.LENGTH_SHORT);
			
		}else if(mCardNum.isEmpty() || mCardNum.length()<4 || !ValidationUtil.isNumeric(mCardNum)){
			toast = Toast.makeText(mContext, "Please enter the valid card number", Toast.LENGTH_SHORT);
			
		}else if(mCode.isEmpty()){
			toast = Toast.makeText(mContext, "Please enter the valid verification code", Toast.LENGTH_SHORT);
			
		}
		
		if(toast == null)
			return true;
		else{
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
			return false;
		}
	}
	
	private void clearFocus(){
		mPhoneEt.clearFocus();
		mCardNumEt.clearFocus();
		mCodeEt.clearFocus();
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.registerBtn:
			//Toast.makeText(mContext, "Register Clicked", Toast.LENGTH_SHORT).show();
			if(checkValidation()){
				clearFocus();
				SingletonClass.hideSoftKeyboard(mCardNumEt);
				registerCardService();
			}
			
			break;
		}
	}

	
	/** ...///.... WEB SERVICE....///...*/
	
	private void registerCardService(){
		APIService service = APIService.registerCard(mContext, mPhoneNum, mCardNum, mCode);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub
				
				if (error == null) {
					Log.d("APISeriveTest", data.toString());

					if (data instanceof JSONObject) {
					//	mCardList = new ArrayList<Card>();
						CardEntryDialog.this.dismiss();	
						JSONObject jsonObject = (JSONObject) data;
						mCard = new Card(jsonObject);
						/*if(((SplashActivity)mContext).mCardList.size() >0){
							int pos = ((SplashActivity)mContext).mCardList.size()-1;
							((SplashActivity)mContext).mCardList.add(pos, mCard);
							((SplashActivity)mContext).mCardList.add(new Card("",0.0, 0.0, 0.0, 0.0, "", null, null, null, 0l));
						}*/
						
						((SplashActivity)mContext).getCardsService(true);
						Log.v("card Number", mCard.cardNum);
					}
				}else{
					Toast toast = Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0,30);
					toast.show();
				}

			}
		});
		service.getData(true);
	}
	
	
}
