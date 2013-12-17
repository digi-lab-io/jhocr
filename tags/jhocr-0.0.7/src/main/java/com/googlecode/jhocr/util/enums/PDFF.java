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
package com.googlecode.jhocr.util.enums;

import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO: add support for PDF_A_1A, PDF_A_2A, PDF_A_3A,
 * Supported PDF formats JHOCR.
 * 
 */
public enum PDFF {

	PDF_A_3U(PdfAConformanceLevel.PDF_A_3U) {
		@Override
		public Object getValue() {
			return PdfAConformanceLevel.PDF_A_3U;
		};
	},
	PDF_A_3B(PdfAConformanceLevel.PDF_A_3B) {
		@Override
		public Object getValue() {
			return PdfAConformanceLevel.PDF_A_3B;
		};
	},
	PDF_A_2U(PdfAConformanceLevel.PDF_A_2U) {
		@Override
		public Object getValue() {
			return PdfAConformanceLevel.PDF_A_2U;
		};
	},
	PDF_A_2B(PdfAConformanceLevel.PDF_A_2B) {
		@Override
		public Object getValue() {
			return PdfAConformanceLevel.PDF_A_2B;
		};
	},
	PDF_A_1B(PdfAConformanceLevel.PDF_A_1B) {
		@Override
		public Object getValue() {
			return PdfAConformanceLevel.PDF_A_1B;
		};
	},
	PDFX1A2001(PdfWriter.PDFX1A2001) {
		@Override
		public Object getValue() {
			return PdfWriter.PDFX1A2001;
		};
	},
	PDFX32002(PdfWriter.PDFX32002) {
		@Override
		public Object getValue() {
			return PdfWriter.PDFX32002;
		};
	};

	private Object	obj;

	private PDFF(PdfAConformanceLevel cl) {
		this.obj = cl;
	}

	private PDFF(int i) {
		this.obj = i;
	}

	public Object getValue() {
		return this.obj;
	}

}
