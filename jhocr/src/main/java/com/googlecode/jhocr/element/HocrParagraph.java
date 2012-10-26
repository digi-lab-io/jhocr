/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.element;

import com.googlecode.jhocr.attribute.ParagraphDirection;
import java.util.ArrayList;
import java.util.List;
import com.googlecode.jhocr.attribute.BBox;

/**
 *
 * @author pablo-moreira
 */
public class HocrParagraph extends HocrElement {
    
    public static final String TAGNAME = "p";
    public static final String CLASSNAME = "ocr_par";
    
    private HocrCarea carea;
    private ParagraphDirection direction = ParagraphDirection.LTR;
    private List<HocrLine> lines = new ArrayList<HocrLine>();

    public HocrParagraph(String id, BBox bbox, ParagraphDirection direction) {
        super(id, bbox);
        this.direction = direction;
    }
    
    @Override
    public String getClassName() {
        return TAGNAME;
    }

    @Override
    public String getTagName() {
        return CLASSNAME;
    }

    public List<HocrLine> getLines() {
        return lines;
    }

    public void setLines(List<HocrLine> lines) {
        this.lines = lines;
    }
    
    public void addLine(HocrLine line) {
        line.setParagraph(this);
        getLines().add(line);
    }

    public HocrCarea getCarea() {
        return carea;
    }

    public void setCarea(HocrCarea carea) {
        this.carea = carea;
    }

    public ParagraphDirection getDirection() {
        return direction;
    }

    public void setDirection(ParagraphDirection direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "HocrParagraph{" + super.toString() + ", direction=" + direction + ", lines=" + lines.size() + '}';
    }
}
