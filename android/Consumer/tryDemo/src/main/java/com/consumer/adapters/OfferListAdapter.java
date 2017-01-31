package com.consumer.adapters;

import java.util.List;

import com.consumer.rows.OfferListItemRow;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItem.ListItemType;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class OfferListAdapter extends BaseAdapter{

	private LayoutInflater minflater;
	private List<OfferListItemRow> offerList;
	
	private Context mContext;
	
	public OfferListAdapter(Context context) {
		mContext = context;
		minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return null == offerList ? 0 : offerList.size();
	}

	@Override
	public Object getItem(int position) {
		return offerList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		TranxListItem item = (TranxListItem) getItem(position);		
		return item.getViewType();

	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = null;
		
		TranxListItem item = (TranxListItem) getItem(position);
		row = item.getView(minflater, convertView, parent);

		return row;
	}
	
	
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		Boolean enabled = super.isEnabled(position);
		if (getItemViewType(position) == ListItemType.Header.ordinal()) {
			enabled = false;
		}
		return enabled;
	}
	
/*	OnClickListener rowClick = new OnClickListener() {

		
		@Override
		public void onClick(View v) {
			TranxListItemRow txnRowItem = (TranxListItemRow) getItem(v.getId());
			Intent intent = new Intent(mContext , TransDetailActivity.class);
			intent.putExtra(TransDetailActivity.ExtraTransactionVO, txnRowItem.tranx);
			mContext.startActivity(intent);
		}
	};*/
		
	
	public void setList(List<OfferListItemRow> list){	
		offerList = list;
		notifyDataSetChanged();
	}
	
}

