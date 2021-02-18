package com.zrk.data.pump.utils;

import org.slf4j.Logger;

public class LogUtil {


	public static String logErrorStackMsg(Logger log, Throwable e) {
		log.error("{}", e.getMessage());
		
		StackTraceElement[] aStackTraceElements = e.getStackTrace();
		StringBuilder errorInfo = new StringBuilder();
		errorInfo.append(e.getMessage()).append("\n");
		for (StackTraceElement tStackTraceElement : aStackTraceElements) {
			errorInfo.append(tStackTraceElement.toString()).append("\n");
		}
		log.error(errorInfo.toString());
		return errorInfo.toString();
	}
	
}
