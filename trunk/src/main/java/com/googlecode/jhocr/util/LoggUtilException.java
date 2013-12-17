package com.googlecode.jhocr.util;

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
