package com.consumer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.consumer.R;
import com.consumer.adapters.SplashPagerAdapter;
import com.consumer.common.Constants;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.gcm.GcmIntentService;
import com.consumer.gcm.MyNotification;
import com.consumer.models.Card;
import com.consumer.models.Error;
import com.consumer.services.APIService;
import com.consumer.services.APIService.APIServiceListener;
import com.consumer.widget.CirclePageIndicator;

public class SplashActivity extends BaseActivity {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public ViewPager mScreenSlideVp;
	public CirclePageIndicator mIndicator;
	private ViewPager.OnPageChangeListener mPageChangeListener;
	private FragmentManager fm;
	public SplashPagerAdapter pagerAdapter;

	public List<Card> mCardList = new ArrayList<Card>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.useDrawerLayout = false;
		setContentView(R.layout.splash_activity);
		controlInitialization();

		/** Instantiating FragmentPagerAdapter */

		pagerAdapter = new SplashPagerAdapter(fm, this);
		mScreenSlideVp.setAdapter(pagerAdapter);
		mIndicator.setViewPager(mScreenSlideVp);

		mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(final int position) {
				switch (position) {

				}
			}
		};

		checkAndRegisterForGCM();
		setDefaultData();
		getCardsService(false);
		
		if(!Constants.MODE_PRODUCTION){
			Toast.makeText(this, "Development Mode", Toast.LENGTH_LONG).show();
		}

	}
	

	private void setDefaultData() {
		mCardList = new PreferanceUtil(this).getCards();
		if (mCardList == null) {
			mCardList = new ArrayList<Card>();
		}
		this.addNewCard();
		pagerAdapter.setList(mCardList);
		mScreenSlideVp.setAdapter(pagerAdapter);
		mIndicator.setViewPager(mScreenSlideVp);
	}

	private void addNewCard() {
		mCardList
				.add(new Card("", 0.0, 0.0, 0.0, 0.0, "", null, null, null, 0l));

	}

	private void controlInitialization() {

		ImageView logo = (ImageView)findViewById(R.id.logo);

		switch (Constants.bank){
			case SBI:
				logo.setImageResource(R.drawable.logo_sbi_mid);
				break;
			case IndusInd:
				logo.setImageResource(R.drawable.indusind_logo_big);
				break;
		}

		mScreenSlideVp = (ViewPager) findViewById(R.id.screen_slide_vp);
		mScreenSlideVp.setClipToPadding(false);
		float cardWidth = getResources().getDimension(R.dimen.CardWidth);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = (int) ((metrics.widthPixels - cardWidth) / 2) - 20;
		mScreenSlideVp.setPadding(width, 0, width, 0);
		// mScreenSlideVp.setPageMargin(10);
		mIndicator = (com.consumer.widget.CirclePageIndicator) findViewById(R.id.indicator);

		// Listeners
		fm = getSupportFragmentManager();
		mIndicator.setOnPageChangeListener(mPageChangeListener);

	}

	/** ...////// WEB SERVICES ..////.. */

	public void getCardsService(boolean shouldReloadUI) {

		final boolean needsToReloadUI = shouldReloadUI;
		APIService service = APIService.getAllCards(this);
		service.setServiceListener(new APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				// TODO Auto-generated method stub
				if (error == null) {

					if (data instanceof JSONArray) {

						JSONArray jsonArray = (JSONArray) data;
						new PreferanceUtil(SplashActivity.this)
								.setCards(jsonArray);

						if (needsToReloadUI || mCardList == null
								|| mCardList.size() <= 1) {
							mCardList = new ArrayList<Card>();
							for (int i = 0; i < jsonArray.length(); i++) {

								try {
									mCardList.add(new Card(jsonArray
											.getJSONObject(i)));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							addNewCard();
							pagerAdapter.setList(mCardList);
							mScreenSlideVp.setAdapter(pagerAdapter);
							mIndicator.setViewPager(mScreenSlideVp);
						}

						/*
						 * mScreenSlideVp.postDelayed(new Runnable() {
						 * 
						 * @Override public void run() {
						 * 
						 * //pagerAdapter.setList(mCardList);
						 * 
						 * pagerAdapter.setList(mCardList); } }, 200);
						 */

						/**/
					}
				}

			}
		});
		service.getData(false);
	}

	private void checkAndRegisterForGCM() {

		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		if (checkPlayServices()) {

			String regid = new PreferanceUtil(this).getRegistrationId();

			if (regid.isEmpty()) {
				GcmIntentService.registerInBackground(getApplicationContext());
			} else {
				GcmIntentService
						.trackGCMRegIdToBackend(getApplicationContext());
			}
		} else {
			Log.i("", "No valid Google Play Services APK found.");
		}
	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
						resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST);
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();

					}
				});
				dialog.show();

			} else {
				Log.i("GCM", "This device is not supported.");
				finish();
			}
			return false;
		}
		return true;
	}

}
