package com.consumer.fragments;

import java.util.ArrayList;
import java.util.List;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.AnalysisVO;
import com.consumer.models.BackendConstants;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CompareFragment extends Fragment {

	private ListView mListView;
	private CompareListAdapter mListAdapter;
	private ArrayList<AnalysisVO> mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.compare_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initialization(view);

		mListAdapter = new CompareListAdapter(getActivity());
		mList = BackendConstants.getDummyAnalysisList();
		mListView.setAdapter(mListAdapter);
		mListAdapter.setList(mList);
	}

	private void initialization(View view) {

		mListView = (ListView) view.findViewById(R.id.listView);
	}

	class CompareListAdapter extends BaseAdapter {

		private List<AnalysisVO> list;
		private LayoutInflater minflater;
		private Context mContext;

		public CompareListAdapter(Context context) {
			mContext = context;
			minflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return null == list ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
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

			AnalysisVO item = (AnalysisVO) getItem(position);

			CompareRowHolder viewHolder;
			if (convertView == null) {
				viewHolder = new CompareRowHolder();
				convertView = minflater.inflate(R.layout.compare_row, null,
						false);
				viewHolder.thisMonthTv = (TextView) convertView
						.findViewById(R.id.thisMonthTv);
				viewHolder.lastMonthTv = (TextView) convertView
						.findViewById(R.id.lastMonthTv);
				viewHolder.thisMonthBar = (ProgressBar) convertView
						.findViewById(R.id.thisMonthPB);
				viewHolder.lastMonthBar = (ProgressBar) convertView
						.findViewById(R.id.lastMonthPB);
				viewHolder.amountTv = (TextView) convertView
						.findViewById(R.id.amountTv);
				viewHolder.categoryTv = (TextView) convertView
						.findViewById(R.id.categoryTv);
				viewHolder.categoryImgView = (ImageView) convertView
						.findViewById(R.id.categoryImgView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (CompareRowHolder) convertView.getTag();
			}

			 viewHolder.categoryImgView.setImageResource(BackendConstants
			.getIconForCategory(item.category));
			 
			 viewHolder.categoryTv.setText(item.category.name);
			 
			 viewHolder.amountTv.setText(SingletonClass.getPriceString(item.expenseThisMonth));
			 viewHolder.thisMonthTv.setText("This Month ("+ item.expensePercentThisMonth+"%)");
			 viewHolder.lastMonthTv.setText("Last Month ("+ item.expensePercentLastMonth+"%)");
			 
			 viewHolder.thisMonthBar.setProgress(item.expensePercentThisMonth*2);
			 viewHolder.lastMonthBar.setProgress(item.expensePercentLastMonth*2);

			return convertView;
		}

		@Override
		public boolean isEnabled(int position) {
			Boolean enabled = super.isEnabled(position);
			return enabled;
		}

		public void setList(List<AnalysisVO> paramList) {
			list = paramList;
			notifyDataSetChanged();
		}

		public class CompareRowHolder {

			public TextView categoryTv, thisMonthTv, lastMonthTv, amountTv;
			public ImageView categoryImgView;
			public ProgressBar thisMonthBar, lastMonthBar;
		}

	}

}
