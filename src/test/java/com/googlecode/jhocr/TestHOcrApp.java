package com.googlecode.jhocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.googlecode.jhocr.converter.HocrToPdf;
import com.googlecode.jhocr.util.FExt;

public class TestHOcrApp {
											  
	private File 			IMG_FILE		= new File("src/test/resources/test-data/phototest.tif");	
	private File			RESULT_PATH		= new File("src/test/resources/test-results");
	private File			HOCR_FILE		= new File(RESULT_PATH, String.format("%s.%s", "phototest.tif", FExt.HTML));
	private File			PDF_FILE		= new File(RESULT_PATH, String.format("%s.%s", "phototest.tif", FExt.PDF));
	
	/**
	 * This will generate the ocr html file (hocr) and merge that and image into one pdf.
	 * 
	 * @return true if the test passed.
	 * @throws Exception 
	 */
	@Test
	public void testConvertionFromHOCRToPDF() throws Exception {

		// !!! - Install tesseract before execute this text
					
		String cmd = MessageFormat.format("tesseract {0} {1} hocr", IMG_FILE.getAbsolutePath(), RESULT_PATH.getAbsolutePath() + "/" + IMG_FILE.getName());

		Process process = Runtime.getRuntime().exec(cmd);
		
		StreamGobbler error = new StreamGobbler(process.getErrorStream());
		StreamGobbler info = new StreamGobbler(process.getInputStream());
	       
		error.start();
		info.start();

		int code = process.waitFor();

		if (code != 0) {
			throw new Exception(MessageFormat.format("Erro when perform command: ({0}), msg: \n{1}", cmd, error.getResult()));
		}

		FileOutputStream os = new FileOutputStream(PDF_FILE);

		HocrToPdf hocrToPdf = new HocrToPdf(os);
		hocrToPdf.addHocrDocument(new FileInputStream(HOCR_FILE), new FileInputStream(IMG_FILE));
		hocrToPdf.convert();
		
		os.close();
	}

	public class StreamGobbler extends Thread {
	    
	    private Logger log = Logger.getLogger(StreamGobbler.class);
	    
	    private InputStream is;
	    private StringBuilder out = new StringBuilder();
		
	    public StreamGobbler(InputStream is) {
	    	this.is = is;
	    }
	    
	    @Override
	    public void run() {
			try {
			    InputStreamReader isr = new InputStreamReader(is);
			    BufferedReader br = new BufferedReader(isr);
			    
			    String line = null;
			    
			    while ((line = br.readLine()) != null) {
			        log.debug(line);
			        out.append(line).append("\n");
			    }
			} 
			catch (IOException e) {
			    log.error(e);
			}
	    }

	    public String getResult() {
	    	return out.toString();
	    }
	}
}