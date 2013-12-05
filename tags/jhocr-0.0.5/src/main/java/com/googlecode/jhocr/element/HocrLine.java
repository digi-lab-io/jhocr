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
 * TODO add documentation
 * 
 */
public class HocrLine extends HocrElement {

	public static final String	TAGNAME		= "span";
	public static final String	CLASSNAME	= "ocr_line";

	private HocrParagraph		paragraph;
	private List<HocrWord>		words		= new ArrayList<HocrWord>();

	/**
	 * TODO add documentation
	 * 
	 * @param id
	 * @param bbox
	 */
	public HocrLine(String id, BBox bbox) {
		super(id, bbox);
	}

	/**
	 * TODO add documentation
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
	public List<HocrWord> getWords() {
		return words;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param words
	 */
	public void setWords(List<HocrWord> words) {
		this.words = words;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param word
	 */
	public void addWord(HocrWord word) {
		word.setLine(this);
		getWords().add(word);
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public HocrParagraph getParagraph() {
		return paragraph;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param paragraph
	 */
	public void setParagraph(HocrParagraph paragraph) {
		this.paragraph = paragraph;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public String getText() {

		StringBuilder text = new StringBuilder();

		boolean first = true;

		/**
		 * TODO add documentation
		 */
		for (HocrWord word : getWords()) {
			if (first) {
				text.append(word.getText());
				first = false;
			} else {
				text.append(" ").append(word.getText());
			}

		}

		return text.toString();
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String toString() {
		return "HocrLine{" + super.toString() + ", words=" + words.size() + '}';
	}
}
