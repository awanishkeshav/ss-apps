package com.merchant.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.merchant.models.BackendConstants;
import com.merchant.models.Card;
import com.merchant.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class SingletonClass{

	private static SingletonClass instance = null;
	private Typeface cursiveFont;
	private BackendConstants bConstants;

	public BackendConstants getbConstants() {
		if(bConstants == null)
			bConstants = new BackendConstants();
		return bConstants;
	}

	protected SingletonClass() {
		// Exists only to defeat instantiation.
	}

	public static SingletonClass sharedInstance() {
		if (instance == null) {
			instance = new SingletonClass();
		}
		return instance;
	}
	
	public static boolean isMerchantRegistered(Context context){
		boolean isRegistered = false;
		String merchantUUID = new PreferanceUtil(context).getMerchantUUID();
		if (merchantUUID != null && !merchantUUID.isEmpty()) {
			isRegistered = true;
		}
		return isRegistered;
	}

	public Card getCard() {
		return null;
	}

	public void setCard(Card mCard) {
		
	}

	public static String getStringFromStringArray(ArrayList<String> strArr,
			String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String n : strArr) {
			if (sb.length() > 0 && delimiter != null) {
				sb.append(delimiter);
			}
			sb.append(n);
		}
		return sb.toString();
	}

	public static Date getDateFromString(String str) {
		Date date = null;
		if (str != null) {
			try {
				date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(str);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return date;

	}

	public static Bitmap decodeSampledBitmapFromByteArray(byte[] bytes,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		// BitmapFactory.Options optionss = new BitmapFactory.Options();
		// optionss.inPreferredConfig = Bitmap.Config.RGB_565;

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static boolean isNetworkAvailable(Context context) {
		
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null
				&& activeNetwork.isConnectedOrConnecting();
		return isConnected;
	}

	public static boolean isSdPresent() {

		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

	
	// -- Fonts -- //
    public void setTypeface(TextView textView, Context context) {
        if(textView != null) {
        	cursiveFont = Typeface.createFromAsset(context.getAssets(),"Eutemia.ttf");
            textView.setTypeface(cursiveFont);
        }
    }
   
	
	/*public static void dismissKeyBoard(View currentFocus, Context context) {
		if (currentFocus != null && context != null) {
			InputMethodManager in = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			in.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}

	}*/

	
	/** .... Hide opened soft keyboard...*/
	public static void hideSoftKeyboard(View view) {
		if (null != view) {
			InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**.... Show opened soft keyboard.. */
	public static void showSoftKeyboard(View view) {
		if (null != view) {
			InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputMethodManager.showSoftInput(view, 0);
		}
	}
	
	public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
	
	public static String getPriceStringWithoutCurrencySymbol(Double price){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("");
		((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
		return nf.format(price);
		
	}
	
	public static String getPriceString(Object price){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("$");
		((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
		return nf.format(price);
		
	}
	
	public static String stringFromNumber(Double num, int decimalDigitCount){
		DecimalFormat formatter = new DecimalFormat("#,###,###");
		return formatter.format(num);
	}
	public static String stringFromNumber(int num, int decimalDigitCount){
		DecimalFormat formatter = new DecimalFormat("#,###,###");
		return formatter.format(num);
	}
	public static String stringFromNumber(long num, int decimalDigitCount){
		DecimalFormat formatter = new DecimalFormat("#,###,###");
		return formatter.format(num);
	}
	
//	public static Intent getIntentForAddReviewActivity(Context context, Transaction txn){
//		Intent intent = new Intent(context, ReviewActivity.class);
//		//intent.putExtra(ReviewActivity.Extra_Transaction, txn);
//		return intent;
//	}
	
	public static float pixelsFromDip(Context context, int dip){
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return px;
	}
	
	public static void showItemPicker(Context context, String title, ArrayList<String> items, DialogInterface.OnClickListener listener){
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(
				context);
		if (title != null) {
	        builderSingle.setTitle(title);
		}
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.picker_list_item, items);
        
        builderSingle.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(arrayAdapter,listener);
        builderSingle.show();
	}
}
