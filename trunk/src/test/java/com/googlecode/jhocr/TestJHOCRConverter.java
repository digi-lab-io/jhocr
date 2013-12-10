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
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;

/**
 * TODO add documentation
 * 
 */
public class TestJHOCRConverter {

	private File		pdf		= null;
	private ClassLoader	loader	= null;

	/**
	 * TODO add documentation
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		loader = Thread.currentThread().getContextClassLoader();
		String packageFolder = "src/test/resources/test-results";
		String fileName = "tess4j-jhocr-pdf.pdf";

		pdf = new File(String.format("%s/%s", packageFolder, fileName));
	}

	/**
	 * TODO add documentation
	 * TODO re-implemt this test to not only create an PDF with metadata such as bookmarks, but also to test against it if those were really added to the PDF.
	 * 
	 * @throws Throwable
	 */
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
