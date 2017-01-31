package com.consumer.fragments;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.consumer.TransDetailActivity;
import com.consumer.R;
import com.consumer.adapters.TagAdapter;
import com.consumer.adapters.TransactionAdapter;
import com.consumer.common.PreferanceUtil;
import com.consumer.common.SingletonClass;
import com.consumer.handler.FooterTabHandler;
import com.consumer.handler.FooterTabHandler.FooterTabListener;
import com.consumer.models.Error;
import com.consumer.models.Tag;
import com.consumer.models.Transaction;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItemHeader;
import com.consumer.rows.TranxListItemRow;
import com.consumer.services.APIService;
import com.consumer.utility.DateUtil;

public class TranxListFragment extends Fragment implements FooterTabListener{

	private ListView mListView;
	//private ListView mTagListView;

	private View filterView;
	private View noDataView;
	private LinearLayout mLoadingLayout;
	
	private TagAdapter mTagLvAdapter;
	private ArrayList<Transaction> mTransactionList;
	private ArrayList<Tag> mTagList;
	private TransactionAdapter mTransactionAdapter;
	private APIService mTransactionService;
	private FragmentActivity mFragmentActivity;
	private FooterTabHandler mFooterTabHandler;
	
	private String searchText;

	private View popupView;
	//public PopupWindow popupWindow;
	private TagDialog mTagDialog;
	//View dialogview;

	Activity activity;
	
