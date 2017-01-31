package com.consumer.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.consumer.ReviewActivity;
import com.consumer.TxnSearchActivity;
import com.consumer.R;
import com.consumer.models.BackendConstants;
import com.consumer.models.Card;
import com.consumer.models.CardSettings;
import com.consumer.models.Error;
import com.consumer.models.Tag;
import com.consumer.models.Transaction;
import com.consumer.models.TxnCategory;
import com.consumer.services.APIService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


public class SingletonClass{

	private static SingletonClass instance = null;
	private Typeface cursiveFont;
	
	private Card mCard;
	private BackendConstants bConstants;

	public CardSettings cardSettings;

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

	public Card getCard() {
		return mCard;
	}

	public void setCard(Card mCard) {
		this.mCard = mCard;
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
	
	public static String getPriceString(Double price){
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
		decimalFormatSymbols.setCurrencySymbol("₹");
		((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
		return nf.format(price);
		
	}
	
	public static void showShareOptionDialog(Context context){
		/*AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
       
        builder.setTitle("Share via");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.select_dialog_item);
        arrayAdapter.add("Facebook");
        arrayAdapter.add("Twitter");
        arrayAdapter.add("Email");
        arrayAdapter.add("Text");
   
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        
                    }
                });
        builder.show();*/
		
		final Dialog dialog = new Dialog(context);
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.trans_tag_dialog);
			
			ImageView closeIv = (ImageView)dialog.findViewById(R.id.closeIv);
			ListView  tagList = (ListView)dialog.findViewById(R.id.tagLv);
			TextView titleTv = (TextView)dialog.findViewById(R.id.dialogTitleTv);
			titleTv.setText("Share using");
			LinearLayout clearFilterll = (LinearLayout) dialog.findViewById(R.id.clearFilterLv);
			clearFilterll.setVisibility(View.GONE);
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                context,R.layout.select_dialog_row);
	        
	        arrayAdapter.add("Email");
	        arrayAdapter.add("Messaging");	
	        arrayAdapter.add("WhatsApp");
	        arrayAdapter.add("Facebook");
			
			tagList.setAdapter(arrayAdapter);
		    
			tagList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					dialog.dismiss();
					
				}
			});
			
			closeIv.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			if(!dialog.isShowing())
				dialog.show();
		
	}
	
	public void loadBackendConstants(Context context, boolean asyncLoad){
		
		
		APIService service = APIService.getAppConstants(context);
		service.setServiceListener(new APIService.APIServiceListener() {
			
			@Override
			public void onCompletion(APIService service, Object data, Error error) {
				if(error ==null){
					if(bConstants == null){
						bConstants = new BackendConstants();
					}
					bConstants.setJson((JSONObject) data);
				}
				
			}
		});
		if(asyncLoad)
			service.getData(false);
		else
			service.getDataAndWaitForCompletion();
		
		APIService catService = APIService.getAllCategories(context);
		catService.setServiceListener(new APIService.APIServiceListener() {
			
			@Override
			public void onCompletion(APIService service, Object data, Error error) {
				if(error == null){
					if(bConstants == null){
						bConstants = new BackendConstants();
					}
					JSONArray jsonArray = (JSONArray) data;
					ArrayList<TxnCategory> cats = new ArrayList<TxnCategory>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.optJSONObject(i);
						cats.add(new TxnCategory(jsonObj));
					}
					bConstants.categories = cats;
				}
				
			}
		});
		if(asyncLoad)
			catService.getData(false);
		else
			catService.getDataAndWaitForCompletion();
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
	
	public static Intent getIntentForAddReviewActivity(Context context, Transaction txn){
		Intent intent = new Intent(context, ReviewActivity.class);
		intent.putExtra(ReviewActivity.Extra_Transaction, txn);
		return intent;
	}
	public static void showTxnSearchActivity(Activity context, String searchParam){
		Intent intent = new Intent(context, TxnSearchActivity.class);
		if (searchParam != null && !searchParam.isEmpty()) {
			intent.putExtra(TxnSearchActivity.ExtraSearchParam, searchParam);
		}
		
		context.startActivity(intent);
	}
	public static float pixelsFromDip(Context context, int dip){
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return px;
	}
}
