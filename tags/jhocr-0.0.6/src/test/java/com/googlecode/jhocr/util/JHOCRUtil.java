package com.googlecode.jhocr.util;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class JHOCRUtil {

	private static JHOCRUtil	instance	= null;
	private static String		OS			= System.getProperty("os.name").toLowerCase();
	private final static Logger	logger		= LoggerFactory.getLogger(JHOCRUtil.class.getName());

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

	/**
	 * TODO improve test accuracy. This test should find out if the converted pdf is searchable or not.
	 * 
	 * @param word
	 *            the that should be searchable &/ containing in the PDF.
	 * @param pdfFileName
	 *            without the file extension.
	 * @return true is the test were successful and false is unsuccessful.
	 * 
	 */
	public boolean testGeneratedPDF(String word, String pdfFileName) {
		boolean result = true;
		ArrayList<String> pagesTextResult = new ArrayList<String>();

		try {

			PdfReader reader = new PdfReader(pdfFileName);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				pagesTextResult.add(strategy.getResultantText());
			}

			reader.close();

			if (pagesTextResult.isEmpty()) {

				result = false;
				logger.error("The pdf is empty or corrupted.");

			} else {

				for (String page : pagesTextResult) {
					if (!page.contains(word)) {
						result = false;
						logger.error("The word '{}' could not be found in the PDF '{}'.", word, pdfFileName);
					}
				}
			}

			logger.info("The word '{}' could be found in the PDF '{}'.", word, pdfFileName);
			return result;

		} catch (IOException e) {
			logger.error("The PDF is empty or corrupted or not accessible.", e);
			return false;
		}

	}

}
