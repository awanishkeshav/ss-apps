package com.merchant.dialogs;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.merchant.R;
import com.merchant.models.Error;
import com.merchant.models.Offer;
import com.merchant.services.APIService;
import com.merchant.services.APIService.APIServiceListener;

public class ReviewResponseDialog extends Dialog implements
		android.view.View.OnClickListener {

	private EditText responseEt;
	private Spinner offerSpinner;
	private ArrayAdapter<String> moffersAdapter;
	private ArrayList<Offer> mOffers;;
	private String response;
	private Long reviewId;

	private Context mContext;

	public ReviewResponseDialog(Context context, Long paramReviewId) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.review_response_dialog);
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// setCancelable(false);
		this.reviewId = paramReviewId;
		setWidth();

		mContext = context;
		controlInitialization();
		reloadOffers();
	}

	private void controlInitialization() {

		responseEt = (EditText) findViewById(R.id.responseNoteEt);
		offerSpinner = (Spinner) findViewById(R.id.offerSpin);

		Button applyBtn = (Button) findViewById(R.id.apply_button);
		applyBtn.setOnClickListener(this);
		Button cancelBtn = (Button) findViewById(R.id.cancel_button);
		cancelBtn.setOnClickListener(this);

		ImageView closeIv = (ImageView) findViewById(R.id.closeIv);
		closeIv.setOnClickListener(this);

		offerSpinner.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				clearFocus();
				return false;
			}
		});

	}

	private ArrayList<String> getOffersDisplayStrings() {

		ArrayList<String> list = new ArrayList<String>();
		list.add("Include Offer");
		if (this.mOffers != null) {
			for (Offer offer : this.mOffers) {
				list.add(offer.title);
			}
		}
		return list;
	}

	private void reloadOffers() {
		moffersAdapter = new ArrayAdapter<String>(mContext,
				R.layout.spinner_item, getOffersDisplayStrings());
		moffersAdapter.setDropDownViewResource(R.layout.spinner_item);
		offerSpinner.setAdapter(moffersAdapter);
		//offerSpinner.setSelection(AdapterView.INVALID_POSITION);
	}

	public void setmOffers(ArrayList<Offer> mOffers) {
		this.mOffers = mOffers;
		reloadOffers();
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

		response = responseEt.getText().toString().trim();

		Toast toast = null;
		if (response.isEmpty()) {
			toast = Toast.makeText(mContext, "Response cannot be empty",
					Toast.LENGTH_SHORT);

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
		responseEt.clearFocus();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_button:

			if (checkValidation()) {
				clearFocus();
				postRsponseService();
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

	private void postRsponseService() {
		
		Long offerId = null;
		if (offerSpinner.getSelectedItemPosition() != 0) {
			offerId = mOffers.get(offerSpinner.getSelectedItemPosition()-1).id;
		}

		APIService service = APIService.postReviewResponse(mContext, reviewId, response,offerId);
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

}
