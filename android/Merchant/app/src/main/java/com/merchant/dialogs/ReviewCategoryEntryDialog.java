package com.merchant.dialogs;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.GetMetadataRequest;
import com.merchant.R;
import com.merchant.models.Error;
import com.merchant.models.ReviewTemplate;
import com.merchant.services.APIService;
import com.merchant.services.APIService.APIServiceListener;

public class ReviewCategoryEntryDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText cat1Et, cat2Et, cat3Et;

	private String cat1, cat2, cat3;

	private Context mContext;

	public ReviewCategoryEntryDialog(Context context) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_category_entry_dialog);
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// setCancelable(false);
		setWidth();

		mContext = context;
		controlInitialization();
		getReviewTemplate();
	}

	private void controlInitialization() {

		cat1Et = (EditText) findViewById(R.id.cat1ET);
		cat2Et = (EditText) findViewById(R.id.cat2ET);
		cat3Et = (EditText) findViewById(R.id.cat3ET);

		Button applyBtn = (Button) findViewById(R.id.apply_button);
		applyBtn.setOnClickListener(this);
		Button cancelBtn = (Button) findViewById(R.id.cancel_button);
		cancelBtn.setOnClickListener(this);

		ImageView closeIv = (ImageView) findViewById(R.id.closeIv);
		closeIv.setOnClickListener(this);

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

		cat1 = cat1Et.getText().toString().trim();
		cat2 = cat2Et.getText().toString().trim();
		cat3 = cat3Et.getText().toString().trim();

		Toast toast = null;
		if (cat1.isEmpty() && cat2.isEmpty() && cat3.isEmpty()) {
			toast = Toast.makeText(mContext,
					"Please enter atleast one category", Toast.LENGTH_SHORT);

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
		cat1Et.clearFocus();
		cat2Et.clearFocus();
		cat3Et.clearFocus();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_button:

			if (checkValidation()) {
				clearFocus();
				setReviewTemplate();
			}

			break;
		case R.id.closeIv:
		case R.id.cancel_button:
			clearFocus();
			dismiss();
			break;
		}
	}

	/** ...///.... WEB SERVICE....///... */

	private void setReviewTemplate() {

		ArrayList<String> cats = new ArrayList<String>();
		if (!cat1.isEmpty()) {
			cats.add(cat1);
		}
		if (!cat2.isEmpty()) {
			cats.add(cat2);
		}
		if (!cat3.isEmpty()) {
			cats.add(cat3);
		}
		String c1 = null;
		String c2 = null;
		String c3 = null;

		if (cats.size() > 0) {
			c1 = cats.get(0);
		}
		if (cats.size() > 1) {
			c2 = cats.get(1);
		}
		if (cats.size() > 2) {
			c3 = cats.get(2);
		}

		APIService service = APIService.setReviewTemplate(mContext, c1, c2, c3);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub

				if (error == null) {
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

	private void getReviewTemplate() {

		APIService service = APIService.getMerchantReviewTemplate(mContext);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub

				if (error == null) {
					if (data instanceof JSONObject) {
						
					}
					ReviewTemplate template = new ReviewTemplate((JSONObject) data);
					if (template.criteria1 != null && !template.criteria1.isEmpty()) {
						cat1Et.setText(template.criteria1);
					}
					if (template.criteria2 != null && !template.criteria2.isEmpty()) {
						cat2Et.setText(template.criteria2);
					}
					if (template.criteria3 != null && !template.criteria3.isEmpty()) {
						cat3Et.setText(template.criteria3);
					}

				} 

			}
		});
		service.getData(false);
	}

}
