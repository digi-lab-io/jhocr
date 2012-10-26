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
public class HocrLine extends HocrElement {
   
    public static final String TAGNAME = "span";
    public static final String CLASSNAME = "ocr_line";
    
    private HocrParagraph paragraph;
    private List<HocrWord> words = new ArrayList<HocrWord>();

    public HocrLine(String id, BBox bbox) {
        super(id, bbox);
    }
    
    @Override
    public String getClassName() {
        return TAGNAME;
    }

    @Override
    public String getTagName() {
        return CLASSNAME;
    }

    public List<HocrWord> getWords() {
        return words;
    }

    public void setWords(List<HocrWord> words) {
        this.words = words;
    }
    
    public void addWord(HocrWord word) {        
        word.setLine(this);
        getWords().add(word);
    }

    public HocrParagraph getParagraph() {
        return paragraph;
    }

    public void setParagraph(HocrParagraph paragraph) {
        this.paragraph = paragraph;
    }
    
    public String getText() {
        
        StringBuilder text = new StringBuilder();
        
        boolean first = true;
        
        for (HocrWord word : getWords()) {            
            if (first) {
                text.append(word.getText());
                first = false;
            }
            else {
                text.append(" ").append(word.getText());
            }
            
        }
        
        return text.toString();
    }

    @Override
    public String toString() {
        return "HocrLine{" + super.toString() + ", words=" + words.size() + '}';
    }
}