package create_forms;

import entity.Category;
import helpers.*;
import info_menu_manager.CategoryTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

import static java.awt.GridBagConstraints.*;

/**
 * Форма створення нової категорії
 */
public class CreateCategoryForm extends JFrame {

    private JPanel backPanel;

    public CreateCategoryForm(DefaultTableModel model, JFrame frame){
        this.setSize(600,500);
        this.setLocation(600,100);
        start(model,frame);
    }

    /**
     * Початкова ініціалізація графічних об'єктів
     */
    private void init(DefaultTableModel model, JFrame frame){
        backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.PAGE_AXIS));
        JPanel captionPanel = new JPanel();
        captionPanel.setLayout(new BoxLayout(captionPanel, BoxLayout.LINE_AXIS));
        GridBagConstraints c = new GridBagConstraints();
        JLabel captionLabel = new JLabel("New Category");
        captionLabel.setFont(new Font("TimesRoman", Font.BOLD, 30));
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        captionPanel.add(captionLabel);
        backPanel.add(captionPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        final int max_length=50;

        JLabel getCaptionLabel = new JLabel("Caption: ");
        getCaptionLabel.setFont(new Font("TimesRoman",Font.PLAIN, 20));
        c.gridy = 0;
        c.gridx = 0;
        mainPanel.add(getCaptionLabel,c);

        JTextField captionField = new JTextField();
        captionField.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        c.gridx = 1;
        c.weightx = 2;
        c.fill = BOTH;
        c.ipadx=200;
        mainPanel.add(captionField,c);

        JButton createButton = new JButton("Create");
        createButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CheckForErrors.tFields=new ArrayList<>(Arrays.asList(captionField));
                List<String> errors = CheckForErrors.checkForEmptyErrors();
                if(errors!=null){
                    showError(errors.get(0),CheckForErrors.getErrorTextFields(errors));
                }else if(!CheckForErrors.checkForLength(max_length,0)){
                    showError("Caption is too long. Maximum: 50 symbols.",new JTextField[]{captionField});
                }else{
                    createNewItem(model, frame,captionField);
                    dispose();
                }
            }

        });
        c=new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.weightx = 0.5;
        c.fill=HORIZONTAL;
        c.insets = new Insets(5,5,5,0);
        createButton.setBackground(Color.green);
        buttonPanel.add(createButton,c);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwitchFrames.switchFramesForCategory(frame,model);
                dispose();
            }
        });
        c.gridx = 1;
        c.insets = new Insets(5,10,5,8);
        cancelButton.setBackground(Color.cyan);
        buttonPanel.add(cancelButton,c);
        c.gridy=1;
        c.gridx=0;
        c.gridwidth = 2;
        mainPanel.add(buttonPanel,c);
        backPanel.add(mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                SwitchFrames.switchFramesForCategory(frame,model);
                dispose();
            }
        });

    }

    /**
     * Створюєм нову категорію
     * @param model модель таблички в основному фреймі
     * @param frame основний фрейм з табличкою
     * @param captionField поле з назвою категорії
     */
    private void createNewItem(DefaultTableModel model, JFrame frame, JTextField captionField){
        Random random = new Random();
        int id=random.nextInt();
        while(bd_connection.Category.findCategoryById(id) != null)
            id=random.nextInt();
        Category category = new Category(id, captionField.getText());
        bd_connection.Category.addCategory(category);
        CategoryTable.getCategoryList().add(category);
        SwitchFrames.switchFramesForCategory(frame,model);
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
     * Показуєм повідомлення про помилку
     * @param text текст помилки
     * @param fields поля, в яких виникла помилка
     */
    private void showError(String text, JTextField[] fields){
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.red);
        }
        JOptionPane.showMessageDialog(null,text,"Error",JOptionPane.ERROR_MESSAGE);
        for(int i=0;i<fields.length;i++){
            fields[i].setBackground(Color.white);
            fields[i].setText("");
        }
    }

}