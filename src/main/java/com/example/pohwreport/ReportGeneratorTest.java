/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pohwreport;

/**
 *
 * @author user
 */
import java.io.File;
import java.io.FileOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
	
	public class ReportGeneratorTest {
	    public String main(String empCode) throws Exception {
                
                String fileName="";
	        try {
	            // Set up parameters
	            System.out.println("JVM Args: " + java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments());

                    System.out.println("TEST JVM OPTION:");
                    System.out.println(System.getProperty("exec.args"));
                    //String jasperFilePath = "D:\\pohw_status_jrxml\\pohw_status.jasper";
                    String jasperFilePath = "/root/pohw/pohw_status.jasper";
	            
                    //String empCode = "13550";
	                     
	            // Database connection details
	            
                    String dbUrl = "";
	            
                    String dbUser = "";
	            String dbPassword = "";
	            
	            // Generate the report
	            byte[] pdfBytes = JasperReportGenerator.generateReport(
	                jasperFilePath, 
	                empCode,
	                dbUrl,
	                dbUser,
	                dbPassword);
	            
	            // Save the PDF to a file
	            //String outputDir = "d:/pdf_files/";
                    String outputDir ="/root/pohw/status/";
                    fileName = outputDir+"pohw_status_" + empCode + ".pdf";
                    System.out.println("File Path="+fileName);
	            try (FileOutputStream fos = new FileOutputStream(fileName)) {
	                fos.write(pdfBytes);
	            }
	            
	            System.out.println("Report generated successfully: " + fileName);
                    
                    String backgroundImagePath = "/root/pohw/dpl.jpg";
                    addBackground(fileName, backgroundImagePath);
                    System.out.println("Background image added successfully: " + backgroundImagePath);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
                
                return fileName;
	    }
            
            private void addBackground(String pdfPath, String imagePath) throws Exception {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);

          for (PDPage page : document.getPages()) {
                PDPageContentStream contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.PREPEND, true, true);

                float pageWidth = page.getMediaBox().getWidth();
                float pageHeight = page.getMediaBox().getHeight();

                float scaleFactor = 0.5f;  // 50% of page size
                float imageWidth = pageWidth * scaleFactor;
                float imageHeight = pageHeight * scaleFactor;

                float x = (pageWidth - imageWidth) / 2;
                float y = (pageHeight - imageHeight) / 2;

                contentStream.drawImage(pdImage, x, y, imageWidth, imageHeight);

                contentStream.close();
            }

            document.save(pdfPath);
        }
    }
            
            
            
	}