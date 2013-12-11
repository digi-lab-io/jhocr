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

package com.googlecode.jhocr.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;

import org.apache.log4j.Logger;

import com.googlecode.jhocr.attribute.BBox;
import com.googlecode.jhocr.attribute.ParagraphDirection;
import com.googlecode.jhocr.element.HocrCarea;
import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrLine;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.element.HocrParagraph;
import com.googlecode.jhocr.element.HocrWord;

/**
 * TODO add documentation
 * 
 */
public class HocrParser {

	private static Logger	log						= Logger.getLogger(HocrParser.class);

	private static String	ATTRIBUTE_ID			= "id";
	private static String	ATTRIBUTE_CLASS			= "class";
	private static String	ATTRIBUTE_TITLE			= "title";
	private static String	ATTRIBUTE_DIR			= "dir";
	private static String	TAG_STRONG				= "strong";

	/**
	 * TODO implement unit tests for the patterns
	 * TODO add documentation
	 */
	private Pattern			PATTERN_IMAGE			= Pattern.compile("image\\s+([^;]+)");
	private Pattern			PATTERN_BBOX			= Pattern.compile("bbox(\\s+-?\\d+){4}");
	private Pattern			PATTERN_BBOX_COORDINATE	= Pattern.compile("(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)");

	private InputStream		inputStream;
	private HocrDocument	document;

