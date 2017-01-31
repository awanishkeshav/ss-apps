package com.consumer.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.ManageSpendActivity;
import com.consumer.SpendActivity;
import com.consumer.TransDetailActivity;
import com.consumer.R;
import com.consumer.adapters.TransactionAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.models.Card;
import com.consumer.models.Card.CardType;
import com.consumer.models.Card.Status;
import com.consumer.models.Error;
import com.consumer.models.Transaction;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItemRow;
import com.consumer.services.APIService;
import com.consumer.utility.DialogUtil;

public class HomeFragment extends Fragment implements OnClickListener {

	private int nummberOfTxnToShow = 20;
	private ListView mListView;
	private ImageView mLockBtn, mCallBtn;
	private TextView mAvailTv, mBalanceTv, mBalCaptionTv, noTranxTv, mDecimalAvail, mDecimalBal, mNewOffersTv;
	private LinearLayout mManageLl , mCallLl;
	private RelativeLayout mSpendRl;
	private View mLockBtnBg;
	private Long newOfferCnt = 0l;
	
	
	
	private String mClientPhone;
	ArrayList<Transaction> mTransactionList;

	private TransactionAdapter mTransactionAdapter;

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
		reloadUI();
		//getCardDetails(false);
	}

	private void initialization(View view) {
		mListView 	= (ListView) view.findViewById(R.id.transactionList);
		mAvailTv 	= (TextView) view.findViewById(R.id.availableTv);
		mBalanceTv 	= (TextView) view.findViewById(R.id.balanceTv);
		mBalCaptionTv = (TextView) view.findViewById(R.id.balCaptionTv);
		mManageLl	= (LinearLayout) view.findViewById(R.id.manageLl);
		mSpendRl	= (RelativeLayout) view.findViewById(R.id.SpendRl);
		mCallLl		= (LinearLayout) view.findViewById(R.id.btnCAllkBg);
		

		mDecimalBal	  = (TextView) view.findViewById(R.id.DecimalBalTv);
		mDecimalAvail = (TextView) view.findViewById(R.id.decimalAvailTv);
		noTranxTv = (TextView) view.findViewById(R.id.noTranxTv);
		mLockBtn  = (ImageView) view.findViewById(R.id.btnLock);
		mLockBtnBg = (LinearLayout) view.findViewById(R.id.btnLockBg);
		mCallBtn = (ImageView) view.findViewById(R.id.btnCall);
		
		mNewOffersTv = (TextView) view.findViewById(R.id.newOfferCountTv);

		mCallBtn.setOnClickListener(this);
		mLockBtn.setOnClickListener(this);
		mManageLl.setOnClickListener(this);
		mSpendRl.setOnClickListener(this);
		mCallLl.setOnClickListener(this);
		mLockBtnBg.setOnClickListener(this);
		// objects..
		mTransactionAdapter = new TransactionAdapter(getActivity());
		mListView.setAdapter(mTransactionAdapter);

	}

	private void reloadUI() {

		if (mTransactionList != null) {
			ArrayList<TranxListItem> source = new ArrayList<TranxListItem>();
			for (Transaction tranx : mTransactionList) {
				source.add(new TranxListItemRow(this.getActivity(), tranx));
			}
			mTransactionAdapter.setList(source);
			//mListView.setAdapter(mTransactionAdapter);
			 this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if(mTransactionAdapter != null){
							Object item = mTransactionAdapter.getItem(position);
							if (item instanceof TranxListItemRow) {
								Transaction txn = ((TranxListItemRow)item).tranx;
								Intent intent = new Intent(getActivity() , TransDetailActivity.class);
								intent.putExtra(TransDetailActivity.ExtraTransactionVO, txn);
								getActivity().startActivity(intent);
							}
							
						}
						
					}
				});
		}

		Card card = SingletonClass.sharedInstance().getCard();
		if (card != null) {
			if (card.status == Status.ACTIVE) {
				mLockBtn.setImageResource(R.drawable.icon_unlocked);
				mLockBtnBg.setBackgroundResource(R.drawable.selector_grey);

			} else {
				mLockBtn.setImageResource(R.drawable.icon_locked);
				mLockBtnBg.setBackgroundResource(R.drawable.selector_red);
			}

			if (card.cardType != CardType.CREDIT) {
				mBalanceTv.setVisibility(View.GONE);
				mBalCaptionTv.setVisibility(View.GONE);
			} else {
				mBalanceTv.setVisibility(View.VISIBLE);
				mBalCaptionTv.setVisibility(View.VISIBLE);
			}
			
			mAvailTv.setText(SingletonClass.stringFromNumber(card.avaialbleLimit,0));
			mBalanceTv.setText(SingletonClass.stringFromNumber(card.currOS, 0));
		}
		
		if (newOfferCnt == 0) {
			mNewOffersTv.setText("NO NEW OFFERS");
		}else if(newOfferCnt == 1){
			mNewOffersTv.setText("1 NEW OFFER");
		}else{
			mNewOffersTv.setText(newOfferCnt.toString() + " NEW OFFERS");
		}

	}
	
	private void getNewOfferCount(){
		
		APIService service = APIService.getNewOfferCount(getActivity());
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					Long num = Long.parseLong(data.toString());
					if (num != null) {
						newOfferCnt = num;
					}
				}
				
				reloadUI();

			}
		});
		service.getData(false);
	}

	public void showTxnSearchActivity(){
		Intent intent = new Intent(getActivity(), ManageSpendActivity.class);
		intent.putExtra("ActivityName", "HomeFragment");
		startActivity(intent);
		//getActivity().overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
	
	/** ...//// Listeners..///.. */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.manageLl:
			showTxnSearchActivity();
			break;
		
		case R.id.btnLock:
			confirmCardLockUnlock();
			break;
		case R.id.btnLockBg:
			confirmCardLockUnlock();
			break;

		case R.id.btnCall:
			callClient();
			break;
		case R.id.btnCAllkBg:
			callClient();
			break;

		case R.id.SpendRl:
			Intent intent = new Intent(getActivity(), SpendActivity.class);
			startActivity(intent);
			break;
			
		/*case R.id.moreIv:
			Intent intent = new Intent(getActivity(), TransSearchActivity.class);
			// intent.putParcelableArrayListExtra("transactions",
			// mTransactionList);
			intent.putExtra("ActivityName", "HomeFragment");
			startActivity(intent);
			break;*/
		}

	}

	public void confirmCardLockUnlock() {

		Card card = SingletonClass.sharedInstance().getCard();
		String message = "Locking your card will disallow all future transactions.  Would you like to continue?";
		if(card.status != Status.ACTIVE){
			message = "Unlock your card and re-enable all transactions?";
		}
		
		/*if(card.status == Status.ACTIVE)
			message = "Locking your card will disallow all future transactions.  Would you like to continue?";
		*/
		DialogUtil.showOkCancelDialog(getActivity(),0, message, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggelCardStatus();
				Dialog dialog = (Dialog) v.getTag();
				dialog.cancel();
			}
		});
		/*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
		alertDialogBuilder.setTitle("Confirm");
		

		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								toggelCardStatus();
							}
						})
				.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
*/
	}

	private void toggelCardStatus() {
		mLockBtn.setEnabled(false);
		Long cardId = new PreferanceUtil(getActivity()).getCardId();
		APIService service = APIService.toggelCardStatus(getActivity(), cardId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (true) {
						Card card = SingletonClass.sharedInstance().getCard();
						if (card != null) {
							String msg = null;
							Integer status = Integer.valueOf(data + "");
							if (status == 1) {
								card.status = Status.ACTIVE;
								msg = card.getCardDisplayString()
										+ " is unlocked now";
							} else {
								card.status = Status.INACTIVE;
								msg = card.getCardDisplayString()
										+ " is locked now";
							}
							Toast.makeText(getActivity(), msg,
									Toast.LENGTH_LONG).show();
						}
					}
				}
				mLockBtn.setEnabled(true);
				reloadUI();

			}
		});
		service.getData(true);
	}

	private void callClient() {
		if (mClientPhone != null && !mClientPhone.equals("")) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ mClientPhone));
			startActivity(intent);
		}
	}

	/** ...//// Web Services..///.. */

	public void getCardDetails(boolean showLoadingIndicator) {
		
		getNewOfferCount();
		getTransactionsService();
		
		Long cardId = new PreferanceUtil(getActivity()).getCardId();
		APIService cardDetailService = APIService.getCardDetails(getActivity(),
				cardId);
		cardDetailService
				.setServiceListener(new APIService.APIServiceListener() {

					@Override
					public void onCompletion(APIService service, Object data,
							Error error) {

						if (error == null) {
							if (data instanceof JSONObject) {
								JSONObject json = (JSONObject) data;
								Card card = new Card(json.optJSONObject("card"));
								SingletonClass.sharedInstance().setCard(card);


								JSONObject client = json
										.optJSONObject("client");
								if (client != null) {
									mClientPhone = client.optString("phone");
								}
							}

						}
						reloadUI();
					}

				});
		cardDetailService.getData(showLoadingIndicator);
	}

	private void getTransactionsService() {

		Long cardId = new PreferanceUtil(this.getActivity()).getCardId();

		APIService service = APIService.getAllTransactions(this.getActivity(),
				cardId, null, null);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
									 Error error) {
				noTranxTv.setVisibility(View.GONE);
				if (error == null) {
					if (data instanceof JSONArray) {
						mTransactionList = new ArrayList<Transaction>();

						JSONArray txnArray = (JSONArray) data;
						if (txnArray != null && txnArray.length() > 0) {
							// mMoreBgView.setVisibility(View.VISIBLE);
							ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
							for (int i = 0; i < txnArray.length(); i++) {
								try {
									if (transactionList.size() == nummberOfTxnToShow) {
										break;
									}
									JSONObject jsonObj = txnArray
											.getJSONObject(i);
									transactionList
											.add(new Transaction(
													jsonObj));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							mTransactionList = transactionList;

						}

						if (mTransactionList == null
								|| mTransactionList.size() == 0) {
							noTranxTv.setText("No transactions found");
							noTranxTv.setVisibility(View.VISIBLE);
						}
						reloadUI();



					}
				}

			}
		});
		service.getData(false);
	}


}
