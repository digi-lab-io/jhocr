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

public class HocrDocument {

	public static final String	TESSERACT_VERSION_3_02	= "tesseract 3.02";
	public static final String	TESSERACT_VERSION_3_01	= "tesseract";

	private String				contentType;
	private String				ocrSystem;

	private List<HocrPage>		pages					= new ArrayList<HocrPage>();

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getOcrSystem() {
		return ocrSystem;
	}

	public void setOcrSystem(String ocrSystem) {
		this.ocrSystem = ocrSystem;
	}

	public List<HocrPage> getPages() {
		return pages;
	}

	public void setPages(List<HocrPage> pages) {
		this.pages = pages;
	}

	public void addPage(HocrPage page) {
		page.setDocument(this);
		getPages().add(page);
	}

	@Override
	public String toString() {
		return "HocrDocument{" + "contentType=" + contentType + ", ocrSystem=" + ocrSystem + ", pages=" + pages.size() + '}';
	}

	public boolean isOcrSystemTesseract3_01() {
		return TESSERACT_VERSION_3_01.equals(getOcrSystem());
	}

	public boolean isOcrSystemTesseract3_02() {
		return TESSERACT_VERSION_3_02.equals(getOcrSystem());
	}
}
