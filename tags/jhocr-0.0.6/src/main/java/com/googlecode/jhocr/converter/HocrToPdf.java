/**
 * Copyright (©) 2013 Pablo Filetti Moreira & O.J. Sousa Rodrigues
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.parser.HocrParser;
import com.googlecode.jhocr.util.enums.PDFF;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO add documentation
 * TODO improve the way the information is beeing passed to the document e.g.: com-googlecode-jhocr-info
 * TODO add documentation, for example why exactly 72.0f
 * 
 */
public class HocrToPdf {
	public static float						POINTS_PER_INCH			= 72.0f;
	private OutputStream					outputStream;
	private List<HocrDocumentItem>			items					= new ArrayList<HocrDocumentItem>();
	private List<HashMap<String, Object>>	outlines				= new ArrayList<HashMap<String, Object>>();
	private boolean							useImageDpi				= true;
	private PDFF							pdfFormat				= null;

	private static Logger					log						= Logger.getLogger(HocrToPdf.class);

	private static final String				KEY_JHOCR_INFO			= "com-googlecode-jhocr-info";
	private static final String				KEY_JHOCR_INFO_VALUE	= "This document were generated with jhocr, for more information visit: https://code.google.com/p/jhocr";

	/**
	 * TODO add documentation
	 * 
	 * @param outputStream
	 */
	public HocrToPdf(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HocrDocumentItem> getItems() {
		return items;
	}

	/**
	 * TODO add documentation
	 * 
	 * @throws DocumentException
	 */
	public boolean convert() {
		boolean result = false;

		if (!getItems().isEmpty() && getItems() != null) {

			PDFF pdff = getPdfFormat();

			if (pdff != null) {

				if (pdff.getValue() instanceof Integer) {

					result = convertToPDFX((Integer) pdff.getValue());

				} else if (pdff instanceof PDFF) {

					result = convertToPDFA((PdfAConformanceLevel) pdff.getValue());

				}

			} else {
				result = convertSimple();
			}

		}

		return result;

	}

	private boolean convertSimple() {
		boolean result = false;

		Document document = new Document();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, getOutputStream());

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);

			/**
			 * TODO add documentation
			 */
			for (HocrDocumentItem item : getItems()) {

				HocrParser parser = new HocrParser(item.getHocrInputStream());

				HocrDocument hocrDocument = parser.parse();

				/**
				 * TODO add documentation
				 * TODO add multipage image support
				 */
				if (hocrDocument.getPages().size() > 1) {
					throw new UnsupportedOperationException("Multipage tif are not yet implemented, please report: http://code.google.com/p/jhocr/issues/list");
				}

				/**
				 * TODO add documentation
				 */
				for (HocrPage hocrPage : hocrDocument.getPages()) {
					HocrPageProcessor pageProcessor = new HocrPageProcessor(hocrPage, item.getImageInputStream(), isUseImageDpi());

					if (pageProcessor.isInitialized()) {
						pageProcessor.process(document, writer);
					}
				}
			}

			if (!outlines.isEmpty()) {
				writer.setOutlines(outlines);
			}

			/**
			 * Closing the document body stream.
			 */
			document.close();
			getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (DocumentException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (IOException e) {
			document.close();
			log.error(e);
			result = false;
		}

		return result;
	}

	/**
	 * 
	 * @param xConformance
	 * @return
	 */
	private boolean convertToPDFX(int xConformance) {
		boolean result = false;
		Document document = new Document();

		try {
			PdfWriter writer = PdfWriter.getInstance(document, getOutputStream());
			writer.setPDFXConformance(xConformance);

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);

			/**
			 * TODO add documentation
			 */
			for (HocrDocumentItem item : getItems()) {

				HocrParser parser = new HocrParser(item.getHocrInputStream());

				HocrDocument hocrDocument = parser.parse();

				/**
				 * TODO add documentation
				 * TODO add multipage image support
				 */
				if (hocrDocument.getPages().size() > 1) {
					throw new UnsupportedOperationException("Multipage tif are not yet implemented, please report: http://code.google.com/p/jhocr/issues/list");
				}

				/**
				 * TODO add documentation
				 */
				for (HocrPage hocrPage : hocrDocument.getPages()) {
					HocrPageProcessor pageProcessor = new HocrPageProcessor(hocrPage, item.getImageInputStream(), isUseImageDpi());

					if (pageProcessor.isInitialized()) {
						pageProcessor.process(document, writer);
					}
				}
			}

			if (!outlines.isEmpty()) {
				writer.setOutlines(outlines);
			}

			/**
			 * Closing the document body stream.
			 */
			document.close();
			getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			result = false;
			log.error(e);
		} catch (DocumentException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (IOException e) {
			document.close();
			log.error(e);
			result = false;
		}

		return result;

	}

