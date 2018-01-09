package com.ql.jcjr.http;

import com.lidroid.xutils.exception.HttpException;

public class ResponseEntity {
	public boolean isError = false;
	public String errorInfo = "";
	public String errorTitle = "提示";
	// 错误类型 -1  -3//没有更多数据 -2 未知的
	public int errNo = -1;
	//Http请求返回的异常
	public HttpException exception;
	/**
	 * 请求的url
	 */
	public String url;

	public String responeJson = "";
}
