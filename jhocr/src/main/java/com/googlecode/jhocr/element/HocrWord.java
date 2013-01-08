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
public class HocrWord extends HocrElement {

    public static final String TAGNAME = "span";
    public static final String CLASSNAME = "ocr_word";
    public static final String CLASSNAME_X = "ocrx_word";
    
    private boolean strong;
    private HocrLine line;
    private String text;

    public HocrWord(String id, BBox bbox, String text, boolean strong) {
        super(id, bbox);
        this.text = text;
        this.strong = strong;        
    }
    
    @Override
    public String getClassName() {
        return TAGNAME;
    }

    @Override
    public String getTagName() {
        return CLASSNAME;
    }

    public boolean isStrong() {
        return strong;
    }

    public void setStrong(boolean strong) {
        this.strong = strong;
    }

    public HocrLine getLine() {
        return line;
    }

    public void setLine(HocrLine line) {
        this.line = line;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HocrWord{" + super.toString() + ", strong=" + strong + ", text=" + text + '}';
    }
}
