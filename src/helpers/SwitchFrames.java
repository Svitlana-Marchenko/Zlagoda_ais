package helpers;

import entity.*;
import info_menu_common.ProductTable2;
import info_menu_common.StoreProductTable;
import info_menu_manager.CategoryTable;
import info_menu_manager.CustomerTableManager;
import info_menu_manager.EmployeeTableManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SwitchFrames {
    public static void switchFramesForCustomer(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (CustomerCard customer : CustomerTableManager.getCustomerList()) {
            model.addRow(new Object[]{customer.getNumber(), customer.getSurname(), customer.getName(), (customer.getPatronymic()==null?"":customer.getPatronymic()), customer.getPhoneNumber(), customer.getPercent()});
        }
        frame.setEnabled(true);
    }
    public static void switchFramesForProduct(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (Product sp : ProductTable2.getProduct_List()) {
            model.addRow(new Object[]{sp.getId(), sp.getName(), sp.getCategory().getName(), sp.getCharacteristics(), sp.getProducer()});
        }
        frame.setEnabled(true);
    }
    public static void switchFramesForStoreProduct(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (ProductInStore sp : StoreProductTable.getStore_productListList()) {
            model.addRow(new Object[]{sp.getUPC(), sp.getProduct().getName(), sp.getProduct().getCategory().getName(), sp.getPrice(), sp.getAmount(), sp.isPromotional(), sp.getProduct().getProducer()});
        }
        frame.setEnabled(true);
    }

    public static void switchFramesForEmployee(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (Employee sp : EmployeeTableManager.getEmployee_List()) {
            model.addRow(new Object[]{sp.getId(), sp.getSurname(), sp.getName(), (sp.getPatronymic() == null ? "" : sp.getPatronymic()), sp.getRole().toString(), sp.getPhoneNumber(), sp.getCity()+", "+sp.getStreet()+", "+sp.getZipCode() ,sp.getSalary()});
        }
        frame.setEnabled(true);
    }

    public static void switchFramesForCategory(JFrame frame, DefaultTableModel model){
        model.setRowCount(0);

        for (Category category : CategoryTable.getCategoryList()) {
            model.addRow(new Object[]{category.getId(), category.getName()});
        }
        frame.setEnabled(true);
    }
}
