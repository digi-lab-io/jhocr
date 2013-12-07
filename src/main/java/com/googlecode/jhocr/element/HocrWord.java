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

import com.googlecode.jhocr.attribute.BBox;

/**
 * Class used to store information "ocr_word" element.
 */
public class HocrWord extends HocrElement {

	public static final String	TAGNAME		= "span";
	public static final String	CLASSNAME	= "ocr_word";
	public static final String	CLASSNAME_X	= "ocrx_word";

	
	private boolean				strong;
	private HocrLine			line;
	private String				text;

	/**
	 * Constructs an <code>HocrWord</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element
	 * @param bbox Sets the coordinates of the element
	 * @param text Set the text of element
	 * @param strong Set the text is strong or not
	 */
	public HocrWord(String id, BBox bbox, String text, boolean strong) {
		super(id, bbox);
		this.text = text;
		this.strong = strong;
	}

	/**
	 * @return The class name of element
	 */
	@Override
	public String getClassName() {
		return TAGNAME;
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String getTagName() {
		return CLASSNAME;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public boolean isStrong() {
		return strong;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param strong
	 */
	public void setStrong(boolean strong) {
		this.strong = strong;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public HocrLine getLine() {
		return line;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param line
	 */
	public void setLine(HocrLine line) {
		this.line = line;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrWord{" + super.toString() + ", strong=" + strong + ", text=" + text + "}";
	}
}
