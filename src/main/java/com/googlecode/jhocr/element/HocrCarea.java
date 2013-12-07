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

package com.googlecode.jhocr.element;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jhocr.attribute.BBox;

/**
 * Class used to store information "ocr_carea" element and its children.
 */
public class HocrCarea extends HocrElement {

	public static final String	TAGNAME		= "div";
	public static final String	CLASSNAME	= "ocr_carea";

	private HocrPage			page;
	private List<HocrParagraph>	paragraphs	= new ArrayList<HocrParagraph>();

	/**
	 * Constructs an <code>HocrCarea</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element.
	 * @param bbox Sets the coordinates of element.
	 */
	public HocrCarea(String id, BBox bbox) {
		super(id, bbox);
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrParagraph> getParagraphs() {
		return paragraphs;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param paragraphs
	 */
	public void setParagraphs(List<HocrParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param paragraph
	 */
	public void addParagraph(HocrParagraph paragraph) {
		paragraph.setCarea(this);
		getParagraphs().add(paragraph);
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String getClassName() {
		return CLASSNAME;
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String getTagName() {
		return TAGNAME;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public HocrPage getPage() {
		return page;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param page
	 */
	public void setPage(HocrPage page) {
		this.page = page;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrLine> getLines() {

		List<HocrLine> lines = new ArrayList<HocrLine>();

		for (HocrParagraph paragraph : getParagraphs()) {
			lines.addAll(paragraph.getLines());
		}

		return lines;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrWord> getWords() {

		List<HocrWord> words = new ArrayList<HocrWord>();

		for (HocrLine line : getLines()) {
			words.addAll(line.getWords());
		}

		return words;
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrCarea{" + super.toString() + ", paragraphs=" + paragraphs.size() + "}";
	}
}
