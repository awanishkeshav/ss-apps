package com.merchant.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.merchant.MerchantProfileActivity;
import com.merchant.OffersListActivity;
import com.merchant.R;
import com.merchant.ReviewActivity;
import com.merchant.ReviewListActivity;
import com.merchant.adapters.ReviewAdapter;
import com.merchant.common.SingletonClass;
import com.merchant.handler.DateRangeSelector;
import com.merchant.handler.FooterTabHandler;
import com.merchant.handler.FooterTabHandler.FooterTabListener;
import com.merchant.handler.StateLayoutHolder;
import com.merchant.models.Error;
import com.merchant.models.KeyValueObject;
import com.merchant.models.MerchantSummary;
import com.merchant.models.Review;
import com.merchant.rows.MyListItem;
import com.merchant.rows.ReviewListItemRow;
import com.merchant.services.APIService;

public class HomeFragment extends Fragment implements OnClickListener,
		FooterTabListener {

	private int nummberOfTxnToShow = 20;
	private ListView mListView;
	private TextView noTranxTv, serviceCountTv;
	private RelativeLayout mSpendRl;
	private DateRangeSelector dateSelector;
	private FooterTabHandler footerTabHandler;
	private StateLayoutHolder mVisitHolder, mSpendHolder, mReviewHolder;
	private Long serviceRequestCount = (long) 0;
	private APIService summaryService;
	private APIService mReviewService;
	private MerchantSummary summary;
	

	ArrayList<Review> mReviewList;

	private ReviewAdapter mReviewAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.home_fragment, container,
				false);

		return rootView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initialization(view);
		setData();
		getSummary(false);
		getReviewsService();
		reloadList();
	}

	private void initialization(View view) {
		mListView = (ListView) view.findViewById(R.id.transactionList);
		mSpendRl = (RelativeLayout) view.findViewById(R.id.SpendRl);
		noTranxTv = (TextView) view.findViewById(R.id.noReviewsTv);
		serviceCountTv = (TextView) view.findViewById(R.id.serviceCountTV);

		dateSelector = new DateRangeSelector(
				view.findViewById(R.id.dateRangeSelector), SingletonClass
						.sharedInstance().getbConstants().getDateRangeStrings());
		dateSelector.listener = new DateRangeSelector.DateRangeSelectorListener() {

			@Override
			public void onSelectionChange(int pos, String title) {
				getSummary(true);

			}
		};
		footerTabHandler = new FooterTabHandler(
				view.findViewById(R.id.tab_footer));
		footerTabHandler.handleTabs(-1, this);
		footerTabHandler.setTabs(R.drawable.icon_tags,
				R.drawable.icon_review_blue, R.drawable.icon_gear,
				FooterTabHandler.RES_NONE);
		footerTabHandler.setTabTitles("Offers", "Reviews", "Profile", "");
		mSpendRl.setOnClickListener(this);

		// objects..
		mReviewAdapter = new ReviewAdapter(getActivity());
		mVisitHolder = new StateLayoutHolder(
				view.findViewById(R.id.stateVisit), false);
		mVisitHolder.rootView.setOnClickListener(this);
		mSpendHolder = new StateLayoutHolder(
				view.findViewById(R.id.stateSpend), false);
		mSpendHolder.rootView.setOnClickListener(this);
		mReviewHolder = new StateLayoutHolder(
				view.findViewById(R.id.stateReview), true);
		mReviewHolder.rootView.setOnClickListener(this);
		mReviewHolder.setRatingBarVisible();
		
		noTranxTv.setVisibility(View.VISIBLE);
		noTranxTv.setText("Getting recent reviews");

	}

	private void setData() {

		mVisitHolder.mLabelTv.setText("Visits");
		mSpendHolder.mLabelTv.setText("Total Spend");
		mVisitHolder.mCaptionTv.setText("0");
		mSpendHolder.mCaptionTv.setText("$00.00");
		mReviewHolder.mLabelTv.setText("0 Reviews");
		mReviewHolder.setRatingBarVisible();
		mReviewHolder.mRatingBar.setRating(0);

		if (summary != null) {
			if (summary.visitCount == 1) {
				mVisitHolder.mLabelTv.setText("Visit");
			}
			mVisitHolder.mCaptionTv.setText(SingletonClass.stringFromNumber(
					summary.visitCount, 0));

			mSpendHolder.mCaptionTv.setText(SingletonClass
					.getPriceString(summary.totalSpend));
			if (summary.reviewCount == 1) {
				mReviewHolder.mLabelTv.setText("1 Review");
			} else {
				mReviewHolder.mLabelTv.setText(SingletonClass.stringFromNumber(
						summary.reviewCount, 0) + " Reviews");
			}

			mReviewHolder.mRatingBar.setRating(summary.avgRating.floatValue());

		}

		serviceCountTv.setText("NO SERVICE REQUESTS");
		if (serviceRequestCount > 0) {
			if (serviceRequestCount == 1) {
				serviceCountTv.setText("1 SERVICE REQUEST");
			} else {
				serviceCountTv.setText(serviceRequestCount
						+ " SERVICE REQUESTS");
			}
		}
	}

	private void reloadList() {

		if (mReviewList != null) {
			ArrayList<MyListItem> source = new ArrayList<MyListItem>();
			for (Review review : mReviewList) {
				source.add(new ReviewListItemRow(this.getActivity(), review));
			}
			mReviewAdapter.setList(source);
			mListView.setAdapter(mReviewAdapter);
			this.mListView
					.setOnItemClickListener(new AdapterView.OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							if (mReviewAdapter != null) {
								Object item = mReviewAdapter.getItem(position);
								if (item instanceof ReviewListItemRow) {
									Review review = ((ReviewListItemRow) item).review;
									Intent intent = new Intent(HomeFragment.this.getActivity(), ReviewActivity.class);
									intent.putExtra(ReviewActivity.Extra_Review, review);
									startActivity(intent);

								}

							}

						}
					});
		}
	}

	/** ...//// Listeners..///.. */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		getReviewsService();
	}

	/** ...//// Web Services..///.. */

	public void getSummary(boolean showLoadingIndicator) {

		if (SingletonClass.sharedInstance().getbConstants().isLoaded()) {
			String dateRangeKey = SingletonClass.sharedInstance()
					.getbConstants().dateRanges
					.get(dateSelector.getSelection()).key;
			if (summaryService != null) {
				summaryService.cancelRequest();
			}
			summaryService = APIService.getMerchantSummary(getActivity(), dateRangeKey);
			summaryService.setServiceListener(new APIService.APIServiceListener() {
				
				@Override
				public void onCompletion(APIService service, Object data, Error error) {
					if (error == null) {
						if (data instanceof JSONObject) {
							summary = new MerchantSummary((JSONObject) data);
						}
						
						setData();
						
					}
					
				}
			});
			summaryService.getData(showLoadingIndicator);
			
		}

	}
	
	private void getReviewsService() {

		if (mReviewService != null) {
			mReviewService.cancelRequest();
		}

		mReviewService = APIService.getMerchantReviews(getActivity(),null);
		mReviewService.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				
				if (error == null) {
					if (data instanceof JSONArray) {
						noTranxTv.setVisibility(View.VISIBLE);
						noTranxTv.setText("Recent Reviews");
						
						mReviewList = new ArrayList<Review>();
						JSONArray jsonArray = (JSONArray) data;
						for (int i = 0; i < jsonArray.length(); i++) {
							try {
								if (mReviewList.size() == nummberOfTxnToShow) {
									break;
								}
								JSONObject json = jsonArray.getJSONObject(i);
								mReviewList.add(new Review(json));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (mReviewList.isEmpty()) {
							noTranxTv.setVisibility(View.VISIBLE);
							noTranxTv.setText("No Reviews");
						}
						
						reloadList();
					}
				}
			}
		});
		mReviewService.getData(false);
	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		
		case R.id.tab_1_tv:
			Intent intent = new Intent(getActivity(), OffersListActivity.class);
			startActivity(intent);
			break;
		case R.id.tab_2_tv:
			intent = new Intent(getActivity(), ReviewListActivity.class);
			getActivity().startActivity(intent);
			break;
		case R.id.tab_3_tv:
			intent = new Intent(getActivity(), MerchantProfileActivity.class);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}

	}

	public void onBackendConstantsLoad(Error error) {
		if (error == null) {
			dateSelector.setTitles(SingletonClass.sharedInstance()
					.getbConstants().getDateRangeStrings());
			getSummary(false);
		}
	}

}
