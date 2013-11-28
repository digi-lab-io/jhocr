package com.googlecode.jhocr.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

public class HocrToPdfTest {

     public static void main(String args[]) throws Exception {
    	
        File pdf = new File(System.getProperty("user.home") + "/Desktop/", "hocr.pdf");
        
        FileOutputStream os = new FileOutputStream(pdf);
        
        HocrToPdf hocrToPdf = new HocrToPdf(os);        

        hocrToPdf.addHocrDocument(
            new FileInputStream(System.getProperty("user.home") + "/Desktop/img.jpg.html"),
            new FileInputStream(System.getProperty("user.home") + "/Desktop/img.jpg")
        );
        
        HashMap<String, Object> bookmark1 = new HashMap<String, Object>();

        bookmark1.put("Title", "Bookmark 1");
        bookmark1.put("Action", "GoTo");
        bookmark1.put("Page", String.format("%d Fit", 1));
        
        hocrToPdf.addOutline(bookmark1);
                
        hocrToPdf.convert();
        
        os.close();
    }
}