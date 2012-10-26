/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.element;

import com.googlecode.jhocr.attribute.BBox;

/**
 *
 * @author pablo-moreira
 */
abstract public class HocrElement {
    
    private String id;
    private BBox bbox;

    public HocrElement() {}    
    
    public HocrElement(String id, BBox bbox) {
        this.id = id;
        this.bbox = bbox;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public BBox getBbox() {
        return bbox;
    }

    public void setBbox(BBox bbox) {
        this.bbox = bbox;
    }
    
    abstract public String getClassName();
    abstract public String getTagName();

    @Override
    public String toString() {
        return "id=" + id + ", bbox=" + bbox;
    }
}