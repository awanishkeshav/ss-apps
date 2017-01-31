package com.consumer.models;

public class Error {

	// Backend Errors
	public static final int SignInFailed = -101;
	public static final int InvalidRequest = -102;
	public static final int InvalidSession = -103;
	
	// Http status
	public static final int HTTPInvalidSession = 401;
	
	// self defined errors
	public static final int CancelAPIRequest = -100000;
	public static final int JsonParsingFailed = -100001;
	public static final int InvalidData = -100002;
	public static final int NoInternet = -1000003;
	public static final int OtherException = -100100;
	
	int code;
	String message;
	
	public int getCode() {
        return this.code;
     }
	
	public String getMessage() {
        return this.message;
     }
	
	Error(int code){
		this.code = code;
	}
	
	public Error(int code, String message){
		this.code = code;
		this.message = message;
	}
}
