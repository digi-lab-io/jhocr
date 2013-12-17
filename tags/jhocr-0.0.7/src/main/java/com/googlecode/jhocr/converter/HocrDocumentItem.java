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

package com.googlecode.jhocr.converter;

import java.io.InputStream;

/**
 * Contains the<br>
 * hocr file {@link com.googlecode.jhocr.converter.HocrDocumentItem#hocrInputStream} and <br>
 * the image {@link com.googlecode.jhocr.converter.HocrDocumentItem#imageInputStream}.
 * 
 */
public class HocrDocumentItem {

	private InputStream	hocrInputStream;
	private InputStream	imageInputStream;

	/**
	 * Returns an {@link com.googlecode.jhocr.converter.HocrDocumentItem} object that can be used for the hocr to pdf conversion from example {@link com.googlecode.jhocr.converter.HocrToPdf}.
	 * 
	 * @param hocrInputStream
	 *            is the input stream of the hocr file.
	 * @param imageInputStream
	 *            is the input stream of the image.
	 */
	public HocrDocumentItem(InputStream hocrInputStream, InputStream imageInputStream) {
		this.hocrInputStream = hocrInputStream;
		this.imageInputStream = imageInputStream;
	}

	/**
	 * @return the {@link #hocrInputStream} of the hocr file.
	 */
	public InputStream getHocrInputStream() {
		return hocrInputStream;
	}

	/**
	 * Sets the hocr file {@link #hocrInputStream}.
	 * 
	 * @param hocrInputStream
	 *            will be set.
	 */
	public void setHocrInputStream(InputStream hocrInputStream) {
		this.hocrInputStream = hocrInputStream;
	}

	/**
	 * @return the {@link #imageInputStream} of the image.
	 */
	public InputStream getImageInputStream() {
		return imageInputStream;
	}

	/**
	 * Sets the image file {@link #imageInputStream} object.
	 * 
	 * @param imageInputStream
	 *            will be set.
	 */
	public void setImageInputStream(InputStream imageInputStream) {
		this.imageInputStream = imageInputStream;
	}
}
