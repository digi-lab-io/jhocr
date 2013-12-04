/**
 * Copyright (©) 2013 Pablo Filetti Moreira
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jhocr;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;
import com.googlecode.jhocr.util.FExt;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

/**
 * Convert html-ocr files generated in with the tesseract.exe version 3.02 and
 * image files into a searchable pdf document.
 * 
 */
public class TestHOcrConverterTesseractExe {

	private ClassLoader		loader					= null;
	private static String	testFileResourcesPath	= "test-data/";
	private static String	testFileResultsPath		= "src/test/resources/test-results";
	private static String	testFileName			= "eurotext_tesseract-exe";

	private static String	htmlOcrRelFileName		= String.format("%s%s.%s", testFileResourcesPath, testFileName, FExt.HTML);
	private static String	imageRelFileName		= String.format("%s%s.%s", testFileResourcesPath, testFileName, FExt.TIF);
	private static String	pdfFileName				= String.format("%s.%s", testFileName, FExt.PDF);

	private File			testResultFileName		= new File(String.format("%s/%s", testFileResultsPath, pdfFileName));		;

	/**
	 * Test setup and preparation.
	 * 
	 */
	@Before
	public void setUp() {

		loader = Thread.currentThread().getContextClassLoader();

	}

	/**
	 * This will map the ocr html file (hocr) and image into one pdf.
	 * 
	 * @return true if the test passed.
	 */
	@Test
	public void testConvertionFromHOCRToPDF() {

		try {
			FileOutputStream os = new FileOutputStream(testResultFileName);

			HocrToPdf hocrToPdf = new HocrToPdf(os);
			hocrToPdf.addHocrDocument(loader.getResourceAsStream(htmlOcrRelFileName), loader.getResourceAsStream(imageRelFileName));
			hocrToPdf.convert();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * TODO improve test accuracy. This test should find out if the converted
	 * pdf is searchable.
	 * 
	 */
	@Test
	public void testConvertedPDF() {
		ArrayList<String> pagesTextResult = new ArrayList<String>();

		try {

			PdfReader reader = new PdfReader(testResultFileName.getPath());
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			TextExtractionStrategy strategy;

			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
				pagesTextResult.add(strategy.getResultantText());
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();

		}

		for (String page : pagesTextResult) {
			if (!page.contains("a")) {
				fail("The char 'a' could not be found, it's looks like the pdf is not searchable at all.");
			}
		}
	}
}
