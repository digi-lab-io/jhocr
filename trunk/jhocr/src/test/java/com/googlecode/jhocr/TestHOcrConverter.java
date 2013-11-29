package com.googlecode.jhocr;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;

public class TestHOcrConverter {

	private File		pdf		= null;
	private ClassLoader	loader	= null;

	@Before
	public void setUp() throws Exception {

		loader = Thread.currentThread().getContextClassLoader();
		String packageFolder = "src/test/resources/test-results";
		String fileName = "jhocr.pdf";

		pdf = new File(String.format("%s/%s", packageFolder, fileName));
	}

	@Test
	public void test() throws Throwable {

		FileOutputStream os = new FileOutputStream(pdf);

		HocrToPdf hocrToPdf = new HocrToPdf(os);

		hocrToPdf.addHocrDocument(loader.getResourceAsStream("test-data/eurotext_tess4j.html"), loader.getResourceAsStream("test-data/eurotext.tif"));

		HashMap<String, Object> bookmark1 = new HashMap<String, Object>();

		bookmark1.put("Title", "Bookmark 1");
		bookmark1.put("Action", "GoTo");
		bookmark1.put("Page", String.format("%d Fit", 1));

		hocrToPdf.addOutline(bookmark1);
		hocrToPdf.convert();

		os.close();
	}
}
