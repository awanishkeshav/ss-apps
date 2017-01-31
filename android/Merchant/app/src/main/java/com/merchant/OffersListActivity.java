package com.merchant;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.merchant.adapters.OfferListAdapter;
import com.merchant.handler.FooterTabHandler;
import com.merchant.handler.FooterTabHandler.FooterTabListener;
import com.merchant.handler.HeaderLayout;
import com.merchant.handler.LoadingHolder;
import com.merchant.models.Error;
import com.merchant.models.Offer;
import com.merchant.models.Offer.Status;
import com.merchant.rows.OfferListItemRow;
import com.merchant.services.APIService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class OffersListActivity extends BaseActivity implements
		FooterTabListener, View.OnClickListener {

	public static String Extra_IS_CONFIG_FOR_ARCHIVED = "extra_is_config_for_archived";

	private HeaderLayout mHeaderLayout;
	private FooterTabHandler mFooterTabHandler;
	private ListView mListView;
	private TextView headerTv;
	private LoadingHolder mLoadingHolder;
	private OfferListAdapter mOfferListAdapter;
	private APIService mOfferService;
	private ArrayList<Offer> mOfferList;

	private boolean configuiredForArchivedOffers = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers_list);
		initialization();

		reloadData();
	}

	private void initialization() {
		// LayoutInflater inflater = (LayoutInflater)
		// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		configuiredForArchivedOffers = getIntent().getBooleanExtra(
				Extra_IS_CONFIG_FOR_ARCHIVED, false);
		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setListener(this, this, this, this);
		mHeaderLayout.setHeaderIITII(R.drawable.selector_back_arrow_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				HeaderLayout.RES_NONE, R.drawable.icon_menu_white);
		mHeaderLayout.setTitle("OFFERS");

		mListView = (ListView) findViewById(R.id.offersLV);
		headerTv = (TextView) findViewById(R.id.headerTV);

		mLoadingHolder = new LoadingHolder(findViewById(R.id.loadingLayout));
		mLoadingHolder.setLoadingText("Getting offers...");

		View footerView = findViewById(R.id.tab_footer);
		mFooterTabHandler = new FooterTabHandler(footerView);
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_archive,
				FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE,
				FooterTabHandler.RES_NONE);
		mFooterTabHandler.setTabTitles("Archived", null, null, null);

		ImageButton plusButton = (ImageButton) findViewById(R.id.addBtn);
		plusButton.setOnClickListener(this);
		if (this.isConfiguiredForArchivedOffers()) {
			plusButton.setVisibility(View.INVISIBLE);
			footerView.setVisibility(View.GONE);
			mHeaderLayout.setSubtitle("Archived");
		}

		// objects..
		mOfferListAdapter = new OfferListAdapter(this);
		mListView.setAdapter(mOfferListAdapter);

	}
	
	@Override
	public void onResume() {
		super.onResume();

		if (mOfferService != null && !mOfferService.isRequestRunnung()) {
			getAllOffersService(false);
		}
		
	}

	private boolean isConfiguiredForArchivedOffers() {

		return configuiredForArchivedOffers;
	}

	private void reloadListHeaderView() {

		String headerText = "";

		if (this.mOfferList != null && this.mOfferList.size() == 0) {
			headerText = "No Offers Found";
		} else {
			int activeCnt = 0;
			if (mOfferList != null) {
				for (Offer offer : mOfferList) {
					if (offer.status == Status.Active) {
						activeCnt++;
					}
				}

				String offerCntStr = "1 Offer";
				if (mOfferList.size() != 1) {
					offerCntStr = mOfferList.size() + " Offers";
				}
				if (isConfiguiredForArchivedOffers()) {
					headerText = offerCntStr;
				} else {
					String activeCntStr = activeCnt + " Active";
					headerText = offerCntStr + ", " + activeCntStr;
				}
			}

		}

		headerTv.setText(headerText);

		this.mListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mOfferListAdapter != null) {
							Object item = mOfferListAdapter.getItem(position);
							if (item instanceof OfferListItemRow) {
								Offer offer = ((OfferListItemRow) item).offer;
								Intent intent = new Intent(
										OffersListActivity.this,
										OfferDetailsActivity.class);
								intent.putExtra(
										OfferDetailsActivity.ExtraOfferVO,
										offer);
								startActivity(intent);
							}

						}

					}
				});

	}

	private void prepareListItem() {
		ArrayList<OfferListItemRow> source = new ArrayList<OfferListItemRow>();

		if (mOfferList != null) {
			for (Offer offer : mOfferList) {
				source.add(new OfferListItemRow(offer,
						this.configuiredForArchivedOffers));
			}
		}
		mOfferListAdapter.setList(source);
	}

	private void getAllOffersService(boolean showLoading) {

		if (mOfferService != null) {
			mOfferService.cancelRequest();
		}

		mLoadingHolder.setVisibile(showLoading);
		
		String status = null;
		if (isConfiguiredForArchivedOffers()) {
			status = "Archived";
		}
		mOfferService = APIService.getAllOffersFromMerchant(this,status);
		mOfferService.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				mLoadingHolder.setVisibile(false);
				if (error == null) {
					if (data instanceof JSONArray) {
						mOfferList = new ArrayList<Offer>();
						JSONArray jsonArray = (JSONArray) data;
						for (int i = 0; i < jsonArray.length(); i++) {
							try {
								JSONObject json = jsonArray.getJSONObject(i);

								mOfferList.add(new Offer(json));
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
		mOfferService.getData(false);
	}

	public void reloadData() {
		this.mOfferList = null;
		reloadListHeaderView();
		getAllOffersService(true);
	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:

			Intent intent = new Intent(this, OffersListActivity.class);
			intent.putExtra(OffersListActivity.Extra_IS_CONFIG_FOR_ARCHIVED,
					true);
			startActivity(intent);

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
		case R.id.addBtn:
			Intent intent = new Intent(
					OffersListActivity.this,
					OfferDetailsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}
}
