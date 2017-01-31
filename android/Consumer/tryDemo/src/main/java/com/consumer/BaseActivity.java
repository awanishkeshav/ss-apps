package com.consumer;

import com.consumer.R;
import com.consumer.adapters.DrawerListAdapter;
import com.consumer.common.SingletonClass;
import com.consumer.gcm.LocationUpdateService;
import com.consumer.gcm.MyNotification;
import com.consumer.models.BackendConstants;
import com.consumer.models.Card;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity {

	public static String ExtraSubMenuIndex = "extra_submenu_index";

	private DrawerLayout mDrawerLayout;
	private ListView mListView;
	public boolean useDrawerLayout = true;
	public int subMenuIndex = 1;

	private DrawerListAdapter listAdapter;
	private LinearLayout mDrawerHeaderll;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		if (arg0 == null) // 1st time
		{
			this.overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_right);
		}
		subMenuIndex = getIntent().getIntExtra(ExtraSubMenuIndex, 1);

		// Load constants if not loaded before
		BackendConstants bConst = SingletonClass.sharedInstance()
				.getbConstants();
		if (bConst == null || !bConst.isLoaded()) {
			SingletonClass.sharedInstance().loadBackendConstants(this, true);
		}

		// Check if location update service running or not
		boolean isLocationIsRunning = false;
		if (!isLocationIsRunning) {
			// Start location update
			Intent i = new Intent(this, LocationUpdateService.class);
			startService(i);
		}
	}

	@Override
	public void finish() {
	
		super.finish();
		this.overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left
				);
	}

	@Override
	public void setContentView(int layoutResID) {
		if (!useDrawerLayout) {
			super.setContentView(layoutResID);
			return;
		}

		super.setContentView(R.layout.activity_drawar_layout);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mListView = (ListView) findViewById(R.id.left_drawer);
		mDrawerHeaderll = (LinearLayout) inflater.inflate(
				R.layout.drawer_header_layout, null);
		TextView cardNameTv = (TextView) mDrawerHeaderll
				.findViewById(R.id.drawerHeaderCardNameTv);
		Card card = SingletonClass.sharedInstance().getCard();
		if(card != null){
			cardNameTv.setText(card.getCardDisplayString());
		}
		

		listAdapter = new DrawerListAdapter(this);
		listAdapter.setSelectedIndex(selectedMenuIndex());
		mListView.addHeaderView(mDrawerHeaderll);
		mListView.setAdapter(listAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view == mDrawerHeaderll) {
					handleMenuClick(view,DrawerListAdapter.IndexHome);
				} else {
					handleMenuClick(view, (int) id);
				}
			}
		});

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_dl);

		FrameLayout contentView = (FrameLayout) findViewById(R.id.content_frame);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT, Gravity.BOTTOM);
		final int marginTop = 0;
		layoutParams.setMargins(0, marginTop, 0, 0);
		contentView.addView(inflater.inflate(layoutResID, null), layoutParams);
	}

	public void setSubmenuIndex(int index) {
		subMenuIndex = index;
		listAdapter.setSelectedIndex(selectedMenuIndex());
		listAdapter.notifyDataSetChanged();
	}

	private int selectedMenuIndex() {
		int index = -1;

		if (this instanceof HomeActivity) {
			index = DrawerListAdapter.IndexHome;
		} else if (this instanceof SpendActivity) {
			index = DrawerListAdapter.IndexOffers;
		} else if (this instanceof ManageSpendActivity) {
			index = DrawerListAdapter.IndexTransaction;
		}
		return index + subMenuIndex - 1;
	}

	public void onSubmenuItemClick(int pos) {
		setSubmenuIndex(pos);
	}

	private void handleMenuClick(View view, final int position) {
		hideDrawer();
		
		view.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (position != selectedMenuIndex()) {
					
					BaseActivity activity = BaseActivity.this;

					if (position == DrawerListAdapter.IndexHome) {
						Intent intent = new Intent(activity, HomeActivity.class);
						startActivity(intent);

					} else if (position == DrawerListAdapter.IndexManage
							|| position == DrawerListAdapter.IndexTransaction
							|| position == DrawerListAdapter.IndexAnalysis
							|| position == DrawerListAdapter.IndexSecure) {
						if (activity instanceof ManageSpendActivity) {
							if (position == DrawerListAdapter.IndexTransaction
									|| position == DrawerListAdapter.IndexManage) {
								activity.onSubmenuItemClick(1);

							} else if (position == DrawerListAdapter.IndexAnalysis) {
								activity.onSubmenuItemClick(2);

							} else if (position == DrawerListAdapter.IndexSecure) {
								activity.onSubmenuItemClick(3);
							}

						} else {
							Intent intent = new Intent(activity, ManageSpendActivity.class);
							if (position == DrawerListAdapter.IndexAnalysis) {
								intent.putExtra(ExtraSubMenuIndex, 2);

							} else if (position == DrawerListAdapter.IndexSecure) {
								intent.putExtra(ExtraSubMenuIndex, 3);
							}

							startActivity(intent);
						}

					} else if (position == DrawerListAdapter.IndexSpend
							|| position == DrawerListAdapter.IndexOffers
							|| position == DrawerListAdapter.IndexNearMe
							|| position == DrawerListAdapter.IndexShop) {

						if (activity instanceof SpendActivity) {
							if (position == DrawerListAdapter.IndexOffers
									|| position == DrawerListAdapter.IndexSpend) {
								activity.onSubmenuItemClick(1);

							} else if (position == DrawerListAdapter.IndexNearMe) {
								activity.onSubmenuItemClick(2);

							} else if (position == DrawerListAdapter.IndexShop) {
								activity.onSubmenuItemClick(3);
							}

						} else {
							Intent intent = new Intent(activity, SpendActivity.class);
							if (position == DrawerListAdapter.IndexNearMe) {
								intent.putExtra(ExtraSubMenuIndex, 2);

							} else if (position == DrawerListAdapter.IndexShop) {
								intent.putExtra(ExtraSubMenuIndex, 3);
							}
							startActivity(intent);
						}
					}
				}
				
			}
		}, 300);

		
	}

	public void showDrawer() {
		mDrawerLayout.openDrawer(mListView);
	}

	public void hideDrawer() {
		mDrawerLayout.closeDrawer(mListView);
	}

	private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {


		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isNotifHandeled = MyNotification
					.handleNotificationIfNeeded(BaseActivity.this, intent);
			if (isNotifHandeled) {
				onNotificationHandled();
			}

		}
	};

	public void onNotificationHandled() {

	}

	@Override
	public void onResume() {
		super.onResume();

		LocalBroadcastManager.getInstance(this).registerReceiver(
				mNotificationReceiver,
				new IntentFilter(MyNotification.ActionNone));
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister since the activity is not visible
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				mNotificationReceiver);

	}
}
