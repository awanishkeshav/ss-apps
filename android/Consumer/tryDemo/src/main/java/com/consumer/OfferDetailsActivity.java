package com.consumer;

import org.json.JSONObject;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.gcm.MyNotification;
import com.consumer.handler.HeaderLayout;
import com.consumer.models.Error;
import com.consumer.models.Merchant;
import com.consumer.models.Offer;
import com.consumer.services.APIService;
import com.consumer.utility.DateConverter;
import com.consumer.utility.ImageDownloader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OfferDetailsActivity extends BaseActivity implements
		OnClickListener {

	public static final String ExtraOfferVO = "ext_offer_vo";
	public static final String ExtraOfferID = "ext_offer_id";

	private ImageView mBackIv;
	
	private HeaderLayout mHeaderLayout;
	private TextView mMerchantNameTv, mAddressTv, mTotalRatingTv,
			mOfferTitleTv, mOfferDescTv, mOfferExpiryTv, mImgTitleTv, mImgDescTv,mOfferCodeTv;
	private RatingBar mAvgRatingBar;
	private LinearLayout mContentLayout, mImgLayput;
	private RelativeLayout mCallMerchantView;
	private ImageView mOfferImageView,mOfferCodeImageView;
	private LinearLayout mImgLL;
	private ProgressBar mImgLoading;
	private Offer offer;
	private Long offerId;
	
	private Long avgRating = (long) 0;
	private Long totalReview = (long) 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);
		MyNotification notif = MyNotification.getNotification(getIntent()
				.getExtras());
		if (notif == null) {
			this.offerId = getIntent().getLongExtra(ExtraOfferID, 0);
			Object obj = getIntent().getSerializableExtra(ExtraOfferVO);
			if (obj != null) {
				this.offer = (Offer) obj;
				this.offerId = this.offer.id;
			}
		}else{
			this.offerId = notif.getOfferId();
		}
		

		this.initialization();
		this.reloadUI();
		
		if (this.offer == null) {
			this.loadOfferdetails(true);
		}else{
			this.loadOfferdetails(false);
		}
		
		this.loadMerchantReviewCount();
		loadImageIfNeeded();
	}

	private void initialization() {

		mBackIv		= (ImageView)findViewById(R.id.backIv);
		mBackIv.setOnClickListener(this);
		
		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				R.drawable.icon_search, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);
		mMerchantNameTv = (TextView) findViewById(R.id.merchantNameTv);
		mAddressTv = (TextView) findViewById(R.id.address_tv);
		mTotalRatingTv = (TextView) findViewById(R.id.totalRatingTv);
		mAvgRatingBar = (RatingBar) findViewById(R.id.avgRatingBar);
		
		mOfferTitleTv = (TextView) findViewById(R.id.offerTitleTv);
		mOfferDescTv = (TextView) findViewById(R.id.offerDescTv);
		mOfferExpiryTv = (TextView) findViewById(R.id.expiryTv);
		mImgTitleTv = (TextView) findViewById(R.id.imgTitleTv);
		mImgDescTv = (TextView) findViewById(R.id.imgDescTv);
		
		mContentLayout = (LinearLayout) findViewById(R.id.contentLL);
		mContentLayout.setVisibility(View.GONE);
		
		mImgLayput = (LinearLayout) findViewById(R.id.imageLL);
		mImgLayput.setVisibility(View.GONE);
		
		mCallMerchantView = (RelativeLayout) findViewById(R.id.callMerchantView);
		mCallMerchantView.setOnClickListener(this);
		
		mOfferImageView = (ImageView) findViewById(R.id.offerIv);
		mOfferImageView.setImageBitmap(null);
		mImgLoading = (ProgressBar) findViewById(R.id.imgLoading);
		mImgLL = (LinearLayout) findViewById(R.id.imgLayout);
		mImgLL.setVisibility(View.GONE);
		
		mOfferCodeImageView = (ImageView) findViewById(R.id.code_image);
		mOfferCodeTv = (TextView) findViewById(R.id.offerCodeTv);
		mOfferCodeTv.setVisibility(View.GONE);
		mOfferCodeImageView.setVisibility(View.GONE);
		

	}

	private void reloadUI() {

		Merchant merchant = null;
		if (this.offer != null && this.offer.merchant != null) {
			merchant = this.offer.merchant;
		}

		this.mMerchantNameTv.setText("-----------");
		this.mAddressTv.setText("-----\n-----");
		this.mOfferTitleTv.setText("");
		this.mOfferDescTv.setText("");
		this.mOfferExpiryTv.setText("");
		this.mImgDescTv.setText("");
		this.mImgTitleTv.setText("");
		this.mAvgRatingBar.setRating(0);
		this.mTotalRatingTv.setText("0 Reviews");

		if (merchant != null) {
			this.mMerchantNameTv.setText(merchant.name);
			this.mAddressTv.setText(merchant.address);
			this.mImgTitleTv.setText(merchant.name);
		}

		Long totalratings = this.totalReview;
		if (totalratings == 1) {
			this.mTotalRatingTv.setText("1 Review");
		} else {
			this.mTotalRatingTv.setText(SingletonClass.stringFromNumber(
					totalratings, 0) + " Reviews");
		}
		mAvgRatingBar.setRating(avgRating);
		
		if (this.offer != null) {
			
			if (this.offer.getImageUrl() != null && !this.offer.getImageUrl().isEmpty()) {
				this.mContentLayout.setVisibility(View.GONE);
				this.mImgLayput.setVisibility(View.VISIBLE);
			}else{
				this.mContentLayout.setVisibility(View.VISIBLE);
				this.mImgLayput.setVisibility(View.GONE);
			}
			
			this.mOfferTitleTv.setText(this.offer.title);
			this.mOfferDescTv.setText(this.offer.desc);
			String expiryStr = "";
			if(this.offer.expiryDate != null){
				expiryStr = "Offer valid till "+ DateConverter.getStringFromDateWitoutTime(
						this, this.offer.expiryDate);
				
			}
			this.mOfferExpiryTv.setText(expiryStr);
			this.mImgDescTv.setText(expiryStr);
			
			if (this.offer.codeType != null) {
				if (this.offer.codeType.equals("text") && this.offer.code != null && !this.offer.code.isEmpty()) {
					mOfferCodeTv.setVisibility(View.VISIBLE);
					mOfferCodeTv.setText(this.offer.code);
				}else {
					mOfferCodeImageView.setVisibility(View.VISIBLE);
					if (this.offer.codeType.startsWith("bar")) {
						mOfferCodeImageView.setImageResource(R.drawable.default_barcode);
						
					}else if (this.offer.codeType.startsWith("qr")) {
						mOfferCodeImageView.setImageResource(R.drawable.default_qrcode);

					}
				}
			}
			
			
		}

	}

	private void loadOfferdetails(boolean showLoading) {
		APIService service = APIService.getOfferDetails(this, this.offerId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						offer = new Offer((JSONObject) data);
						loadImageIfNeeded();
						reloadUI();
					}

				}

			}
		});
		service.getData(showLoading);
	}
	
	private void loadMerchantReviewCount() {
		if (this.offer == null || this.offer.merchant == null) {
			return;
		}
		APIService service = APIService
				.getReviewCountForMerchant(this, this.offer.merchant.id);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					JSONObject json = (JSONObject) data;
					avgRating = json.optLong("average");
					totalReview = json.optLong("total");
					reloadUI();
				}

			}
		});
		service.getData(false);
	}
	
	private void loadImageIfNeeded(){
		
		if (this.offer != null && this.offer.getImageUrl() != null && !this.offer.getImageUrl().isEmpty()) {
			mImgLoading.setVisibility(View.VISIBLE);
			ImageDownloader imgDownloader = ImageDownloader.download(this.offer.getImageUrl());
			imgDownloader.setmDownloadListener(new ImageDownloader.ImageDownloaderListener() {
				
				@Override
				public void onReceive(Bitmap bitmap, String url) {
					mImgLoading.setVisibility(View.GONE);
					mImgLL.setVisibility(View.VISIBLE);
					mOfferImageView.setImageBitmap(bitmap);
					
				}
			});
		}
		
	}
	private void callMerchant() {
		if (this.offer != null && offer.merchant != null && offer.merchant.phone != null
				&& !offer.merchant.phone.equals("")) {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ offer.merchant.phone));
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backIv :
			finish();

			break;
		case R.id.left_ib:
			finish();

			break;
		case R.id.callMerchantView:
			callMerchant();

			break;
		case R.id.right_ib:
			//Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;

		default:
			break;
		}

	}
}
