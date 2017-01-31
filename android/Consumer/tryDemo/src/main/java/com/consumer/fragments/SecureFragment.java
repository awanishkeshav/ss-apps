package com.consumer.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.adapters.ManageAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.dialogs.ManageDialog;
import com.consumer.dialogs.ManageDialog.AddLimitListner;
import com.consumer.models.CardSettings;
import com.consumer.models.Error;
import com.consumer.models.Limit;
import com.consumer.models.Merchant;
import com.consumer.services.APIService;
import com.consumer.utility.DateConverter;

public class SecureFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener, OnSeekBarChangeListener {

	// private static final int TAG_MONTHLY = 1;
	// private static final int TAG_DAILY = 0;

	private CheckBox mOnlineCb, mInterCb;
	private ImageView mAddManageIv, mAddBlockedIv;
	private ListView /* mManageLv, */mBlockedLv;
	private LinearLayout mManageLl, mBlockedLl;

	private LinearLayout mDailyLl, mMonthlyLl;

	private RelativeLayout mBlockedTopRl;
	private Spinner mCategoryDailySp, mCategoryMonthSp;
	private SeekBar mDailySb, mMonthlySb;
	LayoutInflater mInflator;
	private Activity activity;
	private ManageDialog mManageDialog;
	private ManageAdapter mManageAdapter;
	private TextView mDailyLimitTv, mMonthLimitTv, mLeftDailyTv,
			mLeftMonthlyTv;
	private ImageView mDailyDeleteIv, mMonthlyDeleteIv;
	private CardSettings cardSettings;
	private ArrayList<Merchant> mBlockedMerList;
	private APIService cardService;
	public Limit monthlylimit;
	public AddLimitListner addLimitListner;
	// private CategoryAdapter mCategoryAdapter;
	public ArrayAdapter<String> mCategoryAdapter;

	private SingletonClass mSingletonClass;

	public String[] mCategoriesArr = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = getActivity();
		View rootView = inflater.inflate(R.layout.secure_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Load app constants if not already loaded
		if (SingletonClass.sharedInstance().getbConstants() == null) {
			SingletonClass.sharedInstance().loadBackendConstants(getActivity(),
					false);
		}

		mSingletonClass = SingletonClass.sharedInstance();

		initialization(view);

		addLimitListner = new AddLimitListner() {

			@Override
			public void onAddLimit(Limit limit) {
				// cardSettings.customLimist.add(limit);
				saveCardLimit(limit, true, true);
				// mManageAdapter.setList(mLimitList);
				// setManageListCustom();
				// saveCardLimit(limit, false); //123

			}
		};


		List<String> cateList = mSingletonClass.getbConstants()
				.getActionDisplayStrings();
		mCategoriesArr = new String[cateList.size()];
		mCategoriesArr = cateList.toArray(mCategoriesArr);

		mCategoryAdapter = new ArrayAdapter<String>(activity,
				R.layout.spinner_item, mCategoriesArr);

		// Specify the layout to use when the list of choices appears
		mCategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
		mCategoryDailySp.setAdapter(mCategoryAdapter);
		mCategoryMonthSp.setAdapter(mCategoryAdapter);

		if (SingletonClass.sharedInstance().cardSettings == null){
			getCardSettingsService(true);
		}else {
			// don't show loading next time
			cardSettings = SingletonClass.sharedInstance().cardSettings;
			setManageListCustom();
			setFormData();
			getCardSettingsService(false);
		}

		getBlockedMerchants(false);

		// mCategoryMonthSp.setSelection(2); //123
		// mManageDialog = new ManageDialog(activity, addLimitListner);
	}

