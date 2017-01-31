package com.consumer.utility;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloader extends AsyncTask<String,Integer,Bitmap> {

	
	public String url;
	
	public interface ImageDownloaderListener {
		public abstract void onReceive(Bitmap bitmap, String url);
	}
	
	ImageDownloaderListener mDownloadListener;
	
	public void setmDownloadListener(ImageDownloaderListener mDownloadListener) {
		this.mDownloadListener = mDownloadListener;
	}
	
	public ImageDownloader(String url){
		this.url = url;
	}
	
	public static ImageDownloader download(String url){
		ImageDownloader imageDownloader = new ImageDownloader(url);
		imageDownloader.execute(url);	
		return imageDownloader;
	}

	@Override
	protected Bitmap doInBackground(String... param) {
		// TODO Auto-generated method stub
		return downloadBitmap(param[0]);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (this.mDownloadListener != null) {
			this.mDownloadListener.onReceive(result, url);
		}
	}

	private Bitmap downloadBitmap(String url) {
		// initilize the default HTTP client object
		final DefaultHttpClient client = new DefaultHttpClient();

		//forming a HttoGet request 
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			//check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode + 
						" while retrieving bitmap from " + url);
				return null;

			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream 
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that android understands
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					//final Bitmap bitmap = decodeInputStream(inputStream);

					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// You Could provide a more explicit error message for IOException
			getRequest.abort();
			Log.e("ImageDownloader", "Something went wrong while" +
					" retrieving bitmap from " + url + e.toString());
		} 

		return null;
	}
	
	private Bitmap decodeInputStream(InputStream inputStream){
		
		Bitmap scaledBitmap = null;

		BitmapFactory.Options options = new BitmapFactory.Options();

		// by setting this field as true, the actual bitmap pixels are not
		// loaded in the memory. Just the bounds are loaded. If
		// you try the use the bitmap here, you will get null.
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, options);

		int actualHeight = options.outHeight;
		int actualWidth = options.outWidth;

		// max Height and width values of the compressed image is taken as
		// 816x612

		float maxHeight = 2 * 816.0f;
		float maxWidth = 2 * 612.0f;
		float imgRatio = actualWidth / actualHeight;
		float maxRatio = maxWidth / maxHeight;

		// width and height values are set maintaining the aspect ratio of the
		// image

		if (actualHeight > maxHeight || actualWidth > maxWidth) {
			if (imgRatio < maxRatio) {
				imgRatio = maxHeight / actualHeight;
				actualWidth = (int) (imgRatio * actualWidth);
				actualHeight = (int) maxHeight;
			} else if (imgRatio > maxRatio) {
				imgRatio = maxWidth / actualWidth;
				actualHeight = (int) (imgRatio * actualHeight);
				actualWidth = (int) maxWidth;
			} else {
				actualHeight = (int) maxHeight;
				actualWidth = (int) maxWidth;

			}
		}

		// setting inSampleSize value allows to load a scaled down version of
		// the original image

		options.inSampleSize = calculateInSampleSize(options, actualWidth,
				actualHeight);

		// inJustDecodeBounds set to false to load the actual bitmap
		options.inJustDecodeBounds = false;

//		// this options allow android to claim the bitmap memory if it runs low
//		// on memory
//		options.inPurgeable = true;
//		options.inInputShareable = true;
		options.inTempStorage = new byte[16 * 1024];

		try {
			// load the bitmap from its path
			bmp = BitmapFactory.decodeStream(inputStream, null, options);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();

		}
		try {
			scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
					Bitmap.Config.ARGB_8888);
		} catch (OutOfMemoryError exception) {
			exception.printStackTrace();
		}

//		float ratioX = actualWidth / (float) options.outWidth;
//		float ratioY = actualHeight / (float) options.outHeight;
//		float middleX = actualWidth / 2.0f;
//		float middleY = actualHeight / 2.0f;
//
//		Matrix scaleMatrix = new Matrix();
//		scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
//
//		Canvas canvas = new Canvas(scaledBitmap);
//		canvas.setMatrix(scaleMatrix);
//		canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
//				middleY - bmp.getHeight() / 2, new Paint(
//						Paint.FILTER_BITMAP_FLAG));

//		// check the rotation of the image and display it properly
//		ExifInterface exif;
//		try {
//			exif = new ExifInterface(inputStream);
//
//			int orientation = exif.getAttributeInt(
//					ExifInterface.TAG_ORIENTATION, 0);
//			Log.d("EXIF", "Exif: " + orientation);
//			Matrix matrix = new Matrix();
//			if (orientation == 6) {
//				matrix.postRotate(90);
//				Log.d("EXIF", "Exif: " + orientation);
//			} else if (orientation == 3) {
//				matrix.postRotate(180);
//				Log.d("EXIF", "Exif: " + orientation);
//			} else if (orientation == 8) {
//				matrix.postRotate(270);
//				Log.d("EXIF", "Exif: " + orientation);
//			}
//			scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
//					scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
//					true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		return scaledBitmap;
	}
	
	private int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		final float totalPixels = width * height;
		final float totalReqPixelsCap = reqWidth * reqHeight * 2;
		while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
			inSampleSize++;
		}

		return inSampleSize;
	}

}