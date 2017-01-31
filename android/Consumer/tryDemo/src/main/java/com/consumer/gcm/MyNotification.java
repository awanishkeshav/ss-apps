package com.consumer.gcm;

import java.io.Serializable;

import com.consumer.HomeActivity;
import com.consumer.OfferDetailsActivity;
import com.consumer.ReviewActivity;
import com.consumer.TransDetailActivity;
import com.consumer.R;
import com.consumer.common.Constants;
import com.consumer.models.Error;
import com.consumer.services.APIService;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class MyNotification implements Serializable {

	/**
	 * 
	 */

	public enum NotificationType {
		Unknown, TxnInfo, TxnApprovalRequired, TxnDenied, MerchantReviewRequested, Offer
	}

	private static final long serialVersionUID = 1L;

	public static final String ExtraNotification = "notif";
	public static final String ActionApprove = "com.merchant.action.approve";
	public static final String ActionReject = "com.merchant.action.reject";
	public static final String ActionDismiss = "com.merchant.action.dismiss";
	public static final String ActionView = "com.merchant.action.view";
	public static final String ActionReviewRequest = "com.merchant.action.reviewreq";
	public static final String ActionNone = "com.merchant.action.none";

	public static final String ActionApproveLabel = "Allow";
	public static final String ActionRejectLabel = "Deny";
	public static final String ActionDismissLabel = "Dismiss";
	public static final String ActionViewLabel = "Details";
	public static final String ActionViewLabelLong = "View Details";
	public static final String ActionReviewRequestLabel = "Feedback";

	private static final int ActionApproveIcon = 0;
	private static final int ActionRejectIcon = 0;
	private static final int ActionDismissIcon = 0;
	private static final int ActionViewIcon = 0;
	private static final int ActionReviewRequestIcon = 0;

	private boolean sound = false;
	private String title;
	private String message;
	private String screenMessage;
	private String nTypeStr;
	private String urlToHit;
	private Long transactionId;
	private Long offerId;
	private Long cardId;

	public NotificationType notifType;
	private boolean canBeHandled;
	private int notifId;

	public static MyNotification getNotification(Bundle bundle) {
		if (bundle != null) {
			Object notifObject = bundle
					.getSerializable(MyNotification.ExtraNotification);
			if (notifObject != null) {
				MyNotification notif = (MyNotification) notifObject;
				return notif;
			}
		}
		return null;
	}

	public String getTitle() {
		return title;
	}

	public Long getCardId() {
		return cardId;
	}

	public Long getTransactionId() {
		return transactionId;
	}
	public Long getOfferId() {
		return offerId;
	}

	public String getMessage() {
		return message;
	}
	public String getScreenMessage() {
		return screenMessage;
	}

	public MyNotification(Bundle bundle) {

		Object tId = bundle.get("txnId");
		if (tId != null) {
			transactionId = Long.parseLong(tId + "");
		}

		Object soundParam = bundle.get("sound");
		if (soundParam != null) {
			if (Integer.parseInt(soundParam + "") == 1) {
				sound = true;
			}
		}

		Object cId = bundle.get("cardId");
		if (cId != null) {
			cardId = Long.parseLong(cId + "");
		}
		
		Object ofrId = bundle.get("offerId");
		if (ofrId != null) {
			offerId = Long.parseLong(ofrId + "");
		}

		title = bundle.getString("title");
		message = bundle.getString("message");
		screenMessage = bundle.getString("screenMessage");
		nTypeStr = bundle.getString("type");
		urlToHit = bundle.getString("url");
		
		notifId = (int)System.currentTimeMillis();

		notifType = NotificationType.Unknown;

		if (nTypeStr != null) {

			if (nTypeStr.equals("Info")) {
				notifType = NotificationType.TxnInfo;

			} else if (nTypeStr.equals("ApprovalRequired")) {
				notifType = NotificationType.TxnApprovalRequired;

			} else if (nTypeStr.equals("Block")) {
				notifType = NotificationType.TxnDenied;

			} else if (nTypeStr.equals("ReviewRequired")) {
				notifType = NotificationType.MerchantReviewRequested;
				
			}else if (nTypeStr.equals("Offer")) {
				notifType = NotificationType.Offer;
			}
			
		}

		// notifType = NotificationType.MerchantReviewRequested;
		// transactionId = Long.parseLong("10");

		if (notifType != NotificationType.Unknown) {
			canBeHandled = true;
		}
	}

	private Intent getIntentForNone(Context context) {

		Class<HomeActivity> listenerActivity = HomeActivity.class;
		Intent intent = new Intent(context, listenerActivity);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionNone);
		return intent;
	}

	private Intent getIntentForApprove(Context context) {

		Class<HomeActivity> listenerActivity = HomeActivity.class;
		Intent intent = new Intent(context, listenerActivity);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionApprove);
		return intent;
	}

	private Intent getIntentForDeny(Context context) {

		Class<HomeActivity> listenerActivity = HomeActivity.class;
		Intent intent = new Intent(context, listenerActivity);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionReject);
		return intent;
	}

	private Intent getIntentForView(Context context) {

		Class<HomeActivity> listenerActivity = HomeActivity.class;
		Intent intent = new Intent(context, listenerActivity);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionView);
		return intent;
	}

	private Intent getIntentForDismiss(Context context) {

		Intent intent = new Intent(context, NotifDismissService.class);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionDismiss);
		return intent;
	}

	private Intent getIntentForReviewRequest(Context context) {

		Class<HomeActivity> listenerActivity = HomeActivity.class;
		Intent intent = new Intent(context, listenerActivity);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(ActionReviewRequest);
		return intent;
	}

	private PendingIntent getPendingIntent(Context context, Intent intent) {
		int requestID = (int) System.currentTimeMillis();
		return PendingIntent.getActivity(context, requestID, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}
	private PendingIntent getPendingIntentForDismiss(Context context, Intent intent) {
		int requestID = (int) System.currentTimeMillis();
		return PendingIntent.getService(context, requestID, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}

	public void buildNotification(Context context) {

		if (!canBeHandled) {
			return;
		}

		int notifIcon = 0;
		switch (Constants.bank){
			case SBI:
				notifIcon = R.drawable.logo_sbi_mid;
				break;
			case IndusInd:
				notifIcon = R.drawable.indusind_logo_mid;
				break;
		}

		NotificationManager notifManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(notifIcon)
				.setContentTitle(title)
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message))
				.setContentText(message).setPriority(Notification.PRIORITY_MAX);
		;
		mBuilder.setColor(context.getResources().getColor(R.color.Transparent));

		if (this.sound) {
			// Play default sound
			Uri defaultRingtoneUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			mBuilder.setSound(defaultRingtoneUri);
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS
					| Notification.DEFAULT_VIBRATE);
		}
		
		//Intent actionDismissIntent = this.getIntentForDismiss(context);

		switch (notifType) {
		case TxnInfo:
			Intent action1Intent = this.getIntentForView(context);
			Intent action2Intent = this.getIntentForReviewRequest(context);
			mBuilder.addAction(ActionViewIcon, ActionViewLabel,
					getPendingIntent(context, action1Intent))
					.addAction(ActionReviewRequestIcon,
							ActionReviewRequestLabel,
							getPendingIntent(context, action2Intent));
			
					//.addAction(ActionDismissIcon, ActionDismissLabel,
						//	getPendingIntentForDismiss(context, actionDismissIntent));
			Intent contentIntent = this.getIntentForView(context);
			PendingIntent contentPendingIntent = this.getPendingIntent(context,contentIntent);

            mBuilder.setContentIntent(contentPendingIntent);

			break;
		case TxnApprovalRequired:
			action1Intent = this.getIntentForApprove(context);
			action2Intent = this.getIntentForDeny(context);
			Intent action3Intent = this.getIntentForView(context);

			mBuilder.addAction(ActionApproveIcon, ActionApproveLabel,
					getPendingIntent(context, action1Intent))
					.addAction(ActionRejectIcon, ActionRejectLabel,
							getPendingIntent(context, action2Intent))
					.addAction(ActionViewIcon, ActionViewLabel,
							getPendingIntent(context, action3Intent));
			contentIntent = this.getIntentForView(context);
			contentPendingIntent = this.getPendingIntent(context,contentIntent);

			mBuilder.setContentIntent(contentPendingIntent);
			break;

		case TxnDenied:
			action1Intent = this.getIntentForView(context);
			mBuilder.addAction(ActionViewIcon, ActionViewLabel,
					getPendingIntent(context, action1Intent));
			//.addAction(
				//	ActionDismissIcon, ActionDismissLabel,
			//		getPendingIntentForDismiss(context, actionDismissIntent));
			contentIntent = this.getIntentForView(context);
			contentPendingIntent = this.getPendingIntent(context,contentIntent);

            mBuilder.setContentIntent(contentPendingIntent);

			break;
		case MerchantReviewRequested:
			action1Intent = this.getIntentForReviewRequest(context);
			
			mBuilder.addAction(ActionReviewRequestIcon,
					ActionReviewRequestLabel,
					getPendingIntent(context, action1Intent));
			//.addAction(
				//	ActionDismissIcon, ActionDismissLabel,
			//		getPendingIntentForDismiss(context, actionDismissIntent));
			contentIntent = this.getIntentForView(context);
			contentPendingIntent = this.getPendingIntent(context,contentIntent);

            mBuilder.setContentIntent(contentPendingIntent);

			break;
		case Offer:
			if (offerId != null) {
				action1Intent = this.getIntentForView(context);
				mBuilder.addAction(ActionViewIcon, ActionViewLabel,
						getPendingIntent(context, action1Intent));
			}
			contentIntent = this.getIntentForView(context);
			contentPendingIntent = this.getPendingIntent(context,contentIntent);

            mBuilder.setContentIntent(contentPendingIntent);

			break;

		case Unknown:
			break;
		}

		
		notifManager.notify(notifId, mBuilder.build());
	}

	public void sendBroadCast(Context context) {
		if (!canBeHandled) {
			return;
		}

		Intent noActionIntent = new Intent(ActionNone);
		noActionIntent.putExtra(ExtraNotification, this);
		noActionIntent.setAction(ActionNone);
		LocalBroadcastManager.getInstance(context)
				.sendBroadcast(noActionIntent);

	}

	public void showApprovalRequiredDialog(final Context context) {
		if (!canBeHandled) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(false);
		AlertDialog alert = builder.create();

		alert.setButton(AlertDialog.BUTTON_POSITIVE, ActionApproveLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						approve(context);
						dialog.cancel();
					}
				});
		alert.setButton(AlertDialog.BUTTON_NEUTRAL, ActionRejectLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						reject(context);
						dialog.cancel();
					}
				});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, ActionViewLabelLong,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						viewDetails(context, ActionView);
						dialog.cancel();
					}
				});

		alert.show();
	}

	public void showReviewRequestDialog(final Context context) {
		if (!canBeHandled) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(false);
		AlertDialog alert = builder.create();

		alert.setButton(AlertDialog.BUTTON_POSITIVE, ActionReviewRequestLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						review(context);
						dialog.cancel();
					}
				});
		alert.setButton(AlertDialog.BUTTON_NEGATIVE, ActionDismissLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss(context);
						dialog.cancel();
					}
				});

		alert.show();
	}

	public void processIntent(Context context, Intent intent) {
		String action = intent.getAction();
		if (this.canBeHandled && action != null) {

			switch (notifType) {
			case TxnInfo:
				if (action.equals(ActionNone)) {
					showTxnInformativeDialog(context);
				} else if (action.equals(ActionView)) {
					viewDetails(context, ActionView);
				} else if (action.equals(ActionDismiss)) {
					dismiss(context);
				}else if (action.equals(ActionReviewRequest)) {
					review(context);
				}

				break;
			case TxnApprovalRequired:
				if (action.equals(ActionNone)) {
					this.showApprovalRequiredDialog(context);
				} else if (action.equals(ActionApprove)) {
					this.approve(context);
				} else if (action.equals(ActionReject)) {
					this.reject(context);
				} else if (action.equals(ActionView)) {
					this.viewDetails(context, ActionView);
				}
				break;

			case TxnDenied:
				if (action.equals(ActionNone)) {
					showTxnInformativeDialog(context);
				} else if (action.equals(ActionView)) {
					viewDetails(context, ActionView);
				} else if (action.equals(ActionDismiss)) {
					dismiss(context);
				}

				break;
			case MerchantReviewRequested:
				if (action.equals(ActionNone)) {
					showReviewRequestDialog(context);
				} else if (action.equals(ActionReviewRequest)) {
					review(context);
				} else if (action.equals(ActionDismiss)) {
					dismiss(context);
				}

				break;
			case Offer:
				if (action.equals(ActionNone)) {
					showOfferDialog(context);
				} else if (action.equals(ActionView)) {
					viewOfferDetails(context, action);
					
				} else if (action.equals(ActionDismiss)) {
					dismiss(context);
				}

				break;

			case Unknown:
				break;
			}

		}
	}

	private void showTxnInformativeDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.setButton(AlertDialog.BUTTON_POSITIVE, ActionViewLabelLong,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						viewDetails(context, ActionView);
						dialog.cancel();

					}
				});
		alert.setButton(AlertDialog.BUTTON_NEUTRAL, ActionReviewRequestLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						review(context);
						dialog.cancel();
					}
				});

		alert.setButton(AlertDialog.BUTTON_NEGATIVE,ActionDismissLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
						dismiss(context);
					}
				});

		
		alert.show();
	}
	
	private void showOfferDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		if (offerId != null) {
			alert.setButton(AlertDialog.BUTTON_POSITIVE, ActionViewLabelLong,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							viewOfferDetails(context, ActionView);
							dialog.cancel();

						}
					});
		}
		
		alert.setButton(AlertDialog.BUTTON_NEGATIVE,ActionDismissLabel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();
						dismiss(context);
					}
				});

		
		alert.show();
	}

	public void approve(Context context) {
		trackNotificationAction(context, urlToHit, 1, notifId);
	}

	public void reject(Context context) {
		trackNotificationAction(context, urlToHit, 0, notifId);
	}

	public void dismiss(Context context) {
		clearNotifiation(context, notifId);
	}

	public void viewDetails(Context context, String action) {
		Intent intent = new Intent(context, TransDetailActivity.class);
		intent.putExtra(ExtraNotification, this);
		intent.setAction(action);
		context.startActivity(intent);
		clearNotifiation(context, notifId);
	}
	
	public void viewOfferDetails(Context context, String action) {
		if (offerId != null) {
			Intent intent = new Intent(context, OfferDetailsActivity.class);
			intent.putExtra(ExtraNotification, this);
			intent.setAction(action);
			context.startActivity(intent);
			
		}
		clearNotifiation(context, notifId);
		
	}

	public void review(Context context) {
		Intent intent = new Intent(context, ReviewActivity.class);
		intent.putExtra(ExtraNotification, this);
		context.startActivity(intent);
		clearNotifiation(context, notifId);
	}

	private void trackNotificationAction(final Context context, String url,
			final int approved, final int notifId) {
		APIService service = APIService.trackUserResponseApprovedOrDeny(
				context, url, approved);
		service.setServiceListener(new APIService.APIServiceListener() {

			@Override
			public void onCompletion(APIService service, Object data,
					Error error) {
				if (error == null) {
					String message = "Approval recorded.\n\nPlease retry your transaction.";
					if (approved == 0) {
						message = "Transaction denied";
					}
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					clearNotifiation(context, notifId);
				}

			}
		});
		service.getData(true);
	}

	private void clearNotifiation(Context context, int notifId) {
		NotificationManager nMgr = (NotificationManager) context
				.getApplicationContext().getSystemService(
						Context.NOTIFICATION_SERVICE);
		nMgr.cancel(notifId);
	}

	public static boolean handleNotificationIfNeeded(Context context,
			Intent intent) {
		Bundle bundle = intent.getExtras();
		MyNotification notif = MyNotification.getNotification(bundle);
		if (notif != null) {
			notif.processIntent(context, intent);
			return true;
		}
		return false;
	}

}
