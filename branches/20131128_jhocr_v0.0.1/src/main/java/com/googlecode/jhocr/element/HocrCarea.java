/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.element;

import com.googlecode.jhocr.attribute.BBox;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pablo-moreira
 */
public class HocrCarea extends HocrElement {
    
    public static final String TAGNAME = "div";
    public static final String CLASSNAME = "ocr_carea";

    private HocrPage page;
    private List<HocrParagraph> paragraphs = new ArrayList<HocrParagraph>();

    public HocrCarea(String id, BBox bbox) {
        super(id, bbox);
    }

    public List<HocrParagraph> getParagraphs() {
        return paragraphs;
    }   
    
    public void setParagraphs(List<HocrParagraph> paragraphs) {
        this.paragraphs = paragraphs;
    }
    
    public void addParagraph(HocrParagraph paragraph) {
        paragraph.setCarea(this);            
        getParagraphs().add(paragraph);
    }

    @Override
    public String getClassName() {
        return CLASSNAME;
    }

    @Override
    public String getTagName() {
        return TAGNAME;
    }

    public HocrPage getPage() {
        return page;
    }

    public void setPage(HocrPage page) {
        this.page = page;
    }
    
    public List<HocrLine> getLines() {
        
        List<HocrLine> lines = new ArrayList<HocrLine>();
        
        for (HocrParagraph paragraph : getParagraphs()) {
            lines.addAll(paragraph.getLines());
        }
     
        return lines;
    }
    
    public List<HocrWord> getWords() {

        List<HocrWord> words = new ArrayList<HocrWord>();
        
        for (HocrLine line : getLines()) {
            words.addAll(line.getWords());
        }

        return words;
    }

    @Override
    public String toString() {
        return "HocrCarea{" + super.toString() + ", paragraphs=" + paragraphs.size() + '}';
    }
}