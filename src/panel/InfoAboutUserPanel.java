package panel;

import entity.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

import static bd_connection.Employee.getEmployee;

public class InfoAboutUserPanel extends JPanel {

    private JPanel panel;
    private String userId;

    public InfoAboutUserPanel(String userId) {
        super(new GridLayout(13,2));
        this.userId = userId;
        panel = this;

        create();
    }

    private void create() {
        try {
            //TODO no data or don`t write? (now no data) (write id?)
            Employee user = getEmployee(userId);

            JLabel idLabel = new JLabel("id:");
            idLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(idLabel);
            JLabel idRes = new JLabel(user.getId());
            idRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(idRes);

            JLabel surnameLabel = new JLabel("surname:");
            surnameLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(surnameLabel);
            JLabel surnameRes = new JLabel(user.getSurname());
            surnameRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(surnameRes);

            JLabel nameLabel = new JLabel("name:");
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(nameLabel);
            JLabel nameRes = new JLabel(user.getName());
            nameRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(nameRes);

            String patronymic = user.getPatronymic();
            if(patronymic == null) patronymic="No data.";
            JLabel patronymicLabel = new JLabel("patronymic:");
            patronymicLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(patronymicLabel);
            JLabel patronymicRes = new JLabel(patronymic);
            patronymicRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(patronymicRes);

            JLabel roleLabel = new JLabel("role:");
            roleLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(roleLabel);
            JLabel roleRes = new JLabel(user.getRole().name().toLowerCase());
            roleRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(roleRes);

            JLabel salaryLabel = new JLabel("salary:");
            salaryLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(salaryLabel);
            JLabel salaryRes = new JLabel(user.getSalary().toString());
            salaryRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(salaryRes);

            JLabel birthdateLabel = new JLabel("date of birth:");
            birthdateLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(birthdateLabel);
            JLabel birthdateRes = new JLabel(user.getBirthdate().toString());
            birthdateRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(birthdateRes);

            JLabel startdateLabel = new JLabel("date of start:");
            startdateLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(startdateLabel);
            JLabel startdateRes = new JLabel(user.getStartDate().toString());
            startdateRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(startdateRes);

            JLabel phoneLabel = new JLabel("phone number:");
            phoneLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(phoneLabel);
            JLabel phoneRes = new JLabel(user.getPhoneNumber());
            phoneRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(phoneRes);

            JLabel cityLabel = new JLabel("city:");
            cityLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(cityLabel);
            JLabel cityRes = new JLabel(user.getCity());
            cityRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(cityRes);

            JLabel streetLabel = new JLabel("street:");
            streetLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(streetLabel);
            JLabel streetRes = new JLabel(user.getStreet());
            streetRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(streetRes);

            JLabel zipLabel = new JLabel("zip code:");
            zipLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(zipLabel);
            JLabel zipRes = new JLabel(user.getZipCode());
            zipRes.setFont(new Font("Arial", Font.PLAIN, 25));
            panel.add(zipRes);

            JButton backButton = new JButton("back");
            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    back();
                }
            });
            panel.add(backButton);
        }catch(SQLException e){
            //TODO do smth with exceptions
        }
    }

    private void back(){
        //TODO back to main method
    }

}
