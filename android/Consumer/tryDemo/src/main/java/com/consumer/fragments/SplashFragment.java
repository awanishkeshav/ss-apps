package com.consumer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.consumer.HomeActivity;
import com.consumer.SplashActivity;
import com.consumer.R;
import com.consumer.adapters.SplashPagerAdapter;
import com.consumer.common.Constants;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.dialogs.CardEntryDialog;
import com.consumer.models.Card;
import com.consumer.models.Card.CardNetwork;

/**
 * setting image in adaptor of fragment.
 */
@SuppressLint("ValidFragment")
public class SplashFragment extends Fragment {

	
	public TextView mCardNameTv;
	private ImageView mTopLogoIv, mBottomLogoIv;
	private View v;
	private RelativeLayout mTopRl;
	
	static int pageNo;
	int mCurrentPage, mListLength;
	Context mContext = null;

	public Card mCard;
	public Integer[] imgArray;
	public String titleArray[];
	
	SplashActivity mainObj;

	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		

	}*/

	public SplashFragment() {
		// empty constructor
	}

	public SplashFragment(Context mContext) {

		super();
		this.mContext = mContext;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		if(data != null){
			mCurrentPage= data.getInt("current_page", 0);
			mCard 		= (Card) data.getSerializable("Card");
			mListLength	= data.getInt("CardListLength", 0);
		}

		v = inflater.inflate(R.layout.splash_fragment_row, container,false);
		mTopLogoIv 		= (ImageView) v.findViewById(R.id.cardIv);
		switch (Constants.bank){
			case SBI:
				mTopLogoIv.setImageResource(R.drawable.logo_sbi_small);
				break;
			case IndusInd:
				mTopLogoIv.setImageResource(R.drawable.indusind_logo_small);
				break;
		}

		mBottomLogoIv	= (ImageView) v.findViewById(R.id.cardBottomIv);
		mCardNameTv		= (TextView) v.findViewById(R.id.cardName);
		mTopRl			= (RelativeLayout) v.findViewById(R.id.cardBgRl);
		
		if(mCard != null){
			
			
			if(mCurrentPage == mListLength){
				mTopRl.setBackgroundResource(R.drawable.selector_splashcard_add);
				mTopLogoIv.setVisibility(View.GONE);
				mCardNameTv.setVisibility(View.GONE);
				mBottomLogoIv.setVisibility(View.GONE);
				
				mTopRl.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CardEntryDialog cardEntryDialog = new CardEntryDialog(getActivity());
						
						cardEntryDialog.show();
						
					}
				});
				
			}else{
				mTopLogoIv.setVisibility(View.VISIBLE);
				mCardNameTv.setVisibility(View.VISIBLE);
				mBottomLogoIv.setVisibility(View.VISIBLE);
				mCardNameTv.setText(mCard.getCardDisplayString());
				Resources res = getResources();
				
				mBottomLogoIv.setImageResource(mCard.cardNetwork == CardNetwork.MASTER?R.drawable.logo_mastercard: R.drawable.logo_visa);
				
				mTopRl.setBackgroundResource(mCurrentPage % 3 == 0 ? R.drawable.selector_splashcard_1
				: mCurrentPage % 3 == 1 ? R.drawable.selector_splashcard_2 : R.drawable.selector_splashcard_3);
				
				mTopRl.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						PreferanceUtil preferanceUtil = new PreferanceUtil(getActivity());
						preferanceUtil.setCardDetail(mCard);
						SingletonClass.sharedInstance().setCard(mCard);
						Intent intent = new Intent(getActivity(), HomeActivity.class);
						intent.putExtra("ActivityName", "Splash");
						startActivity(intent);
						//getActivity().overridePendingTransition(R.anim.slide_in_right,
			                    //R.anim.slide_out_right);
						//getActivity().finish();
						
					}
				});
			}
		}
		return v;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
}
