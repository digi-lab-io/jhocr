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
 * 
 * Element example: {@code <div class='ocr_carea' id='block_1_1' title="bbox 36 90 619 363">} 
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
	 * @return The children <code>HocrParagraph</code> of this.
	 */
	public List<HocrParagraph> getParagraphs() {
		return paragraphs;
	}

	/**
	 * Set the children <code>HocrParagraph</code> of this.
	 * @param paragraphs The children <code>HocrParagraph</code> of this.
	 */
	public void setParagraphs(List<HocrParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	/**
	 * Add new paragraph. 
	 * 
	 * @param paragraph The new paragraph.
	 */
	public void addParagraph(HocrParagraph paragraph) {
		paragraph.setCarea(this);
		getParagraphs().add(paragraph);
	}

	@Override
	public String getClassName() {
		return CLASSNAME;
	}

	@Override
	public String getTagName() {
		return TAGNAME;
	}

	/**
	 * @return The parent <code>HocrPage</code> of this.
	 */
	public HocrPage getPage() {
		return page;
	}

	/**
	 * Set the parent <code>HocrPage</code> of this.
	 * @param page The parent <code>HocrPage</code> of this.
	 */
	public void setPage(HocrPage page) {
		this.page = page;
	}

	/**
	 * @return The children <code>HocrLine</code> of this.
	 */
	public List<HocrLine> getLines() {

		List<HocrLine> lines = new ArrayList<HocrLine>();

		for (HocrParagraph paragraph : getParagraphs()) {
			lines.addAll(paragraph.getLines());
		}

		return lines;
	}

	/**
	 * @return The children <code>HocrWord</code> of this.
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