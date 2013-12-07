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
 * Class used to store information Element Hocr. 
 */
abstract public class HocrElement {

	private String	id;
	private BBox	bbox;

	public HocrElement() {}

	/**
	 * Constructs an <code>HocrElement</code> with a unique id and a coordinates <code>BBox</code>.
	 * 
	 * @param id Set the id of element.
	 * @param bbox Sets the coordinates of element.
	 */
	public HocrElement(String id, BBox bbox) {
		this.id = id;
		this.bbox = bbox;
	}

	/**
	 * @return Id of element
	 */
	public String getId() {
		return id;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public BBox getBbox() {
		return bbox;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param bbox
	 */
	public void setBbox(BBox bbox) {
		this.bbox = bbox;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	abstract public String getClassName();

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	abstract public String getTagName();


	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "id=" + id + ", bbox=" + bbox;
	}
}
