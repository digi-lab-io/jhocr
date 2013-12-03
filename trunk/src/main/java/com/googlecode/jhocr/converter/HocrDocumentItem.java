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

package com.googlecode.jhocr.converter;

import java.io.InputStream;

/**
 * TODO add documentation
 * 
 */
public class HocrDocumentItem {

	private InputStream	hocrInputStream;
	private InputStream	imageInputStream;

	/**
	 * TODO add documentation
	 * 
	 * @param hocrInputStream
	 * @param imageInputStream
	 */
	public HocrDocumentItem(InputStream hocrInputStream, InputStream imageInputStream) {
		this.hocrInputStream = hocrInputStream;
		this.imageInputStream = imageInputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public InputStream getHocrInputStream() {
		return hocrInputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param hocrInputStream
	 */
	public void setHocrInputStream(InputStream hocrInputStream) {
		this.hocrInputStream = hocrInputStream;
	}

	/**
	 * TODO add documentation
	 */
	public InputStream getImageInputStream() {
		return imageInputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param imageInputStream
	 */
	public void setImageInputStream(InputStream imageInputStream) {
		this.imageInputStream = imageInputStream;
	}
}
