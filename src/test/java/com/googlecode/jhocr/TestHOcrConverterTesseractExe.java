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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;

/**
 * Convert html-ocr and image files into a searchable pdf.
 * 
 */
public class TestHOcrConverterTesseractExe {

	private ClassLoader		loader					= null;
	private static String	testFileResourcesPath	= "test-data/";
	private static String	testFileResultsPath		= "src/test/resources/test-results";
	private static String	testFileName			= "eurotext_tesseract-exe";

	private static String	htmlOcrFileExtension	= "html";
	private static String	htmlOcrRelFileName		= String.format("%s%s.%s", testFileResourcesPath, testFileName, htmlOcrFileExtension);

	private static String	imageFileExtension		= "tif";
	private static String	imageRelFileName		= String.format("%s%s.%s", testFileResourcesPath, testFileName, imageFileExtension);

	private static String	pdfFileExtension		= "pdf";
	private static String	pdfFileName				= String.format("%s.%s", testFileName, pdfFileExtension);

	private File			testResultFileName		= null;

	/**
	 * TODO add documentation
	 * 
	 */
	@Before
	public void setUp() {

		loader = Thread.currentThread().getContextClassLoader();

		testResultFileName = new File(String.format("%s/%s", testFileResultsPath, pdfFileName));
	}

	/**
	 * TODO add documentation
	 * 
	 * @throws IOException
	 * 
	 * @throws Throwable
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
	 * TODO implement test if the converted pdf contains know text from the image.
	 */
	@Test
	public void testConvertedPDF() {
	}
}
