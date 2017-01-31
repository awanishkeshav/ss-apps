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

public class AnalysisSummaryFragment extends Fragment {

	private ListView mListView;
	private TimeListAdapter mListAdapter;
	private ArrayList<AnalysisVO> mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.analysis_summary_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initialization(view);
		
		LayoutInflater layoutInflater = (LayoutInflater) getActivity()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mListAdapter = new TimeListAdapter(getActivity());
		mList = BackendConstants.getDummyAnalysisList();
		mListView.addHeaderView(layoutInflater.inflate(R.layout.pie_chart, null));
		mListView.setAdapter(mListAdapter);
		mListAdapter.setList(mList);
	}

	private void initialization(View view) {

		mListView = (ListView) view.findViewById(R.id.listView);
	}

	class TimeListAdapter extends BaseAdapter {

		private List<AnalysisVO> list;
		private LayoutInflater minflater;
		private Context mContext;

		public TimeListAdapter(Context context) {
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

			TimeRowHolder viewHolder;
			if (convertView == null) {
				viewHolder = new TimeRowHolder();
				convertView = minflater.inflate(R.layout.analysis_summary_row, null,
						false);
				viewHolder.expenseTv = (TextView) convertView
						.findViewById(R.id.amauntTv);
				viewHolder.expensePercentTv = (TextView) convertView
						.findViewById(R.id.percentTv);
				
				viewHolder.categoryTv = (TextView) convertView
						.findViewById(R.id.nameTv);
				viewHolder.categoryImgView = (ImageView) convertView
						.findViewById(R.id.categoryImgView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (TimeRowHolder) convertView.getTag();
			}

			 viewHolder.categoryImgView.setImageResource(BackendConstants
			.getIconForCategory(item.category));
			 
			 viewHolder.categoryTv.setText(item.category.name);
			 
			 viewHolder.expenseTv.setText(SingletonClass.getPriceString(item.expenseThisMonth));
			 viewHolder.expensePercentTv.setText(item.expensePercentThisMonth+"%");

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

		public class TimeRowHolder {

			public TextView categoryTv, expenseTv, expensePercentTv;
			public ImageView categoryImgView;
		}

	}

}
