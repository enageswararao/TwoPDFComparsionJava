package com.nagesh.utility;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.reporters.Files;

import com.nagesh.utility.CompareMode;
import com.nagesh.utility.PDFUtil;

public class PDFUtilTest {
	
	PDFUtil pdfutil = new PDFUtil();
	
	@Test(priority=1)
	public void checkForPDFPageCount() throws IOException{
		int actual = pdfutil.getPageCount(getFilePath("image-extract/CD-ExistingPDF.pdf"));
		Assert.assertEquals(actual, 5);
	}

	@Test(priority=2)
	public void checkForFileContent() throws IOException{
		String actual = pdfutil.getText(getFilePath("text-extract/CD-ExistingPDF.pdf"));
		String expected = Files.readFile(new File(getFilePath("text-extract/expected.txt")));
		Assert.assertEquals(actual.trim(), expected.trim());
	}
	
	@Test(priority=3)
	public void extractImages() throws IOException{
		List<String> actualExtractedImages = pdfutil.extractImages(getFilePath("image-extract/CD-ExistingPDF.pdf"));
		Assert.assertEquals(actualExtractedImages.size(), 0);
	}
	
	@Test(priority=4)
	public void saveAsImages() throws IOException{
		List<String> actualExtractedImages = pdfutil.savePdfAsImage(getFilePath("image-extract/CD-ExistingPDF.pdf"));
		Assert.assertEquals(actualExtractedImages.size(), 5);		
	}
	
 	
	@Test(priority=5)
	public void comparePDFTextModeSameAfterExcludePattern() throws IOException{
		String file1 = getFilePath("text-compare/CD-ExistingPDF1.pdf");
		String file2 = getFilePath("text-compare/CD-ExistingPDF2.pdf");
		pdfutil.setCompareMode(CompareMode.TEXT_MODE);
		pdfutil.excludeText("\\d+");
		//pdfutil.excludeText("1999","1998");
		boolean result = pdfutil.compare(file1, file2);
		Assert.assertTrue(result);
	}		

	@Test(priority=6)
	public void comparePDFImageModeSame() throws IOException{
		String file1 = getFilePath("image-compare-same/CD-ExistingPDF1.pdf");
		String file2 = getFilePath("image-compare-same/CD-ExistingPDF2.pdf");
		pdfutil.setCompareMode(CompareMode.VISUAL_MODE);
		
		boolean result = pdfutil.compare(file1, file2);
		Assert.assertTrue(result);
	}
 
	@Test(priority=8)
	public void comparePDFImageModeDiffSpecificPage() throws IOException{
		pdfutil.highlightPdfDifference(true);
		String file1 = getFilePath("image-compare-diff/CD-ExistingPDF1.pdf");
		String file2 = getFilePath("image-compare-diff/CD-ExistingPDF2.pdf");
		boolean result = pdfutil.compare(file1, file2, 3);
		Assert.assertTrue(result);
	}
	
	private String getFilePath(String filename){
		return new File(getClass().getClassLoader().getResource(filename).getFile()).getAbsolutePath();
	}
}
