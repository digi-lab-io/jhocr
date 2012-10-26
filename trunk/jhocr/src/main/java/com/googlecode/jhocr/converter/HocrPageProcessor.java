/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.googlecode.jhocr.converter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.InputStream;
import com.googlecode.jhocr.element.HocrLine;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.element.HocrWord;

/**
 * @author pablo-moreira
 */
public class HocrPageProcessor {
    
    private static final int DPI_DEFAULT = 200;
    
    private HocrPage hocrPage;
    private float dotsPerPointX;
    private float dotsPerPointY;
    private BaseFont font;
    private Image image;
    private Rectangle imageRectangle;

    public HocrPageProcessor(HocrPage hocrPage, InputStream imageInputStream) throws Exception {
        this.hocrPage = hocrPage;
        init(imageInputStream);        
    }

    private void init(InputStream iis) throws Exception {
        
        font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
        
        byte [] bytes = new byte[iis.available()];
        
        iis.read(bytes);
        
        this.image = Image.getInstance(bytes);
        
        int dpiX = getImage().getDpiX();
        if (dpiX == 0) { 
            dpiX = DPI_DEFAULT;
        }
        int dpiY = getImage().getDpiY();
        if (dpiY == 0) { 
            dpiY = DPI_DEFAULT;
        }
        
        this.dotsPerPointX = dpiX / HocrToPdf.POINTS_PER_INCH;
        this.dotsPerPointY = dpiY / HocrToPdf.POINTS_PER_INCH;        
        
        this.imageRectangle = new Rectangle(getImage().getWidth() / getDotsPerPointX(), getImage().getHeight() / getDotsPerPointY());
    }
    
    public HocrPage getHocrPage() {
        return hocrPage;
    }

    public Image getImage() {
        return image;
    }

    public float getDotsPerPointX() {
        return dotsPerPointX;
    }

    public float getDotsPerPointY() {
        return dotsPerPointY;
    }      
    
    public Rectangle getImageRectangle() {
        return imageRectangle;
    }
    
    public void process(Document document, PdfWriter pdfWriter) throws DocumentException {

        document.setPageSize(getImageRectangle());
        
        if (!document.isOpen()) {
            document.open();
        }
        else {
            document.newPage();
        }
        
        PdfContentByte cb = pdfWriter.getDirectContentUnder();
        
        getImage().scaleToFit(getImageRectangle().getWidth(), getImageRectangle().getHeight());        
        getImage().setAbsolutePosition(0, 0);
        
        pdfWriter.getDirectContent().addImage(getImage());
        
        for (HocrLine hocrLine : getHocrPage().getLines()) {
            processHocrLine(cb, hocrLine);
        }
    }
    
    @SuppressWarnings("unused")
	private static void processHocrWordCharacterSpacing(PdfContentByte cb, HocrWord hocrWord, float wordWidthPt) {

        float charSpacing = 0;
        cb.setCharacterSpacing(charSpacing);

        //System.out.println("hocrWord: " + hocrWord.getId());
        //System.out.println("box width: " + wordWidthPt);
        //System.out.println("text width: " + textWidthPt);
        
        float textWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);

        if (textWidthPt > wordWidthPt) {
            while (textWidthPt > wordWidthPt) {
                charSpacing -= 0.05f;
                cb.setCharacterSpacing(charSpacing);
                float newTextWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);
                // !!! deadlock
                if (newTextWidthPt == textWidthPt || charSpacing > -0.5f) {
                    break;
                }
                else {
                    textWidthPt = newTextWidthPt;
                }
            }
        }       
        else {          
            while (wordWidthPt > textWidthPt) {
                charSpacing += 0.1f;
                cb.setCharacterSpacing(charSpacing);
                float newTextWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);
                // !!! deadlock
                if (newTextWidthPt == textWidthPt || charSpacing > 0.5f) {
                    break;
                }
                else {
                    textWidthPt = newTextWidthPt;
                }
            }
        }
    }

    public BaseFont getFont() {
        return font;
    }
    
    private void processHocrLine(PdfContentByte cb, HocrLine hocrLine) {
        
        String lineText = hocrLine.getText();
        
        if (!lineText.isEmpty()) {

            float lineHeightPt = hocrLine.getBbox().getHeight() / getDotsPerPointY();

            int fontSize = Math.round(lineHeightPt) - 1; // Coloquei menos um para o limite de erro

            if (fontSize == 0) {
                fontSize = 6;
            }

            cb.beginText();                    
            cb.setFontAndSize(getFont(), fontSize);
            cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);

            int t = hocrLine.getWords().size();

            for (int i=0; i < t; i++) {

                HocrWord hocrWord = hocrLine.getWords().get(i);

                float wordWidthPt = hocrWord.getBbox().getWidth() / getDotsPerPointX();

                // Nao melhorou muito
                //processHocrWordCharacterSpacing(cb, hocrWord, wordWidthPt);

                float y = (getImage().getHeight() + (lineHeightPt / 2) - hocrLine.getBbox().getBottom()) / dotsPerPointY;
                
                if (i==0 && t > 1) {
                    float x = hocrWord.getBbox().getLeft() / getDotsPerPointX();
                    cb.showTextAligned(PdfContentByte.ALIGN_LEFT, hocrWord.getText(), x, y, 0);
                }
                else if (i+1 == t && t > 1) {
                    float x = hocrWord.getBbox().getRight() / getDotsPerPointX();
                    cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, hocrWord.getText(), x, y, 0);
                }
                else {
                    float x = (hocrWord.getBbox().getLeft() / getDotsPerPointX()) + (wordWidthPt / 2);
                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, hocrWord.getText(), x, y, 0);
                }
            }
            cb.endText();
        }
    }
}
