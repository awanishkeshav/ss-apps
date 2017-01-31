package com.merchant.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.merchant.R;
import com.merchant.common.SingletonClass;
import com.merchant.models.Error;
import com.merchant.models.KeyValueObject;
import com.merchant.models.TargetingVO;
import com.merchant.services.APIService;
import com.merchant.services.APIService.APIServiceListener;

public class AddOfferTargetDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText typeEt, visitEt, spendEt;
	
	private String type;
	private Double spend;
	private Long visit;
	private AddTargetingListner mListner;
	private TargetingVO mTarget;
	private KeyValueObject selectedTargetType;
	private Long mOfferId;

	private Context mContext;
	
	public interface AddTargetingListner {

		void onAddTarget();
	}

	public AddOfferTargetDialog(Context context, Long offerId, TargetingVO target, AddTargetingListner listner) {
		super(context);
		mContext = context;
		mTarget = target;
		mOfferId = offerId;
		mListner = listner;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_offer_target_dialog);
		getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// setCancelable(false);
		setWidth();

		
		controlInitialization();
	}

	private void controlInitialization() {

		typeEt = (EditText) findViewById(R.id.typeET);
		typeEt.setOnClickListener(this);
		typeEt.setInputType(EditorInfo.TYPE_NULL);
		visitEt = (EditText) findViewById(R.id.visitET);
		spendEt = (EditText) findViewById(R.id.spendET);
		
		Button applyBtn = (Button)findViewById(R.id.apply_button);
		applyBtn.setOnClickListener(this);
		Button cancelBtn= (Button)findViewById(R.id.cancel_button);
		cancelBtn.setOnClickListener(this);
		
		ImageView closeIv = (ImageView)findViewById(R.id.closeIv);
		closeIv.setOnClickListener(this);
		
		if (mTarget != null) {
			if (mTarget.targetType != null && !mTarget.targetType.isEmpty()) {
				selectedTargetType = SingletonClass.sharedInstance().getbConstants().offerTargetTypeByKey(mTarget.targetType);
				typeEt.setText(selectedTargetType.value);
			}
			
			visitEt.setText(mTarget.minVisit.toString());
			spendEt.setText(mTarget.minTotalSpend.toString());
		}
		
		
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
	
	private void showTypePicker(){
		final ArrayList<String> list = SingletonClass.sharedInstance().getbConstants().getOfferTargetTypeStrings();
		
		SingletonClass.showItemPicker(mContext, null, list, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				selectedTargetType = SingletonClass.sharedInstance().getbConstants().offerTargetTypes.get(which);
				typeEt.setText(list.get(which));
				
			}
		});
	}

	private boolean checkValidation() {

		type = typeEt.getText().toString().trim();
		
		try {
			spend = Double.parseDouble(spendEt.getText().toString().trim());
			visit = Long.parseLong(visitEt.getText().toString().trim());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		

		Toast toast = null;
		if (type.isEmpty()) {
			//toast = Toast.makeText(mContext,
					//"Please enter atleast one type", Toast.LENGTH_SHORT);

		}
		
//		else if(spendParam == null){
//			toast = Toast.makeText(mContext,
//					"Please enter valid minimum spend", Toast.LENGTH_SHORT);
//		}else if(visitParam == null){
//			toast = Toast.makeText(mContext,
//					"Please enter valid minimum visit count", Toast.LENGTH_SHORT);
//		}

		if (toast == null)
			return true;
		else {
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
			return false;
		}
	}

	private void clearFocus() {
		typeEt.clearFocus();
		visitEt.clearFocus();
		spendEt.clearFocus();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_button:
			
			if (checkValidation()) {
				clearFocus();
				setOfferTarget();
			}

			break;
			case R.id.closeIv:
			case R.id.cancel_button:
				clearFocus();
				dismiss();
				break;
			case R.id.typeET:
				showTypePicker();
				break;
		}
	}

	/** ...///.... WEB SERVICE....///... */

	private void setOfferTarget() {
		
		Long offerId = mOfferId;
		Long targetId = null;
		String selectedType = null;
		if (selectedTargetType != null) {
			selectedType = selectedTargetType.key;
		}
		
		APIService service = APIService.addOrUpdateOfferTarget(mContext, offerId, targetId, selectedType, visit, spend);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub

				if (error == null){
					mListner.onAddTarget();
					dismiss();
					
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
