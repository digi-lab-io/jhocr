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
	public HocrDocument parse() throws Exception {

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
		 * TODO add documentation
		 */
		while (pageTag != null) {
			document.addPage(parsePageTag(pageTag));
			pageTag = source.getNextStartTag(pageTag.getEnd(), ATTRIBUTE_CLASS, HocrPage.CLASSNAME, false);
		}

		return document;
	}

	/**
	 * TODO add documentation
	 * TODO add better logging
	 * TODO catch exception and return null if any thrown
	 * 
	 * @param pageTag
	 * @return
	 * @throws Exception
	 */
	private HocrPage parsePageTag(StartTag pageTag) throws Exception {

		Element element = pageTag.getElement();

		Matcher imageMatcher = PATTERN_IMAGE.matcher(element.getAttributeValue(ATTRIBUTE_TITLE));

		if (!imageMatcher.find()) {
			throw new Exception("Erro ao realizar o parser do arquivo HOCR, não foi possível encontrar o atributo image.");
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
	 * TODO add documentation
	 * 
	 * @param careaTag
	 * @return
	 * @throws Exception
	 */
	private HocrCarea parseCareaTag(StartTag careaTag) throws Exception {

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
	 * TODO add documentation
	 * 
	 * @param paragraphTag
	 * @return
	 * @throws Exception
	 */
	private HocrParagraph parseParagraphTag(StartTag paragraphTag) throws Exception {

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
	 * TODO add documentation
	 * 
	 * @param lineTag
	 * @return
	 * @throws Exception
	 */
	private HocrLine parseLineTag(StartTag lineTag) throws Exception {

		Element element = lineTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		BBox bbox = parseAttributeBBox(element);

		HocrLine line = new HocrLine(id, bbox);

		List<StartTag> wordTags;

		/**
		 * TODO add documentation
		 */
		if (document.isOcrSystemTesseract3_02()) {
			wordTags = element.getAllStartTagsByClass(HocrWord.CLASSNAME_X);
		} else {
			wordTags = element.getAllStartTagsByClass(HocrWord.CLASSNAME);
		}

		for (StartTag wordTag : wordTags) {
			line.addWord(parseWordTag(wordTag));
		}

		return line;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param wordTag
	 * @return
	 * @throws Exception
	 */
	private HocrWord parseWordTag(StartTag wordTag) throws Exception {

		Element element = wordTag.getElement();

		String id = element.getAttributeValue(ATTRIBUTE_ID);
		BBox bbox = parseAttributeBBox(element);

		List<StartTag> strongTags = element.getAllStartTags(TAG_STRONG);

		boolean strong = (strongTags.size() > 0);

		String text = element.getContent().getTextExtractor().toString();

		return new HocrWord(id, bbox, text, strong);
	}

	/**
	 * TODO add documentation
	 * TODO translate logging
	 * 
	 * @param element
	 * @return
	 * @throws Exception
	 */
	private BBox parseAttributeBBox(Element element) throws Exception {

		String attributeTitleValue = element.getAttributeValue("title");

		if (attributeTitleValue == null) {
			return null;
		}

		Matcher bboxMatcher = PATTERN_BBOX.matcher(attributeTitleValue);

		/**
		 * TODO add documentation
		 */
		if (!bboxMatcher.find()) {
			String msg = "Erro ao realizar o parser do arquivo HOCR, não foi possível encontrar o atributo bbox.";
			log.error(msg);
			throw new Exception(msg);
		}

		Matcher bboxCoordinateMatcher = PATTERN_BBOX_COORDINATE.matcher(bboxMatcher.group());

		/**
		 * TODO add documentation
		 */
		if (!bboxCoordinateMatcher.find()) {
			String msg = "Erro ao realizar o parser do arquivo HOCR, não foi possível realizar o parse do atributo bbox.";
			log.error(msg);
			throw new Exception(msg);
		}

		return new BBox(Integer.parseInt((bboxCoordinateMatcher.group(1))), Integer.parseInt((bboxCoordinateMatcher.group(2))), Integer.parseInt((bboxCoordinateMatcher.group(3))), Integer.parseInt((bboxCoordinateMatcher.group(4))));
	}
}
