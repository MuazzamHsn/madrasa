
package com.madrasa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class CertificateGenerator extends JFrame implements Printable {
    private JTextField admNoField;
    private JTextField nameField, fatherNameField, deptField, totalMarksField, obtainedMarksField, percentField;
    private JButton generateBtn, fetchBtn, printBtn, closeBtn;
    private BufferedImage certificate; // To store the generated certificate

    public CertificateGenerator() {
        setTitle("Certificate Generator");
        setSize(750, 600);
        setLayout(null);  // Using absolute positioning

        // Create the fields and buttons
        admNoField = new JTextField();
        nameField = new JTextField();
        fatherNameField = new JTextField();
        deptField = new JTextField();
        totalMarksField = new JTextField();
        obtainedMarksField = new JTextField();
        percentField = new JTextField();

        fetchBtn = new JButton("Fetch Details");
        generateBtn = new JButton("Generate Certificate");
        printBtn = new JButton("Print Certificate");
        closeBtn = new JButton("Close");

        // Set bounds for labels and fields
        int labelWidth = 150;
        int fieldWidth = 250;
        int height = 30;
        int yOffset = 20;

        add(new JLabel("Admission No:")).setBounds(50, yOffset, labelWidth, height);
        admNoField.setBounds(200, yOffset, fieldWidth, height);
        add(admNoField);

        fetchBtn.setBounds(480, yOffset, 150, height);
        add(fetchBtn);  // Button to fetch details from the database

        yOffset += 40;  // Move down for the next row
        add(new JLabel("Name:")).setBounds(50, yOffset, labelWidth, height);
        nameField.setBounds(200, yOffset, fieldWidth, height);
        add(nameField);

        yOffset += 40;
        add(new JLabel("Father's Name:")).setBounds(50, yOffset, labelWidth, height);
        fatherNameField.setBounds(200, yOffset, fieldWidth, height);
        add(fatherNameField);

        yOffset += 40;
        add(new JLabel("Department:")).setBounds(50, yOffset, labelWidth, height);
        deptField.setBounds(200, yOffset, fieldWidth, height);
        add(deptField);

        yOffset += 40;
        add(new JLabel("Total Marks:")).setBounds(50, yOffset, labelWidth, height);
        totalMarksField.setBounds(200, yOffset, fieldWidth, height);
        add(totalMarksField);

        yOffset += 40;
        add(new JLabel("Obtained Marks:")).setBounds(50, yOffset, labelWidth, height);
        obtainedMarksField.setBounds(200, yOffset, fieldWidth, height);
        add(obtainedMarksField);

        yOffset += 40;
        add(new JLabel("Percentage: ")).setBounds(50, yOffset, labelWidth, height);
        percentField.setBounds(200, yOffset, fieldWidth, height);
        add(percentField);

        yOffset += 100;
        generateBtn.setBounds(40, yOffset, 150, height);
        add(generateBtn);  // Button to generate the certificate
        printBtn.setBounds(200, yOffset, 150, height);
        add(printBtn);  // Button to print the certificate
        closeBtn.setBounds(360, yOffset, 100, height);
        add(closeBtn);  // Button to print the certificate

        // Fetch details when the button is clicked
        fetchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchDetails();
            }
        });

        // Generate certificate when the button is clicked
        generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCertificate();
            }
        });

        // Print certificate when the button is clicked
        printBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printCertificate();
            }
        });

        //Close 
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                setVisible(false);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void fetchDetails() {
        String admissionNo = admNoField.getText();

        try {
            DBcon con = new DBcon();
            String query = "SELECT name, father_name, department, total_marks, obtained_marks, percentage FROM student INNER JOIN exam ON student.admission_no = exam.adm_no WHERE admission_no = '"+admissionNo+"'";

            ResultSet rs = con.stmnt.executeQuery(query);

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                fatherNameField.setText(rs.getString("father_name"));
                deptField.setText(rs.getString("department"));
                totalMarksField.setText(rs.getString("total_marks"));
                obtainedMarksField.setText(rs.getString("obtained_marks"));
                percentField.setText(rs.getString("percentage"));
            } else {
                JOptionPane.showMessageDialog(this, "No student found with Admission No: " + admissionNo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching details");
        }
    }

    private void generateCertificate() {
        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String department = deptField.getText();
        String admissionNo = admNoField.getText();
        String totalMarks = totalMarksField.getText();
        String obtainedMarks = obtainedMarksField.getText();
        String percentage = percentField.getText();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Create a high-resolution image (e.g., 2400x1800 for HD quality)
        int width = 2400;
        int height = 1800;
        certificate = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = certificate.createGraphics();

        // Enable anti-aliasing for smoother text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background color
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Set font
        g.setFont(new Font("Urdu Typesetting", Font.BOLD, 120));
        g.setColor(Color.BLACK);

        // Draw title

        g.drawString("جامعہ عمرینؓ تعلیم القرآن", 720, 200);

        g.setFont(new Font("Clicker Script",Font.ITALIC,160));

        g.drawString("Certificate of Completion", 540, 500);

        // Draw details
        g.setFont(new Font("Serif", Font.PLAIN, 60));
        g.drawString("Name                                      :          " + name, 360, 700);
        g.drawString("Father's Name                         :          " + fatherName, 360, 800);
        g.drawString("Department                             :          " + department, 360, 900);
        g.drawString("Admission No                         :          " + admissionNo, 360, 1000);
        g.drawString("Total Marks                            :          " + totalMarks, 360, 1100);
        g.drawString("Obtained Marks                      :          " + obtainedMarks, 360, 1200);
        g.drawString("Percentage                              :          " + percentage, 360, 1300);
        g.drawString("Date and Time Generated      :          " + timeStamp, 360, 1400);


        g.setFont(new Font("Serif", Font.PLAIN, 40));
        g.drawString("This is computer generated certificate. Does not require any Signature", 610, 1700);
        g.drawString("Errors / Omissions Accepted.", 860, 1770);

        // Save the image to a file
        try {
            ImageIO.write(certificate, "png", new File(""+name+" certificate.png"));
            JOptionPane.showMessageDialog(this, "Certificate Generated of "+name+" Certificate.png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        g.dispose();
    }

    private void printCertificate() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        if (page > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());

        // Draw the certificate image on the page
        if (certificate != null) {
            g2d.drawImage(certificate, 0, 0, certificate.getWidth(), certificate.getHeight(), null);
        }

        return PAGE_EXISTS;
    }

    public static void main(String[] args){
        new CertificateGenerator();
    }
}