/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.attribute;

/**
 *
 * @author pablo-moreira
 */
public class BBox {
    
    private Integer left;
    private Integer top;
    private Integer right;
    private Integer bottom;

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
