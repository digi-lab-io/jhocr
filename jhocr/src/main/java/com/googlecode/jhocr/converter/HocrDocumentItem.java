/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.converter;

import java.io.InputStream;

/**
 * @author pablo-moreira
 */
public class HocrDocumentItem {
    
    private InputStream hocrInputStream;
    private InputStream imageInputStream;

    public HocrDocumentItem(InputStream hocrInputStream, InputStream imageInputStream) {
        this.hocrInputStream = hocrInputStream;
        this.imageInputStream = imageInputStream;
    }

    public InputStream getHocrInputStream() {
        return hocrInputStream;
    }

    public void setHocrInputStream(InputStream hocrInputStream) {
        this.hocrInputStream = hocrInputStream;
    }

    public InputStream getImageInputStream() {
        return imageInputStream;
    }

    public void setImageInputStream(InputStream imageInputStream) {
        this.imageInputStream = imageInputStream;
    }
}
