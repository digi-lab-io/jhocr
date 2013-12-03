/**
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

import java.io.IOException;
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

/**
 * TODO add documentation
 * 
 */

public class HocrPageProcessor {

	/**
	 * TODO add documentation, for example why exactly 300
	 */
	private static final int	DPI_DEFAULT	= 300;
	private static Logger		log			= Logger.getLogger(HocrPageProcessor.class);

	private HocrPage			hocrPage;
	private float				dotsPerPointX;
	private float				dotsPerPointY;
	private BaseFont			font;
	private Image				image;
	private Rectangle			imageRectangle;
	private boolean				useImageDpi;
	private volatile boolean	initialized	= false;

	/**
	 * TODO add documentation
	 * 
	 * @param hocrPage
	 * @param imageInputStream
	 * @param useImageDpi
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	public HocrPageProcessor(HocrPage hocrPage, InputStream imageInputStream, boolean useImageDpi) {
		this.hocrPage = hocrPage;
		this.useImageDpi = useImageDpi;

		try {
			init(imageInputStream);
			/**
			 * object construction successful
			 */
			this.initialized = true;

		} catch (DocumentException e) {
			log.error("object construction unsuccessful.", e);
		} catch (IOException e) {
			log.error("object construction unsuccessful.", e);
		}
	}

	/**
	 * TODO add documentation
	 * 
	 * @param iis
	 * @throws IOException
	 * @throws DocumentException
	 * @throws Exception
	 */
	private void init(InputStream iis) throws DocumentException, IOException {

		font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

		byte[] bytes = new byte[iis.available()];

		iis.read(bytes);

		this.image = Image.getInstance(bytes);

		int dpiX, dpiY;

		/**
		 * TODO add documentation, for example what if and what else
		 */
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

		/**
		 * TODO add documentation
		 */
		this.imageRectangle = new Rectangle(getHocrPage().getBbox().getWidth() / getDotsPerPointX(), getHocrPage().getBbox().getHeight() / getDotsPerPointY());
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public HocrPage getHocrPage() {
		return hocrPage;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public float getDotsPerPointX() {
		return dotsPerPointX;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public float getDotsPerPointY() {
		return dotsPerPointY;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public Rectangle getImageRectangle() {
		return imageRectangle;
	}

	/**
	 * This method will process the document fitting the image into the documents page.
	 * 
	 * @param document
	 * @param pdfWriter
	 */
	public boolean process(Document document, PdfWriter pdfWriter) {
		try {

			if (initialized) {
				document.setPageSize(getImageRectangle());

				if (!document.isOpen()) {
					document.open();
				} else {
					document.newPage();
				}

				PdfContentByte cb = pdfWriter.getDirectContentUnder();

				/**
				 * This will fit the image into the documents page using the width and height from the image and fitting it into x0 and y0 of the page.
				 */
				getImage().scaleToFit(getImageRectangle().getWidth(), getImageRectangle().getHeight());
				getImage().setAbsolutePosition(0, 0);

				pdfWriter.getDirectContent().addImage(getImage());

				for (HocrLine hocrLine : getHocrPage().getLines()) {
					processHocrLine(cb, hocrLine);
				}
			}

			return true;
		} catch (DocumentException e) {
			log.error("Document could not be processed.", e);
			return false;
		}
	}

	/**
	 * TODO add documentation
	 * 
	 * @param cb
	 * @param hocrWord
	 * @param wordWidthPt
	 */
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

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public BaseFont getFont() {
		return font;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param cb
	 * @param hocrLine
	 */
	private void processHocrLine(PdfContentByte cb, HocrLine hocrLine) {

		String lineText = hocrLine.getText();

		/**
		 * TODO add documentation
		 */
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

			/**
			 * TODO add documentation
			 */
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

	/**
	 * Compliant Solution (initialized flag): https://www.securecoding.cert.org/confluence/display/java/OBJ11-J.+Be+wary+of+letting+constructors+throw+exceptions <br>
	 * Will return true if this class was initialised successfully.
	 * 
	 * @return
	 */
	public synchronized boolean isInitialized() {
		return initialized;
	}
}
