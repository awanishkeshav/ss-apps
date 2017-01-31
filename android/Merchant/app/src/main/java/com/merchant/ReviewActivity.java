package com.merchant;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.merchant.R;
import com.merchant.common.SingletonClass;
import com.merchant.dialogs.ReviewResponseDialog;
import com.merchant.handler.CriteriaHolder;
import com.merchant.handler.HeaderLayout;
import com.merchant.models.Error;
import com.merchant.models.Offer;
import com.merchant.models.Review;
import com.merchant.services.APIService;
import com.merchant.utility.DateConverter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewActivity extends BaseActivity implements OnClickListener {

	public static final String Extra_Review = "Extra_Review";
	public static final String Extra_ReviewID = "Extra_ReviewID";
	// public static final String Extra_MerchantId = "Extra_Merchant_ID";

	private HeaderLayout mHeaderLayout;
	private TextView mTitleTv, mDateTv, mExtraInfoTv, mCommentTv;
	private RatingBar mAvgRatingBar;
	private Button mSubmitButtonl;
	private CriteriaHolder criteria1, criteria2, criteria3;

	private Double avgRating = (Double) 0.0;
	private Double totalSpend = (Double) 0.0;
	private Long visitCount = (long) 0;

	private Long reviewId;
	private Review review;

	private ReviewResponseDialog responseDialog;
	private ArrayList<Offer> offers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review);
		handleIntent(getIntent());

		this.initialization();
		this.reloadUI();
		this.getReviewDetails();

	}

	private void handleIntent(Intent intent) {

		this.reviewId = getIntent().getLongExtra(Extra_ReviewID, 0);

		Object obj = getIntent().getSerializableExtra(Extra_Review);
		if (obj != null) {
			this.review = (Review) obj;
			this.reviewId = this.review.id;
		}
	}

	private void initialization() {

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				HeaderLayout.RES_NONE, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);
		mTitleTv = (TextView) findViewById(R.id.reviewTitleTV);
		mDateTv = (TextView) findViewById(R.id.dateTV);
		mExtraInfoTv = (TextView) findViewById(R.id.extraInfoTV);
		mAvgRatingBar = (RatingBar) findViewById(R.id.avgRatingBar);
		mSubmitButtonl = (Button) findViewById(R.id.submitButton);
		mSubmitButtonl.setOnClickListener(this);
		mCommentTv = (TextView) findViewById(R.id.commentBox);

		criteria1 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria1Layout));
		criteria2 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria2Layout));
		criteria3 = new CriteriaHolder(
				(LinearLayout) findViewById(R.id.criteria3Layout));
	}

	private void reloadUI() {

		this.mTitleTv.setText("-----------");
		this.mDateTv.setText("-----");
		this.mExtraInfoTv.setText("0 Visits, $00.00 Total Spend");
		String visitStr = visitCount + " Visits";
		if (visitCount == 1) {
			visitStr = "1 Visit";
		}

		this.mExtraInfoTv.setText(visitStr + ", "
				+ SingletonClass.getPriceString(totalSpend) + " Total Spend");

		this.mCommentTv.setText("No comment provided");
		this.mCommentTv.setTypeface(mCommentTv.getTypeface(), Typeface.ITALIC);
		this.mCommentTv.setTextColor(Color.parseColor("#aaaaaa"));

		this.criteria1.rootLayout.setVisibility(View.GONE);
		this.criteria2.rootLayout.setVisibility(View.GONE);
		this.criteria3.rootLayout.setVisibility(View.GONE);
		if (this.review != null) {
			this.mTitleTv.setText("Reviewed " + this.review.getReviewTitle());
			this.mDateTv.setText(DateConverter.getStringFromDate(this,
					review.date));
			
			if (this.review.comment != null && !this.review.comment.isEmpty()) {
				this.mCommentTv.setText(this.review.comment);
				this.mCommentTv.setTypeface(mCommentTv.getTypeface(), Typeface.NORMAL);
				this.mCommentTv.setTextColor(Color.parseColor("#444444"));
			}
			mAvgRatingBar.setRating(this.avgRating.floatValue());
			if (this.review.reviewTemplate != null) {

				if (!this.review.reviewTemplate.criteria1.isEmpty()) {
					this.criteria1.titleTv
							.setText(this.review.reviewTemplate.criteria1);
					this.criteria1.ratingBar.setRating(this.review.criteria1
							.floatValue());
					this.criteria1.rootLayout.setVisibility(View.VISIBLE);

					if (!this.review.reviewTemplate.criteria2.isEmpty()) {
						this.criteria2.titleTv
								.setText(this.review.reviewTemplate.criteria2);
						this.criteria2.ratingBar
								.setRating(this.review.criteria2.floatValue());
						this.criteria2.rootLayout.setVisibility(View.VISIBLE);

						if (!this.review.reviewTemplate.criteria3.isEmpty()) {
							this.criteria3.titleTv
									.setText(this.review.reviewTemplate.criteria3);
							this.criteria3.ratingBar
									.setRating(this.review.criteria3
											.floatValue());
							this.criteria3.rootLayout
									.setVisibility(View.VISIBLE);
						}
					}
				}
			}
		}
	}

	private void getReviewDetails() {

		APIService service = APIService.getReviewDetails(this, reviewId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						JSONObject json = (JSONObject) data;
						review = new Review(json.optJSONObject("review"));
						visitCount = json.optLong("visits");
						avgRating = json.optDouble("averageRating");
						totalSpend = json.optDouble("spend");
						reloadUI();

					}
				}

			}
		});
		service.getData(false);
	}

	private void getAllOffers() {
		APIService service = APIService.getAllOffers(this, false);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {

				if (error == null) {
					if (data instanceof JSONArray) {
						offers = new ArrayList<Offer>();
						JSONArray arr = (JSONArray) data;
						for (int i = 0; i < arr.length(); i++) {
							Offer offer = new Offer(arr.optJSONObject(i));
							offers.add(offer);
						}

						if (responseDialog != null) {
							responseDialog.setmOffers(offers);
						}

					}
				}
			}
		});
		service.getData(false);
	}

	private void showReviewResponseDialog() {
		if (responseDialog == null) {
			responseDialog = new ReviewResponseDialog(this, reviewId);
		}
		if (offers == null) {
			getAllOffers();
		} else {
			responseDialog.setmOffers(offers);
		}
		if (!responseDialog.isShowing()) {
			responseDialog.show();
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
			showReviewResponseDialog();
			break;
		case R.id.right_ib:
			// Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;
		case R.id.right_extra_ib:

			break;
		default:
			break;
		}

	}
}
