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

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.googlecode.jhocr.element.HocrCarea;
import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrLine;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.element.HocrParagraph;
import com.googlecode.jhocr.element.HocrWord;
import com.googlecode.jhocr.parser.HocrParser;

/**
 * TODO add documentation
 * 
 */
public class TestHOcrParser {

	private HocrDocument	document	= null;

	/**
	 * TODO add documentation
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream hocrInputStream = loader.getResourceAsStream("test-data/eurotext.tif.html");

		HocrParser parser = new HocrParser(hocrInputStream);

		document = parser.parse();

		System.out.println(document);

	}

	/**
	 * TODO add documentation
	 */
	@Test
	public void test() {
		String pre = "-- ";
		for (HocrPage page : document.getPages()) {
			System.out.println(pre + page);
			for (HocrCarea carea : page.getCareas()) {
				System.out.println(pre + pre + carea);
				for (HocrParagraph paragraph : carea.getParagraphs()) {
					System.out.println(pre + pre + pre + paragraph);
					for (HocrLine line : paragraph.getLines()) {
						System.out.println(pre + pre + pre + pre + line);
						for (HocrWord word : line.getWords()) {
							System.out.println(pre + pre + pre + pre + pre + word);
						}
					}
				}
			}
		}
	}

}
