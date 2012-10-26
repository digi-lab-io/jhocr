package com.googlecode.jhocr.converter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import junit.framework.TestCase;


public class HocrToPdfTest extends TestCase {

    public void testParser() throws Exception {
    	
        File pdf = new File(System.getProperty("user.home"), "hocr.pdf");
        
        FileOutputStream os = new FileOutputStream(pdf);
        
        HocrToPdf hocrToPdf = new HocrToPdf(os);        

        hocrToPdf.addHocrDocument(
            getClass().getResourceAsStream("../img.png.html"),
            getClass().getResourceAsStream("../img.png")
        );
        hocrToPdf.addHocrDocument(
            getClass().getResourceAsStream("../img.png.html"),
            getClass().getResourceAsStream("../img.png")
        );
        
        HashMap<String, Object> bookmark1 = new HashMap<String, Object>();

        bookmark1.put("Title", "Bookmark 1");
        bookmark1.put("Action", "GoTo");
        bookmark1.put("Page", String.format("%d Fit", 1));
        
        hocrToPdf.addOutline(bookmark1);
        
        HashMap<String, Object> bookmark2 = new HashMap<String, Object>();

        bookmark2.put("Title", "Bookmark 2");
        bookmark2.put("Action", "GoTo");
        bookmark2.put("Page", String.format("%d Fit", 2));
        
        hocrToPdf.addOutline(bookmark2);
        
        hocrToPdf.convert();
        
        os.close();
        
        assertTrue( true );
    }
}