	/**
	 * TODO add documentation
	 * 
	 * @param inputStream
	 */
	public HocrParser(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 * @throws Exception
	 */
	public HocrDocument parse() {

		try {

			Source source = new Source(inputStream);

			List<Element> allElements = source.getAllElements("meta");

			document = new HocrDocument();

			/**
			 * Iterate all meta data found in the html file and pass relavant information to the <code>HocrDocument</code> obj.
			 * TODO improve this iteration to be either static using such variables or dynamic making it configurable.
			 */
			for (Element elem : allElements) {

				String attrHttp = elem.getAttributeValue("http-equiv");

				/**
				 * Set content type.
				 */
				if (attrHttp != null && attrHttp.equals("Content-Type")) {
					document.setContentType(elem.getAttributeValue("content"));
					continue;
				}

				String attrName = elem.getAttributeValue("name");

				/**
				 * Set ocr system.
				 */
				if (attrName != null && attrName.equals("ocr-system")) {
					document.setOcrSystem(elem.getAttributeValue("content"));
				}
			}

			StartTag pageTag = source.getNextStartTag(0, ATTRIBUTE_CLASS, HocrPage.CLASSNAME, false);

			/**
			 * Iterate all "ocr_page" found in the html file and pass relavant information to the <code>HocrPage</code> obj.
			 * Tiff images can store multiple pages in a single file.
			 */
			while (pageTag != null) {
				document.addPage(parsePageTag(pageTag));
				pageTag = source.getNextStartTag(pageTag.getEnd(), ATTRIBUTE_CLASS, HocrPage.CLASSNAME, false);
			}

			return document;

		} catch (IOException e) {
			log.error("It was not possible to convert the document, returning null.", e);
			return null;
		}
	}

	/**
	 * Performs parse "ocr_page" element and their children.<br/>
	 * Element example: {@code <div class='ocr_page' id='page_1' title='image "phototest.tif"; bbox 0 0 640 480'>}
	 * 
	 * TODO add better logging
	 *   
	 * @param pageTag The "ocr_page" tag 
	 * @return an instance of {@link HocrPage}.
	 */
	private HocrPage parsePageTag(StartTag pageTag) {

		Element element = pageTag.getElement();

		Matcher imageMatcher = PATTERN_IMAGE.matcher(element.getAttributeValue(ATTRIBUTE_TITLE));

		if (!imageMatcher.find()) {

		}

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		String image = imageMatcher.group(1);
		BBox bbox = parseAttributeBBox(element);

		HocrPage page = new HocrPage(id, bbox, image);

		List<StartTag> careaTags = element.getAllStartTagsByClass(HocrCarea.CLASSNAME);

		for (StartTag careaTag : careaTags) {
			page.addCarea(parseCareaTag(careaTag));
		}

		return page;
	}

	/**
	 * Performs parse "ocr_carea" element and their children.<br/>
	 * Element example: {@code <div class='ocr_carea' id='block_1_1' title="bbox 36 90 619 363">}
	 * 
	 * @param careaTag The "ocr_carea" tag.
	 * @return an instance of {@link HocrCarea}.
	 */
	private HocrCarea parseCareaTag(StartTag careaTag) {

		Element element = careaTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		BBox bbox = parseAttributeBBox(element);

		HocrCarea carea = new HocrCarea(id, bbox);

		List<StartTag> paragraphTags = element.getAllStartTagsByClass(HocrParagraph.CLASSNAME);

		for (StartTag paragraphTag : paragraphTags) {
			carea.addParagraph(parseParagraphTag(paragraphTag));
		}

		return carea;
	}

	/**
	 * Performs parse "ocr_par" element and their children.<br/>
	 * Element example: {@code <p class='ocr_par' dir='ltr' id='par_1' title="bbox 36 92 618 184">}
	 * 
	 * @param paragraphTag The "ocr_par" tag.
	 * @return an instance of {@link HocrParagraph}.
	 */
	private HocrParagraph parseParagraphTag(StartTag paragraphTag) {

		Element element = paragraphTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		String dir = element.getAttributeValue(ATTRIBUTE_DIR);
		BBox bbox = parseAttributeBBox(element);

		HocrParagraph paragraph = new HocrParagraph(id, bbox, (dir != null ? ParagraphDirection.valueOf(dir.toUpperCase()) : ParagraphDirection.LTR));

		List<StartTag> lineTags = element.getAllStartTagsByClass(HocrLine.CLASSNAME);

		for (StartTag lineTag : lineTags) {
			paragraph.addLine(parseLineTag(lineTag));
		}

		return paragraph;
	}

	/**
	 * Performs parse "ocr_line" element and their children.<br/>
	 * Element example: {@code <span class='ocr_line' id='line_1' title="bbox 36 92 580 122">}
	 *
	 * @param lineTag The "ocr_line" tag.
	 * @return an instance of {@link HocrLine}.
	 */
	private HocrLine parseLineTag(StartTag lineTag) {

		Element element = lineTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		BBox bbox = parseAttributeBBox(element);

		HocrLine line = new HocrLine(id, bbox);
		
		/**
		 * Tesseract change class name of element HocrWord in version 3.02.
		 * From: ocr_word, To: ocrx_word 
		 */
		List<StartTag> wordTags = element.getAllStartTagsByClass(HocrWord.CLASSNAME_X);

		if (wordTags == null || wordTags.isEmpty()) {
			wordTags = element.getAllStartTagsByClass(HocrWord.CLASSNAME);
		} 

		for (StartTag wordTag : wordTags) {
			line.addWord(parseWordTag(wordTag));
		}

		return line;
	}

	/**
	 * Performs parse "ocr_word" element and their children.<br/>
	 * Element example: {@code <span class='ocr_word' id='word_1' title="bbox 36 92 96 116">This</span>}
	 *
	 * @param lineTag The "ocr_word" tag.
	 * @return an instance of {@link HocrWord}.
	 */
	private HocrWord parseWordTag(StartTag wordTag) {

		Element element = wordTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		BBox bbox = parseAttributeBBox(element);

		List<StartTag> strongTags = element.getAllStartTags(TAG_STRONG);

		boolean strong = (strongTags.size() > 0);

		String text = element.getContent().getTextExtractor().toString();

		return new HocrWord(id, bbox, text, strong);
	}

	/**
	 * 
	 * TODO add documentation
	 * 
	 * @param element
	 * @return an instance of {@link BBox}.
	 * @throws Exception
	 */
	private BBox parseAttributeBBox(Element element) {

		try {

			String attributeTitleValue = element.getAttributeValue("title");

			if (attributeTitleValue == null) {
				return null;
			}

			Matcher bboxMatcher = PATTERN_BBOX.matcher(attributeTitleValue);

			/**
			 * TODO add documentation
			 */
			if (!bboxMatcher.find()) {
				throw new Exception();
			}

			Matcher bboxCoordinateMatcher = PATTERN_BBOX_COORDINATE.matcher(bboxMatcher.group());

			/**
			 * TODO add documentation
			 */
			if (!bboxCoordinateMatcher.find()) {
				throw new Exception();
			}

			return new BBox(Integer.parseInt((bboxCoordinateMatcher.group(1))), Integer.parseInt((bboxCoordinateMatcher.group(2))), Integer.parseInt((bboxCoordinateMatcher.group(3))), Integer.parseInt((bboxCoordinateMatcher.group(4))));

		} catch (Exception e) {
			log.error("Error when performing file parser hOCR, It was not possible to parse the bbox attribute");
			return null;
		}

	}
}