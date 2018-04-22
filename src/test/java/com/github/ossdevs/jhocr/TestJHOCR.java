/**
 * Copyright (©) 2013 Pablo Filetti Moreira & O.J. Sousa Rodrigues
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.ossdevs.jhocr;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ossdevs.jhocr.converter.HocrToPdf;
import com.github.ossdevs.jhocr.element.HocrCarea;
import com.github.ossdevs.jhocr.element.HocrDocument;
import com.github.ossdevs.jhocr.element.HocrLine;
import com.github.ossdevs.jhocr.element.HocrPage;
import com.github.ossdevs.jhocr.element.HocrParagraph;
import com.github.ossdevs.jhocr.element.HocrWord;
import com.github.ossdevs.jhocr.parser.HocrParser;
import com.github.ossdevs.jhocr.util.JHOCRUtil;
import com.github.ossdevs.jhocr.util.LoggUtilException;
import com.github.ossdevs.jhocr.util.enums.FExt;
import com.github.ossdevs.jhocr.util.enums.PDFF;

/**
 * TODO implement here all tests around jhocr and the tesseract binaries. For
 * more information, visit:
 * <li>https://code.google.com/p/jhocr/wiki/HowToUse</li>
 */
@RunWith(value = Parameterized.class)
public class TestJHOCR {

    private final static Logger logger = LoggerFactory.getLogger(new LoggUtilException().toString());
    private static String testFileResultsPath = "src/test/resources/test-results";
    private static String testFilesSrcPath = "src/test/resources/test-data";
    private String testFileName = "";
    /**
     * This method will be called whenever a test method is entered, helping to log
     * the information and to know which test is currently running.
     */
    @Rule
    public TestWatcher watchman = new TestWatcher() {
	@Override
	protected void starting(Description description) {
	    logger.info("* {} being run for '{}'.", description.getMethodName(), testFileName);
	}

    };

    /**
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
	String[][] data = new String[][] { { "eurotext" }, { "phototest" }, { "eurotext_tess4j" } };
	return Arrays.asList(data);
    }

    /**
     * Test setup and preparation. TODO delete all files inside the
     * <code>testFileResultsPath</code>.
     */
    @Before
    public void setUp() {

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
     * TODO re-implement this test, printing it out is pretty ugly and does not test
     * anything.
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

	    String pre = "-- ";
	    for (HocrPage page : document.getPages()) {
		logger.info(pre + page);
		for (HocrCarea carea : page.getCareas()) {
		    logger.info(pre + pre + carea);
		    for (HocrParagraph paragraph : carea.getParagraphs()) {
			logger.info(pre + pre + pre + paragraph);
			for (HocrLine line : paragraph.getLines()) {
			    logger.info(pre + pre + pre + pre + line);
			    for (HocrWord word : line.getWords()) {
				logger.info(pre + pre + pre + pre + pre + word);
			    }
			}
		    }
		}
	    }

	} catch (FileNotFoundException e) {
	    logger.error(
		    "It seams, that the file does not exists or were delete before accessing it, check the log for more information.",
		    e);

	}
    }

}
