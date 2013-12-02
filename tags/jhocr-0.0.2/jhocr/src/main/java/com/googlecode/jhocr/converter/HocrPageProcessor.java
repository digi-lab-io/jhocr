/**
 * TODO: describe: <one line to give the program's name and a brief idea of what it does.>
 * 
 * <br>
 * Copyright (©) 2013 Pablo Filetti Moreira
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jhocr.converter;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.googlecode.jhocr.element.HocrLine;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.element.HocrWord;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class HocrPageProcessor {

	private static final int	DPI_DEFAULT	= 300;
	private static Logger		log			= Logger.getLogger(HocrPageProcessor.class);

	private HocrPage			hocrPage;
	private float				dotsPerPointX;
	private float				dotsPerPointY;
	private BaseFont			font;
	private Image				image;
	private Rectangle			imageRectangle;
	private boolean				useImageDpi;

	public HocrPageProcessor(HocrPage hocrPage, InputStream imageInputStream, boolean useImageDpi) throws Exception {
		this.hocrPage = hocrPage;
		this.useImageDpi = useImageDpi;
		init(imageInputStream);
	}

	private void init(InputStream iis) throws Exception {

		font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

		byte[] bytes = new byte[iis.available()];

		iis.read(bytes);

		this.image = Image.getInstance(bytes);

		int dpiX, dpiY;

		if (useImageDpi) {
			dpiX = getImage().getDpiX();
			if (dpiX == 0) {
				dpiX = DPI_DEFAULT;
			}
			dpiY = getImage().getDpiY();
			if (dpiY == 0) {
				dpiY = DPI_DEFAULT;
			}
		} else {
			dpiX = DPI_DEFAULT;
			dpiY = DPI_DEFAULT;
		}

		this.dotsPerPointX = dpiX / HocrToPdf.POINTS_PER_INCH;
		this.dotsPerPointY = dpiY / HocrToPdf.POINTS_PER_INCH;

		this.imageRectangle = new Rectangle(getHocrPage().getBbox().getWidth() / getDotsPerPointX(), getHocrPage().getBbox().getHeight() / getDotsPerPointY());
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
		} else {
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

	private static void processHocrWordCharacterSpacing(PdfContentByte cb, HocrWord hocrWord, float wordWidthPt) {

		float charSpacing = 0;
		cb.setCharacterSpacing(charSpacing);

		float textWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);

		log.debug("hocrWord: " + hocrWord.getId());
		log.debug("box width: " + wordWidthPt);
		log.debug("text width: " + textWidthPt);

		if (textWidthPt > wordWidthPt) {
			while (textWidthPt > wordWidthPt) {
				charSpacing -= 0.05f;
				cb.setCharacterSpacing(charSpacing);
				float newTextWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);
				// !!! deadlock
				if (newTextWidthPt == textWidthPt || charSpacing > -0.5f) {
					break;
				} else {
					textWidthPt = newTextWidthPt;
				}
			}
		}
		/*
		 * else {
		 * while (wordWidthPt > textWidthPt) {
		 * charSpacing += 0.1f;
		 * cb.setCharacterSpacing(charSpacing);
		 * float newTextWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);
		 * // !!! deadlock
		 * if (newTextWidthPt == textWidthPt || charSpacing > 0.5f) {
		 * break;
		 * }
		 * else {
		 * textWidthPt = newTextWidthPt;
		 * }
		 * }
		 * }
		 */
	}

	public BaseFont getFont() {
		return font;
	}

	private void processHocrLine(PdfContentByte cb, HocrLine hocrLine) {

		String lineText = hocrLine.getText();

		if (!lineText.isEmpty()) {

			float lineHeightPt = hocrLine.getBbox().getHeight() / getDotsPerPointY();

			float fontSize = Math.round(lineHeightPt) - 0.8f; // Coloquei para o limite de erro

			if (fontSize == 0) {
				fontSize = 0.5f;
			}

			cb.beginText();
			cb.setFontAndSize(getFont(), fontSize);
			cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);

			int t = hocrLine.getWords().size();

			for (int i = 0; i < t; i++) {

				HocrWord hocrWord = hocrLine.getWords().get(i);

				float wordWidthPt = hocrWord.getBbox().getWidth() / getDotsPerPointX();

				processHocrWordCharacterSpacing(cb, hocrWord, wordWidthPt);

				float y = (getHocrPage().getBbox().getHeight() + (lineHeightPt / 2) - hocrLine.getBbox().getBottom()) / dotsPerPointY;
				float x = hocrWord.getBbox().getLeft() / getDotsPerPointX();

				cb.showTextAligned(PdfContentByte.ALIGN_LEFT, hocrWord.getText(), x, y, 0);
			}
			cb.endText();
		}
	}
}
