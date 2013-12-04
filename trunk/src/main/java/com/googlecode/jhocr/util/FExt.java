package com.googlecode.jhocr.util;

/**
 * Supported file extensions by jhocr.
 *
 */
public enum FExt {

	HTML("html"), PDF("pdf"), TIF("tif"), TIFF("tiff"), PNG("png"), JPG("jpg");

	private String fileExtension;

	private FExt(String s) {
		fileExtension = s;
	}

	/**
	 * Returns the file extension with out the file separator.
	 */
	@Override
	public String toString() {
		return fileExtension;
	}

}