	private void initialization(View view) {
		mOnlineCb = (CheckBox) view.findViewById(R.id.online_cb);
		mInterCb = (CheckBox) view.findViewById(R.id.inter_cb);
		mAddManageIv = (ImageView) view.findViewById(R.id.addManageIv);
		mAddBlockedIv = (ImageView) view.findViewById(R.id.addBlockedIv);
		// mManageLv = (ListView) view.findViewById(R.id.manageLv);
		mManageLl = (LinearLayout) view.findViewById(R.id.manageLl);
		mBlockedLl = (LinearLayout) view.findViewById(R.id.blockLl);
		mBlockedTopRl = (RelativeLayout) view.findViewById(R.id.blockedTopRl);
		/* mBlockedLv = (ListView) view.findViewById(R.id.blockedLv); */
		mDailyLl = (LinearLayout) view.findViewById(R.id.dailyLL);
		mMonthlyLl = (LinearLayout) view.findViewById(R.id.monthlyLL);

		mCategoryDailySp = (Spinner) mDailyLl.findViewById(R.id.CategorySp);
		mCategoryMonthSp = (Spinner) mMonthlyLl.findViewById(R.id.CategorySp);

		mDailySb = (SeekBar) mDailyLl.findViewById(R.id.limitSb);
		mMonthlySb = (SeekBar) mMonthlyLl.findViewById(R.id.limitSb);

		mDailyLimitTv = (TextView) mDailyLl.findViewById(R.id.CenterBottomTv);
		mMonthLimitTv = (TextView) mMonthlyLl.findViewById(R.id.CenterBottomTv);
		mLeftDailyTv = (TextView) mDailyLl.findViewById(R.id.leftTv);
		mLeftMonthlyTv = (TextView) mMonthlyLl.findViewById(R.id.leftTv);
		mDailyDeleteIv = (ImageView) mDailyLl.findViewById(R.id.removeIv);
		mMonthlyDeleteIv = (ImageView) mMonthlyLl.findViewById(R.id.removeIv);
		
		View line = mMonthlyLl.findViewById(R.id.seperator);
		line.setVisibility(View.GONE);

		mOnlineCb.setOnCheckedChangeListener(this);
		mInterCb.setOnCheckedChangeListener(this);
		mAddManageIv.setOnClickListener(this);
		mAddBlockedIv.setOnClickListener(this);

		mDailySb.setOnSeekBarChangeListener(this);
		mMonthlySb.setOnSeekBarChangeListener(this);

		mInflator = activity.getLayoutInflater();
		mManageAdapter = new ManageAdapter(activity);
	}

	/** .......///.....CUSTOM LIST......///...... */

	int j = 0;

	private void setManageListCustom() {
		if (cardSettings == null)
			return;

		if (cardSettings.customLimist != null
				&& !cardSettings.customLimist.isEmpty()) {
			mManageLl.removeAllViews();
			j = 0;
			for (int i = 0; i < cardSettings.customLimist.size(); i++) {

				View rowView = mInflator.inflate(R.layout.security_row, null);

				final SeekBar limitSb = (SeekBar) rowView
						.findViewById(R.id.limitSb);
				final Spinner categorySp = (Spinner) rowView
						.findViewById(R.id.CategorySp);
				ImageView removeIv = (ImageView) rowView
						.findViewById(R.id.removeIv);
				LinearLayout topLl = (LinearLayout) rowView
						.findViewById(R.id.topLl);
				final TextView limitValueTv = (TextView) rowView
						.findViewById(R.id.CenterBottomTv);
				final TextView periodTv = (TextView) rowView
						.findViewById(R.id.leftTv);
				final TextView transCat = (TextView) rowView
						.findViewById(R.id.LeftBottomTv);
				if (i == cardSettings.customLimist.size()-1) {
					View line = rowView.findViewById(R.id.seperator);
					line.setVisibility(View.GONE);
				}
				

				j = i;
				final Limit limit = cardSettings.customLimist.get(i);

				periodTv.setText(Limit.getPeroidString(limit.period) + " ("
						+ limit.getCategoryKey() + ")");
				// transCat.setText(limit.getCategoryKey());

				limitSb.setProgress(cardSettings.customLimist.get(j)
						.getLimitPercentValue());
				limitSb.setTag(cardSettings.customLimist.get(j)
						.getLimitPercentValue());

				limitValueTv.setText(SingletonClass.stringFromNumber(
						limit.userLimit, 0));

				categorySp.setAdapter(mCategoryAdapter);
				categorySp.setSelection(mSingletonClass.getbConstants()
						.getActionPosition(limit.actionkey));
				categorySp.setTag(mSingletonClass.getbConstants()
						.getActionPosition(limit.actionkey));

				removeIv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cardSettings.customLimist.remove(j);
						mManageLl.removeAllViews();
						setManageListCustom();
						deleteCardLimit(limit, true);
					}
				});

				limitSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						limitSb.postDelayed(new Runnable() {

							@Override
							public void run() {
								// Limit limit =
								// cardSettings.customLimist.get(i);
								saveCardLimit(limit, false, false);
							}
						}, 300);

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

						// final Limit limit = cardSettings.customLimist.get(j);

