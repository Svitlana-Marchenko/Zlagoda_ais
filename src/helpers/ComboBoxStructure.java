package helpers;

import bd_connection.Store_Product;
import entity.Category;
import entity.Product;
import entity.ProductInStore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class ComboBoxStructure {

    public static int getIdOfSelectedValue(JComboBox comboBox){
        int id=-1;
        if (comboBox.getSelectedItem() instanceof Object[]) {
            Object[] rowData = (Object[]) comboBox.getSelectedItem();
            id = Integer.valueOf(rowData[0].toString());
        }
        return id;
    }

    public static DefaultComboBoxModel createProductsList(entity.Category category, boolean condition, Product product, boolean shouldIncludeAll)  {
        ArrayList<Product> productArrayList = (ArrayList<Product>) bd_connection.Product.getAllProductsInCategorySorted(true, category);
        if(!shouldIncludeAll){
            for(int j=0;j<productArrayList.size();j++){
                List<ProductInStore> productInStores = Store_Product.findStoreProductsByProductId(productArrayList.get(j).getId());
                if(condition ? (productInStores.size()==2 || productInStores.size()==0) : productInStores.size()==2 || productInStores.size()==1) {
                    productArrayList.remove(j--);
                }
            }
        }
        if(!productArrayList.contains(product) && product!=null)
            productArrayList.add(product);
        return createComboBoxModelForProduct(productArrayList, product!=null, product);
    }
    public static DefaultComboBoxModel createComboBoxModelForProduct(ArrayList<Product> productArrayList, boolean shouldIncludeThis, Product product){
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (int i = 0; i < productArrayList.size(); i++) {
            Object[] rowData = new Object[4];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = createProductRow(productArrayList.get(i)).getValueAt(0, j);
            }
            model.addElement(rowData);
            if(shouldIncludeThis && productArrayList.get(i).equals(product))
                model.setSelectedItem(rowData);
        }
        return model;
    }

    public static ArrayList<entity.Category> getCategoriesArrayList(boolean condition, boolean shouldIncludeProducts){
        ArrayList<entity.Category> categoryArrayList = bd_connection.Category.findAll();
        if(shouldIncludeProducts){
            for(int i=0;i<categoryArrayList.size();i++){
                int count=0;
                List<Product> products = bd_connection.Product.getAllProductsInCategorySorted(true,categoryArrayList.get(i));
                for(int j=0;j<products.size();j++){
                    List<ProductInStore> productInStores = Store_Product.findStoreProductsByProductId(products.get(j).getId());
                    if(condition ? (productInStores.size()==2 || productInStores.size()==0) : productInStores.size()==2 || productInStores.size()==1)
                        count++;
                }
                if(count==products.size()){
                    categoryArrayList.remove(i--);
                }
            }
        }
        return categoryArrayList;
    }

    public static DefaultComboBoxModel createCategoriesList(boolean condition, boolean shouldIncludeProducts, Category category, boolean shouldIncludeThis) {
        ArrayList<entity.Category> categoryArrayList = getCategoriesArrayList(condition,shouldIncludeProducts);
        if(shouldIncludeThis && !categoryArrayList.contains(category))
            categoryArrayList.add(category);
        DefaultComboBoxModel model1 = new DefaultComboBoxModel();
        for (int i = 0; i < categoryArrayList.size(); i++) {
            Object[] rowData = new Object[2];
            for (int j = 0; j < rowData.length; j++) {
                rowData[j] = createCategoryRow(categoryArrayList.get(i)).getValueAt(0, j);
            }
            model1.addElement(rowData);
            if(category!= null && categoryArrayList.get(i).equals(category))
                model1.setSelectedItem(rowData);
        }

        return model1;
    }

    public static JTable createProductRow(Product product){
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Id", "Name", "Characteristics", "Producer"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{product.getId(), product.getName(), product.getCharacteristics(), product.getProducer()});
        return table;
    }

    public static JTable createCategoryRow(entity.Category category){
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Id", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        model.addRow(new Object[]{category.getId(), category.getName()});
        return table;
    }
}
