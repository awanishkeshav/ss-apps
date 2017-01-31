package com.consumer.adapters;

import java.util.List;

import com.consumer.TransDetailActivity;
import com.consumer.R;
import com.consumer.models.Tag;

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

public class TagAdapter extends BaseAdapter {

	private LayoutInflater minflater;
	private List<Tag> mTagList;

	private boolean editMode;
	private Context mContext;

	public TagAdapter(Context context, boolean isEditMode) {
		mContext = context;
		minflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		editMode = isEditMode;
	}

	@Override
	public int getCount() {
		return null == mTagList || mTagList.size() == 0 ? 1 : mTagList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mTagList == null) {
			return "Getting tags...";
		}

		return mTagList.size() > position ? mTagList.get(position) : "No Tags";
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		Object obj = this.getItem(position);

		return obj instanceof Tag ? 1 : 0;

	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		TextView tv = null;
		ImageView imgView = null;
		
		if (row == null) {
			row = minflater.inflate(R.layout.tag_item_row, parent, false);
		}
		tv = (TextView) row.findViewById(R.id.tagTv);
		imgView = (ImageView) row.findViewById(R.id.checkIv);
		imgView.setVisibility(View.GONE);
		tv.setTextColor(mContext.getResources().getColor(R.color.TagListText));
		Object obj = this.getItem(position);
		if (obj instanceof Tag) {
			Tag tag = (Tag) obj;
			tv.setText(tag.getTagDisplayString());
			imgView.setImageResource(R.drawable.btn_add);
			if(tag.isSelected){
				tv.setTextColor(mContext.getResources().getColor(R.color.textColorBlue));
				imgView.setImageResource(R.drawable.btn_del);
			}
			if (editMode) {
				imgView.setVisibility(View.VISIBLE);
			}

		} else {
			tv.setText(obj.toString());
			
		}

		return row;
	}

	OnClickListener rowClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, TransDetailActivity.class);
			mContext.startActivity(intent);
		}
	};

	public void setList(List<Tag> list) {
		mTagList = list;
		notifyDataSetChanged();
		notifyDataSetInvalidated();
	}
}
