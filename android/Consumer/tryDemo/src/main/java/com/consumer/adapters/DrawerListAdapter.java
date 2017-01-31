package com.consumer.adapters;

import java.util.ArrayList;
import java.util.List;

import com.consumer.TransDetailActivity;
import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.Transaction;
import com.consumer.rows.TranxListItem;
import com.consumer.rows.TranxListItemRow;
import com.consumer.rows.TranxListItem.ListItemType;
import com.consumer.rows.TranxListItemRow.TransactionHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DrawerListAdapter extends BaseAdapter{
	
	public static int IndexHome = -1;
	public static int IndexManage = 0;
	public static int IndexTransaction = 1;
	public static int IndexAnalysis = 2;
	public static int IndexSecure = 3;
	public static int IndexSpend = 4;
	public static int IndexOffers = 5;
	public static int IndexNearMe = 6;
	public static int IndexShop = 7;
	public static int IndexMyPurchases = 8;
	public static int IndexAlertHistory = 9;
	public static int IndexSearch = 10;

	private LayoutInflater minflater;
	private ArrayList<String> menuList;
	
	private int selectedIndex;
	
	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	private Context mContext;
	
	public DrawerListAdapter(Context context) {
		mContext = context;
		minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menuList = new ArrayList<String>();
		
		menuList.add("MANAGE");
		menuList.add("Transactions");
		menuList.add("Analysis");
		menuList.add("Secure");
		
		menuList.add("SAVE");
		menuList.add("My Offers");
		menuList.add("Near Me");
		menuList.add("Shop");
		
		menuList.add("My Purchases");
		menuList.add("Alerts History");
		menuList.add("Search");
	}

	
	@Override
	public int getCount() {
		return null == menuList ? 0 : menuList.size();
	}

	@Override
	public Object getItem(int position) {
		return menuList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public int getItemViewType(int position) {
		return 0;

	}

	@Override
	public int getViewTypeCount() {

		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		
		if (convertView == null) {
			
			row = minflater.inflate(R.layout.menu_row, null, false);
		} 
		
		TextView titleTv 	= (TextView) row.findViewById(R.id.titleTv);
		View emptyView = (View) row.findViewById(R.id.emptyView);
		View line = (View) row.findViewById(R.id.lineSeperator);
		View bottomPading = (View) row.findViewById(R.id.bottomPadView);
		View topPading = (View) row.findViewById(R.id.topPadView);
		
		
		line.setVisibility(View.GONE);
		topPading.setVisibility(View.GONE);
		emptyView.setVisibility(View.VISIBLE);
		bottomPading.setVisibility(View.VISIBLE);
		titleTv.setTextColor(Color.parseColor("#333333"));
		
		if (position == IndexSpend || position == IndexManage) {
			emptyView.setVisibility(View.GONE);
			bottomPading.setVisibility(View.GONE);
			titleTv.setTextColor(Color.parseColor("#257FCF"));
		}
		if (position == IndexSecure || position == IndexShop){
			line.setVisibility(View.VISIBLE);
		}
		
		if (position == IndexMyPurchases) {
			topPading.setVisibility(View.VISIBLE);
		}
		titleTv.setText(menuList.get(position));
		
		
		if (position == this.selectedIndex) {
			//titleTv.setTextColor(Color.parseColor("#257FCF"));
		}else{
			//
		}
		return row;
	}
	
	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}
	
}