	/**
	 * 
	 * @param pdff
	 * @return
	 */
	private boolean convertToPDFA(PdfAConformanceLevel pdff) {
		boolean result = false;
		Document document = new Document();
		String profile = "src/main/resources/sRGB.profile";

		try {
			PdfAWriter writer = PdfAWriter.getInstance(document, getOutputStream(), PdfAConformanceLevel.PDF_A_1B);
			writer.createXmpMetadata();

			document.open();
			document.addHeader(KEY_JHOCR_INFO, KEY_JHOCR_INFO_VALUE);
			document.setMargins(0, 0, 0, 0);

			/**
			 * TODO add documentation
			 */
			for (HocrDocumentItem item : getItems()) {

				HocrParser parser = new HocrParser(item.getHocrInputStream());

				HocrDocument hocrDocument = parser.parse();

				/**
				 * TODO add documentation
				 * TODO add multipage image support
				 */
				if (hocrDocument.getPages().size() > 1) {
					throw new UnsupportedOperationException("Multipage tif are not yet implemented, please report: http://code.google.com/p/jhocr/issues/list");
				}

				/**
				 * TODO add documentation
				 */
				for (HocrPage hocrPage : hocrDocument.getPages()) {
					HocrPageProcessor pageProcessor = new HocrPageProcessor(hocrPage, item.getImageInputStream(), isUseImageDpi());

					if (pageProcessor.isInitialized()) {
						pageProcessor.process(document, writer);
					}
				}
			}

			if (!outlines.isEmpty()) {
				writer.setOutlines(outlines);
			}

			ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream(profile));
			writer.setOutputIntents("JHOCR", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

			/**
			 * Closing the document body stream.
			 */
			document.close();
			getOutputStream().close();
			result = true;

		} catch (UnsupportedOperationException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (DocumentException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (FileNotFoundException e) {
			document.close();
			log.error(e);
			result = false;
		} catch (IOException e) {
			document.close();
			log.error(e);
			result = false;
		}

		return result;

	}

	/**
	 * TODO add documentation
	 * 
	 * @param hocrInputStream
	 *            Html-OCR (HOCR) InputStream
	 * @param imgInputStream
	 *            Image InputStream, this image will be used to create the pdf searchable
	 */
	public void addHocrDocument(InputStream hocrInputStream, InputStream imgInputStream) {
		this.items.add(new HocrDocumentItem(hocrInputStream, imgInputStream));
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public List<HashMap<String, Object>> getOutlines() {
		return outlines;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param outlines
	 */
	public void setOutlines(List<HashMap<String, Object>> outlines) {
		this.outlines = outlines;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param outline
	 */
	public void addOutline(HashMap<String, Object> outline) {
		getOutlines().add(outline);
	}

	/**
	 * TODO add documentation
	 * 
	 * @return
	 */
	public boolean isUseImageDpi() {
		return useImageDpi;
	}

	/**
	 * TODO add documentation
	 * 
	 * @param useImageDpi
	 */
	public void setUseImageDpi(boolean useImageDpi) {
		this.useImageDpi = useImageDpi;
	}

	/**
	 * 
	 * @return the PDF format that was set for the current document.
	 */
	public PDFF getPdfFormat() {
		return pdfFormat;
	}

	/**
	 * The PDF can be converted to a certain format, you can find all current supported and tested formats in the <code>PDFF.java</code> class.
	 * 
	 * @param pdfFormat
	 *            sets the PDF format for the current document to be converted.
	 */
	public void setPdfFormat(PDFF pdfFormat) {
		this.pdfFormat = pdfFormat;
	}

}
