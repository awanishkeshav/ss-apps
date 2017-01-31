package com.consumer.fragments;

import java.util.ArrayList;
import java.util.List;

import com.consumer.R;
import com.consumer.common.SingletonClass;
import com.consumer.models.AnalysisVO;
import com.consumer.models.BackendConstants;
import com.consumer.models.TxnCategory;
import com.consumer.models.TxnCategory.Type;

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

public class TrendsFragment extends Fragment {

	private ListView mListView;
	private TrendsListAdapter mListAdapter;
	private ArrayList<AnalysisVO> mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.trends_fragment, container,
				false);
		return rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initialization(view);

		mListAdapter = new TrendsListAdapter(getActivity());
		mList = BackendConstants.getDummyAnalysisList();
		
		TxnCategory cat = new TxnCategory("Overall", Type.UNKNOWN);
		AnalysisVO vo = new AnalysisVO(cat, 718.93, 38, 36);
		mList.add(0, vo);
		
		mListView.setAdapter(mListAdapter);
		mListAdapter.setList(mList);
	}

	private void initialization(View view) {

		mListView = (ListView) view.findViewById(R.id.listView);
	}

	class TrendsListAdapter extends BaseAdapter {

		private List<AnalysisVO> list;
		private LayoutInflater minflater;
		private Context mContext;

		public TrendsListAdapter(Context context) {
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

			TrendsRowHolder viewHolder;
			if (convertView == null) {
				viewHolder = new TrendsRowHolder();
				convertView = minflater.inflate(R.layout.trends_row, null,
						false);

				viewHolder.titleTv = (TextView) convertView
						.findViewById(R.id.titleTv);
				viewHolder.chartImgView = (ImageView) convertView
						.findViewById(R.id.imageView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (TrendsRowHolder) convertView.getTag();
			}

			int chart = 0;
			if (position %3 == 0) {
				chart = R.drawable.blue_graph_w_rupee;
			} else if (position %3 == 1) {
				chart = R.drawable.yellow_graph_w_rupee;
			} else if (position %3 == 2) {
				chart = R.drawable.red_graph_w_rupee;
			}

			viewHolder.chartImgView.setImageResource(chart);

			viewHolder.titleTv.setText(item.category.name);

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

		public class TrendsRowHolder {

			public TextView titleTv;
			public ImageView chartImgView;
		}

	}

}
