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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jhocr.element.HocrLine;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.element.HocrWord;
import com.googlecode.jhocr.util.LoggUtilException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO add documentation
 * Compliant Solution (initialized flag)
 * 
 * @see "https://www.securecoding.cert.org/confluence/display/java/OBJ11-J.+Be+wary+of+letting+constructors+throw+exceptions"
 * 
 */
public class HocrPageProcessor {

	/**
	 * TODO add documentation, for example why exactly 300
	 */
	private static final int	DPI_DEFAULT	= 300;

	private final static Logger	logger		= LoggerFactory.getLogger(new LoggUtilException().toString());

	private final HocrPage		hocrPage;
	private float				dotsPerPointX;
	private float				dotsPerPointY;
	private Image				image;
	private Rectangle			imageRectangle;
	private final boolean		useImageDpi;
	private volatile boolean	initialized	= false;
	private Font				font		= null;
	private BaseFont			baseFont	= null;

	/**
	 * 
	 * @param hocrPage
	 *            this object will be processed in this class.
	 * @param imageInputStream
	 *            of the image.
	 * 
	 * @param useImageDpi
	 *            determines if the dpi of the image should be used.
	 */
	public HocrPageProcessor(HocrPage hocrPage, InputStream imageInputStream, boolean useImageDpi) {

		this.hocrPage = hocrPage;
		this.useImageDpi = useImageDpi;

		if (init(imageInputStream)) {
			/**
			 * object construction successful
			 */
			this.initialized = true;

		} else {

			this.initialized = false;
		}

	}

	/**
	 * TODO describe this method.
	 * 
	 * @param imageInputStream
	 *            of the image.
	 * @return true if the initialisation was successful.
	 */
	private boolean init(InputStream imageInputStream) {

		boolean result = false;

		try {

			/**
			 * fontname the name of the font
			 * encoding the encoding of the font
			 * embedded true if the font is to be embedded in the PDF
			 * size the size of this font
			 * style the style of this font
			 * color the BaseColor of this font.
			 */
			font = FontFactory.getFont("/fonts/Sansation_Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, BaseColor.BLACK);
			baseFont = font.getBaseFont();
			logger.trace("Loaded font: '{}'.", baseFont.getPostscriptFontName());

			byte[] bytes = new byte[imageInputStream.available()];

			imageInputStream.read(bytes);

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
			 * TODO simplify this line, too many arguments.
			 */
			this.imageRectangle = new Rectangle(getHocrPage().getBbox().getWidth() / getDotsPerPointX(), getHocrPage().getBbox().getHeight() / getDotsPerPointY());

			result = true;

		} catch (DocumentException e) {
			logger.error("Error while processing the document, please check th elog for more information.", e);
			result = false;
		} catch (IOException e) {
			logger.error("Error while processing the document, please check th elog for more information.", e);
			result = false;
		}

		return result;

	}

	/**
	 * @return the {@link #hocrPage} object.
	 */
	public HocrPage getHocrPage() {
		return hocrPage;
	}

	/**
	 * 
	 * @return the {@link #image} object.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * 
	 * @return the {@link #dotsPerPointX} value.
	 */
	public float getDotsPerPointX() {
		return dotsPerPointX;
	}

	/**
	 * @return the {@link #dotsPerPointY} value.
	 */
	public float getDotsPerPointY() {
		return dotsPerPointY;
	}

	/**
	 * @return the {@link #imageRectangle} object.
	 */
	public Rectangle getImageRectangle() {
		return imageRectangle;
	}

	/**
	 * This method will process the {@link com.itextpdf.text.Document} fitting the image into the documents page.
	 * 
	 * @param document
	 *            will be processed.
	 * @param pdfWriter
	 *            will be used to process the {@link com.itextpdf.text.Document}
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
			logger.error("Document could not be processed.", e);
			return false;
		}
	}

	/**
	 * TODO describe this method.
	 * 
	 * @param cb
	 *            here the character spacing will be processed.
	 * @param hocrWord
	 *            will be used to process the {@link PdfContentByte}.
	 * @param wordWidthPt
	 *            will be used to process the {@link PdfContentByte}.
	 */
	private static void processHocrWordCharacterSpacing(PdfContentByte cb, HocrWord hocrWord, float wordWidthPt) {

		float charSpacing = 0;
		cb.setCharacterSpacing(charSpacing);

		float textWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);

		logger.debug("hocrWord: " + hocrWord.getId());
		logger.debug("box width: " + wordWidthPt);
		logger.debug("text width: " + textWidthPt);

		if (textWidthPt > wordWidthPt) {
			while (textWidthPt > wordWidthPt) {
				charSpacing -= 0.05f;
				cb.setCharacterSpacing(charSpacing);
				float newTextWidthPt = cb.getEffectiveStringWidth(hocrWord.getText(), false);
				// !!! deadlock
				/**
				 * TODO {@author Pablo} please confirm &/ describe the bug (deadlock)
				 */
				if (newTextWidthPt == textWidthPt || charSpacing > -0.5f) {
					break;
				} else {
					textWidthPt = newTextWidthPt;
				}
			}
		}

	}

	/**
	 * TODO add documentation describing what this method does actually do.
	 * 
	 * @see https://code.google.com/p/jhocr/issues/detail?id=4
	 * @param cb
	 *            TODO describe this parameter &/ it's purpose.
	 * @param hocrLine
	 *            is used to process the {@link com.itextpdf.text.pdf.PdfContentByte}
	 * @throws IOException
	 * @throws DocumentException
	 */
	private void processHocrLine(PdfContentByte cb, HocrLine hocrLine) {

		try {
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

				cb.setFontAndSize(baseFont, fontSize);
				cb.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
				cb.beginText();

				int t = hocrLine.getWords().size();

				/**
				 * TODO add documentation
				 * 
				 * @see <a>https://code.google.com/p/jhocr/issues/detail?id=4</a>
				 */
				for (int i = 0; i < t; i++) {

					HocrWord hocrWord = hocrLine.getWords().get(i);

					float wordWidthPt = hocrWord.getBbox().getWidth() / getDotsPerPointX();

					processHocrWordCharacterSpacing(cb, hocrWord, wordWidthPt);

					float y = (getHocrPage().getBbox().getHeight() + lineHeightPt / 2 - hocrLine.getBbox().getBottom()) / dotsPerPointY;
					float x = hocrWord.getBbox().getLeft() / getDotsPerPointX();

					cb.showTextAligned(PdfContentByte.ALIGN_LEFT, hocrWord.getText(), x, y, 0);
				}

				cb.endText();

			}

		} catch (Exception e) {
			// TODO log error
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return {@link #initialized} (true) if this class was initialised successfully.
	 */
	public synchronized boolean isInitialized() {
		return initialized;
	}
}
