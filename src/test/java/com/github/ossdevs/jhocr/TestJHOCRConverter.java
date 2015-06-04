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

import com.github.ossdevs.jhocr.converter.HocrToPdf;
import com.github.ossdevs.jhocr.util.JHOCRUtil;
import com.github.ossdevs.jhocr.util.LoggUtilException;
import com.github.ossdevs.jhocr.util.enums.FExt;
import com.github.ossdevs.jhocr.util.enums.PDFF;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.fail;

/**
 * TODO add documentation
 * TODO implement tests for all supported formats
 */
@RunWith(value = Parameterized.class)
public class TestJHOCRConverter {

    private final static Logger logger = LoggerFactory.getLogger(new LoggUtilException().toString());
    private static String testFileResultsPath = "src/test/resources/test-results";
    private static String testFilesSrcPath = "src/test/resources/test-data";
    private String testFileName = "";

    /**
     * @param testFileName without the file extension.
     */
    public TestJHOCRConverter(String testFileName) {
        this.testFileName = testFileName;
    }

    @Parameters
    public static Collection<String[]> data() {
        String[][] data = new String[][]{{"eurotext"}, {"phototest"}, {"eurotext_tess4j"}};
        return Arrays.asList(data);
    }

    /**
     * TODO add documentation
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        String packageFolder = "src/test/resources/test-results";
        String fileName = "tess4j-jhocr-pdf.pdf";

    }

    /**
     * TODO add documentation
     * TODO re-implemt this test to not only create an PDF with metadata such as bookmarks, but also to test against it if those were really added to the PDF.
     *
     * @throws FileNotFoundException
     * @throws Throwable
     */
    @Test
    public void test() {

        File htmlOcrAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.HTML));
        File imageAbsFileName = new File(testFilesSrcPath, String.format("%s.%s", testFileName, FExt.TIF));
        File pdfAbsFileName = new File(testFileResultsPath, String.format("%s.%s", testFileName, FExt.PDF));

        String word = "a";

        try {
            FileOutputStream os = new FileOutputStream(pdfAbsFileName);

            HocrToPdf hocrToPdf = new HocrToPdf(os);
            hocrToPdf.addHocrDocument(new FileInputStream(htmlOcrAbsFileName), new FileInputStream(imageAbsFileName));
            hocrToPdf.setPdfFormat(PDFF.PDF_A_1B);
            hocrToPdf.convert();

            os.close();

            if (!JHOCRUtil.getInstance().testGeneratedPDF(word, pdfAbsFileName.getAbsolutePath())) {
                fail("The PDF is not searchable, see the log for more information.");
            }

        } catch (IOException e) {
            logger.error("I/O error, please check the log for more information.", e);
        }

    }
}
