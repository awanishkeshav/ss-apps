package com.merchant;

import org.json.JSONObject;

import com.merchant.dialogs.ReviewCategoryEntryDialog;
import com.merchant.handler.FooterTabHandler;
import com.merchant.handler.FooterTabHandler.FooterTabListener;
import com.merchant.handler.HeaderLayout;
import com.merchant.models.Error;
import com.merchant.models.Merchant;
import com.merchant.services.APIService;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MerchantProfileActivity extends BaseActivity implements
		FooterTabListener, View.OnClickListener {

	private HeaderLayout mHeaderLayout;
	private FooterTabHandler mFooterTabHandler;
	private EditText nameEt, descEt, hoursEt;
	private Merchant merchant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_profile);
		getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
								| WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initialization();
		getMurchantDetails();
	}

	private void initialization() {
		// LayoutInflater inflater = (LayoutInflater)
		// this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mHeaderLayout = new HeaderLayout(this);
		mHeaderLayout.setListener(this, this, this, this);
		mHeaderLayout.setHeaderIITII(R.drawable.selector_back_arrow_white,
				HeaderLayout.RES_NONE, HeaderLayout.RES_NONE,
				HeaderLayout.RES_NONE, R.drawable.icon_menu_white);
		mHeaderLayout.setTitle("BUSINESS PROFILE");

		mFooterTabHandler = new FooterTabHandler(findViewById(R.id.tab_footer));
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_gear,
				FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE,
				FooterTabHandler.RES_NONE);
		mFooterTabHandler.setTabTitles("Feedback Setup", null, null, null);
		mFooterTabHandler.removeLastTab();

		nameEt = (EditText) findViewById(R.id.nameET);
		descEt = (EditText) findViewById(R.id.descET);
		hoursEt = (EditText) findViewById(R.id.hoursET);

		Button updateButton = (Button) findViewById(R.id.updateButton);
		updateButton.setOnClickListener(this);

	}

	private void setData() {
		if (merchant != null) {
			nameEt.setText(merchant.name);
			descEt.setText(merchant.desc);
			hoursEt.setText(merchant.businessHours);

		}
	}

	private void showAddReviewCategoriesDialong() {
		ReviewCategoryEntryDialog dialog = new ReviewCategoryEntryDialog(this);
		dialog.show();
	}

	private void getMurchantDetails() {
		APIService service = APIService.getMerchantDetails(this);
		service.setServiceListener(new APIService.APIServiceListener() {
			
			@Override
			public void onCompletion(APIService service, Object data, Error error) {
				if (error == null) {
					if (data instanceof JSONObject) {
						merchant = new Merchant((JSONObject) data);
						setData();
					}
				}
				
			}
		});
		service.getData(false);

	}

	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:
			showAddReviewCategoriesDialong();
			break;

		default:
			break;
		}

	}
	
	private boolean checkValidation() {

		String name = nameEt.getText().toString().trim();
		//String desc = descEt.getText().toString().trim();
		//String hours = hoursEt.getText().toString().trim();

		Toast toast = null;
		if (name.isEmpty()) {
			toast = Toast.makeText(this,
					"Please enter valid business name", Toast.LENGTH_SHORT);

		} 
		
		/*else if (desc.isEmpty()) {
			toast = Toast.makeText(this,
					"Please enter valid description", Toast.LENGTH_SHORT);

		}else if (hours.isEmpty()) {
			toast = Toast.makeText(this,
					"Please enter valid hours", Toast.LENGTH_SHORT);
		}
		*/

		if (toast == null)
			return true;
		else {
			toast.setGravity(Gravity.TOP, 0, 30);
			toast.show();
			return false;
		}
	}

	private void clearFocus() {
		nameEt.clearFocus();
		descEt.clearFocus();
		hoursEt.clearFocus();
	}
	
	private void updateProfile(){
		String name = nameEt.getText().toString().trim();
		String desc = descEt.getText().toString().trim();
		String hours = hoursEt.getText().toString().trim();
		
		APIService service = APIService.updateMerchantProfile(this, name, desc, hours);
		service.setServiceListener(new APIService.APIServiceListener() {
			
			@Override
			public void onCompletion(APIService service, Object data, Error error) {
				if (error == null){
					finish();
					
				} else {
					Toast toast = Toast.makeText(MerchantProfileActivity.this, error.getMessage(),
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 30);
					toast.show();
				}
				
			}
		});
		service.getData(true);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_ib:
			finish();
			break;
		case R.id.right_ib:
			showDrawer();
			break;
		case R.id.updateButton:
			if (checkValidation()) {
				clearFocus();
				updateProfile();
				
			}
			break;

		default:
			break;
		}

	}

}
