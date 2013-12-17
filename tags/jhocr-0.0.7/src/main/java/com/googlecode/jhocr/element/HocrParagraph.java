/**
 * Copyright (©) 2013 Pablo Filetti Moreira & O.J. Sousa Rodrigues
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
import com.googlecode.jhocr.attribute.ParagraphDirection;

/**
 * Class used to store information "ocr_paragraph" element and their children.<br>
 * Element example: <br>
 * {@literal <p class='ocr_par' dir='ltr' id='par_2' title="bbox 36 194 597 361">}
 */
public class HocrParagraph extends HocrElement {

	public static final String	TAGNAME		= "p";
	public static final String	CLASSNAME	= "ocr_par";

	private HocrCarea			carea;
	private ParagraphDirection	direction	= ParagraphDirection.LTR;
	private List<HocrLine>		lines		= new ArrayList<HocrLine>();

	/**
	 * Constructs the {@link com.googlecode.jhocr.element.HocrParagraph} with a unique id and a coordinates {@link com.googlecode.jhocr.attribute.BBox}.
	 * 
	 * @param id
	 *            sets the id of element.
	 * @param bbox
	 *            sets the coordinates of element.
	 * @param image
	 *            sets the {@link com.googlecode.jhocr.attribute.ParagraphDirection} of element.
	 */
	public HocrParagraph(String id, BBox bbox, ParagraphDirection direction) {
		super(id, bbox);
		this.direction = direction;
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
	 * @return a collection of {@link#lines}.
	 */
	public List<HocrLine> getLines() {
		return lines;
	}

	/**
	 * Sets the {@link#lines}.
	 * 
	 * @param lines
	 *            will be set.
	 */
	public void setLines(List<HocrLine> lines) {
		this.lines = lines;
	}

	/**
	 * Adds a new {@link com.googlecode.jhocr.element.HocrLine#line} to the current {@link#lines}.
	 * 
	 * @param line
	 *            will be added to {@link#lines}.
	 */
	public void addLine(HocrLine line) {
		line.setParagraph(this);
		getLines().add(line);
	}

	/**
	 * @return the {@link #carea} object.
	 */
	public HocrCarea getCarea() {
		return carea;
	}

	/**
	 * Sets the {@link #carea}.
	 * 
	 * @param carea
	 *            will be set.
	 */
	public void setCarea(HocrCarea carea) {
		this.carea = carea;
	}

	/**
	 * @return the text {@link #direction} of the paragraph.
	 */
	public ParagraphDirection getDirection() {
		return direction;
	}

	/**
	 * Set the text {@link #direction} of the paragraph.
	 * 
	 * @param direction
	 *            will be set.
	 */
	public void setDirection(ParagraphDirection direction) {
		this.direction = direction;
	}

	/**
	 * @return the {@link #toString()} value this element.
	 */
	@Override
	public String toString() {
		return "HocrParagraph{" + super.toString() + ", direction=" + direction + ", lines=" + lines.size() + "}";
	}
}
