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

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jhocr.attribute.BBox;
import com.googlecode.jhocr.attribute.ParagraphDirection;

public class HocrParagraph extends HocrElement {

	public static final String	TAGNAME		= "p";
	public static final String	CLASSNAME	= "ocr_par";

	private HocrCarea			carea;
	private ParagraphDirection	direction	= ParagraphDirection.LTR;
	private List<HocrLine>		lines		= new ArrayList<HocrLine>();

	public HocrParagraph(String id, BBox bbox, ParagraphDirection direction) {
		super(id, bbox);
		this.direction = direction;
	}

	@Override
	public String getClassName() {
		return TAGNAME;
	}

	@Override
	public String getTagName() {
		return CLASSNAME;
	}

	public List<HocrLine> getLines() {
		return lines;
	}

	public void setLines(List<HocrLine> lines) {
		this.lines = lines;
	}

	public void addLine(HocrLine line) {
		line.setParagraph(this);
		getLines().add(line);
	}

	public HocrCarea getCarea() {
		return carea;
	}

	public void setCarea(HocrCarea carea) {
		this.carea = carea;
	}

	public ParagraphDirection getDirection() {
		return direction;
	}

	public void setDirection(ParagraphDirection direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "HocrParagraph{" + super.toString() + ", direction=" + direction + ", lines=" + lines.size() + '}';
	}
}
