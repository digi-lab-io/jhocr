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
 * TODO add documentation
 * 
 */
public class BBox {

	private Integer	left;
	private Integer	top;
	private Integer	right;
	private Integer	bottom;

	/**
	 * TODO add documentation
	 * 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public BBox(Integer left, Integer top, Integer right, Integer bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getLeft() {
		return left;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getTop() {
		return top;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getRight() {
		return right;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getBottom() {
		return bottom;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getWidth() {
		return getRight() - getLeft();
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Integer getHeight() {
		return getBottom() - getTop();
	}

	/**
	 * TODO add documentation
	 */
	@Override
	public String toString() {
		return "BBox{" + "l=" + left + ", t=" + top + ", r=" + right + ", b=" + bottom + '}';
	}
}
