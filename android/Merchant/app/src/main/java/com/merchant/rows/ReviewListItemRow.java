package com.merchant.rows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.merchant.R;
import com.merchant.models.Review;

public class ReviewListItemRow implements MyListItem {
	public Review review = null;
	private Context mContext;
/*	private View.OnClickListener reviewButtonClick;
	private View.OnClickListener rowClick;*/
	

	public ReviewListItemRow(Context context, Review r) {
		
		this.review = r;
		this.mContext = context;
	}

/*	public void setReviewButtonClick(View.OnClickListener reviewButtonClick) {
		this.reviewButtonClick = reviewButtonClick;
	}

	public void setRowClick(View.OnClickListener rowClick) {
		this.rowClick = rowClick;
	}
*/
	@Override
	public int getViewType() {
		// TODO Auto-generated method stub
		return ListItemType.ROW.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub

		ReviewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ReviewHolder();
			convertView = inflater.inflate(R.layout.review_row, null, false);

			viewHolder.titleTv 	= (TextView) convertView.findViewById(R.id.titleTV);
			viewHolder.commentTv = (TextView) convertView.findViewById(R.id.commentTv);
			viewHolder.noCommentTv = (TextView) convertView.findViewById(R.id.noCommentTv);
			viewHolder.ratinhgBar 	= (RatingBar) convertView.findViewById(R.id.avgRatingBar);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ReviewHolder) convertView.getTag();
		}

		viewHolder.titleTv.setText(review.getReviewTitle());
		if (review.comment == null || review.comment.isEmpty()) {
			
			viewHolder.noCommentTv.setVisibility(View.VISIBLE);
			viewHolder.commentTv.setVisibility(View.GONE);
		}else{
			
			viewHolder.noCommentTv.setVisibility(View.GONE);
			viewHolder.commentTv.setVisibility(View.VISIBLE);
			viewHolder.commentTv.setText(review.comment);
		}
		
		viewHolder.ratinhgBar.setRating(review.avgRating.floatValue());
		
		return convertView;
	}
	
	public class ReviewHolder{
		
		public TextView titleTv, commentTv, noCommentTv;
		public RatingBar ratinhgBar;
	}
}

