package com.googlecode.jhocr.util;

public class JHOCRUtil {

	private static JHOCRUtil	instance	= null;
	private static String		OS			= System.getProperty("os.name").toLowerCase();

	public static JHOCRUtil getInstance() {
		if (instance == null) {
			instance = new JHOCRUtil();
		}
		return instance;
	}

	public boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

}
