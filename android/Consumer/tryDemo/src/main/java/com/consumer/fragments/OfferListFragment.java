package com.consumer.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.consumer.OfferDetailsActivity;
import com.consumer.R;
import com.consumer.adapters.OfferListAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.models.Error;
import com.consumer.models.Offer;
import com.consumer.rows.OfferListItemRow;
import com.consumer.services.APIService;

public class OfferListFragment extends Fragment implements OnClickListener {

	private static String KeySavedStateIsNearBy = "KeySavedStateIsNearBy";

	private boolean configuiredForNearByOffers = false;

	private ListView mListView;
	private View headerView;;
	private LinearLayout mLoadingLayout;
	private TextView mNoDataTv;
	private RelativeLayout mShowNearbyOffersRL, mClearNearbyOffersRL;
	private OfferListAdapter mOfferListAdapter;
	private APIService mOfferService;
	private ArrayList<Offer> mOfferList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			configuiredForNearByOffers = savedInstanceState
					.getBoolean(KeySavedStateIsNearBy);
		}

		View rootView = inflater.inflate(R.layout.offer_list_fragment,
				container, false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initialization(view);
		getAllOffersService();

	}

	public boolean isConfiguiredForNearByOffers() {
		return configuiredForNearByOffers;
	}

	public void setConfiguiredForNearByOffers(boolean configuiredForNearByOffers) {
		this.configuiredForNearByOffers = configuiredForNearByOffers;
	}

	private void initialization(View view) {

		mListView = (ListView) view.findViewById(R.id.offersLv);
		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);

		mShowNearbyOffersRL = (RelativeLayout) view
				.findViewById(R.id.showNearbyOffersRL);
		mClearNearbyOffersRL = (RelativeLayout) view
				.findViewById(R.id.clearNearbyOffersRL);
		mShowNearbyOffersRL.setOnClickListener(this);
		mClearNearbyOffersRL.setOnClickListener(this);
		mClearNearbyOffersRL.setVisibility(View.GONE);
		mShowNearbyOffersRL.setVisibility(View.GONE);

		mNoDataTv = (TextView) view.findViewById(R.id.noDataTv);
		mNoDataTv.setVisibility(View.GONE);
		headerView = inflater.inflate(R.layout.transactions_section_header,
				null, false);
		mOfferListAdapter = new OfferListAdapter(getActivity());
		mListView.setAdapter(mOfferListAdapter);
	}

	private void reloadListHeaderView() {

		if (this.isConfiguiredForNearByOffers()) {

			mClearNearbyOffersRL.setVisibility(View.VISIBLE);
			mShowNearbyOffersRL.setVisibility(View.GONE);
		} else {
			mClearNearbyOffersRL.setVisibility(View.GONE);
			mShowNearbyOffersRL.setVisibility(View.VISIBLE);
		}

		mNoDataTv.setVisibility(View.GONE);
		if (this.mOfferList != null && this.mOfferList.size() == 0) {
			mNoDataTv.setVisibility(View.VISIBLE);
		}

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
								Intent intent = new Intent(getActivity(),
										OfferDetailsActivity.class);
								intent.putExtra(
										OfferDetailsActivity.ExtraOfferVO,
										offer);
								getActivity().startActivity(intent);
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
						this.configuiredForNearByOffers));
			}
		}
		mOfferListAdapter.setList(source);
	}

	private void getAllOffersService() {

		if (mOfferService != null) {
			mOfferService.cancelRequest();
		}

		//Long cardId = new PreferanceUtil(this.getActivity()).getCardId();

		mLoadingLayout.setVisibility(View.VISIBLE);
		mOfferService = APIService.getAllOffers(this.getActivity(),
				configuiredForNearByOffers);
		mOfferService.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				mLoadingLayout.setVisibility(View.GONE);
				if (error == null) {
					if (data instanceof JSONArray) {
						
						if (!configuiredForNearByOffers) {
							markOffersRead();
						}
						
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
	
	private void markOffersRead(){
		
		APIService service = APIService.markReadNewOffers(getActivity());
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					
				}
				
				

			}
		});
		service.getData(false);
	}
	public void reloadData(){
		this.mOfferList = null;
		reloadListHeaderView();
		getAllOffersService();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(KeySavedStateIsNearBy, configuiredForNearByOffers);
		super.onSaveInstanceState(outState);
	}
	
	private void showNearByOffers(){
		this.configuiredForNearByOffers = true;
		this.mOfferList = null;
		reloadListHeaderView();
		getAllOffersService();
	}
	
	private void hideNearByOffers(){
		this.configuiredForNearByOffers = false;
		this.mOfferList = null;
		reloadListHeaderView();
		getAllOffersService();
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.showNearbyOffersRL:
			showNearByOffers();
			break;

		case R.id.clearNearbyOffersRL:
			hideNearByOffers();
			break;

		default:
			break;
		}

	}

}
