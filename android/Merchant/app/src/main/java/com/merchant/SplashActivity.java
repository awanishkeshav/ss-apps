package com.merchant;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.merchant.R;
import com.merchant.adapters.SplashPagerAdapter;
import com.merchant.common.Constants;
import com.merchant.common.PreferanceUtil;
import com.merchant.common.SingletonClass;
import com.merchant.gcm.GcmIntentService;
import com.merchant.models.Error;
import com.merchant.models.Merchant;
import com.merchant.services.APIService;
import com.merchant.widget.CirclePageIndicator;

public class SplashActivity extends BaseActivity implements
		View.OnClickListener {

	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	public ViewPager mScreenSlideVp;
	public CirclePageIndicator mIndicator;
	private ViewPager.OnPageChangeListener mPageChangeListener;
	private FragmentManager fm;
	public SplashPagerAdapter pagerAdapter;

	private ArrayList<Merchant> merchants;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.useDrawerLayout = false;
		setContentView(R.layout.splash_activity);
		controlInitialization();

		pagerAdapter = new SplashPagerAdapter(fm, this);
		mScreenSlideVp.setAdapter(pagerAdapter);
		mIndicator.setViewPager(mScreenSlideVp);

		if (!SingletonClass.isMerchantRegistered(this)) {
			// checkForMerchantRegistration();
		} else {
			reloadUI();
		}

		getAllMerchants();

		if (!Constants.MODE_PRODUCTION) {
			Toast.makeText(this, "Development Mode", Toast.LENGTH_LONG).show();
		}
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
		mIndicator = (com.merchant.widget.CirclePageIndicator) findViewById(R.id.indicator);

		// Listeners
		fm = getSupportFragmentManager();
		mIndicator.setOnPageChangeListener(mPageChangeListener);

		View sbiLogo = findViewById(R.id.logo);
		sbiLogo.setOnClickListener(this);

	}

	private void checkForMerchantRegistration() {

		// new
		// PreferanceUtil(SplashActivity.this).setMerchantUUID(Constants.MERCHANT_UUID);

		APIService service = APIService.isMerchantRegistered(this);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					String merchantUUID = new PreferanceUtil(
							SplashActivity.this).getMerchantUUID();
					// new
					// PreferanceUtil(SplashActivity.this).setMerchantUUID(Constants.MERCHANT_UUID);
				}
				reloadUI();
			}
		});
		service.getData(true);
	}

	public void reloadUI() {

		mScreenSlideVp.setAdapter(pagerAdapter);
		pagerAdapter.notifyDataSetChanged();

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

	private void getAllMerchants() {
		APIService service = APIService.getAllMerchant(this);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONArray) {
						JSONArray arr = (JSONArray) data;
						merchants = new ArrayList<Merchant>();
						for (int i = 0; i < arr.length(); i++) {
							merchants.add(new Merchant(arr.optJSONObject(i)));
						}
					}
				}

			}
		});
		service.getData(false);
	}

	private void showMerchantList() {
		if (merchants != null) {
			ArrayList<String> merchantNames = new ArrayList<String>();
			for (Merchant merchant : merchants) {
				merchantNames.add(merchant.name);
			}

			SingletonClass.showItemPicker(this, null, merchantNames,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Merchant merchant = merchants.get(which);
							new PreferanceUtil(SplashActivity.this)
									.setMerchantUUID(merchant.UUID);
                            new PreferanceUtil(SplashActivity.this).setMerchantName(merchant.name);
							reloadUI();
							Toast.makeText(SplashActivity.this,
									"Merchant Changed: " + merchant.name,
									Toast.LENGTH_SHORT).show();

						}
					});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.logo:
			showMerchantList();
			break;

		default:
			break;
		}
	}

}
