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

package com.googlecode.jhocr;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.jhocr.converter.HocrToPdf;
import com.googlecode.jhocr.element.HocrDocument;
import com.googlecode.jhocr.parser.HocrParser;
import com.googlecode.jhocr.util.JHOCRUtil;
import com.googlecode.jhocr.util.LoggUtilException;
import com.googlecode.jhocr.util.UtilRunCmd;
import com.googlecode.jhocr.util.enums.FExt;
import com.googlecode.jhocr.util.enums.PDFF;

/**
 * TODO implement here all tests around jhocr and the tesseract binaries.
 * For more information, visit: <li>https://code.google.com/p/jhocr/wiki/HowToUse</li>
 * 
 */
@RunWith(value = Parameterized.class)
public class TestJHOCR {

	/**
	 * 
	 * @param testFileName
	 *            without the file extension.
	 */
	public TestJHOCR(String testFileName) {
		this.testFileName = testFileName;
	}

	/**
	 * This will pass the constructor one value at a time for each test.
	 * 
	 * @return testFileName
	 */
	@Parameters
	public static Collection<String[]> data() {
		String[][] data = new String[][] { { "eurotext_tesseract_bin" }, { "eurotext" }, { "phototest" }, { "eurotext_tess4j" } };
		return Arrays.asList(data);
	}

	private String				testFileName		= "";
	private static String		testFileResultsPath	= "src/test/resources/test-results";
	private static String		testFilesSrcPath	= "src/test/resources/test-data";

	private final static Logger	logger				= LoggerFactory.getLogger(new LoggUtilException().toString());

	/**
	 * Test setup and preparation.
	 * TODO delete all files inside the <code>testFileResultsPath</code>.
	 * 
	 */
	@Before
	public void setUp() {

	}

	/**
	 * This method will be called whenever a test method is entered, helping to log the information and to know which test is currently running.
	 */
	@Rule
	public MethodRule	watchman	= new TestWatchman() {
										@Override
										public void starting(FrameworkMethod method) {
											logger.info("* {} being run for '{}'.", method.getName(), testFileName);
										}
									};

	/**
	 * 1. This will generate the hocr files with the tesseract-bin (Linux, OSX and Windows).<br>
	 * 2. The generate / converted PDF will be searched for an known string, to test if the PDF is searchable or not.
	 * 
	 * @param testFileName
	 *            without the file extension.
	 */
	@Test
	public void testRuntimeGeneratedFiles() {

		File htmlOcrAbsFileName = new File(testFileResultsPath, String.format("%s.%s", testFileName, FExt.HTML));
		File imageAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.TIF));
		File pdfAbsFileName = new File(testFileResultsPath, String.format("%s.%s", testFileName, FExt.PDF));

		String word = "a";

		try {

			String cmd = "";
			UtilRunCmd exe = new UtilRunCmd();
			String tesseractBin = "";

			if (JHOCRUtil.getInstance().isWindows()) {

				tesseractBin = "tesseract";

			} else if (JHOCRUtil.getInstance().isMac()) {

				tesseractBin = "/usr/local/bin/tesseract";

			} else if (JHOCRUtil.getInstance().isUnix()) {
				throw new UnsupportedOperationException();
			} else if (JHOCRUtil.getInstance().isSolaris()) {
				throw new UnsupportedOperationException();
			} else {
				throw new UnsupportedOperationException();
			}

			cmd = MessageFormat.format("{0} {1} {2} hocr", tesseractBin, imageAbsFileName.getAbsoluteFile(), htmlOcrAbsFileName.getAbsolutePath().replaceAll(".html", ""));
			logger.info("Console cmd is: '{}'.", cmd);

			if (exe.run(cmd) == -1) {
				fail(String.format("Error while executing the command: ({0}).", cmd));
			}

			FileOutputStream os = new FileOutputStream(pdfAbsFileName);

			HocrToPdf hocrToPdf = new HocrToPdf(os);
			hocrToPdf.addHocrDocument(new FileInputStream(htmlOcrAbsFileName), new FileInputStream(imageAbsFileName));
			hocrToPdf.setPdfFormat(PDFF.PDF_A_3B);
			hocrToPdf.convert();

			os.close();

			/**
			 * TODO describe the exceptions
			 */
		} catch (IOException e) {
			String errMsg = "IO Error, please check if all source files are accessible or does exists at all.";
			logger.error(errMsg, e);
		} catch (UnsupportedOperationException e) {
			String errMsg = "Your OS is not yet support!!";
			logger.error(errMsg, e);
		}

		if (!JHOCRUtil.getInstance().testGeneratedPDF(word, pdfAbsFileName.getAbsolutePath())) {
			fail("The PDF is not searchable, see the log for more information.");
		}
	}

	/**
	 * This will test the manual generated test-files from an tesseract-bin.
	 * 
	 * @param testFileName
	 *            without the file extension.
	 */
	@Test
	public void testManualGeneratedTestFiles() {

		File htmlOcrAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.HTML));
		File imageAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.TIF));
		File pdfAbsFileName = new File(testFileResultsPath, String.format("%s.%s", testFileName, FExt.PDF));

		String word = "a";

		try {
			FileOutputStream os = new FileOutputStream(pdfAbsFileName);

			HocrToPdf hocrToPdf = new HocrToPdf(os);
			hocrToPdf.addHocrDocument(new FileInputStream(htmlOcrAbsFileName), new FileInputStream(imageAbsFileName));
			hocrToPdf.setPdfFormat(PDFF.PDF_A_3U);
			hocrToPdf.convert();
			os.close();

			if (!JHOCRUtil.getInstance().testGeneratedPDF(word, pdfAbsFileName.getAbsolutePath())) {
				fail("The PDF is not searchable, see the log for more information.");
			}

		} catch (IOException e) {
			logger.error("I/O error, please check the log for more information.", e);
		}

	}

	/**
	 * TODO re-implement this test, printing it out is pretty ugly and does not test anything.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void testParser() throws FileNotFoundException {
		try {

			File htmlOcrAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.HTML));
			HocrParser parser = new HocrParser(new FileInputStream(htmlOcrAbsFileName));

			HocrDocument document = parser.parse();
			logger.info("TODO re-implement this test, printing it out is pretty ugly and does not test anything.");

			// String pre = "-- ";
			// for (HocrPage page : document.getPages()) {
			// System.out.println(pre + page);
			// for (HocrCarea carea : page.getCareas()) {
			// System.out.println(pre + pre + carea);
			// for (HocrParagraph paragraph : carea.getParagraphs()) {
			// System.out.println(pre + pre + pre + paragraph);
			// for (HocrLine line : paragraph.getLines()) {
			// System.out.println(pre + pre + pre + pre + line);
			// for (HocrWord word : line.getWords()) {
			// System.out.println(pre + pre + pre + pre + pre + word);
			// }
			// }
			// }
			// }
			// }

		} catch (FileNotFoundException e) {
			logger.error("It seams, that the file does not exists or were delete before accessing it, check the log for more information.", e);

		}
	}

}
