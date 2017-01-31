package com.consumer.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.Limit;
import com.consumer.models.Limit.Period;

public class ManageDialog extends Dialog implements OnCheckedChangeListener, android.view.View.OnClickListener{

	Context mContext;
	RadioButton mDaillyRd, mWeekllyRd, mMonthlyRd;
	RadioGroup mTimeRg;
	Button mOkBtn, mCancelBtn;
	TextView mLimitTv;
	SeekBar mLimitSb;
	Spinner mCategorySp, mActionSp;
	ImageView mCloseIv;
	private ArrayAdapter<String> mCategoryAdapter;
	private ArrayAdapter<String> mActionAdapter;
	
	private double mLimitDouble = 0;
	private String mCategoryText;
	private Limit mLimmit;
	
	private AddLimitListner mAddLimitListner;
	
	int selectedId;
	
	public ManageDialog(Context context, AddLimitListner listener, Limit monthlylimit) {
		super(context);
		mContext = context;
		mAddLimitListner = listener;
		mLimmit = new Limit();
		mLimmit  = monthlylimit;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.manage_dialog);
		//setCancelable(false);
		setWidth();
		mCategoryAdapter = new ArrayAdapter<String>(context,R.layout.spinner_item, SingletonClass.sharedInstance().getbConstants().getCategoriesDisplayStrings());
		mCategoryAdapter.setDropDownViewResource(R.layout.spinner_item);
		
		mActionAdapter = new ArrayAdapter<String>(context, R.layout.spinner_item, SingletonClass.sharedInstance().getbConstants().getActionDisplayStrings());
		mActionAdapter.setDropDownViewResource(R.layout.spinner_item);

		mContext = context;
		controlInitialization();
		
		mCategorySp.setAdapter(mCategoryAdapter);
		mActionSp.setAdapter(mActionAdapter);
		if(mLimmit != null){
			mLimitSb.setTag(mLimmit.getLimitPercentValue());
			mLimitSb.setProgress(mLimmit.getLimitPercentValue());
			
			mLimitTv.setText(SingletonClass.stringFromNumber(
					mLimmit.userLimit, 0));
		}
	}

	private void controlInitialization(){
		
		mDaillyRd 	= (RadioButton)findViewById(R.id.daillyRd);
		mWeekllyRd 	= (RadioButton)findViewById(R.id.weeklyRd);
		mMonthlyRd 	= (RadioButton)findViewById(R.id.monthlyRd);
		mTimeRg 	= (RadioGroup) findViewById(R.id.timeRg);
		
		mLimitTv	= (TextView)findViewById(R.id.CenterBottomTv);
		mOkBtn		= (Button) findViewById(R.id.ok_btn);
		mCancelBtn	= (Button) findViewById(R.id.cancel_btn);
		mLimitSb	= (SeekBar) findViewById(R.id.limitSb);
		mCategorySp	= (Spinner) findViewById(R.id.CategorySpin);
		mActionSp	= (Spinner) findViewById(R.id.actionSpin);
		mCloseIv	= (ImageView) findViewById(R.id.closeIv);
		
		mTimeRg.setOnCheckedChangeListener(this);
		mOkBtn.setOnClickListener(this);
		mCancelBtn.setOnClickListener(this);
		mCloseIv.setOnClickListener(this);
		
		//mLimitSb.setMax(((ManageSpendActivity)mContext).mSecureFragment.monthlylimit.getLimitPercentValue());
		
		mLimitSb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {	}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {	}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//mLimitDouble = progress;
				//mLimitTv.setText(String.valueOf(progress));
				if(mLimmit != null){
					mLimmit.setLimitPercentValue(progress);
					mLimitDouble = mLimmit.userLimit;
					mLimitTv.setText(SingletonClass.stringFromNumber(
							mLimmit.userLimit, 0));
				}
			}
		});
	}

	private void setWidth(){
		//Grab the window of the dialog, and change the width
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = this.getWindow();
		lp.copyFrom(window.getAttributes());
		//This makes the dialog take up the full width
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
	}

	/** ....../////.....  LISTENERS....../////....*/
	
	public interface AddLimitListner {

		void onAddLimit(Limit manage);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		
		switch (checkedId) {
			case R.id.daillyRd:
				selectedId = 1;
				//Toast.makeText(mContext, ""+selectedId, Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.weeklyRd:
				selectedId = 2;
				//Toast.makeText(mContext, ""+selectedId, Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.monthlyRd:
				selectedId = 3;
				//Toast.makeText(mContext, ""+selectedId, Toast.LENGTH_SHORT).show();
				break;
		}	
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.ok_btn:
			
			if (mCategorySp.getSelectedItemPosition() == AdapterView.INVALID_POSITION) {
				return;
			}
			
			String categoryKey = (String) mCategorySp.getSelectedItem();
			
			
			Limit limit = Limit.defaultLimit();
			limit.userLimit = mLimitDouble ;
			limit.setCategoryKey(categoryKey);
			limit.setAction( SingletonClass.sharedInstance().getbConstants().approvalTypes.get(mActionSp.getSelectedItemPosition()));;
			
			if (mDaillyRd.isChecked()) {
				limit.period = Period.DAILY;
			}else if (mWeekllyRd.isChecked()) {
				limit.period = Period.WEEKLY;
			}else if (mMonthlyRd.isChecked()) {
				limit.period = Period.MONTHLY;
			}
			
			mAddLimitListner.onAddLimit(limit);
			this.dismiss();
			break;

		case R.id.cancel_btn:
			this.dismiss();
			break;
			
		case R.id.closeIv:
			this.dismiss();
			break;
	
			
		default:
			break;
		}
	}
	
}
