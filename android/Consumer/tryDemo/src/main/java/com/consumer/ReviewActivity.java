package com.consumer;

import org.json.JSONException;
import org.json.JSONObject;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.gcm.MyNotification;
import com.consumer.handler.CriteriaHolder;
import com.consumer.handler.HeaderLayout;
import com.consumer.models.Error;
import com.consumer.models.Merchant;
import com.consumer.models.ReviewTemplate;
import com.consumer.models.Transaction;
import com.consumer.models.Transaction.ReviewStatus;
import com.consumer.services.APIService;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewActivity extends BaseActivity implements OnClickListener {

	public static final String Extra_Transaction = "Extra_TXNVO";
	public static final String Extra_TxnId = "Extra_TXN_ID";
	public static final String Extra_MerchantId = "Extra_Merchant_ID";

	private HeaderLayout mHeaderLayout;
	private TextView mMerchantNameTv, mAddressTv, mTotalRatingTv;
	private EditText mCommentBox;
	private RatingBar mAvgRatingBar;
	private Button mSubmitButtonl;
	private CriteriaHolder criteria1, criteria2, criteria3;
	private Double avgRating = (Double) 0.0;
	private Long totalReview = (long) 0;

	private ReviewTemplate reviewTemplate;
	private Long txnId, merchantId;
	private Transaction txn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		handleIntent(getIntent());
		
		this.initialization();
		this.reloadUI();
		this.loadExistingReviewIfAny();
		
		if (this.merchantId != null && this.merchantId != 0) {
			this.loadReviewTemplate();
			this.loadMerchantReviewCount();
		}
		
	}
	
	private void handleIntent(Intent intent) {

		this.txnId = getIntent().getLongExtra(Extra_TxnId, 0);
		this.merchantId = getIntent().getLongExtra(Extra_MerchantId, 0);
		Object obj = getIntent().getSerializableExtra(Extra_Transaction);
		if (obj != null) {
			this.txn = (Transaction) obj;
			this.txnId = this.txn.id;
			if (this.txn.merchant != null) {
				this.merchantId = this.txn.merchant.id;
			}
		}

		MyNotification notif = MyNotification.getNotification(intent
				.getExtras());
		if ((txnId == null || txnId == 0) && notif != null) {
			this.txnId = notif.getTransactionId();
			this.getTransactionDetails(notif.getCardId(),true);
		}
	}

	private void initialization() {

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);
		mMerchantNameTv = (TextView) findViewById(R.id.merchantNameTv);
		mAddressTv = (TextView) findViewById(R.id.address_tv);
		mTotalRatingTv = (TextView) findViewById(R.id.totalRatingTv);
		mAvgRatingBar = (RatingBar) findViewById(R.id.avgRatingBar);
		mSubmitButtonl = (Button) findViewById(R.id.submitButton);
		mSubmitButtonl.setOnClickListener(this);
		mCommentBox = (EditText) findViewById(R.id.commentBox);

		criteria1 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria1Layout));
		criteria2 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria2Layout));
		criteria3 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria3Layout));
	}

	private void reloadUI() {
		
		Merchant merchant = null;
		if(this.reviewTemplate != null && this.reviewTemplate.merchant != null){
			merchant = this.reviewTemplate.merchant;
		}else if(this.txn != null && this.txn.merchant != null){
			merchant = this.txn.merchant;
		}
		
		if (this.txn != null && this.txn.reviewStatus == ReviewStatus.REVIEWED) {
			mSubmitButtonl.setText("Update");
		}
		
		this.mMerchantNameTv.setText("-----------");
		this.mAddressTv.setText("-----\n-----");
		
		this.mTotalRatingTv.setText("0 Reviews");
		this.criteria1.rootLayout.setVisibility(View.GONE);
		this.criteria2.rootLayout.setVisibility(View.GONE);
		this.criteria3.rootLayout.setVisibility(View.GONE);
		this.mCommentBox.setVisibility(View.GONE);
		
		if(merchant != null){
			this.mMerchantNameTv.setText(merchant.name);
			this.mAddressTv.setText(merchant.address);
		}
		
		Long totalratings = this.totalReview;
		if (totalratings == 1) {
			this.mTotalRatingTv.setText("1 Review");
		} else {
			this.mTotalRatingTv.setText(SingletonClass.stringFromNumber(
					totalratings, 0) + " Reviews");
		}
		mAvgRatingBar.setRating(avgRating.floatValue());
		//mAvgRatingBar.setRating((float) 2.5);
		if (this.reviewTemplate != null) {
			

			if (!this.reviewTemplate.criteria1.isEmpty()) {
				this.criteria1.titleTv.setText(this.reviewTemplate.criteria1);
				this.criteria1.rootLayout.setVisibility(View.VISIBLE);

				if (!this.reviewTemplate.criteria2.isEmpty()) {
					this.criteria2.titleTv
							.setText(this.reviewTemplate.criteria2);
					this.criteria2.rootLayout.setVisibility(View.VISIBLE);

					if (!this.reviewTemplate.criteria3.isEmpty()) {
						this.criteria3.titleTv
								.setText(this.reviewTemplate.criteria3);
						this.criteria3.rootLayout.setVisibility(View.VISIBLE);
					}
				}
			}
			
			if (this.reviewTemplate.isCommentRequired) {
				this.mCommentBox.setVisibility(View.VISIBLE);
			}

		}
	}

	private void loadMerchantReviewCount() {
		APIService service = APIService
				.getReviewCountForMerchant(this, this.merchantId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					JSONObject json = (JSONObject) data;
					avgRating = json.optDouble("average");
					totalReview = json.optLong("total");
					reloadUI();
				}

			}
		});
		service.getData(false);
	}
	
	private void loadReviewTemplate() {
		APIService service = APIService
				.getReviewTemplate(this, this.merchantId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					reviewTemplate = new ReviewTemplate((JSONObject) data);
					reloadUI();
				}

			}
		});
		service.getData(true);
	}
	
	private void loadExistingReviewIfAny() {
		APIService service = APIService
				.getReviewOnTxn(this, txnId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						JSONObject json = (JSONObject) data;
						int critera1Value = Integer.parseInt((json.opt("criteria1Value")+""));
						int critera2Value = Integer.parseInt((json.opt("criteria2Value").toString()+""));
						int critera3Value = Integer.parseInt((json.opt("criteria3Value").toString()+""));
						String comment = json.optString("comment");
						criteria1.ratingBar.setRating(critera1Value);
						criteria2.ratingBar.setRating(critera2Value);
						criteria3.ratingBar.setRating(critera3Value);
						mCommentBox.setText(comment);
					}
					
				}

			}
		});
		service.getData(true);
	}
	
	private void getTransactionDetails(Long cardId, boolean isForeground) {
		APIService service = APIService.getTxnDetails(this, cardId,
				txnId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						txn = new Transaction((JSONObject) data);
						merchantId = txn.merchant.id;
						loadReviewTemplate();
						loadMerchantReviewCount();
						
					}
				}

			}
		});
		service.getData(isForeground);
	}
	
	private void postReview(){
	
		
		try {
			String comment = this.mCommentBox.getText().toString().trim();
			JSONObject json = new JSONObject();
			
			json.put("comment", comment);
			if (!this.reviewTemplate.criteria1.isEmpty()) {
				json.put("criteria1", (int)criteria1.ratingBar.getRating());
				
				if (!this.reviewTemplate.criteria2.isEmpty()) {
					json.put("criteria2", (int)criteria2.ratingBar.getRating());

					if (!this.reviewTemplate.criteria3.isEmpty()) {
						json.put("criteria3", (int)criteria3.ratingBar.getRating());
					}
				}
			}
			
			APIService service = APIService.addReviewOnTxn(this, this.txnId, json);
			service.setServiceListener(new APIService.APIServiceListener() {
				
				@Override
				public void onCompletion(APIService service, Object data, Error error) {
					if(error == null){
						Toast.makeText(ReviewActivity.this, "Thank you for your feedback.", Toast.LENGTH_LONG).show();
						finish();
					}
					
				}
			});
			service.getData(true);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_ib:
			finish();

			break;
		case R.id.submitButton:
			postReview();

			break;
		case R.id.right_ib:
			//Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;
		case R.id.right_extra_ib:
			SingletonClass.showTxnSearchActivity(this,null);
			break;
		default:
			break;
		}
		

	}
}
