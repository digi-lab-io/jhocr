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

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.parser.HocrParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO add documentation
 * 
 */
public class HocrToPdf {

	/**
	 * TODO add documentation, for example why exactly 72.0f
	 */
	public static float						POINTS_PER_INCH	= 72.0f;
	private OutputStream					outputStream;
	private List<HocrDocumentItem>			items			= new ArrayList<HocrDocumentItem>();
	private List<HashMap<String, Object>>	outlines		= new ArrayList<HashMap<String, Object>>();
	private boolean							useImageDpi		= true;
	private static Logger					log				= Logger.getLogger(HocrToPdf.class);

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
	public void convert() {

		if (getItems().isEmpty()) {
			return;
		}

		Document document = new Document();
		document.setMargins(0, 0, 0, 0);

		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, getOutputStream());

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
						pageProcessor.process(document, pdfWriter);
					}
				}
			}

			if (!outlines.isEmpty()) {
				pdfWriter.setOutlines(outlines);
			}

			/**
			 * Closing the document body stream.
			 */
		} catch (DocumentException e) {
			log.error(e);
			document.close();
		} catch (UnsupportedOperationException e) {
			log.error(e);
		}

		finally {
			document.close();
		}
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
}
