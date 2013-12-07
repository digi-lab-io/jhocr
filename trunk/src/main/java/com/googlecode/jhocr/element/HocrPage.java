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
 * Class used to store information "ocr_page" element and their children.
 */
public class HocrPage extends HocrElement {

	public static final String	TAGNAME		= "div";
	public static final String	CLASSNAME	= "ocr_page";

	private HocrDocument		document;
	private String				image;
	private List<HocrCarea>		careas		= new ArrayList<HocrCarea>();

	/**
	 * Constructs an <code>HocrPage</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element.
	 * @param bbox Sets the coordinates of element.
	 * @param image Set the image name of element.
	 */
	public HocrPage(String id, BBox bbox, String image) {
		super(id, bbox);
		this.image = image;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public String getImage() {
		return image;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrCarea> getCareas() {
		return careas;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param careas
	 */
	public void setCareas(List<HocrCarea> careas) {
		this.careas = careas;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param carea
	 */
	public void addCarea(HocrCarea carea) {
		carea.setPage(this);
		getCareas().add(carea);
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
	public List<HocrParagraph> getParagraphs() {

		List<HocrParagraph> paragraphs = new ArrayList<HocrParagraph>();

		for (HocrCarea carea : getCareas()) {
			paragraphs.addAll(carea.getParagraphs());
		}

		return paragraphs;
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
	 * TODO add documentation
	 * 
	 * @param document
	 */
	public void setDocument(HocrDocument document) {
		this.document = document;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public HocrDocument getDocument() {
		return document;
	}


	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrPage{" + super.toString() + ", image=" + image + ", careas=" + careas.size() + "}";
	}
}
