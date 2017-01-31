package com.merchant;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.merchant.utility.ImageDownloader;
import com.merchant.R;
import com.merchant.common.Constants;
import com.merchant.common.PreferanceUtil;
import com.merchant.common.SingletonClass;
import com.merchant.dialogs.AddOfferTargetDialog;
import com.merchant.handler.FooterTabHandler;
import com.merchant.handler.FooterTabHandler.FooterTabListener;
import com.merchant.handler.HeaderLayout;
import com.merchant.models.Error;
import com.merchant.models.KeyValueObject;
import com.merchant.models.Offer;
import com.merchant.models.Offer.Status;
import com.merchant.models.TargetingVO;
import com.merchant.services.APIService;
import com.merchant.utility.DateConverter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OfferDetailsActivity extends BaseActivity implements
		OnClickListener, FooterTabListener {

	public static final String ExtraOfferVO = "ext_offer_vo";
	public static final String ExtraOfferID = "ext_offer_id";

	private static final int PICK_IMAGE = 1;

	private HeaderLayout mHeaderLayout;
	private FooterTabHandler mFooterTabHandler;
	private EditText mTagLineEt, mDetailsEt, mExpiresEt, mCodeEt,
			mCodeDisplayEt;
	private LinearLayout mBottomListLayout, mTargetingLayout;
	private ImageButton mAddImageBtn, mRemoveImageBtn, mAddTargetingBtn;
	private Button mCreateOfferBtn;
	private View imgLoading;

	private DatePickerDialog datePicker;
	private ProgressDialog dialog;

	private ArrayList<TargetingVO> mTargetingList;
	private Offer offer;
	private Long offerId;

	private Bitmap bitmap;
	private String selectedImagePath;
	private Date selectedExpiryDate;

	private KeyValueObject selectedCouponCodeType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		if (getIntent().hasExtra(ExtraOfferID)) {
			this.offerId = getIntent().getLongExtra(ExtraOfferID, 0);
		}

		Object obj = getIntent().getSerializableExtra(ExtraOfferVO);
		if (obj != null) {
			this.offer = (Offer) obj;
			this.offerId = this.offer.id;
		}

		this.initialization();
		this.reloadUI();
		this.loadOfferdetails();
		downloadImageIfNeeded();
		loadOfferTargeting(false);
	}

	private void initialization() {

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setHeaderIITII(R.drawable.icon_arrow_back_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				HeaderLayout.RES_NONE, R.drawable.icon_menu_white);
		mHeaderLayout.setListener(this, this, this, this);

		View footerView = findViewById(R.id.tab_footer);
		mFooterTabHandler = new FooterTabHandler(footerView);
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_archive,
				R.drawable.icon_report, FooterTabHandler.RES_NONE,
				FooterTabHandler.RES_NONE);
		mFooterTabHandler.setTabTitles("Archive", "Suspend", null, null);

		mTagLineEt = (EditText) findViewById(R.id.tagLineET);
		mDetailsEt = (EditText) findViewById(R.id.detailsET);
		mCodeEt = (EditText) findViewById(R.id.offerCodeET);
		mCodeDisplayEt = (EditText) findViewById(R.id.codeDisplayET);
		mExpiresEt = (EditText) findViewById(R.id.expiresET);
		mExpiresEt.setOnClickListener(this);
		mExpiresEt.setInputType(EditorInfo.TYPE_NULL);

		mCodeDisplayEt.setOnClickListener(this);
		mCodeDisplayEt.setInputType(EditorInfo.TYPE_NULL);

		mAddImageBtn = (ImageButton) findViewById(R.id.addImageBtn);
		mRemoveImageBtn = (ImageButton) findViewById(R.id.removeImageBtn);
		mAddTargetingBtn = (ImageButton) findViewById(R.id.addTargetingBtn);
		mCreateOfferBtn = (Button) findViewById(R.id.createOfferButton);
		imgLoading = findViewById(R.id.imgLoading);
		imgLoading.setVisibility(View.GONE);
		mAddImageBtn.setOnClickListener(this);
		mRemoveImageBtn.setOnClickListener(this);
		mAddTargetingBtn.setOnClickListener(this);
		mCreateOfferBtn.setOnClickListener(this);

		mBottomListLayout = (LinearLayout) findViewById(R.id.bottomListLL);
		mTargetingLayout = (LinearLayout) findViewById(R.id.targetingLL);

		if (this.offer != null) {
			mTagLineEt.setText(offer.title);
			mDetailsEt.setText(offer.desc);
			mCodeEt.setText(offer.code);
			selectedCouponCodeType = SingletonClass.sharedInstance()
					.getbConstants().couponCodeTypeByKey(offer.codeType);
			if (selectedCouponCodeType != null) {
				mCodeDisplayEt.setText(selectedCouponCodeType.value);
			}

			if (offer.expiryDate != null) {
				selectedExpiryDate = offer.expiryDate;
				mExpiresEt.setText(DateConverter
						.getStringFromDateWitoutTime(this,selectedExpiryDate));
			}
			
			int tab1 = R.drawable.icon_archive;
			int tab2 = R.drawable.icon_report;
			if (offer.status == Status.Archived) {
				tab1 = FooterTabHandler.RES_NONE;
				tab2 = FooterTabHandler.RES_NONE;
				
			}else if (offer.status == Status.InActive) {
				tab2 = FooterTabHandler.RES_NONE;
			}
			mFooterTabHandler.setTabs(tab1,
					tab2, FooterTabHandler.RES_NONE,
					FooterTabHandler.RES_NONE);

		}

	}

	private void setTargetingListUI() {

		mTargetingLayout.removeAllViews();
		if (mTargetingList != null && !mTargetingList.isEmpty()) {
			// mBottomListLayout.setVisibility(View.VISIBLE);

			for (int i = 0; i < mTargetingList.size(); i++) {

				View rowView = getLayoutInflater().inflate(
						R.layout.blocked_row, null);

				final TextView marchNameTv = (TextView) rowView
						.findViewById(R.id.marchantNameTv);
				final TextView blockDateTv = (TextView) rowView
						.findViewById(R.id.dateTv);
				ImageView removeIv = (ImageView) rowView
						.findViewById(R.id.removeIv);
				removeIv.setTag(i);
				mTargetingLayout.addView(rowView);

				final TargetingVO targetVO = mTargetingList.get(i);
				marchNameTv.setText(targetVO.getTitle());
				blockDateTv.setText(targetVO.getDescription());

				removeIv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int index = (Integer) v.getTag();
						TargetingVO target = mTargetingList.get(index);
						deleteOfferTargeting(target.id);

					}
				});

			}
		}
	}

	private void reloadUI() {

//		if (offer == null || offer.getImageUrl() == null || offer.getImageUrl().isEmpty()) {
//			mRemoveImageBtn.setVisibility(View.INVISIBLE);
//		} else {
//			mRemoveImageBtn.setVisibility(View.VISIBLE);
//		}
		
		if (selectedImagePath == null) {
			mRemoveImageBtn.setVisibility(View.INVISIBLE);
		} else {
			mRemoveImageBtn.setVisibility(View.VISIBLE);
		}

		if (offerId == null) {
			// Create New
			mCreateOfferBtn.setVisibility(View.VISIBLE);
			mBottomListLayout.setVisibility(View.GONE);
			mFooterTabHandler.rootLayout.setVisibility(View.GONE);
			mHeaderLayout.setTitle("NEW OFFER");

		} else {
			mTagLineEt.setEnabled(false);
			mDetailsEt.setEnabled(false);
			mExpiresEt.setEnabled(false);
			mCodeEt.setEnabled(false);
			mCodeDisplayEt.setEnabled(false);

			mCreateOfferBtn.setVisibility(View.GONE);
			mBottomListLayout.setVisibility(View.VISIBLE);
			mFooterTabHandler.rootLayout.setVisibility(View.VISIBLE);
			mHeaderLayout.setTitle("OFFER DETAILS");
			if (offer != null){
				if(offer.status == Status.Active) {
					mHeaderLayout.setSubtitle("Active");
				} else if(offer.status == Status.InActive) {
					mHeaderLayout.setSubtitle("Inactive");
				}
				else if(offer.status == Status.Archived) {
					mHeaderLayout.setSubtitle("Archived");
				}
			}

		}

		setTargetingListUI();
	}

	private void callCreateOfferService() {
		
		if (dialog == null) {
			dialog = ProgressDialog.show(this, "Uploading",
	                "Please wait...", true);
		}
		
		dialog.show();
		
		new ImageUploadTask().execute();
	}

	private void loadOfferdetails() {

		if (this.offerId == null) {
			return;
		}
		APIService service = APIService.getOfferDetails(this, this.offerId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						offer = new Offer((JSONObject) data);
						reloadUI();
					}

				}

			}
		});
		service.getData(false);
	}

	private void loadOfferTargeting(boolean showLoading) {
		if (this.offerId == null) {
			return;
		}
		APIService service = APIService.getAllOfferTargets(this, offerId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {

				if (error == null) {
					if (data instanceof JSONArray) {
						mTargetingList = new ArrayList<TargetingVO>();
						JSONArray arr = (JSONArray)data;
						for (int i = 0; i < arr.length(); i++) {
							mTargetingList.add(new TargetingVO(arr.optJSONObject(i)));
						}

						reloadUI();
					}

				}

			}
		});
		service.getData(showLoading);
	}

	private void deleteOfferTargeting(Long targetId) {

		APIService service = APIService.deleteOfferTarget(this, offerId,
				targetId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					loadOfferTargeting(true);

				}

			}
		});
		service.getData(true);
	}
	
	private void setOfferSatus(final String status){
		
		APIService service = APIService.setOfferStatus(this, offerId, status);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					Toast.makeText(OfferDetailsActivity.this, "Offer "+status, Toast.LENGTH_LONG).show();
					finish();

				}

			}
		});
		service.getData(true);
	}

	private boolean checkValidation() {

		String tagLine = mTagLineEt.getText().toString().trim();
		String codeType = mCodeDisplayEt.getText().toString().trim();

		Toast toast = null;
		if (tagLine.isEmpty()) {
			toast = Toast.makeText(this, "Tag Line cannot be empty",
					Toast.LENGTH_SHORT);

		}

		if (toast == null)
			return true;
		else {
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
			return false;
		}
	}

	private void clearFocus() {
		mTagLineEt.clearFocus();
		mDetailsEt.clearFocus();
		mCodeEt.clearFocus();
		mCodeDisplayEt.clearFocus();
		mExpiresEt.clearFocus();
	}

	private void showDatePicker() {
		if (datePicker == null) {
			final Calendar myCalendar = Calendar.getInstance();

			datePicker = new DatePickerDialog(this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							myCalendar.set(Calendar.YEAR, year);
							myCalendar.set(Calendar.MONTH, monthOfYear);
							myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
							mExpiresEt.setText(DateConverter
									.getStringFromDateWitoutTime(
											OfferDetailsActivity.this,
											myCalendar.getTime()));
							selectedExpiryDate = myCalendar.getTime();

						}
					}, myCalendar.get(Calendar.YEAR),
					myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH));
			datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
			
		}

		datePicker.show();
	}

	private void showAddTargetDialog(TargetingVO target) {
		AddOfferTargetDialog dialog = new AddOfferTargetDialog(this, offerId, target,
				new AddOfferTargetDialog.AddTargetingListner() {

					@Override
					public void onAddTarget() {

						loadOfferTargeting(true);
					}
				});
		dialog.show();

	}

	private void showCodeDisplayPicker() {
		final ArrayList<String> list = SingletonClass.sharedInstance()
				.getbConstants().getCouponCodeTypeStrings();
		SingletonClass.showItemPicker(this, null, list,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedCouponCodeType = SingletonClass
								.sharedInstance().getbConstants().couponCodeTypes
								.get(which);
						mCodeDisplayEt.setText(selectedCouponCodeType.value);

					}
				});
	}

	private void createOffer() {
		if (checkValidation()) {
			clearFocus();
			callCreateOfferService();
		}

	}
	
	private void removeImage(){
		selectedImagePath = null;
		bitmap = null;
		mAddImageBtn.setImageResource(R.drawable.btn_add);
		reloadUI();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	
		case R.id.left_ib:
			finish();

			break;
		case R.id.addImageBtn:
			if (offerId == null) {
				pickImage();
			}

			break;
		case R.id.addTargetingBtn:
			showAddTargetDialog(null);

			break;
		case R.id.removeImageBtn:
			removeImage();

			break;

		case R.id.createOfferButton:
			createOffer();

			break;
		case R.id.expiresET:
			showDatePicker();

			break;

		case R.id.codeDisplayET:
			showCodeDisplayPicker();
			break;

		case R.id.right_ib:
			// Toast.makeText(this, "Menu button", Toast.LENGTH_SHORT).show();
			this.showDrawer();
			break;

		default:
			break;
		}

	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:
			// Archive
			setOfferSatus("Archived");

			break;
		case R.id.tab_2_tv:
			// Suspend
			setOfferSatus("Suspended");

			break;

		default:
			break;
		}

	}
	
	private void downloadImageIfNeeded(){
		
		if (this.offer != null && this.offer.getImageUrl() != null && !this.offer.getImageUrl().isEmpty()) {
			imgLoading.setVisibility(View.VISIBLE);
			mAddImageBtn.setVisibility(View.GONE);
			ImageDownloader imgDownloader = ImageDownloader.download(this.offer.getImageUrl());
			imgDownloader.setmDownloadListener(new ImageDownloader.ImageDownloaderListener() {
				
				@Override
				public void onReceive(Bitmap bitmap, String url) {
					imgLoading.setVisibility(View.GONE);
					mAddImageBtn.setVisibility(View.VISIBLE);
					mAddImageBtn.setImageBitmap(bitmap);
					
				}
			});
		}
		
	}

	private void pickImage() {
		try {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
		} catch (Exception e) {
			Toast.makeText(this, "Can not pick image", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					selectedImagePath = getPath(selectedImageUri);
					reloadUI();

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(getApplicationContext(), "Unknown path",
								Toast.LENGTH_LONG).show();
						Log.e("Bitmap", "Unknown path");
					}

					if (filePath != null) {
						decodeFile(filePath);
					} else {
						bitmap = null;
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			break;
		default:
		}
	}

	public String getPath(Uri contentURI) {
		// return uri.getEncodedPath();
		String result;
		Cursor cursor = getContentResolver().query(contentURI, null, null,
				null, null);
		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			int idx = cursor
					.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(idx);
			cursor.close();
		}
		return result;
	}

	public void decodeFile(String filePath) {
		// Decode image size

		bitmap = SingletonClass.decodeSampledBitmapFromFile(filePath, 1024,
				1024);
		mAddImageBtn.setImageBitmap(bitmap);

	}

	/**************** Image Upload *********/
	class ImageUploadTask extends AsyncTask<Void, Void, String> {
		@Override
        protected String doInBackground(Void... unsued) {
            try {
            	
            	String tagLine = mTagLineEt.getText().toString().trim();
        		String details = mDetailsEt.getText().toString().trim();
        		String code = mCodeEt.getText().toString().trim();
        		String codeDisplayType = "";
        		if (selectedCouponCodeType != null) {
        			codeDisplayType = selectedCouponCodeType.key;
        		}
        		String expiry = "";
        		if (selectedExpiryDate != null) {
        			expiry = ""+selectedExpiryDate.getTime();
				}
        		
      
            	HttpClient client = new DefaultHttpClient();
            	
            	
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                
                entityBuilder.addTextBody("title", tagLine);
                entityBuilder.addTextBody("description", details);
                entityBuilder.addTextBody("code", code);
                entityBuilder.addTextBody("codeType", codeDisplayType);
                if (!expiry.isEmpty()) {
                	entityBuilder.addTextBody("endDate", expiry);
				}
                
          
                if(selectedImagePath != null)
                {
                    entityBuilder.addBinaryBody("imgUrl", new File(selectedImagePath));
                }

                HttpEntity entity = entityBuilder.build();

                HttpPost post = new HttpPost(Constants.getRESTApiBaseUrl()+"/beapi/merchant/offer");
                String merchantUUID = new PreferanceUtil(OfferDetailsActivity.this).getMerchantUUID();
                post.addHeader("ssmerchanttoken", merchantUUID);
                post.setEntity(entity);
                HttpResponse response = client.execute(post);

                HttpEntity httpEntity = response.getEntity();

                String result = EntityUtils.toString(httpEntity);

                return result;
            } catch (Exception e) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(OfferDetailsActivity.this,"Cannot upload image",
                        Toast.LENGTH_LONG).show();
                Log.e("upload", e.getMessage(), e);
                return null;
            }
 
            // (null);
        }

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing())
					dialog.dismiss();

				if (sResponse != null) {
					JSONObject JResponse = new JSONObject(sResponse);
					boolean success = JResponse.optBoolean("success",false);
					
					if (!success) {
						String message = JResponse.optString("msg","");
						
						Toast.makeText(getApplicationContext(), message,
								Toast.LENGTH_LONG).show();
					} else {
						
						Toast.makeText(getApplicationContext(),
								"Offer created successfully",
								Toast.LENGTH_SHORT).show();
						finish();
						
					}
				}
			} catch (Exception e) {
				Toast.makeText(OfferDetailsActivity.this,
						"Something went wrong",
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}

	}

	/**************** Image Upload *********/

}
