package helpers;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckForErrors {
    public static List<JTextField> tFields;
    /**
     * Перевіряєм на пусті поля
     * @return список помилок
     */
    public static List<String> checkForEmptyErrors(){
        List<String> rez = new LinkedList<>();
        rez.add("Field can't be empty!");
        for(int i=0;i<tFields.size();i++){
            if(tFields.get(i).getText().trim().equals("") && tFields.get(i).isEnabled() && tFields.get(i).isEditable())
                rez.add(String.valueOf(i+1));
        }
        if(rez.size()>1){
            return rez;
        }
        return null;
    }

    public static boolean checkPhoneNumber(String phoneNumber){
        Pattern pattern1 = Pattern.compile ("\\+380[0-9]{9}");
        Matcher matcher1 = pattern1.matcher (phoneNumber);
        return matcher1.find();
    }
    public static JTextField[] getErrorTextFields(List<String> fields){
        if(fields!=null){
            JTextField[] textFields = new JTextField[fields.size()-1];
            for(int i=0;i<textFields.length;i++){
                for(int j=0;j<tFields.size();j++){
                    if(fields.get(i+1).equals(String.valueOf(j+1))) {
                        textFields[i] = tFields.get(j);
                        break;
                    }
                }
            }
            return textFields;
        }
        return null;
    }
    public static List<String> checkForNotNumbersErrors(List<JTextField> checkDouble, List<JTextField> checkInt){
        List<String> rez = new LinkedList<>();
        rez.add("Incorrect input!\nField can include only numbers >=0");
        for(int i=0;i<checkDouble.size();i++){
            if(checkDouble.get(i).isEnabled() &&  checkDouble.get(i).isEditable() && (!checkDoubleNumber(checkDouble.get(i).getText()) ||Double.valueOf(checkDouble.get(i).getText())<0))
                rez.add(String.valueOf(tFields.indexOf(checkDouble.get(i))+1));
        }
        for(int i=0;i<checkInt.size();i++){
            if(checkInt.get(i).isEnabled() &&checkInt.get(i).isEditable() &&(!checkNumber(checkInt.get(i).getText()) ||Integer.valueOf(checkInt.get(i).getText())<0))
                rez.add(String.valueOf(tFields.indexOf(checkInt.get(i))+1));
        }
        if(rez.size()>1){
            return rez;
        }
        return null;
    }
    public static boolean checkNumber(String value){
        try {
            Integer.valueOf(value);
        }catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
    public static boolean checkDoubleNumber(String value){
        try {
            Double.valueOf(value);
        }catch (NumberFormatException ex){
            return false;
        }
        return true;
    }
    public static boolean checkForLength(int length, int i){
        return tFields.get(i).getText().length()<=length;
    }
    public static List<String> checkForLengthOne(int length, List<JTextField> fields){
        List<String> rez=new ArrayList<>();
        for(int i=0;i<fields.size();i++){
            if(fields.get(i).getText().length()>length)
                rez.add(String.valueOf(tFields.indexOf(fields.get(i))+1));
        }
       return rez;
    }
    public static List<String> checkForLength(int[] length, List<List<JTextField>> fields){
        List<String> rez=new ArrayList<>();
        rez.add("Field text is too long!");
        for(int i=0;i<fields.size();i++){
            rez.addAll(checkForLengthOne(length[i],fields.get(i)));
        }
        if(rez.size()>1){
            return rez;
        }
        return null;
    }
}
