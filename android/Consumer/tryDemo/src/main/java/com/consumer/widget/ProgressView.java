package com.consumer.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class ProgressView extends View {
	private Context context;
	private int max;
	private int progress;
	private String progressText[];

	private Paint activePaint;
	private Paint inactivePaint;
	private Paint textPaint;

	public ProgressView(Context context) {
		super(context);
		init(context);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		progress = 0;
		max = 10;

		activePaint = new Paint();
		activePaint.setColor(Color.rgb(57, 181, 74));
		activePaint.setAntiAlias(true);
		activePaint.setStyle(Paint.Style.FILL);

		inactivePaint = new Paint();
		inactivePaint.setColor(Color.rgb(198, 212, 199));
		inactivePaint.setAntiAlias(true);
		inactivePaint.setStyle(Paint.Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.rgb(57, 181, 74));
		textPaint.setAntiAlias(true);
		textPaint.setStrokeWidth(3);
		textPaint.setStrokeCap(Paint.Cap.ROUND);
		textPaint.setTextSize(18);
		textPaint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.NORMAL));
	}

	public void setColor(int active, int inactive) {
		activePaint.setColor(active);
		inactivePaint.setColor(inactive);
		invalidate();
	}

	public void setMaxProgress(String[] progressText) {
		if (null != progressText) {
			this.progressText = progressText;
			this.max = progressText.length;
			setProgress(0);
		}
	}

	public void setProgress(int progress) {
		if (progress < 0) {
			progress = 0;
		} else if (progress >= max) {
			progress = max - 1;
		}
		this.progress = progress;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawProgressView(canvas);
	}

	private float radius;
	private float distance;
	private float lineWidth;
	private float textStartX;
	private float textSpaceWidth;

	private void drawProgressView(Canvas canvas) {
		radius = getWidth() / 50f;
		lineWidth = radius / 4f;
		distance = (((float) getWidth() * 55f / 100f) - ((float) radius * 2f * ((float) max + 1f))) / (float) max;

		int circleOffset = 0;
		for (int i = 0; i < max; i++) {
			canvas.drawCircle(circleOffset + radius, getHeight() - radius, radius, i <= progress ? activePaint : inactivePaint);
			float thisDist = (i == progress ? ((float) getWidth() * 45f / 100f) : distance);
			canvas.drawRect(circleOffset + 2 * radius, getHeight() - radius - lineWidth / 2, circleOffset + 2 * radius + thisDist,
					getHeight() - radius + lineWidth / 2, i <= progress ? activePaint : inactivePaint);
			circleOffset += radius * 2 + thisDist;
			if (i == progress - 1) {
				textStartX = circleOffset;
			} else if (i == progress) {
				textSpaceWidth = circleOffset - textStartX;
			}

		}
		if (progress < max - 1) {
			canvas.drawCircle(circleOffset + radius, getHeight() - radius, radius, inactivePaint);
		} else {
			canvas.drawRect(circleOffset, getHeight() - radius - lineWidth / 2, circleOffset + 2 * radius, getHeight() - radius + lineWidth
					/ 2, activePaint);
		}

		textStartX += radius * 2 + lineWidth;
		drawText(canvas, textStartX, getHeight() - radius - 3 * lineWidth);
	}

	private void drawText(Canvas canvas, float startX, float endY) {
		String[] words = progressText[progress].split("\\ ");
		if (null != words) {
			String currLine = "";
			int line = 0;
			for (int i = 0; i < words.length; i++) {
				if (textPaint.measureText(currLine + words[i] + " ") > textSpaceWidth) {
					line++;
					currLine = "";
					i--;
				} else {
					currLine += words[i] + " ";
					words[line] = currLine;
				}
				words[i] = null;
			}

			for (int i = words.length - 1; i >= 0; i--) {
				if (null != words[i]) {
					canvas.drawText(words[i], startX, endY, textPaint);
					endY -= textPaint.getTextSize();
				}
			}
		}
	}
}
