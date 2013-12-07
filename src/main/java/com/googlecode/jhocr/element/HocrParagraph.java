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
import com.googlecode.jhocr.attribute.ParagraphDirection;

/**
 * Class used to store information "ocr_paragraph" element and their children.
 * 
 * Element example: {@code <p class='ocr_par' dir='ltr' id='par_2' title="bbox 36 194 597 361">}
 */
public class HocrParagraph extends HocrElement {

	public static final String	TAGNAME		= "p";
	public static final String	CLASSNAME	= "ocr_par";

	private HocrCarea			carea;
	private ParagraphDirection	direction	= ParagraphDirection.LTR;
	private List<HocrLine>		lines		= new ArrayList<HocrLine>();

	/**
	 * Constructs an <code>HocrParagraph</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element.
	 * @param bbox Sets the coordinates of element.
	 * @param image Set the <code>ParagraphDirection</code> of element.
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
	 * @return The children <code>HocrLine</code> of this.
	 */
	public List<HocrLine> getLines() {
		return lines;
	}

	/**
	 * Set the children <code>HocrLine</code> of this.
	 * @param lines The children <code>HocrLine</code> of this.
	 */
	public void setLines(List<HocrLine> lines) {
		this.lines = lines;
	}

	/**
	 * Add new line. 
	 * 
	 * @param line The new line.
	 */
	public void addLine(HocrLine line) {
		line.setParagraph(this);
		getLines().add(line);
	}

	/**
	 * @return The parent <code>HocrCarea</code> of this.
	 */
	public HocrCarea getCarea() {
		return carea;
	}

	/**
	 * Set the parent <code>HocrCarea</code> of this.
	 * @param page The parent <code>HocrCarea</code> of this.
	 */
	public void setCarea(HocrCarea carea) {
		this.carea = carea;
	}

	/**
	 * @return The text of paragraph direction
	 * @see ParagraphDirection
	 */
	public ParagraphDirection getDirection() {
		return direction;
	}

	/**
	 * Set the text of paragraph direction
	 * 
	 * @see ParagraphDirection
	 * @param direction The text direction
	 */
	public void setDirection(ParagraphDirection direction) {
		this.direction = direction;
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrParagraph{" + super.toString() + ", direction=" + direction + ", lines=" + lines.size() + "}";
	}
}
