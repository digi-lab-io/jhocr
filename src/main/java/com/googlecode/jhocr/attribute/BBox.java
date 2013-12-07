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

package com.googlecode.jhocr.attribute;

/**
 * Class used to store information of attribute "bbox".
 */
public class BBox {

	private Integer	left;
	private Integer	top;
	private Integer	right;
	private Integer	bottom;

	/**
	 * Constructs an <code>BBox</code> with a coordinates left, top, right and bottom.
	 * 
	 * @param left Sets the left coordinate of the element.
	 * @param top Sets the top coordinate of the element.
	 * @param right Sets the right coordinate of the element.
	 * @param bottom Sets the bottom coordinate of the element.
	 */
	public BBox(Integer left, Integer top, Integer right, Integer bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	/** 
	 * @return The left coordinate of the element.
	 */
	public Integer getLeft() {
		return left;
	}

	/**
	 * @return The top coordinate of the element.
	 */
	public Integer getTop() {
		return top;
	}

	/**
	 * @return The right coordinate of the element.
	 */
	public Integer getRight() {
		return right;
	}

	/**
	 * @return The bottom coordinate of the element.
	 */
	public Integer getBottom() {
		return bottom;
	}

	/**
	 * @return The width used by the element.
	 */
	public Integer getWidth() {
		return getRight() - getLeft();
	}

	/**
	 * @return The height used by the element.
	 */
	public Integer getHeight() {
		return getBottom() - getTop();
	}

	/**
	 * Returns the informations of this element as a <code>String</code>.
	 *
	 * @return the informations of this element as a <code>String</code>.
	 */
	@Override
	public String toString() {
		return "BBox{" + "l=" + left + ", t=" + top + ", r=" + right + ", b=" + bottom + "}";
	}
}