package com.consumer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.dialogs.AddTagDialog;
import com.consumer.gcm.MyNotification;
import com.consumer.handler.FooterTabHandler;
import com.consumer.handler.HeaderLayout;
import com.consumer.handler.StateLayoutHolder;
import com.consumer.handler.FooterTabHandler.FooterTabListener;
import com.consumer.models.Error;
import com.consumer.models.Offer;
import com.consumer.models.Transaction;
import com.consumer.models.Transaction.ReviewStatus;
import com.consumer.models.TxnCategory.Type;
import com.consumer.models.TxnMisc;
import com.consumer.services.APIService;
import com.consumer.utility.DateConverter;
import com.consumer.utility.DialogUtil;

public class TransDetailActivity extends BaseActivity implements
		OnClickListener, FooterTabListener {

	public static final String ExtraTransactionVO = "ext_txn";

	private TextView mDate, mNameTv, mAddressTv, mCategoryTv, mAmtThisMonthTv,
			mAmountTv, mNotifTitleTv, mNotifSubtitleTv, mTagTv, mOfferTitle,
			mOfferDesc;
	private Button mOfferBtn, mNotifLeftButton, mNotifRightButton;
	private RelativeLayout mCallButton;
	private StateLayoutHolder mVisitHolder, mSpendHolder, mReviewHolder;
	private LinearLayout mNotifLayout, mNotifHeaderLayout, mBottomLayout;
	private FooterTabHandler bottomTabHandler;
	private HeaderLayout mHeaderLayout;
	private Transaction txn;
	private TxnMisc txnMisc;
	private List<Offer> offers;
	private Dialog mConfirmBlockDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trans_detail_activity);

		controlInitialization();
		handleIntent(getIntent());

		getTransactionDetails(false);
		getTransactionMiscDetails(false);
		getOffers(false);
		setData();
	}

	@Override
	public void onResume() {
		super.onResume();
		getTransactionDetails(false);

	}

	private void handleIntent(Intent intent) {

		Object txnObj = this.getIntent().getSerializableExtra(
				ExtraTransactionVO);
		if (txnObj != null) {
			txn = (Transaction) txnObj;
		}

		final MyNotification notif = MyNotification.getNotification(intent
				.getExtras());
		if (txn == null && notif != null) {
			txn = new Transaction(notif.getCardId(), notif.getTransactionId());
		}

		if (notif != null) {

			String action = intent.getAction();
			if (action.equals(MyNotification.ActionView)
					|| action.equals(MyNotification.ActionNone)) {
				bottomTabHandler.setTabs(FooterTabHandler.RES_NONE,
						FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE,
						R.drawable.icon_report);
				mNotifLayout.setVisibility(View.VISIBLE);
				mBottomLayout.setVisibility(View.GONE);
				mNotifTitleTv.setText(notif.getTitle());
				String subtitle = notif.getMessage();
				if (subtitle != null && notif.getScreenMessage() != null && !notif.getScreenMessage().isEmpty()) {
					subtitle = subtitle + "\n" + notif.getScreenMessage();
				}
				mNotifSubtitleTv.setText(subtitle);
				mNotifRightButton.setVisibility(View.GONE);
				mNotifLeftButton.setVisibility(View.GONE);
				mNotifHeaderLayout.setBackgroundResource(R.color.notifBGYello);

				Button allowButton = null;
				Button denyButton = null;
				Button dismissButton = null;
				Button reviewButton = null;

				switch (notif.notifType) {
				case TxnInfo:
					dismissButton = mNotifLeftButton;
					mNotifHeaderLayout
							.setBackgroundResource(R.color.notifBGYello);
					break;
				case TxnApprovalRequired:
					allowButton = mNotifLeftButton;
					denyButton = mNotifRightButton;
					mNotifHeaderLayout.setBackgroundResource(R.color.notifBGOrange);
					break;

				case TxnDenied:
					dismissButton = mNotifLeftButton;
					mNotifHeaderLayout.setBackgroundResource(R.color.notifBGRed);
					break;
				case MerchantReviewRequested:
					reviewButton = mNotifLeftButton;
					dismissButton = mNotifRightButton;

					break;
				case Unknown:
					break;
				}

				if (allowButton != null) {
					allowButton.setVisibility(View.VISIBLE);
					allowButton.setText(MyNotification.ActionApproveLabel);
					allowButton.setBackgroundResource(R.color.buttonGreen);
					allowButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							notif.approve(TransDetailActivity.this);

						}
					});
				}
				if (denyButton != null) {
					denyButton.setVisibility(View.VISIBLE);
					denyButton.setText(MyNotification.ActionRejectLabel);
					denyButton.setBackgroundResource(R.color.buttonRed);
					denyButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							notif.reject(TransDetailActivity.this);

						}
					});
				}
				if (dismissButton != null) {
					dismissButton.setVisibility(View.VISIBLE);
					dismissButton.setText(MyNotification.ActionDismissLabel);
					dismissButton.setBackgroundResource(R.color.buttonGray);
					dismissButton
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									notif.dismiss(TransDetailActivity.this);
									finish();

								}
							});
				}
				if (reviewButton != null) {
					reviewButton.setVisibility(View.VISIBLE);
					reviewButton
							.setText(MyNotification.ActionReviewRequestLabel);
					reviewButton.setBackgroundResource(R.color.buttonGreen);
					reviewButton.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							notif.review(TransDetailActivity.this);

						}
					});
				}
			}
		}
	}

	private void controlInitialization() {

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);

		bottomTabHandler = new FooterTabHandler(this);
		bottomTabHandler.handleTabs(-1, this);
		bottomTabHandler.setTabs(R.drawable.icon_tags, R.drawable.icon_share,
				R.drawable.icon_review_blue, R.drawable.icon_report);
		bottomTabHandler.getmTab1Tv().setText("Tag");

		mDate = (TextView) findViewById(R.id.txnDateTv);
		mNameTv = (TextView) findViewById(R.id.merchantNameTv);
		mAddressTv = (TextView) findViewById(R.id.address_tv);
		mCategoryTv = (TextView) findViewById(R.id.categoryTv);
		mTagTv = (TextView) findViewById(R.id.tagTv);
		mTagTv.setOnClickListener(this);
		mAmtThisMonthTv = (TextView) findViewById(R.id.amtCategoryTv);
		mAmountTv = (TextView) findViewById(R.id.txnAmountTv);
		mCallButton = (RelativeLayout) findViewById(R.id.callMerchantView);
		mNotifLayout = (LinearLayout) findViewById(R.id.notifLayout);
		mNotifHeaderLayout = (LinearLayout) findViewById(R.id.notifHeaderLayout);
		mBottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
		mNotifLeftButton = (Button) findViewById(R.id.notifLeftButton);
		mNotifRightButton = (Button) findViewById(R.id.notifRightButton);
		mNotifTitleTv = (TextView) findViewById(R.id.notifTitleTv);
		mNotifSubtitleTv = (TextView) findViewById(R.id.notifSubtitleTv);
		mOfferBtn = (Button) findViewById(R.id.offerBtn);
		mOfferTitle = (TextView) findViewById(R.id.offerTitleTv);
		mOfferDesc = (TextView) findViewById(R.id.offerDescTv);
		mOfferBtn.setVisibility(View.INVISIBLE);
		mOfferBtn.setOnClickListener(this);
		View stateVisit = findViewById(R.id.stateVisit);
		mVisitHolder = new StateLayoutHolder(stateVisit,
				false);
		stateVisit.setOnClickListener(this);
		View stateSpend = findViewById(R.id.stateSpend);
		mSpendHolder = new StateLayoutHolder(stateSpend,
				false);
		stateSpend.setOnClickListener(this);
		View stateReview = findViewById(R.id.stateReview);
		mReviewHolder = new StateLayoutHolder(stateReview,true);
		stateReview.setOnClickListener(this);

		mNotifLayout.setVisibility(View.GONE);
		mCallButton.setOnClickListener(this);

	}

	private void getTransactionDetails(boolean isForeground) {
		APIService service = APIService.getTxnDetails(this, this.txn.cardId,
				this.txn.id);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						txn = new Transaction((JSONObject) data);
						getOffers(false);
						setData();
					}
				}

			}
		});
		service.getData(isForeground);
	}

	private void getTransactionMiscDetails(boolean isForeground) {
		APIService service = APIService.getTxnMiscDetails(this,
				this.txn.cardId, this.txn.id);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						txnMisc = new TxnMisc((JSONObject) data);
						setData();
					}
				}

			}
		});
		service.getData(isForeground);
	}

	private void getOffers(boolean isForeground) {
		if (this.txn.merchant == null) {
			return;
		}

		APIService service = APIService.getOffers(this, this.txn.id,
				this.txn.merchant.id);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONArray) {
						JSONArray jsonArr = (JSONArray) data;
						offers = new ArrayList<Offer>();
						for (int i = 0; i < jsonArr.length(); i++) {
							offers.add(new Offer(jsonArr.optJSONObject(i)));
						}
						setData();
					}
				}

			}
		});
		service.getData(isForeground);
	}

    private void requestForOffers(boolean isForeground) {
        if (this.txn.merchant == null) {
            return;
        }

        APIService service = APIService.requestOffers(this, this.txn.id,
                this.txn.merchant.id);
        service.setServiceListener(new APIService.APIServiceListener() {

            @Override
            public void onCompletion(APIService service, Object data,
                                     Error error) {
                if (error == null) {
                    if (data instanceof JSONArray) {
                        JSONArray jsonArr = (JSONArray) data;
						if (jsonArr.length() > 0){
							offers = new ArrayList<Offer>();
							for (int i = 0; i < jsonArr.length(); i++) {
								offers.add(new Offer(jsonArr.optJSONObject(i)));
							}
							setData();
						}
						else{
							DialogUtil.showOkDialog(TransDetailActivity.this, "Offer Requested From Merchant");
						}
                    }
                }else{
					DialogUtil.showOkDialog(TransDetailActivity.this, "Offer Requested From Merchant");
				}

            }
        });
        service.getData(isForeground);
    }

	private void setData() {
		if (this.txn != null) {
			if (txn.date != null) {
				mDate.setText(DateConverter.getStringFromDate(this, txn.date));
			} else {
				mDate.setText("----");
			}

			String tagStr = txn.getTagDisplayString();
			if (!tagStr.isEmpty()) {
				mTagTv.setVisibility(View.VISIBLE);
				mTagTv.setText("" + txn.getTagDisplayStringSeperatedByNewLine() + "");
			} else {
				mTagTv.setText("");
				mTagTv.setVisibility(View.GONE);
			}
			if (txn.amount != null) {
				mAmountTv.setText(SingletonClass.getPriceString(txn.amount));
			} else {
				mAmountTv.setText("------");
			}
			if (txn.txnCategory.type == Type.UNKNOWN) {
				mCategoryTv.setText("-------");
			} else {
				mCategoryTv.setText(txn.txnCategory.name);
			}
			mAddressTv.setText("----");
			mNameTv.setText("--------");
			if (txn.merchant != null) {
				mAddressTv.setText(txn.merchant.address);
				mNameTv.setText(txn.getMerchantName());
			}

			mAmtThisMonthTv.setText("----");
			mVisitHolder.mLabelTv.setText("Visits");
			mSpendHolder.mLabelTv.setText("Total Spend");
			mVisitHolder.mCaptionTv.setText("0");
			mSpendHolder.mCaptionTv.setText("$00.00");

			if (txnMisc != null) {
				if (txnMisc.visitsOnMerchant == 1) {
					mVisitHolder.mLabelTv.setText("Visit");
				}
				mSpendHolder.mCaptionTv.setText(SingletonClass
						.getPriceString(txnMisc.totalSpendOnMerchant));
				mAmtThisMonthTv.setText(SingletonClass
						.getPriceString(txnMisc.monthlySpendOnCategory)
						+ " this month");
				mVisitHolder.mCaptionTv.setText(txnMisc.visitsOnMerchant
						.toString());

			}

			mReviewHolder.mLabelTv.setText("Not Reviewed");
			mReviewHolder.setRatingBarVisible();
			mReviewHolder.mRatingBar.setRating(0);
			
			if (txn.reviewStatus == ReviewStatus.REVIEWED) {
				mReviewHolder.mLabelTv.setText("My Rating");
				
				mReviewHolder.mRatingBar.setRating(txn.reviewCount.floatValue());
			}

			mOfferTitle.setText("");
			mOfferTitle.setVisibility(View.GONE);
			mOfferDesc.setText("");
			mOfferBtn.setVisibility(View.INVISIBLE);
			if (offers != null) {
				mOfferBtn.setVisibility(View.VISIBLE);
				if (offers.size() > 0) {

					Offer offer = offers.get(0);
					mOfferBtn.setText("REDEEM OFFER");
					mOfferTitle.setText(offer.title);
					mOfferDesc.setText(offer.desc);
					mOfferTitle.setVisibility(View.VISIBLE);

				} else {
					mOfferDesc.setText("No current offers from merchant");
					mOfferBtn.setText("Get Offers");
				}
			}
		}

	}

	private void callMerchant() {
		if (txn.merchant != null && txn.merchant.phone != null
				&& !txn.merchant.phone.equals("")) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ txn.merchant.phone));
			startActivity(intent);
		}
	}

	private void blockMerchant() {

		if (this.txn.merchant == null || this.txnMisc == null) {
			return;
		}

		if (this.txnMisc.isMerchantBlocked) {
			DialogUtil.showOkDialog(this, "Merchant is already blocked");
			return;
		}

		String msg = "Do you want to block this merchant?";
		if (!this.txn.getMerchantName().isEmpty()) {
			msg = "Do you want to block " + this.txn.getMerchantName() + "?";
		}

		mConfirmBlockDialog = DialogUtil.showOkCancelDialog(this, 0, msg,
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mConfirmBlockDialog.dismiss();
						APIService service = APIService.blockUnblockMerchant(
								TransDetailActivity.this,
								TransDetailActivity.this.txn.merchant.id, true);
						service.setServiceListener(new APIService.APIServiceListener() {

							@Override
							public void onCompletion(APIService service,
									Object data, Error error) {
								if (error == null) {
									String msg = null;
									msg = "Blocked \"" + txn.getMerchantName()
											+ "\"";
									txnMisc.isMerchantBlocked = true;
									Toast.makeText(TransDetailActivity.this,
											msg, Toast.LENGTH_LONG).show();
								}

							}
						});
						service.getData(true);
					}
				});

	}

	private void showAddReviewScreen() {
		Intent intent = SingletonClass.getIntentForAddReviewActivity(this, txn);
		startActivity(intent);
	}

	private void showBlankScreen() {
		Intent intent = new Intent(this, BlankActivity.class);
		startActivity(intent);
	}
	
	private void handleOfferButtonClick() {
		if (offers != null && !offers.isEmpty()) {
			Offer offer = offers.get(0);
			Intent intent = new Intent(this,
					OfferDetailsActivity.class);
			intent.putExtra(
					OfferDetailsActivity.ExtraOfferVO,
					offer);
			startActivity(intent);
			
		}else{
            this.requestForOffers(true);

			//DialogUtil.showOkDialog(this, "Offer Requested From Merchant");
		}
	}

	private void showAddTagDialong() {
		AddTagDialog dialog = new AddTagDialog(this, this.txn);
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				getTransactionDetails(false);
			}
		});
		dialog.show();
	}

	/** .....////... LISTENERS.....///.... */

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.left_ib:
			finish();
			break;

		case R.id.callMerchantView:
			callMerchant();
			break;

		case R.id.offerBtn:
			handleOfferButtonClick();
			break;
		case R.id.right_ib:
			// Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;
		case R.id.stateReview:
			showAddReviewScreen();
			break;
		case R.id.tagTv:
			showAddTagDialong();
			break;
		case R.id.right_extra_ib:
			SingletonClass.showTxnSearchActivity(this,null);
			break;
		case R.id.stateVisit:
		case R.id.stateSpend:
			if (this.txn != null && this.txn.merchant !=null) 
				SingletonClass.showTxnSearchActivity(this,txn.getMerchantName());
			
			break;
		}

	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:
			showAddTagDialong();
			break;
		case R.id.tab_2_tv:
			SingletonClass.showShareOptionDialog(this);
			break;
		case R.id.tab_3_tv:
			showAddReviewScreen();
			break;
		case R.id.tab_4_tv:
			blockMerchant();
			break;

		default:
			break;
		}

	}

}
