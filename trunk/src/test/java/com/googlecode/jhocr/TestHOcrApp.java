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
