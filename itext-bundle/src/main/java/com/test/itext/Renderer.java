package com.test.itext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.license.LicenseKey;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.XfaForm;
//import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;

public class Renderer {
	
	public void renderDoc(boolean flatten, 
			String templatePath,
			String renderPayload,
			String xfaPdfPath,
			String flatPdfPath,
			String licenseFilePath) throws RuntimeException {
		
		try {
	    	byte[] templateBytes = this.readTemplateBytes(templatePath);
	
	    	// Do the rendering (Note, this does NOT require the iText License)
	    	byte[] renderedXfaBytes = this.populateXFA(templateBytes, renderPayload.getBytes());
	    	writeTestFile(renderedXfaBytes, xfaPdfPath);
	    	
	    	// Get the iText License
	    	InputStream license = readLicense(licenseFilePath);
	    	LicenseKey.loadLicenseFile(license);
	    	
	    	// Do the flattening (if required)
	    	if (flatten) {
	    		byte[] flattenedXfaBytes = this.flattenXfa(renderedXfaBytes);
	    		writeTestFile(flattenedXfaBytes, flatPdfPath);
	    	} 
	    	
		} catch (RuntimeException e) {
			String msg = "Failed to render iText Doc. Msg=" + e.getMessage();
			throw new RuntimeException(msg);			
		} catch (URISyntaxException e) {
			String msg = "Failed to render iText Doc. Msg=" + e.getMessage();
			throw new RuntimeException(msg);			
		} catch (IOException e) {
			String msg = "Failed to render iText Doc. Msg=" + e.getMessage();
			throw new RuntimeException(msg);	
		}
	}

	
	private InputStream readLicense(String licenseFilePath) throws RuntimeException {
			
		ByteArrayInputStream bais = null;
		try {
			File licenseFile = new File(licenseFilePath);
	    	bais = new ByteArrayInputStream(Files.readAllBytes(licenseFile.toPath()));	;
		} catch (IOException e) {
			String msg = "An IOException was thrown while trying to read the iText license file. Msg=" + e.getMessage();
			throw new RuntimeException(msg);				
		} catch (FileSystemNotFoundException e) {
			String msg = "A FileSystemNotFoundException was thrown while trying to read the iText license file. Msg=" + e.getMessage();
			throw new RuntimeException(msg);				
		}
			
		return bais;
	}
	

	private byte[] populateXFA(byte[] templateBytes, byte[] xfaDataBytes)
			throws RuntimeException {
	
		// Create an output stream for the rendered doc
		ByteArrayOutputStream rendered = new ByteArrayOutputStream();
	
		try {
			PdfReader reader = new PdfReader(templateBytes);
			PdfStamper stamper = new PdfStamper(reader, rendered);
			AcroFields form = stamper.getAcroFields();
			XfaForm xfa = form.getXfa();
			xfa.fillXfaForm(new ByteArrayInputStream(xfaDataBytes));
			stamper.close();
			reader.close();			
		} catch (IOException e) {
			String msg = "An IOException was thrown while trying to populate the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);
		} catch (DocumentException e) {
			String msg = "A DocumentException was thrown while trying to populate the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);			
		}
	
		return rendered.toByteArray();
	}

	private byte[] flattenXfa(byte[] sourceXfaBytes) throws RuntimeException {
		
		// Create an output stream for the flattened doc
		ByteArrayOutputStream flattened = new ByteArrayOutputStream();
		
		try {
			Document document = new Document();
			PdfReader reader = new PdfReader(sourceXfaBytes);
			PdfWriter writer = PdfWriter.getInstance(document, flattened);
			XFAFlattener flattener = new XFAFlattener(document, writer);
			System.out.println("flattener instantiated");
			flattener.flatten(reader);
			document.close();			
		} catch (IOException e) {
			String msg = "An IOException was thrown while trying to flatten the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);			
		} catch (DocumentException e) {
			String msg = "A DocumentException was thrown while trying to flatten the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);			
		} catch (InterruptedException e) {
			String msg = "An InterruptedException was thrown while trying to flatten the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);
		} catch (NoClassDefFoundError e) {
			String msg = "A NoClassDefFoundError was thrown while trying to flatten the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);				
		} catch (Exception e) {
			String msg = "A general Exception was thrown while trying to flatten the XFA form. Msg="+e.getMessage();
			e.printStackTrace();
			throw new RuntimeException(msg);			
		}
	
		return flattened.toByteArray();
	}

	private byte[] readTemplateBytes(String templateUri) throws URISyntaxException, IOException {
		
		byte[] templateBytes = null;
	
		try {
			File file = new File(templateUri);
			URI uri = file.toURI();
			templateBytes = Files.readAllBytes(Paths.get(uri));     	  		
		} catch (IOException e) {
			String msg = "An IOException was thrown while trying to read the template. Msg=" + e.getMessage();
			throw new RuntimeException(msg);							
		}
		return templateBytes;
	}

	/*
	private byte[] readInputStreamToBytes (InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
		int nRead;
		byte[] data = new byte[16384];
	
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
	
		buffer.flush();
	
		return buffer.toByteArray();
	}*/

	/*
	 * Temporary test method to write a byte array to file
	 */
	private void writeTestFile(byte[] fileBytes, String path) {
		
		try (FileOutputStream fos = new FileOutputStream(path))
		{
			fos.write(fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
        System.out.println("Rendering PDF");
        Renderer rend = new Renderer();
        
        String repoRoot = "/home/gregf/Greg_OfflineData/Dev/Repos/Other/ItextXfaOnKaraf/";
        
        String templatePath = repoRoot + "templates/BasicXfa.pdf";
        String xfaPdfPath = repoRoot + "templates/rendered.pdf";
    	String flatPdfPath = repoRoot + "templates/renderedFlat.pdf";
    	String licenseFilePath = repoRoot + "templates/itextkey.xml";
    	
        String renderPayload = "<form1><Name>MyName</Name><Surname>MySurname</Surname></form1>";

        boolean flatten = true;
        rend.renderDoc(flatten, templatePath, renderPayload, xfaPdfPath, flatPdfPath, licenseFilePath);
	}
}
