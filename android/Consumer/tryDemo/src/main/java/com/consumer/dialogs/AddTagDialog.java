package com.consumer.dialogs;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.consumer.R;
import com.consumer.adapters.TagAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.models.Error;
import com.consumer.models.Tag;
import com.consumer.models.Transaction;
import com.consumer.services.APIService;
import com.consumer.utility.DialogUtil;

public class AddTagDialog extends Dialog implements android.view.View.OnClickListener{

	private AutoCompleteTextView mAutoCompleteTv;
	private Button mAddBtn, mCancelBtn ;
	private Context mContext;
	private Transaction transaction;
	private TagAdapter mTagLvAdapter;
	private ArrayList<Tag> mTagList;
	
	public AddTagDialog(Context context) {
		super(context);
		
	}
	public AddTagDialog(Context context, Transaction txn) {
		super(context);
		transaction = txn;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_tag_dialog);
		getWindow().setSoftInputMode(
			    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mTagLvAdapter = new TagAdapter(context,true);
		
		mContext = context;
		controlInitialization();
		getAllTagsService();
	}

	
	private void controlInitialization(){
		
		mAutoCompleteTv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
		mAddBtn= (Button)findViewById(R.id.add_tag_button);
		mAddBtn.setOnClickListener(this);
		mCancelBtn= (Button)findViewById(R.id.cancel_button);
		mCancelBtn.setOnClickListener(this);
		ImageView closeIv = (ImageView)findViewById(R.id.closeIv);
		closeIv.setOnClickListener(this);
		ListView  tagList = (ListView)findViewById(R.id.tagLv);
		tagList.setAdapter(mTagLvAdapter);
		
		//SingletonClass.showSoftKeyboard(mAutoCompleteTv);
		
		
		tagList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Object obj = mTagLvAdapter.getItem(position);
				if(obj instanceof Tag){
					Tag tag = (Tag) obj;
					tag.isSelected = !tag.isSelected;
					mTagLvAdapter.notifyDataSetChanged();
				}
				
			}
		});
		

	}
	
	private boolean checkValidation(){
		return true;
	}
	
	private void clearFocus(){
		mAutoCompleteTv.clearFocus();
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_tag_button:
			//Toast.makeText(mContext, "Register Clicked", Toast.LENGTH_SHORT).show();
			if(checkValidation()){
				clearFocus();
				setTags();
			}
			
			break;
			
		case R.id.closeIv:
			clearFocus();
			dismiss();
			break;
		case R.id.cancel_button:
			if (this.mTagList != null) {
				for (Tag tag : mTagList) {
					tag.isSelected = false;
				}
			}
			mTagLvAdapter.notifyDataSetChanged();
			mAutoCompleteTv.setText("");
			
			break;
		}
		
	}

	
	/** ...///.... WEB SERVICE....///...*/
	
	private void getAllTagsService() {
		Long cardId = new PreferanceUtil(mContext).getCardId();
		APIService service = APIService.getAllTags(mContext, cardId);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					if (data instanceof JSONArray) {
						JSONArray jsonArray = (JSONArray) data;
						mTagList = new ArrayList<Tag>();
						
						for (int i = 0; i < jsonArray.length(); i++) {
							try {
								Tag tag = new Tag(jsonArray.getJSONObject(i));
								mTagList.add(tag);
								for (Tag selectedTag : transaction.tags) {
									if (tag.id.equals(selectedTag.id)) {
										tag.isSelected = true;
									}
								}
							} catch (JSONException e) {

								e.printStackTrace();
							}
						}
						mTagLvAdapter.setList(mTagList);
						
						
						//mTagListView.setAdapter(itemsAdapter);
					}
				}
			}
		});
		service.getData(false);
	}
	
	private void setTag(String tag){
		if(tag == null || tag.isEmpty()){
			DialogUtil.showOkDialog(mContext, "Tag can not be empty");
		}else{
			
			APIService service = APIService.setTxnTag(mContext, transaction.id, tag);
			service.setServiceListener(new APIService.APIServiceListener() {
				
				@Override
				public void onCompletion(APIService service, Object data, Error error) {
					if(error == null){
						dismiss();
					}
					
				}
			});
			service.getData(true);
		}
	}
	
	private void setTags(){
		String newTag = mAutoCompleteTv.getText().toString().trim();
		if((this.mTagList == null || this.mTagList.isEmpty()) && newTag.isEmpty()){
			DialogUtil.showOkDialog(mContext, "Tag can not be empty");
		}else{
			
			ArrayList<String> jsonArr = new ArrayList<String>();
			
			for (Tag tag : mTagList) {
				if (tag.isSelected) {
					jsonArr.add(tag.id.toString());
				}
			}
			
			APIService service = APIService.setTxnTags(mContext, transaction.id, SingletonClass.getStringFromStringArray(jsonArr, ","),newTag);
			service.setServiceListener(new APIService.APIServiceListener() {
				
				@Override
				public void onCompletion(APIService service, Object data, Error error) {
					if(error == null){
						dismiss();
					}
					
				}
			});
			service.getData(true);
		}
	}
}
