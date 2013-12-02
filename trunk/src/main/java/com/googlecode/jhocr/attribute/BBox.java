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

package com.googlecode.jhocr.attribute;

public class BBox {

	private Integer	left;
	private Integer	top;
	private Integer	right;
	private Integer	bottom;

	public BBox(Integer left, Integer top, Integer right, Integer bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public Integer getLeft() {
		return left;
	}

	public Integer getTop() {
		return top;
	}

	public Integer getRight() {
		return right;
	}

	public Integer getBottom() {
		return bottom;
	}

	public Integer getWidth() {
		return getRight() - getLeft();
	}

	public Integer getHeight() {
		return getBottom() - getTop();
	}

	@Override
	public String toString() {
		return "BBox{" + "l=" + left + ", t=" + top + ", r=" + right + ", b=" + bottom + '}';
	}
}
