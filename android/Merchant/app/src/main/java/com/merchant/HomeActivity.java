package com.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.merchant.R;
import com.merchant.fragments.HomeFragment;
import com.merchant.gcm.MyNotification;
import com.merchant.handler.HeaderLayout;
import com.merchant.handler.SimpleGestureFilter;
import com.merchant.handler.SimpleGestureFilter.SimpleGestureListener;
import com.merchant.models.Error;

public class HomeActivity extends BaseActivity implements OnClickListener,
		SimpleGestureListener {

	private FragmentManager fragmentManager;

	private HomeFragment mHomeFragment;
	private HeaderLayout mHeaderLayout;

	private SimpleGestureFilter detector;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_activity);

		initialization();

	}
	
	@Override
	public void onBackendConstantsLoad(Error error){
		super.onBackendConstantsLoad(error);
		mHomeFragment.onBackendConstantsLoad(error);
	}

	@Override
	public void onNotificationHandled() {
		mHomeFragment.getSummary(false);
	}

	@Override
	public void onResume() {
		super.onResume();

		mHomeFragment.getSummary(false);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		boolean isNotifHandeled = MyNotification.handleNotificationIfNeeded(
				this, intent);
		if (isNotifHandeled) {
			mHomeFragment.getSummary(false);
		}
	}

	private void initialization() {

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setListener(this, this, this, this);
		
		fragmentManager = this.getSupportFragmentManager();
		mHomeFragment = new HomeFragment();

		fragmentManager.beginTransaction()
				.replace(R.id.fragment_ll, mHomeFragment).commit();
		MyNotification.handleNotificationIfNeeded(this, getIntent());
		detector = new SimpleGestureFilter(this, this);
	}

	/** ...///... Listeners ....///..... */

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

		default:
			break;
		}
	}

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event){
	 * 
	 * return super.onTouchEvent(event); }
	 */

	/** ...///... Gesture Touch ....///..... */

	public boolean dispatchTouchEvent(MotionEvent event) {
		// this.mGestureDetector.onTouchEvent(event);
		this.detector.onTouchEvent(event);
		return super.dispatchTouchEvent(event);

		// Call onTouchEvent of SimpleGestureFilter class

	}

	@Override
	public void onSwipe(int direction) {

		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT:
			// overridePendingTransition(R.anim.right_slide_out,
			// R.anim.right_slide_in);
			// overridePendingTransition(R.anim.slide_in_left,
			// R.anim.slide_out_right);
			// finish();

			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			// mIntent = new Intent(this, ManageSpendActivity.class);
			// startActivity(mIntent);
			// overridePendingTransition(R.anim.slide_in_right,
			// R.anim.slide_out_left);

			break;

		case SimpleGestureFilter.SWIPE_UP:
			break;
		case SimpleGestureFilter.SWIPE_DOWN:
			break;

		}

		// Toast.makeText(HomeActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub

	}
	
	

}
