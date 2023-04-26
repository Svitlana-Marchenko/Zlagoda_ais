package menu;


import java.awt.BorderLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Report extends JFrame implements Printable {

    private JTable table;
    private Printable printable;

    public Report(JTable originalTable) {

        // Create a copy of the original table
        JTable copiedTable = new JTable(originalTable.getModel());

        DefaultTableModel copiedModel = new DefaultTableModel(new Object[][]{}, new Object[originalTable.getColumnCount()]);

// Add column names to copied model
        for (int i = 0; i < copiedTable.getColumnCount(); i++) {
            copiedModel.addColumn(originalTable.getColumnName(i));
        }


        // Add data to copied model
        for (int i = 0; i < copiedTable.getRowCount(); i++) {
            Vector<Object> row = new Vector<Object>();
            for (int j = 0; j < copiedTable.getColumnCount(); j++) {
                row.add(copiedTable.getValueAt(i, j));
            }
            copiedModel.addRow(row);
        }

        // Set up the printable object using the copied table and model
        this.printable = copiedTable.getPrintable(JTable.PrintMode.NORMAL, null, null);

        // Set the copied table's model to the copied model
        copiedTable.setModel(copiedModel);

        // Set up the UI components
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Print");
        printButton.addActionListener(
                e -> {
                    print();
                    ((JFrame) SwingUtilities.getWindowAncestor(printButton)).dispose();
                });
        buttonPanel.add(printButton);

        JScrollPane scrollPane = new JScrollPane(copiedTable);

        // Add the components to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame properties
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    public void print() {
        // Get a PrinterJob object
        PrinterJob job = PrinterJob.getPrinterJob();

        // Set the Printable object for the job
        job.setPrintable(this);

        // Show a print dialog to the user
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        return printable.print(graphics, pageFormat, pageIndex);
    }

}

/*
import java.awt.BorderLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Report extends JFrame implements Printable {

    private JTable table;
    private Printable printable;

    public Report(JTable originalTable) {

        // Create a copy of the original table
        JTable copiedTable = new JTable(originalTable.getModel());

        DefaultTableModel copiedModel = new DefaultTableModel(new Object[][]{}, new Object[]{});

        // Add column names to copied model
        for (int i = 0; i < copiedTable.getColumnCount(); i++) {
            copiedModel.addColumn(copiedTable.getColumnName(i));
        }

        // Add data to copied model
        for (int i = 0; i < copiedTable.getRowCount(); i++) {
            Vector<Object> row = new Vector<Object>();
            for (int j = 0; j < copiedTable.getColumnCount(); j++) {
                row.add(copiedTable.getValueAt(i, j));
            }
            copiedModel.addRow(row);
        }

        // Set up the printable object using the copied table and model
        this.printable = copiedTable.getPrintable(JTable.PrintMode.NORMAL, null, null);

        // Set the copied table's model to the copied model
        copiedTable.setModel(copiedModel);

        // Set up the UI components
        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Print");
        printButton.addActionListener(
                e -> {
                    print();
                    ((JFrame) SwingUtilities.getWindowAncestor(printButton)).dispose();
                });
        buttonPanel.add(printButton);

        JScrollPane scrollPane = new JScrollPane(copiedTable);

        // Add the components to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Set the frame properties
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }



    public void print() {
        // Get a PrinterJob object
        PrinterJob job = PrinterJob.getPrinterJob();

        // Set the Printable object for the job
        job.setPrintable(this);

        // Show a print dialog to the user
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int print(java.awt.Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        return printable.print(graphics, pageFormat, pageIndex);
    }

}

 */