						/*
						 * if ( limit.getLimitPercentValue() == progress) {
						 * return; }
						 */
						if (fromUser) {
							limit.setLimitPercentValue(progress);
							// cardSettings.customLimist.set(j, limit);
							limitValueTv.setText(SingletonClass
									.stringFromNumber(limit.userLimit, 0));
						}
					}
				});

				categorySp
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {

								if (((Integer) categorySp.getTag()) == position) {
									return;
								}
								categorySp.setTag(position);

								// Limit limit =
								// cardSettings.customLimist.get(j);
								limit.setAction(mSingletonClass.getbConstants().approvalTypes
										.get(position));
								saveCardLimit(limit, false, false);

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {
							}
						});

				mManageLl.addView(rowView);

			}
		}
	}

	private void setBlockedCustomList() {
		if (cardSettings == null)
			return;

		mBlockedLl.removeAllViews();
		if (mBlockedMerList != null && !mBlockedMerList.isEmpty()) {
			mBlockedTopRl.setVisibility(View.VISIBLE);

			for (int i = 0; i < mBlockedMerList.size(); i++) {

				View rowView = mInflator.inflate(R.layout.blocked_row, null);

				final TextView marchNameTv = (TextView) rowView
						.findViewById(R.id.marchantNameTv);
				final TextView blockDateTv = (TextView) rowView
						.findViewById(R.id.dateTv);
				ImageView removeIv = (ImageView) rowView.findViewById(R.id.removeIv);
				removeIv.setTag(i);
				mBlockedLl.addView(rowView);

				final Merchant merchant = mBlockedMerList.get(i);
				marchNameTv.setText(merchant.name);
				if (merchant.blockedDate != null) {
					blockDateTv.setText("Blocked "
							+ DateConverter.getStringFromDateWitoutTime(
									getActivity(), merchant.blockedDate));
				}

				removeIv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int index = (Integer) v.getTag();
						mBlockedMerList.remove(index);
						setBlockedCustomList();
						unblockMerchant(merchant, false);

					}
				});

			}
		} else
			mBlockedTopRl.setVisibility(View.GONE);
	}

	private void setFormData() {

		mOnlineCb.setChecked(cardSettings.isOnlineAllowed);
		mInterCb.setChecked(cardSettings.isInternationalAllowed);

		final Limit dailylimit = cardSettings.getDailyLimit();
		monthlylimit = cardSettings.getMonthlyLimit();

		mLeftDailyTv.setText(Limit.getPeroidString(dailylimit.period)
				+ " Limit");
		mLeftDailyTv.setTextColor(getResources().getColor(R.color.DarkGrey));
		mLeftDailyTv.setTextSize(16);
		//float leftPad = SingletonClass.pixelsFromDip(getActivity(), 15);
		//mLeftDailyTv.setPadding((int)leftPad, 0, 0, 0);
		
		mLeftMonthlyTv.setText(Limit.getPeroidString(monthlylimit.period)
				+ " Limit");
		mLeftMonthlyTv.setTextColor(getResources().getColor(R.color.DarkGrey));
		mLeftMonthlyTv.setTextSize(16);
		//mLeftMonthlyTv.setPadding((int)leftPad, 0, 0, 0);
		
		
		mDailySb.setTag(dailylimit.getLimitPercentValue());
		mMonthlySb.setTag(monthlylimit.getLimitPercentValue());

		mDailySb.setProgress(dailylimit.getLimitPercentValue());
		mMonthlySb.setProgress(monthlylimit.getLimitPercentValue());

		mDailyLimitTv.setText(SingletonClass.stringFromNumber(
				dailylimit.userLimit, 0));
		mMonthLimitTv.setText(SingletonClass.stringFromNumber(
				monthlylimit.userLimit, 0));

		// mCategoryMonthSp.getBackground().setColorFilter(getResources().getColor(R.color.accent_material_dark),
		// PorterDuff.Mode.SRC_ATOP);
		mCategoryDailySp.setSelection(mSingletonClass.getbConstants()
				.getActionPosition(dailylimit.actionkey));
		mCategoryMonthSp.setSelection(mSingletonClass.getbConstants()
				.getActionPosition(monthlylimit.actionkey));

		mCategoryDailySp.setTag(mSingletonClass.getbConstants()
				.getActionPosition(dailylimit.actionkey));
		mCategoryMonthSp.setTag(mSingletonClass.getbConstants()
				.getActionPosition(monthlylimit.actionkey));

		mCategoryDailySp
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (((Integer) mCategoryDailySp.getTag()) == position) {
							return;
						}
						mCategoryDailySp.setTag(position);

						dailylimit.setAction(mSingletonClass.getbConstants().approvalTypes
								.get(position));
						saveCardLimit(dailylimit, false, false);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mCategoryMonthSp
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						if (((Integer) mCategoryMonthSp.getTag()) == position) {
							return;
						}
						mCategoryMonthSp.setTag(position);

						monthlylimit.setAction(mSingletonClass.getbConstants().approvalTypes
								.get(position));
						saveCardLimit(monthlylimit, false, false);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		mDailyDeleteIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dailylimit.reset();
				setFormData();
				saveCardLimit(dailylimit, false, false);

			}
		});
		mMonthlyDeleteIv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				monthlylimit.reset();
				setFormData();
				saveCardLimit(monthlylimit, false, false);

			}
		});

		mManageDialog = new ManageDialog(activity, addLimitListner,
				monthlylimit);
	}

	/** .......///..... LISTENERS......///...... */

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.online_cb:
			blockUnblockTxnType(CardSettings.TXN_TYPE_ONLIN,
					!mOnlineCb.isChecked(), false);
			break;

		case R.id.inter_cb:
			blockUnblockTxnType(CardSettings.TXN_TYPE_INTERNATIONAL,
					!mInterCb.isChecked(), false);
			break;
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.addManageIv:
			
			if(mManageDialog != null){
				if (!mManageDialog.isShowing())
					mManageDialog.show();
			}else{
				mManageDialog = new ManageDialog(activity, addLimitListner, monthlylimit);
				
			}
				
			break;

		case R.id.addBlockedIv:
			//Toast.makeText(activity, "addBlockedIv", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {

		if (seekBar == mDailySb) {
			if (cardSettings != null) {
				final Limit dailylimit = cardSettings.getDailyLimit();
				if (fromUser) {
					dailylimit.setLimitPercentValue(progress);
					mDailyLimitTv.setText(SingletonClass.stringFromNumber(
							dailylimit.userLimit, 0));
				}
			}

		} else if (seekBar == mMonthlySb) {
			if (cardSettings != null) {
				final Limit monthlyLimit = cardSettings.getMonthlyLimit();
				if (fromUser) {
					monthlyLimit.setLimitPercentValue(progress);
					mMonthLimitTv.setText(SingletonClass.stringFromNumber(
							monthlyLimit.userLimit, 0));
				}
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

		if (seekBar == mDailySb) {
			if (cardSettings != null) {
				seekBar.postDelayed(new Runnable() {

					@Override
					public void run() {
						saveCardLimit(cardSettings.getDailyLimit(), false,
								false);

					}
				}, 250);
			}

		} else if (seekBar == mMonthlySb) {
			if (cardSettings != null) {
				seekBar.postDelayed(new Runnable() {

					@Override
					public void run() {
						saveCardLimit(cardSettings.getMonthlyLimit(), false,
								false);

					}
				}, 250);
			}
		}
	}

	/** .......///..... WEB SERVICES......///...... */

	private void getCardSettingsService(boolean isForeground) {
		if (cardService != null) {
			cardService.cancelRequest();
		}

		cardService = APIService.getCardSettings(getActivity(),
				new PreferanceUtil(getActivity()).getCardId());
		cardService.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					cardSettings = new CardSettings((JSONObject) data);
					SingletonClass.sharedInstance().cardSettings = cardSettings;
					// mManageAdapter.setList(cardSettings.customLimist);
					setManageListCustom();
					setFormData();
				}

			}
		});
		cardService.getData(isForeground);
	}

	private void saveCardLimit(Limit limit, final boolean isForeground,
			final boolean forceReload) {
		APIService service = APIService.saveCardSettingsLimit(getActivity(),
				new PreferanceUtil(getActivity()).getCardId(),
				limit.getJsonObject());
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				boolean needsToReload = forceReload;
				if (error != null) {
					needsToReload = true;
					if (error.getMessage() != null) {
						Toast.makeText(getActivity(),
								"Cannot save\n\n" + error.getMessage(),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "Cannot save",
								Toast.LENGTH_LONG).show();

					}

				}

				if (needsToReload) {
					getCardSettingsService(isForeground);
				}

			}
		});
		service.getData(isForeground);
	}

	private void deleteCardLimit(Limit limit, final boolean isForeground) {
		APIService service = APIService.deleteCardSettingsLimit(getActivity(),
				new PreferanceUtil(getActivity()).getCardId(),
				limit.getJsonObject());
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					getCardSettingsService(isForeground);
				}

			}
		});
		service.getData(isForeground);
	}

	private void getBlockedMerchants(boolean isForeground) {
		APIService service = APIService.getAllBlockedMerchants(getActivity());
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					mBlockedMerList = new ArrayList<Merchant>();
					JSONArray jsonArray = (JSONArray) data;
					for (int i = 0; i < jsonArray.length(); i++) {
						mBlockedMerList.add(new Merchant(jsonArray
								.optJSONObject(i)));
					}
					setBlockedCustomList();
				}

			}
		});
		service.getData(isForeground);
	}

	private void unblockMerchant(Merchant merchant, final boolean isForeground) {
		APIService service = APIService.blockUnblockMerchant(getActivity(),
				merchant.id, false);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error != null) {
					getBlockedMerchants(isForeground);
				}

			}
		});
		service.getData(isForeground);
	}

	// txnType can only be Online or International
	private void blockUnblockTxnType(String txnType, boolean block,
			final boolean isForeground) {
		APIService service = APIService.blockUnblockTransactionType(
				getActivity(), new PreferanceUtil(getActivity()).getCardId(),
				block, txnType);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error != null) {
					getCardSettingsService(isForeground);
				}

			}
		});
		service.getData(isForeground);
	}

}
