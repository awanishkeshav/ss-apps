package com.consumer.utility;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardUtil {

	/**
	 * Hide opened soft keyboard
	 * */
	public static void hideSoftKeyboard(EditText editText) {
		if (null != editText) {
			InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}

	/**
	 * Hide opened soft keyboard
	 * */
	public static void hideSoftKeyboard(View view) {
		if (null != view) {
			InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * Show opened soft keyboard
	 * */
	public static void showSoftKeyboard(EditText editText) {
		if (null != editText) {
			InputMethodManager inputMethodManager = (InputMethodManager) editText.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
			inputMethodManager.showSoftInput(editText, 0);
		}
	}
}
