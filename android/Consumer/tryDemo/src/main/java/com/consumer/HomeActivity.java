package com.consumer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.fragments.HomeFragment;
import com.consumer.gcm.LocationUpdateService;
import com.consumer.gcm.MyNotification;
import com.consumer.handler.HeaderLayout;
import com.consumer.handler.SimpleGestureFilter;
import com.consumer.handler.SimpleGestureFilter.SimpleGestureListener;

public class HomeActivity extends BaseActivity implements OnClickListener, SimpleGestureListener {

	private FragmentManager fragmentManager;

	private HomeFragment mHomeFragment;
	private HeaderLayout mHeaderLayout;

	Intent mIntent;
	Bundle bundle;

	 private SimpleGestureFilter detector;
	//private GestureDetectorCompat mGestureDetector; 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) // 1st time
        {
            //this.overridePendingTransition(R.anim.slide_in_left,
                    //R.anim.slide_out_left);
        }
		setContentView(R.layout.home_activity);

		initialization();

		mHeaderLayout = new HeaderLayout(this);
		/*mHeaderLayout.setHeaderIITII(R.drawable.logo_sbi_small,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu);*/

		mHeaderLayout.setListener(this, this, this, this);

		mHomeFragment = new HomeFragment();
		bundle = new Bundle();
		// bundle.putParcelable("patient", mPatient);
		mHomeFragment.setArguments(bundle);
		fragmentManager.beginTransaction()
				.replace(R.id.fragment_ll, mHomeFragment).commit();

        if (savedInstanceState == null){
            MyNotification.handleNotificationIfNeeded(this, getIntent());
        }

		
		 //detector = new SimpleGestureFilter(this,this);
		//mGestureDetector = new GestureDetectorCompat(this, new MyGestureListener());
		 
		 // Update location
		 LocationUpdateService.updateLocation(LocationUpdateService.getGPS(this),this);
	}
	
	@Override
	public void onNotificationHandled() {
		mHomeFragment.getCardDetails(false);
	}

	@Override
	public void onResume() {
		super.onResume();

		mHomeFragment.getCardDetails(false);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		boolean isNotifHandeled = MyNotification.handleNotificationIfNeeded(this, intent);
		if(isNotifHandeled){
			mHomeFragment.getCardDetails(false);
		}
	}

	private void initialization() {

		// /////
		fragmentManager = this.getSupportFragmentManager();

		/*
		 * mTasksTabHandler = new TasksTabHandler(headerLl);
		 * mTasksTabHandler.setTextResources(R.string.Home, R.string.Offers,
		 * R.string.Alerts); mTasksTabHandler.setBgResources(R.color.Black,
		 * R.color.dark_greyText, R.color.Black, R.color.dark_greyText,
		 * R.color.Black, R.color.dark_greyText);
		 * 
		 * mTasksTabHandler.setTextColor(getResources().getColor(R.color.LtGrey),
		 * getResources().getColor(R.color.White));
		 * mTasksTabHandler.handleTabs(0, this);
		 */

	}

	
	/**	...///... Listeners ....///.....*/
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_ib:
			//Toast.makeText(this, "Back button", Toast.LENGTH_SHORT).show();
			overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			finish();
			break;

		case R.id.right_extra_ib:
			// Toast.makeText(this, "Search button", Toast.LENGTH_SHORT).show();
			SingletonClass.showTxnSearchActivity(this,null);
			 
			break;

		case R.id.right_ib:
			//Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;

		default:
			break;
		}
	}
	
	/*@Override 
    public boolean onTouchEvent(MotionEvent event){ 
		
        return super.onTouchEvent(event);
    }*/
	
	/**	...///... Gesture Touch ....///.....*/
	
    public boolean dispatchTouchEvent(MotionEvent event) {
   // 	this.mGestureDetector.onTouchEvent(event);
    	 //this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
        
        // Call onTouchEvent of SimpleGestureFilter class
       
    }

	@Override
	public void onSwipe(int direction) { 
		
		switch (direction) {
		case SimpleGestureFilter.SWIPE_RIGHT :
//			overridePendingTransition(R.anim.right_slide_out, R.anim.right_slide_in);
//			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//			finish();
			
		break;
		case SimpleGestureFilter.SWIPE_LEFT :
			mIntent = new Intent(this, ManageSpendActivity.class);
			startActivity(mIntent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		
		break;
		
		case SimpleGestureFilter.SWIPE_UP: 		break;
		case SimpleGestureFilter.SWIPE_DOWN:  	break;
			
		
		}
		
		//Toast.makeText(HomeActivity.this, str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}
	

}
