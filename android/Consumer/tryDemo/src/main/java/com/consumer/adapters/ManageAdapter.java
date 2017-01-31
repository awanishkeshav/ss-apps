package com.consumer.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import com.consumer.R;
import com.consumer.models.Limit;

public class ManageAdapter extends BaseAdapter{

	List<Limit> manageList;
	private LayoutInflater minflater;
	private Context mContext;
	
	public ManageAdapter(Context context) {
		mContext = context;
		minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return null == manageList ? 0 : manageList.size();
	}

	@Override
	public Object getItem(int position) {
		return manageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ManageViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ManageViewHolder();
			convertView = minflater.inflate(R.layout.security_row, null, false);
			viewHolder.limitSb 	  = (SeekBar) convertView.findViewById(R.id.limitSb);
			viewHolder.categorySp = (Spinner) convertView.findViewById(R.id.CategorySp);
			viewHolder.topLl	  = (LinearLayout) convertView.findViewById(R.id.topLl);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ManageViewHolder) convertView.getTag();
		}
		
		viewHolder.topLl.setBackgroundColor(mContext.getResources().getColor(R.color.White));
		/*viewHolder.limitSb.setMax(15);
		ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
        thumb.setIntrinsicHeight(80);
        thumb.setIntrinsicWidth(30);
        viewHolder.limitSb.setThumb(thumb);*/
        
		viewHolder.position = position;
		return convertView;
	}

	public void setList(List<Limit> manageList){
		this.manageList = manageList;
		notifyDataSetChanged();
	}
	
	class ManageViewHolder{
		LinearLayout topLl;
		Spinner categorySp;
		SeekBar limitSb;
		
		int position;
	}
	
}
