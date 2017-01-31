package com.consumer;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.dialogs.BuyItemDialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.animation.Transformation;

public class ShopItemDetailsActivity extends BaseActivity implements
		OnClickListener {

	public static final String ExtraOfferVO = "ext_offer_vo";
	public static final String ExtraOfferID = "ext_offer_id";

	private ImageView mBackIv;
	private TextView mDetailsTv;
	private LinearLayout mDetailLL, mContentLL;
	private Button mBuyButton;
	private PopupWindow popWindow;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_item_details);
		this.initialization();
	}

	private void initialization() {

		mBackIv = (ImageView) findViewById(R.id.backIv);
		mBackIv.setOnClickListener(this);
		mBuyButton = (Button) findViewById(R.id.buyButton);
		mBuyButton.setOnClickListener(this);
		mDetailLL = (LinearLayout) findViewById(R.id.detailsButtonLL);
		mDetailLL.setOnClickListener(this);
		mDetailsTv = (TextView) findViewById(R.id.detailsTv);
		mContentLL = (LinearLayout) findViewById(R.id.contentLL);
		
		//mDetailsTv.getLayoutParams().height = 0;
		mDetailsTv.setVisibility(View.GONE);
		mDetailsTv.setText(getDetails());
	}

	private String getDetails() {
		//   
		String s = "Minimalist design distinguished an iconic watch marked with a signeture dot at 12 o'clock and fitted with a handsome linked bracelet";
		s += "\n\n";
		s += "\t";
		s += "- 40mm case; 21 mm band width.";
		s += "\n\t";
		s += "- Deployant clasp closure.";
		s += "\n\t";
		s += "- Stainless steel/sapphire crystal";
		s += "\n\t";
		s += "- Swiss made";
		return s;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv:
			finish();

			break;
		case R.id.left_ib:
			finish();

			break;
		case R.id.buyButton:

			BuyItemDialog dialog = new BuyItemDialog(this);
			dialog.show();
			break;
		case R.id.detailsButtonLL:
			if (mDetailsTv.getVisibility() == View.GONE/*mDetailsTv.getLayoutParams().height == 0*/) {
				//mDetailsTv.getLayoutParams().height = 100;
				 //ShopItemDetailsActivity.expand(mDetailsTv);
				 //mDetailsTv.setVisibility(View.VISIBLE);
				// mDetailsTv.requestLayout();
				showDetails();
			} else {
				//mDetailsTv.getLayoutParams().height = 0;
				 //ShopItemDetailsActivity.collapse(mDetailsTv);
				 //mDetailsTv.setVisibility(View.GONE);
				 //mDetailsTv.requestLayout();
			}

			break;
		case R.id.right_ib:
			// Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;
		case R.id.popUpLL:
			if (popWindow != null && popWindow.isShowing()) {
				popWindow.dismiss();
			}
			break;
		default:
			break;
		}

	}
	
	private void showDetails(){
		if (popWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View inflatedView = layoutInflater.inflate(R.layout.shop_item_details_popup, null,false);
			inflatedView.setOnClickListener(this);
			TextView detailsTv = (TextView) inflatedView.findViewById(R.id.detailsTv);
			//View hidePopupButton = inflatedView.findViewById(R.id.popUpDetailsButtonLL);
			//hidePopupButton.setOnClickListener(this);
			detailsTv.setText(getDetails());
			inflatedView.requestLayout();
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			popWindow = new PopupWindow(inflatedView, size.x,size.y, true );
			popWindow.setFocusable(true);
			popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
			popWindow.setOutsideTouchable(true);
		}
		
	
		popWindow.setAnimationStyle(R.style.popupAnim);
		popWindow.showAtLocation(mDetailLL, Gravity.BOTTOM, 0,0);  
	}

	public static void expand(final View v) {
		v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		final int targetHeight = v.getMeasuredHeight();

		v.getLayoutParams().height = 0;
		v.setVisibility(View.VISIBLE);
		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				v.getLayoutParams().height = interpolatedTime == 1 ? LayoutParams.WRAP_CONTENT
						: (int) (targetHeight * interpolatedTime);
				v.requestLayout();
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		int t = (int)(targetHeight / v.getContext().getResources()
				.getDisplayMetrics().density);
		a.setDuration(t );
		v.startAnimation(a);
	}

	public static void collapse(final View v) {
		final int initialHeight = v.getMeasuredHeight();

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime,
					Transformation t) {
				if (interpolatedTime == 1) {
					v.setVisibility(View.GONE);
				} else {
					v.getLayoutParams().height = initialHeight
							- (int) (initialHeight * interpolatedTime);
					v.requestLayout();
				}
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};

		// 1dp/ms
		a.setDuration((int) (initialHeight / v.getContext().getResources()
				.getDisplayMetrics().density));
		v.startAnimation(a);
	}
}