	/*public static TranxListFragment newInstance(FragmentActivity activity, String div) {
		TranxListFragment planFragment = new TranxListFragment();
		planFragment.mFragmentActivity = activity;

		Bundle bundle = new Bundle();
		bundle.putString("div", div);
		planFragment.setArguments(bundle);

		return planFragment;
	}
*/
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.tranx_list_fragment,container, false);

		return rootView;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initialization(view);
		getAllTagsService();
		getTransactionsService();

		mFooterTabHandler = new FooterTabHandler(getActivity());
		mFooterTabHandler.handleTabs(-1, this);
		mFooterTabHandler.setTabs(R.drawable.icon_tags, R.drawable.icon_share, FooterTabHandler.RES_NONE, FooterTabHandler.RES_NONE);

		
	}

	private void initialization(View view) {
		
		activity = getActivity();
		mListView = (ListView) view.findViewById(R.id.transactionLv);
		mListView.bringToFront();

		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);
		filterView 	= inflater.inflate(R.layout.transactions_section_header,null, false);
		noDataView 	= inflater.inflate(R.layout.transactions_section_header,null, false);
		popupView 	= inflater.inflate(R.layout.trans_tag_popup, null);  
		
	//	 popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT);
		// objects..
		mTransactionAdapter = new TransactionAdapter(getActivity());
		mListView.setAdapter(mTransactionAdapter);
		mTagLvAdapter = new TagAdapter(getActivity(),false);

		mTagDialog = new TagDialog(activity);
		/*mTagListView = (ListView) view.findViewById(R.id.tagLv);
		mTagListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (mTagList.size() > position) {
							Tag tag = mTagList.get(position);
							if (getSelectedTag() != null
									&& getSelectedTag() == tag) {
								setSelectedTag(null);
							} else {
								setSelectedTag(tag);
							}
						}
						setTagListShow(false);

					}
				});*/
	}
	

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
		getTransactionsService();
	}

	private List<Tag> getSelectedTags(){
		List<Tag> list = new ArrayList<Tag>();
		if(mTagList != null){
			for (Tag tag : mTagList) {
				if(tag.isSelected) list.add(tag);
			}
		}
		return list;
	}
	
	private String getSelectedTagString(){
		ArrayList<String> list = new ArrayList<String>();
		if(mTagList != null){
			for (Tag tag : mTagList) {
				if(tag.isSelected) list.add(tag.getTagDisplayString());
			}
		}
		return SingletonClass.getStringFromStringArray(list, ", ");
	}
	
	private String getSelectedTagIds(){
		ArrayList<String> list = new ArrayList<String>();
		if(mTagList != null){
			for (Tag tag : mTagList) {
				if(tag.isSelected) list.add(tag.id.toString());
			}
		}
		return SingletonClass.getStringFromStringArray(list, ", ");
	}

	private void reloadListHeaderView() {
		  this.mListView.removeHeaderView(filterView);
		  if (this.getSelectedTags().size() != 0) {
		   // Prevent crash for earlier version then KITKAT
		   this.mListView.setAdapter(null);
		   this.mListView.addHeaderView(filterView);
		   this.mListView.setAdapter(mTransactionAdapter);
		   TextView headerTitleTextView = (TextView) filterView
		     .findViewById(R.id.headerTitleTv);
		   headerTitleTextView.setText(this.getSelectedTagString());
		  }
		  this.mListView.removeFooterView(noDataView);
		  if (this.mTransactionList != null && this.mTransactionList.size() == 0) {
		   // Prevent crash for earlier version then KITKAT
		   this.mListView.setAdapter(null);
		   this.mListView.addFooterView(noDataView);
		   this.mListView.setAdapter(mTransactionAdapter);
		   TextView headerTitleTextView = (TextView) noDataView
		     .findViewById(R.id.headerTitleTv);
		   headerTitleTextView.setText("No transactions found");
		  }
		  
		  this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mTransactionAdapter != null){
					Object item = mTransactionAdapter.getItem((int) id);
					if (item instanceof TranxListItemRow) {
						Transaction txn = ((TranxListItemRow)item).tranx;
						Intent intent = new Intent(getActivity() , TransDetailActivity.class);
						intent.putExtra(TransDetailActivity.ExtraTransactionVO, txn);
						getActivity().startActivity(intent);
					}
					
				}
				
			}
		});
		  
		}

	private void prepareListItem() {
		ArrayList<TranxListItem> source = new ArrayList<TranxListItem>();
		String[] orderedKey = { DateUtil.KEY_TODAY, DateUtil.KEY_YESTERDAY,
				DateUtil.KEY_TWO_DAYS_AGO, DateUtil.KEY_LAST_WEEK,
				DateUtil.KEY_LAST_MONTH, DateUtil.KEY_OLDER };

		HashMap<String, ArrayList<Transaction>> map = DateUtil
				.groupDates(mTransactionList);

		int sectionIndex = 0;
		for (int i = 0; i < orderedKey.length; i++) {
			String key = orderedKey[i];
			ArrayList<Transaction> transactions = map.get(key);
			if (transactions.size() > 0) {
				double total = 0;
				for (Transaction transaction : transactions) {
					total += transaction.amount;
				}
				String headerTitle = key + " ("
						+ NumberFormat.getCurrencyInstance().format((total))
						+ ")";
				if(sectionIndex == 0){
					source.add(new TranxListItemHeader(headerTitle, false));
				}else{
					source.add(new TranxListItemHeader(headerTitle, true));
				}
				
				for (Transaction transaction : transactions) {
					source.add(new TranxListItemRow(this.getActivity(),
							transaction));
				}
				sectionIndex++;
			}
		}
		mTransactionAdapter.setList(source);
	}

	public void setTagListShow(boolean show) {

		//mListView.bringToFront();
//		showPopUp();
	//	showDialog();
		if(mTagDialog == null)
			mTagDialog = new TagDialog(getActivity());
		
		if(!mTagDialog.isShowing()){
			mTagDialog.show();
		}else
			mTagDialog.dismiss();
		
	}

	/*private void showPopUp(){
		
		  if(!popupView.isShown() ){
			  //((TransSearchActivity)getActivity()).mSearchEt.setFocusable(false);
			     
			             
			      mTagListView = (ListView)popupView.findViewById(R.id.tagLv);
			      if(mTagList!= null)
			    	  mTagListView.setAdapter(mTagLvAdapter);
			      
			      //popupWindow.showAsDropDown(mFooterTabHandler.mRl1, 05, 02);
			      popupWindow.showAtLocation(((ManageSpendActivity)activity).mFooterTabHandler.mRl1,  Gravity.BOTTOM, 05, ((ManageSpendActivity)activity).mFooterTabHandler.mRl1.getHeight());
		          
			      mTagListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						Object obj = mTagLvAdapter.getItem(position);
						if(obj instanceof Tag){
							Tag tag = (Tag) obj;
							tag.isSelected = !tag.isSelected;
							getTransactionsService();
						}
						popupWindow.dismiss();
					}
				});			      
		}else
			popupWindow.dismiss();
	}*/


	/*void showDialog(){
		
		if(!mTagDialog.isShowing()){
			mTagDialog.show();
		}else
			mTagDialog.dismiss();
	}*/
	
	
	/** ...//// Listeners..///..*/
	
	@Override
	public void onTabSelected(View v) {
		switch (v.getId()) {
		case R.id.tab_1_tv:
			//Toast.makeText(activity, "Tab 1", Toast.LENGTH_SHORT).show();
			setTagListShow(true);
			break;
		case R.id.tab_2_tv:
			//Toast.makeText(activity, "Tab 2", Toast.LENGTH_SHORT).show();
			SingletonClass.showShareOptionDialog(activity);
			break;
		case R.id.tab_3_tv:
			Toast.makeText(activity, "Tab 3", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tab_4_tv:
			Toast.makeText(activity, "Tab 4", Toast.LENGTH_SHORT).show();
			break;
		
		default:
			break;
		}
	}
	
	
	/** ...//,,, WEB SERVICE ,,,,..///..*/
	
	// using "Service" behind the name of method which connect to server.
	private void getAllTagsService() {
		Long cardId = new PreferanceUtil(getActivity()).getCardId();
		APIService service = APIService.getAllTags(this.getActivity(), cardId);
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

	private void getTransactionsService() {

		if (mTransactionService != null) {
			mTransactionService.cancelRequest();
		}

		Long cardId = new PreferanceUtil(this.getActivity()).getCardId();
		String tagId = this.getSelectedTagIds();
		mLoadingLayout.setVisibility(View.VISIBLE);
		mTransactionService = APIService.getAllTransactions(this.getActivity(),
				cardId, tagId, this.searchText);
		mTransactionService.setServiceListener(new APIService.APIServiceListener() {

					@Override
					public void onCompletion(APIService service, Object data,
							Error error) {
						mLoadingLayout.setVisibility(View.GONE);
						if (error == null) {
							if (data instanceof JSONArray) {
								mTransactionList = new ArrayList<Transaction>();
								JSONArray jsonArray = (JSONArray) data;
								for (int i = 0; i < jsonArray.length(); i++) {
									try {
										JSONObject json = jsonArray
												.getJSONObject(i);
										mTransactionList.add(new Transaction(
												json));
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								prepareListItem();
								reloadListHeaderView();

							}
						}

					}
				});
		mTransactionService.getData(false);
	}

	private boolean isAnyTagSelected(){
		if (mTagList != null) {
			for (Tag tag : mTagList) {
				if(tag.isSelected){
					return true;
				}
			}
		}
		return false;
	}
	
	
	ListView  tagList;
	
	class TagDialog extends Dialog{

		public TagDialog(Context tranxListFragment) {
			super(tranxListFragment);
			
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.trans_tag_dialog);
			
			ImageView closeIv = (ImageView)findViewById(R.id.closeIv);
			tagList = (ListView)findViewById(R.id.tagLv);
			LinearLayout clearFilterll = (LinearLayout) findViewById(R.id.clearFilterLv);
			tagList.setAdapter(mTagLvAdapter);
		      
			if (isAnyTagSelected()) {
				clearFilterll.setVisibility(View.VISIBLE);
			}else{
				clearFilterll.setVisibility(View.GONE);
			}
		      //popupWindow.showAsDropDown(mFooterTabHandler.mRl1, 05, 02);
		      //popupWindow.showAtLocation(((ManageSpendActivity)activity).mFooterTabHandler.mRl1,  Gravity.BOTTOM, 05, ((ManageSpendActivity)activity).mFooterTabHandler.mRl1.getHeight());
	          
			tagList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					Object obj = mTagLvAdapter.getItem(position);
					if(obj instanceof Tag){
						Tag tag = (Tag) obj;
						tag.isSelected = !tag.isSelected;
						getTransactionsService();
					}
					dismiss();
					//popupWindow.dismiss();
				}
			});
			
			clearFilterll.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					for (Tag tag : mTagList) {
						tag.isSelected = false;
						getTransactionsService();
						dismiss();
					}
					
				}
			});
			
			
			closeIv.setOnClickListener(new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
					
				}
			});
			
		}
		
		@Override
		public void onAttachedToWindow() {
			super.onAttachedToWindow();
			LinearLayout clearFilterll = (LinearLayout) findViewById(R.id.clearFilterLv);
			if (isAnyTagSelected()) {
				clearFilterll.setVisibility(View.VISIBLE);
			}else{
				clearFilterll.setVisibility(View.GONE);
			}
		}
		
	}

	
}
