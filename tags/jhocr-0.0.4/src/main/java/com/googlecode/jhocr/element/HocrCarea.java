/**
 * TODO: describe: <one line to give the program's name and a brief idea of what it does.>
 * 
 * <br>
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

/**
 * TODO: describe: <one line to give the program's name and a brief idea of what it does.>
 * 
 * <br>
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

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jhocr.attribute.BBox;

public class HocrCarea extends HocrElement {

	public static final String	TAGNAME		= "div";
	public static final String	CLASSNAME	= "ocr_carea";

	private HocrPage			page;
	private List<HocrParagraph>	paragraphs	= new ArrayList<HocrParagraph>();

	public HocrCarea(String id, BBox bbox) {
		super(id, bbox);
	}

	public List<HocrParagraph> getParagraphs() {
		return paragraphs;
	}

	public void setParagraphs(List<HocrParagraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

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

	public HocrPage getPage() {
		return page;
	}

	public void setPage(HocrPage page) {
		this.page = page;
	}

	public List<HocrLine> getLines() {

		List<HocrLine> lines = new ArrayList<HocrLine>();

		for (HocrParagraph paragraph : getParagraphs()) {
			lines.addAll(paragraph.getLines());
		}

		return lines;
	}

	public List<HocrWord> getWords() {

		List<HocrWord> words = new ArrayList<HocrWord>();

		for (HocrLine line : getLines()) {
			words.addAll(line.getWords());
		}

		return words;
	}

	@Override
	public String toString() {
		return "HocrCarea{" + super.toString() + ", paragraphs=" + paragraphs.size() + '}';
	}
}
