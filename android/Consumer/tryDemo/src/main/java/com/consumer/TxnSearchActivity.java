package com.consumer;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.adapters.TagAdapter;
import com.consumer.adapters.TransactionAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.dialogs.AddTagDialog;
import com.consumer.gcm.MyNotification;
import com.consumer.handler.FooterTabHandler;
import com.consumer.handler.HeaderLayout;
import com.consumer.handler.StateLayoutHolder;
import com.consumer.handler.FooterTabHandler.FooterTabListener;
import com.consumer.models.Error;
import com.consumer.models.Offer;
import com.consumer.models.Tag;
import com.consumer.models.Transaction;
import com.consumer.models.Transaction.ReviewStatus;
import com.consumer.models.TxnCategory.Type;
import com.consumer.models.TxnMisc;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItemHeader;
import com.consumer.rows.TranxListItemRow;
import com.consumer.services.APIService;
import com.consumer.utility.DateConverter;
import com.consumer.utility.DateUtil;
import com.consumer.utility.DialogUtil;

public class TxnSearchActivity extends BaseActivity implements OnClickListener {

	public static String ExtraSearchParam = "ExtraSearchParam";
	private HeaderLayout mHeaderLayout;
	private ListView mListView;
	// private ListView mTagListView;

	private View filterView;
	private View noDataView;
	private LinearLayout mLoadingLayout;

	private ArrayList<Transaction> mTransactionList;
	private TransactionAdapter mTransactionAdapter;
	private APIService mTransactionService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_txn_search);
		controlInitialization();
		
		String searchParam = getIntent().getStringExtra(ExtraSearchParam);
		if (searchParam != null && !searchParam.isEmpty()) {
			mHeaderLayout.mSearchEt.setText(searchParam);
			getTransactionsService();
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}else{
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		}

	}

	private void controlInitialization() {

		

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setSearchBarVisible();
		mHeaderLayout.setListener(this, this, this, this);
		mHeaderLayout.mSearchEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				getTransactionsService();

			}
		});

		SingletonClass.showSoftKeyboard(mHeaderLayout.mSearchEt);
		mListView = (ListView) findViewById(R.id.transactionLv);
		mListView.bringToFront();

		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoadingLayout = (LinearLayout) findViewById(R.id.loadingLayout);
		mLoadingLayout.setVisibility(View.GONE);
		filterView = inflater.inflate(R.layout.transactions_section_header,
				null, false);
		noDataView = inflater.inflate(R.layout.transactions_section_header,
				null, false);

		// popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT);
		// objects..
		mTransactionAdapter = new TransactionAdapter(this);
		mListView.setAdapter(mTransactionAdapter);

	}

	private void reloadListHeaderView() {
		this.mListView.removeHeaderView(filterView);

		this.mListView.removeFooterView(noDataView);
		if (this.mTransactionList != null && this.mTransactionList.size() == 0) {
			// Prevent crash for earlier version then KITKAT
			this.mListView.setAdapter(null);
			this.mListView.addFooterView(noDataView);
			this.mListView.setAdapter(mTransactionAdapter);
			TextView headerTitleTextView = (TextView) noDataView
					.findViewById(R.id.headerTitleTv);
			headerTitleTextView.setText("No transactions found");
		}

		this.mListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mTransactionAdapter != null) {
							Object item = mTransactionAdapter.getItem((int) id);
							if (item instanceof TranxListItemRow) {
								Transaction txn = ((TranxListItemRow) item).tranx;
								Intent intent = new Intent(
										TxnSearchActivity.this,
										TransDetailActivity.class);
								intent.putExtra(
										TransDetailActivity.ExtraTransactionVO,
										txn);
								TxnSearchActivity.this.startActivity(intent);
							}

						}

					}
				});

	}

	private void prepareListItem() {
		ArrayList<TranxListItem> source = new ArrayList<TranxListItem>();
		String[] orderedKey = { DateUtil.KEY_TODAY, DateUtil.KEY_YESTERDAY,
				DateUtil.KEY_TWO_DAYS_AGO, DateUtil.KEY_LAST_WEEK,
				DateUtil.KEY_LAST_MONTH, DateUtil.KEY_OLDER };

		HashMap<String, ArrayList<Transaction>> map = DateUtil
				.groupDates(mTransactionList);

		int sectionIndex = 0;
		for (int i = 0; i < orderedKey.length; i++) {
			String key = orderedKey[i];
			ArrayList<Transaction> transactions = map.get(key);
			if (transactions.size() > 0) {
				double total = 0;
				for (Transaction transaction : transactions) {
					total += transaction.amount;
				}
				String headerTitle = key + " ("
						+ NumberFormat.getCurrencyInstance().format((total))
						+ ")";
				if (sectionIndex == 0) {
					source.add(new TranxListItemHeader(headerTitle, false));
				} else {
					source.add(new TranxListItemHeader(headerTitle, true));
				}

				for (Transaction transaction : transactions) {
					source.add(new TranxListItemRow(this, transaction));
				}
				sectionIndex++;
			}
		}
		mTransactionAdapter.setList(source);
	}

	private void getTransactionsService() {

		if (mTransactionService != null) {
			mTransactionService.cancelRequest();
		}
		String searchTerm = mHeaderLayout.mSearchEt.getText().toString().trim();
		if (searchTerm.length() < 2) {
			return;
		}

		Long cardId = new PreferanceUtil(this).getCardId();

		mLoadingLayout.setVisibility(View.VISIBLE);
		mTransactionService = APIService.searchTransactions(this, cardId,
				searchTerm, 0, 100);
		mTransactionService
				.setServiceListener(new APIService.APIServiceListener() {

					@Override
					public void onCompletion(APIService service, Object data,
							Error error) {
						mLoadingLayout.setVisibility(View.GONE);
						if (error == null) {
							if (data instanceof JSONArray) {
								mTransactionList = new ArrayList<Transaction>();
								JSONArray jsonArray = (JSONArray) data;
								for (int i = 0; i < jsonArray.length(); i++) {
									try {
										JSONObject json = jsonArray
												.getJSONObject(i);
										mTransactionList.add(new Transaction(
												json));
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
		mTransactionService.getData(false);
	}

	/** .....////... LISTENERS.....///.... */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.left_ib:
			finish();
			break;

		case R.id.right_ib:
			// Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;

		}

	}

}
