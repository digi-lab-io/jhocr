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

public class TestHOcrParser {

	private HocrDocument	document	= null;

	@Before
	public void setUp() throws Exception {

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream hocrInputStream = loader.getResourceAsStream("testdata/img.jpg.html");

		HocrParser parser = new HocrParser(hocrInputStream);

		document = parser.parse();

		System.out.println(document);

	}

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
