package items_forms;


import bd_connection.Product;
import entity.Category;
import helpers.*;
import info_menu_manager.CategoryTable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.GridBagConstraints.*;

/**
 * Форма для відображення, редагування та видалення категорії
 */
public class CategoryActionForm extends JFrame {

    private JPanel backPanel;
    private JTextField captionField;
    private Category category;

    public CategoryActionForm(Category category, DefaultTableModel model, JFrame frame){
        this.setSize(600,500);
        this.category=category;
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        final int max_length=50;

        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        ButtonsPanel buttonPanel = new ButtonsPanel(true);
        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("Category");
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
        Border border = captionLabel.getBorder();
        Border margin = new EmptyBorder(0,0,5,0);
        captionLabel.setBorder(new CompoundBorder(border, margin));
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        JLabel getCaptionLabel = new JLabel("Caption: ");
        getCaptionLabel.setFont(new Font("TimesRoman",Font.PLAIN, 20));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(getCaptionLabel,c);

        captionField = new JTextField();
        captionField.setFont(new Font("TimesRoman", Font.PLAIN, 20));

        captionField.setEditable(false);
        captionField.setText(category.getName());
        c.gridx = 1;
        c.weightx = 2;
        c.ipadx=200;
        c.fill = BOTH;
        mainPanel.add(captionField,c);

        buttonPanel.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPanel.setEnabled(true);
                captionField.setEditable(true);
            }
        });
        buttonPanel.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<JTextField> textFields = new ArrayList<>();
                textFields.add(captionField);
                CheckForErrors.tFields=textFields;
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                if(errors!=null){
                    showError(errors.get(0), CheckForErrors.getErrorTextFields(errors));
                }else if(!CheckForErrors.checkForLength(max_length,0)){
                    showError("Caption is too long. Maximum: 50 symbols.",new JTextField[]{captionField});
                }else{
                    editItem();
                    buttonPanel.setEnabled(false);
                    captionField.setEditable(false);
                }
            }
        });

        buttonPanel.getDeleteButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null,"Are you sure? Delete this category?", "Delete category",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    deleteItem(model,frame);
                }
            }
        });
        buttonPanel.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForCategory(frame,model);
                dispose();
            }
        });

        backPanel.add(mainPanel);
        backPanel.add(buttonPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForCategory(frame,model);
                dispose();
            }
        });
    }

    /**
     * Видадяєм категорію
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     */
    private void deleteItem(DefaultTableModel model, JFrame frame){
        if(!Product.getAllProductsInCategorySorted(true,category).isEmpty()){
            JOptionPane.showMessageDialog(null,"You can't delete category with products!","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        bd_connection.Category.deleteCategoryById(category.getId());
        CategoryTable.getCategoryList().remove(category);
        SwitchFrames.switchFramesForCategory(frame,model);
        dispose();
    }

    /**
     * Редагуєм категорію
     */
    private void editItem(){
        if(!category.getName().equals(captionField.getText())){
            Category temp = new Category(category.getId(),captionField.getText());
            CategoryTable.getCategoryList().set(CategoryTable.getCategoryList().indexOf(category),temp);
            category=temp;
            bd_connection.Category.updateCategoryById(category);
        }
    }
    /**
     * Запускаєм користувацьку форму
     */
    public void start(DefaultTableModel model, JFrame frame){
        init(model,frame);
        add(backPanel);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Відображаєм помилку
     * @param text текст помилки
     * @param fields поля з помилкою
     */
    private void showError(String text, JTextField[] fields){
        for(int i=0;i<fields.length;i++){
            fields[i].setForeground(Color.red);
        }
        JOptionPane.showMessageDialog(null,text,"Error",JOptionPane.ERROR_MESSAGE);
        for(int i=0;i<fields.length;i++){
            fields[i].setForeground(Color.black);
        }
        for(int i=0;i<fields.length;i++){
            if(fields[i]==captionField)
                captionField.setText(category.getName());
        }
    }

}