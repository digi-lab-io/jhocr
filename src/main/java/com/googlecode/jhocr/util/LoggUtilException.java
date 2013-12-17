package com.googlecode.jhocr.util;

/**
 * Please give me and the apache community a call, if you know a better and elegant way to do this!
 */
public class LoggUtilException extends Exception {

	private static final long	serialVersionUID	= 238204279367101127L;

	@Override
	public String toString() {

		String className = this.getClass().getName();
		StackTraceElement[] sTrace = this.getStackTrace();
		className = sTrace[0].getClassName();

		return className;
	}
}
