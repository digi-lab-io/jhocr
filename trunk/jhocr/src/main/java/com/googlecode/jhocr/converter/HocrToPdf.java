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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.element.HocrPage;
import com.googlecode.jhocr.parser.HocrParser;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class HocrToPdf {

	public static float						POINTS_PER_INCH	= 72.0f;

	private OutputStream					outputStream;
	private List<HocrDocumentItem>			items			= new ArrayList<HocrDocumentItem>();
	private List<HashMap<String, Object>>	outlines		= new ArrayList<HashMap<String, Object>>();
	private boolean							useImageDpi		= true;

	public HocrToPdf(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public List<HocrDocumentItem> getItems() {
		return items;
	}

	public void convert() throws Exception {

		if (getItems().isEmpty()) {
			return;
		}

		Document pdfDocument = new Document();
		pdfDocument.setMargins(0, 0, 0, 0);

		PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, getOutputStream());

		for (HocrDocumentItem item : getItems()) {

			HocrParser parser = new HocrParser(item.getHocrInputStream());

			HocrDocument hocrDocument = parser.parse();

			/*
			 * TODO add multipage image support
			 */
			if (hocrDocument.getPages().size() > 1) {
				throw new Exception("Não foi implementado!");
			}

			for (HocrPage hocrPage : hocrDocument.getPages()) {
				HocrPageProcessor pageProcessor = new HocrPageProcessor(hocrPage, item.getImageInputStream(), isUseImageDpi());
				pageProcessor.process(pdfDocument, pdfWriter);
			}
		}

		if (!outlines.isEmpty()) {
			pdfWriter.setOutlines(outlines);
		}

		pdfDocument.close();
	}

	public void addHocrDocument(InputStream hocrInputStream, InputStream imgInputStream) {
		this.items.add(new HocrDocumentItem(hocrInputStream, imgInputStream));
	}

	public List<HashMap<String, Object>> getOutlines() {
		return outlines;
	}

	public void setOutlines(List<HashMap<String, Object>> outlines) {
		this.outlines = outlines;
	}

	public void addOutline(HashMap<String, Object> outline) {
		getOutlines().add(outline);
	}

	public boolean isUseImageDpi() {
		return useImageDpi;
	}

	public void setUseImageDpi(boolean useImageDpi) {
		this.useImageDpi = useImageDpi;
	}
}
