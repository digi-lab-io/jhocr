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

/**
 * TODO add documentation
 * 
 */
public class HocrDocument {

	public static final String	TESSERACT_VERSION_3_02	= "tesseract 3.02";
	public static final String	TESSERACT_VERSION_3_01	= "tesseract";

	private String				contentType;
	private String				ocrSystem;

	private List<HocrPage>		pages					= new ArrayList<HocrPage>();

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * TODO add documentation
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public String getOcrSystem() {
		return ocrSystem;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param ocrSystem
	 */
	public void setOcrSystem(String ocrSystem) {
		this.ocrSystem = ocrSystem;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrPage> getPages() {
		return pages;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param pages
	 */
	public void setPages(List<HocrPage> pages) {
		this.pages = pages;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param page
	 */
	public void addPage(HocrPage page) {
		page.setDocument(this);
		getPages().add(page);
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String toString() {
		return "HocrDocument{" + "contentType=" + contentType + ", ocrSystem=" + ocrSystem + ", pages=" + pages.size() + '}';
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public boolean isOcrSystemTesseract3_01() {
		return TESSERACT_VERSION_3_01.equals(getOcrSystem());
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public boolean isOcrSystemTesseract3_02() {
		return TESSERACT_VERSION_3_02.equals(getOcrSystem());
	}
}
