/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.pohwreport;

/**
 *
 * @author user
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperReportGenerator {

    /**
     * Generates a PDF report and returns it as a byte array
     * 
     * @param jasperFilePath Full path to the .jasper file
     * @param empCode Employee code
     * @param startMonth Start month for the report
     * @param endMonth End month for the report
     * @param summaryFlag Report type flag
     * @param reportDir Directory where report resources are stored
     * @param dbUrl Database connection URL
     * @param dbUser Database username
     * @param dbPassword Database password
     * @return byte array containing the PDF report
     * @throws Exception if any error occurs during report generation
     */
    public static byte[] generateReport(
            String jasperFilePath, 
            String empCode, 
            String dbUrl,
            String dbUser,
            String dbPassword) throws Exception {
        
        Connection conn = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            // Load the JasperReport
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File(jasperFilePath));

            // Set up report parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Pngs", empCode);
            
            
            // Establish database connection
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            conn = new DBConn().connectOrcl();
            // Get the file name for the report
            String fileName = getReportFileName(conn, empCode);
            
            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
            
            // Export to PDF stream
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            
            return outputStream.toByteArray();
            
        } finally {
            // Clean up resources
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // Log or handle the exception
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Log or handle the exception
                }
            }
        }
    }
    
    /**
     * Gets the file name for the report based on employee code
     * 
     * @param conn Database connection
     * @param empId Employee ID
     * @return File name for the report
     */
    private static String getReportFileName(Connection conn, String empId) {
        String sql = "select uan_no from t_dcpy_emp_acc_dtls where ngs=?";
        String fileName = "";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empId);
            
            ResultSet rs = stmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                fileName = rs.getString(1);
                count++;
            }
            
            return count > 0 ? fileName : empId;
            
        } catch (SQLException e) {
            // Log the error and return the employee ID as fallback
            e.printStackTrace();
            return empId;
        }
    }
}