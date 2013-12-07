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
 * Class used to store information of document hocr.
 * 
 * Element Example: {@code 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title/>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta name='ocr-system' content='tesseract'/>
	</head>
	<body>
		<div class='ocr_page' id='page_1' title='image "phototest.tif"; bbox 0 0 640 480'>
			<div class='ocr_carea' id='block_1_1' title="bbox 36 90 619 363">
				<p class='ocr_par' dir='ltr' id='par_1' title="bbox 36 92 618 184">
					<span class='ocr_line' id='line_1' title="bbox 36 92 580 122">
						<span class='ocr_word' id='word_1' title="bbox 36 92 96 116">This</span>
					</span>
				</p>
			</div>
		</div>
	</body>
</html>}
 */
public class HocrDocument {

	public static final String	TESSERACT_VERSION_3_02	= "tesseract 3.02";
	public static final String	TESSERACT_VERSION_3_01	= "tesseract";

	private String				contentType;
	private String				ocrSystem;

	private List<HocrPage>		pages					= new ArrayList<HocrPage>();

	/** 
	 * @return The content type of document
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Set the content type of document
	 * @param The content type of document
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return The OCR system that generated the document hOCR 
	 */
	public String getOcrSystem() {
		return ocrSystem;
	}

	/**
	 * Set The OCR system that generated the document hOCR
	 * 
	 * @param The OCR system that generated the document hOCR
	 */
	public void setOcrSystem(String ocrSystem) {
		this.ocrSystem = ocrSystem;
	}

	/**
	 * @return The children <code>HocrPage</code> of this.
	 */
	public List<HocrPage> getPages() {
		return pages;
	}

	/**
	 * Set the children <code>HocrPage</code> of this.
	 * 
	 * @param pages The children <code>HocrPage</code> of this.
	 */
	public void setPages(List<HocrPage> pages) {
		this.pages = pages;
	}

	/**
	 * Add a new page
	 * 
	 * @param page The new page
	 */
	public void addPage(HocrPage page) {
		page.setDocument(this);
		getPages().add(page);
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "HocrDocument{" + "contentType=" + contentType + ", ocrSystem=" + ocrSystem + ", pages=" + pages.size() + "}";
	}

	/**
	 * Validates if ocr system is version 3.01 of Tesseract
	 * 
	 * @return Result of validation
	 */
	public boolean isOcrSystemTesseract3_01() {
		return TESSERACT_VERSION_3_01.equals(getOcrSystem());
	}

	/**
	 * Validates if ocr system is version 3.02 of Tesseract
	 * 
	 * @return Result of validation 
	 */
	public boolean isOcrSystemTesseract3_02() {
		return TESSERACT_VERSION_3_02.equals(getOcrSystem());
	}
}
