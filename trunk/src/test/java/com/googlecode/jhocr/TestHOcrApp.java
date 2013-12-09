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

package com.googlecode.jhocr;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;
import com.googlecode.jhocr.util.FExt;
import com.googlecode.jhocr.util.JHOCRUtil;
import com.googlecode.jhocr.util.UtilRunCmd;

public class TestHOcrApp {

	private File	IMG_FILE	= new File("src/test/resources/test-data/phototest.tif");
	private File	RESULT_PATH	= new File("src/test/resources/test-results");
	private File	HOCR_FILE	= new File(RESULT_PATH, String.format("%s.%s", "phototest.tif", FExt.HTML));
	private File	PDF_FILE	= new File(RESULT_PATH, String.format("%s.%s", "phototest.tif", FExt.PDF));

	/**
	 * This will generate the ocr html file (hocr) and merge that and image into one pdf.
	 * 
	 * @return true if the test passed.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws Exception
	 */
	@Test
	public void testConvertionFromHOCRToPDF() {

		try {

			String cmd = "";
			UtilRunCmd exe = new UtilRunCmd();

			if (JHOCRUtil.getInstance().isWindows()) {
				cmd = MessageFormat.format("tesseract {0} {1} hocr", IMG_FILE.getAbsolutePath(), RESULT_PATH.getAbsolutePath() + "/" + IMG_FILE.getName());
			} else if (JHOCRUtil.getInstance().isMac()) {

				String tesseractBin = "/usr/local/bin/tesseract";

				cmd = MessageFormat.format("{0} {1} {2} hocr", tesseractBin, IMG_FILE.getAbsolutePath(), RESULT_PATH.getAbsolutePath() + "/" + IMG_FILE.getName());
				System.out.println(cmd);

			} else if (JHOCRUtil.getInstance().isUnix()) {
				throw new UnsupportedOperationException();
			} else if (JHOCRUtil.getInstance().isSolaris()) {
				throw new UnsupportedOperationException();
			} else {
				throw new UnsupportedOperationException();
			}

			if (exe.run(cmd) == -1) {
				fail(String.format("Error while executing the command: ({0}).", cmd));
			}

			FileOutputStream os = new FileOutputStream(PDF_FILE);

			HocrToPdf hocrToPdf = new HocrToPdf(os);
			hocrToPdf.addHocrDocument(new FileInputStream(HOCR_FILE), new FileInputStream(IMG_FILE));
			hocrToPdf.convert();

			os.close();

			/**
			 * TODO describe the exceptions
			 */
		} catch (IOException e) {
			fail(e.toString());
		} catch (UnsupportedOperationException e) {
			System.out.println("Your OS is not yet support!!");
			fail(e.toString());
		}
	}
}
