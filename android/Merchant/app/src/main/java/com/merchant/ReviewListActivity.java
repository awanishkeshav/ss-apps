package com.merchant;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.merchant.adapters.ReviewAdapter;
import com.merchant.common.SingletonClass;
import com.merchant.dialogs.ReviewCategoryEntryDialog;
import com.merchant.handler.DateRangeSelector;
import com.merchant.handler.FooterTabHandler;
import com.merchant.handler.FooterTabHandler.FooterTabListener;
import com.merchant.handler.HeaderLayout;
import com.merchant.handler.LoadingHolder;
import com.merchant.models.Error;
import com.merchant.models.Review;
import com.merchant.rows.MyListItem;
import com.merchant.rows.ReviewListItemRow;
import com.merchant.rows.TranxListItemHeader;
import com.merchant.services.APIService;
import com.merchant.utility.DateUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewListActivity extends BaseActivity implements
		FooterTabListener, View.OnClickListener {

	private HeaderLayout mHeaderLayout;
	public FooterTabHandler mFooterTabHandler;
	private ListView mListView;
	private View filterView, noDataView;
	private LoadingHolder mLoadingHolder;
	private ArrayList<Review> mReviewsList;
	private ReviewAdapter mReviewAdapter;
	private APIService mReviewService;
	private DateRangeSelector dateSelector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_review_list);
		initialization();
		getReviewsService(false);
	}

	private void initialization() {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setListener(this, this, this, this);
		mHeaderLayout.setHeaderIITII(R.drawable.selector_back_arrow_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				HeaderLayout.RES_NONE, R.drawable.icon_menu_white);
		mHeaderLayout.setTitle("REVIEWS");

		mListView = (ListView) findViewById(R.id.transactionLv);
		filterView 	= inflater.inflate(R.layout.transactions_section_header,null, false);
		noDataView 	= inflater.inflate(R.layout.transactions_section_header,null, false);

		dateSelector = new DateRangeSelector(
				findViewById(R.id.dateRangeSelector), SingletonClass
						.sharedInstance().getbConstants().getDateRangeStrings());
		dateSelector.listener = new DateRangeSelector.DateRangeSelectorListener() {

			@Override
			public void onSelectionChange(int pos, String title) {
				getReviewsService(false);

			}
		};
		mLoadingHolder = new LoadingHolder(findViewById(R.id.loadingLayout));

		mFooterTabHandler = new FooterTabHandler(findViewById(R.id.tab_footer));
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_gear,
				FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE,
				FooterTabHandler.RES_NONE);
		mFooterTabHandler.setTabTitles("Feedback Setup", null, null, null);
		mFooterTabHandler.removeLastTab();

		// objects..
		mReviewAdapter = new ReviewAdapter(this);

	}

	private void prepareListItem() {
		ArrayList<MyListItem> source = new ArrayList<MyListItem>();
		String[] orderedKey = { DateUtil.KEY_TODAY, DateUtil.KEY_YESTERDAY,
				DateUtil.KEY_TWO_DAYS_AGO, DateUtil.KEY_LAST_WEEK,
				DateUtil.KEY_LAST_MONTH, DateUtil.KEY_OLDER };

		HashMap<String, ArrayList<Review>> map = DateUtil
				.groupDates(mReviewsList);

		int sectionIndex = 0;
		for (int i = 0; i < orderedKey.length; i++) {
			String key = orderedKey[i];
			ArrayList<Review> reviews = map.get(key);
			if (reviews.size() > 0) {
				int total = reviews.size();
				String reviewCntStr = "1 Review";
				if (total != 1) {
					reviewCntStr = SingletonClass.stringFromNumber(total, 0) + " Reviews";
				}
				
				String headerTitle = key + " ("
						+ reviewCntStr
						+ ")";
				if (sectionIndex == 0) {
					source.add(new TranxListItemHeader(headerTitle, false));
				} else {
					source.add(new TranxListItemHeader(headerTitle, true));
				}

				for (Review review : reviews) {
					source.add(new ReviewListItemRow(this, review));
				}
				sectionIndex++;
			}
		}
		mReviewAdapter.setList(source);
	}

	private void reloadListHeaderView() {
		this.mListView.setAdapter(mReviewAdapter);
		
		this.mListView.removeFooterView(noDataView);
		if (mReviewsList != null && this.mReviewsList.size() == 0) {
			// Prevent crash for earlier version then KITKAT
			this.mListView.setAdapter(null);
			this.mListView.addFooterView(noDataView);
			this.mListView.setAdapter(mReviewAdapter);
			TextView headerTitleTextView = (TextView) noDataView
					.findViewById(R.id.headerTitleTv);
			headerTitleTextView.setText("No Reviews found");
		}

		this.mListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mReviewAdapter != null) {
							Object item = mReviewAdapter.getItem((int) id);
							if (item instanceof ReviewListItemRow) {
								Review review = ((ReviewListItemRow) item).review;
								Intent intent = new Intent(ReviewListActivity.this, ReviewActivity.class);
								intent.putExtra(ReviewActivity.Extra_Review, review);
								startActivity(intent);

							}

						}

					}
				});

	}

	private void getReviewsService(boolean showLoading) {

		if (mReviewService != null) {
			mReviewService.cancelRequest();
		}

		mLoadingHolder.setVisibile(true);

		if (SingletonClass.sharedInstance().getbConstants().isLoaded()) {
			String dateRangeKey = SingletonClass.sharedInstance()
					.getbConstants().dateRanges
					.get(dateSelector.getSelection()).key;
			mReviewService = APIService.getMerchantReviews(this, dateRangeKey);
			mReviewService
					.setServiceListener(new APIService.APIServiceListener() {

						@Override
						public void onCompletion(APIService service,
								Object data, Error error) {
							mLoadingHolder.setVisibile(false);
							if (error == null) {
								if (data instanceof JSONArray) {
									mReviewsList = new ArrayList<Review>();
									JSONArray jsonArray = (JSONArray) data;
									for (int i = 0; i < jsonArray.length(); i++) {
										try {
											JSONObject json = jsonArray
													.getJSONObject(i);
											mReviewsList.add(new Review(json));
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									prepareListItem();
									reloadListHeaderView();

								}
							}

						}
					});
			mReviewService.getData(showLoading);
		}
	}

	private void showAddReviewCategoriesDialong() {
		ReviewCategoryEntryDialog dialog = new ReviewCategoryEntryDialog(this);
		dialog.show();
	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:
			showAddReviewCategoriesDialong();
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_ib:
			finish();
			break;
		case R.id.right_ib:
			showDrawer();
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackendConstantsLoad(Error error) {
		super.onBackendConstantsLoad(error);
		dateSelector.setTitles(SingletonClass.sharedInstance().getbConstants()
				.getDateRangeStrings());
		getReviewsService(false);
	}

}
